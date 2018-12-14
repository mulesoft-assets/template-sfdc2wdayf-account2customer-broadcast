
# Anypoint Template: Salesforce to Workday Financials Account to Customer Broadcast	

<!-- Header (start) -->
Broadcast new accounts or update existing accounts in Salesforce to Workday Financials as customers in real time. Ensure that the customer (account) information in the Workday Financials module is updated to Salesforce changes. 

The detection criteria and fields to integrate are configurable. Add  systems to be notified of  changes. Real time synchronization is achieved using rapid polling of Salesforce or outbound notifications to reduce the number of API calls. This template uses Mule batch processing and watermarking to efficiently process large numbers of records.

![8a22b5e1-d19d-45ca-b26e-385480c26730-image.png](https://exchange2-file-upload-service-kprod.s3.us-east-1.amazonaws.com:443/8a22b5e1-d19d-45ca-b26e-385480c26730-image.png)
<!-- Header (end) -->

# License Agreement
This template is subject to the conditions of the <a href="https://s3.amazonaws.com/templates-examples/AnypointTemplateLicense.pdf">MuleSoft License Agreement</a>. Review the terms of the license before downloading and using this template. You can use this template for free with the Mule Enterprise Edition, CloudHub, or as a trial in Anypoint Studio. 
# Use Case
<!-- Use Case (start) -->
Synchronize accounts and customers between Salesforce and Workday.

This template is for setting an online sync of accounts from Salesforce instance to a customer in a Workday instance. Each time there is a new account or a change in an existing one with a type stating the string customer, the integration polls for changes in the Salesforce source instance and updates the customer in the target Workday instance. You can use this template as an example and to establish a starting point to adapt your integration to your requirements.

This template leverages the Mule batch module. The integration is triggered by a scheduler defined in the flow that triggers the application, queries the newest Salesforce updates or creates matching a filter criteria and executes the batch job. During the Process stage, each Salesforce account is checked, if it has an existing matching customer in the Workday instance. The last step of the Process stage inserts or updates the customers in Workday. Finally during the On Complete stage the template logs output statistics data to the console.
<!-- Use Case (end) -->

# Considerations
<!-- Default Considerations (start) -->

<!-- Default Considerations (end) -->

<!-- Considerations (start) -->
To make this template run, there are certain preconditions that must be considered. All of them deal with the preparations in both source (Salesforce) and destination (Workday) systems, that must be made for the template to run smoothly. Failing to do so can lead to unexpected behavior of the template.

This template illustrates the broadcast use case between Salesforce and a Workday, thus it requires a Workday instance to work.
<!-- Considerations (end) -->

## Salesforce Considerations

- Where can I check that the field configuration for my Salesforce instance is the right one? See: <a href="https://help.salesforce.com/HTViewHelpDoc?id=checking_field_accessibility_for_a_particular_field.htm&language=en_US">Salesforce: Checking Field Accessibility for a Particular Field</a>.
- Can I modify the Field Access Settings? How? See: <a href="https://help.salesforce.com/HTViewHelpDoc?id=modifying_field_access_settings.htm&language=en_US">Salesforce: Modifying Field Access Settings</a>.

### As a Data Source

If a user who configures the template for the source system does not have at least *read only* permissions for the fields that are fetched, then an *InvalidFieldFault* API fault displays.

```
java.lang.RuntimeException: [InvalidFieldFault [ApiQueryFault 
[ApiFault  exceptionCode='INVALID_FIELD'
exceptionMessage='Account.Phone, Account.Rating, Account.RecordTypeId, 
Account.ShippingCity
^
ERROR at Row:1:Column:486
No such column 'RecordTypeId' on entity 'Account'. If you are 
attempting to use a custom field, be sure to append the '__c' 
after the custom field name. Reference your WSDL or the 
describe call for the appropriate names.'
]
row='1'
column='486'
]
]
```

## Workday Considerations

### As a Data Destination

There are no considerations with using Workday as a data destination.

## Workday Financials Considerations

### As a Data Destination

- When creating new Workday customer, one of existing customer categories must be chosen. To do this, this template uses a lookup table with predefined mappings between the  Salesforce data (Industry) and the Workday Customer Category ID.
- Workday customer categories can be user defined, therefore the mapping must be edited by user manually before running this template.
- To view all the Workday Customer Categories, type "customer categories" in the search field in Workday and select the Customer Categories report. In the report you should see all the customer categories available. Select Integration IDs > View Integration IDs from available actions of each customer category and update the function *industryToCustomerCategoryMapping* in the template's DataWeave component 'Prepare put customer request' manually.
- The default customer category ID can be edited inside the same DataWeave component in the row where a customer category ID is set. This category is used if the source account's industry value does not match any defined mappings in the function.

# Run it!
Simple steps to get this template running.
<!-- Run it (start) -->
See below.
<!-- Run it (end) -->

## Running On Premises
In this section we help you run this template on your computer.
<!-- Running on premise (start) -->

<!-- Running on premise (end) -->

### Where to Download Anypoint Studio and the Mule Runtime
If you are new to Mule, download this software:

- [Download Anypoint Studio](https://www.mulesoft.com/platform/studio)
- [Download Mule runtime](https://www.mulesoft.com/lp/dl/mule-esb-enterprise)

**Note:** Anypoint Studio requires JDK 8.
<!-- Where to download (start) -->

<!-- Where to download (end) -->

### Importing a Template into Studio
In Studio, click the Exchange X icon in the upper left of the taskbar, log in with your Anypoint Platform credentials, search for the template, and click Open.
<!-- Importing into Studio (start) -->
<!-- Importing into Studio (end) -->

### Running on Studio
After you import your template into Anypoint Studio, follow these steps to run it:

1. Locate the properties file `mule.dev.properties`, in src/main/resources.
2. Complete all the properties required per the examples in the "Properties to Configure" section.
3. Right click the template project folder.
4. Hover your mouse over `Run as`.
5. Click `Mule Application (configure)`.
6. Inside the dialog, select Environment and set the variable `mule.env` to the value `dev`.
7. Click `Run`.
<!-- Running on Studio (start) -->
<!-- Running on Studio (end) -->

### Running on Mule Standalone
Update the properties in one of the property files, for example in mule.prod.properties, and run your app with a corresponding environment variable. In this example, use `mule.env=prod`. 

## Running on CloudHub
When creating your application in CloudHub, go to Runtime Manager > Manage Application > Properties to set the environment variables listed in "Properties to Configure" as well as the mule.env value.
<!-- Running on Cloudhub (start) -->
While creating your application on CloudHub (or you can do it later as a next step), you need to go to Deployment > Advanced to set all environment variables detailed in **Properties to Configure** as well as the **mule.env**. 
Every time an account (with type starting with customer) is created or modified, it automatically synchronizes to the Workday instance.
<!-- Running on Cloudhub (end) -->

### Deploying a Template in CloudHub
In Studio, right click your project name in Package Explorer and select Anypoint Platform > Deploy on CloudHub.
<!-- Deploying on Cloudhub (start) -->

<!-- Deploying on Cloudhub (end) -->

## Properties to Configure
To use this template, configure properties such as credentials, configurations, etc.) in the properties file or in CloudHub from Runtime Manager > Manage Application > Properties. The sections that follow list example values.
### Application Configuration
<!-- Application Configuration (start) -->
**Application Configuration**

- scheduler.frequency= `60000`
- scheduler.start.delay= `500`
- watermark.default.expression ` (now() - |PT5M|) as String` or ` 2018-01-02T15:53:00Z`

**Salesforce Connector Configuration**

- sfdc.username `joan.baez@orgb`
- sfdc.password `JoanBaez456`
- sfdc.securityToken `ces56arl7apQs56XTddf34X`

**Workday Connector Configuration**

- wdayf.username `joan`
- wdayf.tenant `acme_pt1`
- wdayf.password `joanPass123`
- wdayf.host `your_impl-cc.workday.com`

- wdayf.country `USA`
- wdayf.state `USA-CA`
- wdayf.postalCode `90001`
- wdayf.city `San Francisco`
- wdayf.street `Main Street 123`
- wdayf.phone `123-4567`
<!-- Application Configuration (end) -->

# API Calls
<!-- API Calls (start) -->
Salesforce imposes limits on the number of API calls that can be made. However, we make API call to Salesforce only once during migration, so this is not something to worry about.
<!-- API Calls (end) -->

# Customize It!
This brief guide provides a high level understanding of how this template is built and how you can change it according to your needs. As Mule applications are based on XML files, this page describes the XML files used with this template. More files are available such as test classes and Mule application files, but to keep it simple, we focus on these XML files:

* config.xml
* businessLogic.xml
* endpoints.xml
* errorHandling.xml
<!-- Customize it (start) -->
<!-- Customize it (end) -->

## config.xml
<!-- Default Config XML (start) -->
This file provides the configuration for connectors and configuration properties. Only change this file to make core changes to the connector processing logic. Otherwise, all parameters that can be modified should instead be in a properties file, which is the recommended place to make changes.
<!-- Default Config XML (end) -->
<!-- Config XML (start) -->
<!-- Config XML (end) -->

## businessLogic.xml
<!-- Default Business Logic XML (start) -->
The functional aspect of this template is implemented in this XML file directed by oane flow that polls for Salesforce creates or updates. The several message processors constitute four high level actions that fully implement the logic of this Template:

1. In Salesforce query all existing accounts that match the filter criteria.
2. During the Process stage, check each Salesforce account ID in the external ID field in Workday (Customer_Reference_ID), to see if it has existing matching objects in Workday.
3. Upsert of the relevant Customer fields is performed in Workday
4. During the On Complete stage log output statistics data to the console.
<!-- Default Business Logic XML (end) -->
<!-- Business Logic XML (start) -->
<!-- Business Logic XML (end) -->

## endpoints.xml
<!-- Default Endpoints XML (start) -->
This file is created by a flow containing the Scheduler that periodically queries Salesforce for updated or created Accounts that meet the defined criteria in the query and then executes the batch job process with the query results.
<!-- Default Endpoints XML (end) -->
<!-- Endpoints XML (start) -->
<!-- Endpoints XML (end) -->

## errorHandling.xml
<!-- Default Error Handling XML (start) -->
This file handles how your integration reacts depending on the different exceptions. This file provides error handling that is referenced by the main flow in the business logic.
<!-- Default Error Handling XML (end) -->
<!-- Error Handling XML (start) -->
<!-- Error Handling XML (end) -->
<!-- Extras (start) -->
<!-- Extras (end) -->
