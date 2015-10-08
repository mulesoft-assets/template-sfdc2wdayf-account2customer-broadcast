/**
 * Mule Anypoint Template
 * Copyright (c) MuleSoft, Inc.
 * All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.integration;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleEvent;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.context.notification.NotificationException;
import org.mule.processor.chain.SubflowInterceptingChainLifecycleWrapper;
import org.mule.templates.builders.SfdcObjectBuilder;

import com.mulesoft.module.batch.BatchTestHelper;

/**
 * The objective of this class is to validate the correct behavior of the Mule
 * Template that make calls to external systems.
 * 
 * The test will update the SFDC test account, then run the poller 
 * and finally check that the corresponding customer in Workday is
 * correctly updated.
 */
public class BusinessLogicIT extends AbstractTemplateTestCase {

	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	protected static final int TIMEOUT_SECONDS = 300;
	private SubflowInterceptingChainLifecycleWrapper UPSERT_ACCOUNT_FLOW;
	private SubflowInterceptingChainLifecycleWrapper RETRIEVE_CUSTOMER_FLOW;
	private String SFDC_TEST_ACCOUNT_ID;
	private BatchTestHelper helper;

	@BeforeClass
    public static void beforeTestClass() {
        System.setProperty("poll.startDelayMillis", "8000");
        System.setProperty("poll.frequencyMillis", "30000");
        System.setProperty("page.size", "200");
    }
	
	/**
	 * Sets up the test prerequisites.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		helper = new BatchTestHelper(muleContext);

		final Properties props = new Properties();
		try {
			props.load(new FileInputStream(PATH_TO_TEST_PROPERTIES));
		} catch (Exception e) {
			logger.error("Error occured while reading mule.test.properties", e);
		}
		stopFlowSchedulers(POLL_FLOW_NAME);
		registerListeners();
		initializeSubflows(props);				
	}

	private void initializeSubflows(final Properties props)	throws InitialisationException {
		SFDC_TEST_ACCOUNT_ID = props.getProperty("sfdc.testaccount.id");
		UPSERT_ACCOUNT_FLOW = getSubFlow("upsertAccountFlow");
		UPSERT_ACCOUNT_FLOW.initialise();
		RETRIEVE_CUSTOMER_FLOW = getSubFlow("retrieveCustomersFlow");
		RETRIEVE_CUSTOMER_FLOW.initialise();
	}
	

    
    private void registerListeners() throws NotificationException {
		muleContext.registerListener(pipelineListener);
	}

	/**
	 * Performs update on the SFDC test account.
	 * 
	 * @throws Exception
	 */
	public void editTestDataInSandbox(String name, String website)
			throws Exception {
		// create object to edit the test account with			
		Map<String, Object> account = SfdcObjectBuilder.anAccount()
				.with("Id", SFDC_TEST_ACCOUNT_ID)
				.with("Name", name)
				.with("Type", "Customer")
				.with("Website", website)
				.with("BillingPostalCode", "90210")
				.with("Phone", "123-4567").build();
		List<Map<String, Object>> payload = new ArrayList<>();
		payload.add(account);

		UPSERT_ACCOUNT_FLOW.process(getTestEvent(payload,
				MessageExchangePattern.REQUEST_RESPONSE));
	}

	/**
	 * Tests if update of a SFDC test Account results in Workday Customer update
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testMainFlow() throws Exception {
		// edit test data
		final String name = generateUniqueName();
		final String website = generateUniqueWebsite();
		editTestDataInSandbox(name,website);
				
		Thread.sleep(10000);
		// run the main migration flow
		runSchedulersOnce(POLL_FLOW_NAME);
		waitForPollToRun();
		helper.awaitJobTermination(TIMEOUT_SECONDS * 1000, 500);
		helper.assertJobWasSuccessful();

		MuleEvent event = RETRIEVE_CUSTOMER_FLOW.process(getTestEvent(SFDC_TEST_ACCOUNT_ID, MessageExchangePattern.REQUEST_RESPONSE));
		List<Map<String,String>> response = (List<Map<String,String>>) event.getMessage().getPayload();
		// assertions
		assertEquals("Workday should return one result", 1, response.size());
		assertEquals("The website should be the same", website, response.get(0).get("Website"));
		assertEquals("The name should be the same", name, response.get(0).get("Name"));
		assertEquals("The phone should be the same", "123-4567", response.get(0).get("Phone"));
		assertEquals("The postal code should be the same", "90210", response.get(0).get("PostalCode"));
	}
}
