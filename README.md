
# Anypoint Template: Salesforce to Workday Financials Account to Customer Broadcast

+ [License Agreement](#licenseagreement)
+ [Use Case](#usecase)
+ [Considerations](#considerations)
	* [Salesforce Considerations](#salesforceconsiderations)
	* [Workday Considerations](#workdayconsiderations)
	* [Workday Financials Considerations](#workdayfinancialsconsiderations)
+ [Run it!](#runit)
	* [Running on premise](#runonopremise)
	* [Running on Studio](#runonstudio)
	* [Running on Mule ESB stand alone](#runonmuleesbstandalone)
	* [Running on CloudHub](#runoncloudhub)
	* [Deploying your Anypoint Template on CloudHub](#deployingyouranypointtemplateoncloudhub)
	* [Properties to be configured (With examples)](#propertiestobeconfigured)
+ [API Calls](#apicalls)
+ [Customize It!](#customizeit)
	* [config.xml](#configxml)
	* [businessLogic.xml](#businesslogicxml)
	* [endpoints.xml](#endpointsxml)
	* [errorHandling.xml](#errorhandlingxml)


# License Agreement <a name="licenseagreement"/>
Note that using this template is subject to the conditions of this [License Agreement](AnypointTemplateLicense.pdf).
Please review the terms of the license before downloading and using this template. In short, you are allowed to use the template for free with Mule ESB Enterprise Edition, CloudHub, or as a trial in Anypoint Studio.

# Use Case <a name="usecase"/>
As a Salesforce admin I want to synchronize Accounts and Customers between Salesfoce and Workday.

This Template should serve as a foundation for setting an online sync of Accounts from SalesForce instance to Customer in Workday instance. Every time there is a new Account or a change in an already existing one with type stating with the string Customer, the integration will poll for changes in SalesForce source instance and it will be responsible for updating the Customer in the target Workday instance.

Requirements have been set not only to be used as examples, but also to establish a starting point to adapt your integration to your requirements.

As implemented, this Anypoint Template leverage the [Batch Module](http://www.mulesoft.org/documentation/display/current/Batch+Processing)
The batch job is divided in Input, Process and On Complete stages.
The integration is triggered by a poll defined in the flow that is going to trigger the application, querying newest SalesForce updates/creations matching a filter criteria and executing the batch job.
During the Process stage, each Salesforce Account will be checked, if it has an existing matching Customer in the Workday instance.
The last step of the Process stage will group the Accounts and insert/update them in Workday.
Finally during the On Complete stage the Template will log output statistics data into the console.

# Considerations <a name="considerations"/>

To make this Anypoint Template run, there are certain preconditions that must be considered. All of them deal with the preparations in both source (Salesforce) and destination (Workday) systems, that must be made in order for all to run smoothly. 
**Failling to do so could lead to unexpected behavior of the template.**

This particular Anypoint Template illustrate the broadcast use case between Salesforce and a Workday, thus it requires a Workday instance to work.



## Salesforce Considerations <a name="salesforceconsiderations"/>

There may be a few things that you need to know regarding Salesforce, in order for this template to work.

In order to have this template working as expected, you should be aware of your own Salesforce field configuration.

###FAQ

 - Where can I check that the field configuration for my Salesforce instance is the right one?

    [Salesforce: Checking Field Accessibility for a Particular Field][1]

- Can I modify the Field Access Settings? How?

    [Salesforce: Modifying Field Access Settings][2]


[1]: https://help.salesforce.com/HTViewHelpDoc?id=checking_field_accessibility_for_a_particular_field.htm&language=en_US
[2]: https://help.salesforce.com/HTViewHelpDoc?id=modifying_field_access_settings.htm&language=en_US

### As source of data

If the user configured in the template for the source system does not have at least *read only* permissions for the fields that are fetched, then a *InvalidFieldFault* API fault will show up.

```
java.lang.RuntimeException: [InvalidFieldFault [ApiQueryFault [ApiFault  exceptionCode='INVALID_FIELD'
exceptionMessage='
Account.Phone, Account.Rating, Account.RecordTypeId, Account.ShippingCity
^
ERROR at Row:1:Column:486
No such column 'RecordTypeId' on entity 'Account'. If you are attempting to use a custom field, be sure to append the '__c' after the custom field name. Please reference your WSDL or the describe call for the appropriate names.'
]
row='1'
column='486'
]
]
```




## Workday Considerations <a name="workdayconsiderations"/>


### As destination of data

There are no particular considerations for this Anypoint Template regarding Workday as data destination.




## Workday Financials Considerations <a name="workdayfinancialsconsiderations"/>


### As destination of data

When creating new Workday customer, one of existing customer categories must be chosen. In order to do this, this template uses DataWeave function with pre-defined mappings between Salesforce data (Industry) and Workday Customer Category ID.
Workday customer categories can be user defined, therefore the mapping must be edited by user manually before running this template.

To view all the Workday Customer Categories, please type "customer categories" into search field in Workday system and select Customer Categories report.
In the report you should see all the customer categories available. Select Integration IDs -> View Integration IDs from available actions of each customer 
category and update the user-defined function in the template's DataWeave component (Map to PutCustomerRequestType) manually.
The default customer category ID can be edited in the same DataWeave component by assigning to *defaultIndustry* variable. This category will be used if the source Account's Industry 
value does not match any defined mappings in the lookup table.

It is not possible to delete customer in Workday system. Therefore, integration test is based on update of test Account in Salesforce identified by property `sfdc.testaccount.id`. 
Use the ID actually returned by the Salesforce connector. 
You can find the proper ID of an Account by using Salesforce Developer Console. Example query: `SELECT Id FROM Account WHERE Name = 'TestAccount'`.
# Run it! <a name="runit"/>
Simple steps to get Salesforce to Workday Financials Account to Customer Broadcast running.
See below.

## Running on premise <a name="runonopremise"/>
In this section we detail the way you should run your Anypoint Template on your computer.


### Where to Download Mule Studio and Mule ESB
First thing to know if you are a newcomer to Mule is where to get the tools.

+ You can download Mule Studio from this [Location](http://www.mulesoft.com/platform/mule-studio)
+ You can download Mule ESB from this [Location](http://www.mulesoft.com/platform/soa/mule-esb-open-source-esb)


### Importing an Anypoint Template into Studio
Mule Studio offers several ways to import a project into the workspace, for instance: 

+ Anypoint Studio generated Deployable Archive (.zip)
+ Anypoint Studio Project from External Location
+ Maven-based Mule Project from pom.xml
+ Mule ESB Configuration XML from External Location

You can find a detailed description on how to do so in this [Documentation Page](http://www.mulesoft.org/documentation/display/current/Importing+and+Exporting+in+Studio).


### Running on Studio <a name="runonstudio"/>
Once you have imported you Anypoint Template into Anypoint Studio you need to follow these steps to run it:

+ Locate the properties file `mule.dev.properties`, in src/main/resources
+ Complete all the properties required as per the examples in the section [Properties to be configured](#propertiestobeconfigured)
+ Once that is done, right click on you Anypoint Template project folder 
+ Hover you mouse over `"Run as"`
+ Click on  `"Mule Application"`


### Running on Mule ESB stand alone <a name="runonmuleesbstandalone"/>
Complete all properties in one of the property files, for example in [mule.prod.properties] (../master/src/main/resources/mule.prod.properties) and run your app with the corresponding environment variable to use it. To follow the example, this will be `mule.env=prod`. 


## Running on CloudHub <a name="runoncloudhub"/>
While [creating your application on CloudHub](http://www.mulesoft.org/documentation/display/current/Hello+World+on+CloudHub) (Or you can do it later as a next step), you need to go to Deployment > Advanced to set all environment variables detailed in **Properties to be configured** as well as the **mule.env**.
While [creating your application on CloudHub](http://www.mulesoft.org/documentation/display/current/Hello+World+on+CloudHub) (Or you can do it later as a next step), you need to go to Deployment > Advanced to set all environment variables detailed in **Properties to be configured** as well as the **mule.env**. 
Follow other steps defined [here](#runonpremise) and once your app is all set and started, there is no need to do anything else. Every time a Account (with type starting with Customer) is created or modified, it will be automatically synchronised to supplied Workday instance.

### Deploying your Anypoint Template on CloudHub <a name="deployingyouranypointtemplateoncloudhub"/>
Mule Studio provides you with really easy way to deploy your Template directly to CloudHub, for the specific steps to do so please check this [link](http://www.mulesoft.org/documentation/display/current/Deploying+Mule+Applications#DeployingMuleApplications-DeploytoCloudHub)


## Properties to be configured (With examples) <a name="propertiestobeconfigured"/>
In order to use this Mule Anypoint Template you need to configure properties (Credentials, configurations, etc.) either in properties file or in CloudHub as Environment Variables. Detail list with examples:
### Application configuration
**Application configuration**

+ poll.frequencyMillis `60000`
+ poll.startDelayMillis `500`
+ watermark.default.expression `YESTERDAY` or `2015-10-08T12:00:00.000Z`

**Salesforce Connector configuration**

+ sfdc.username `joan.baez@orgb`
+ sfdc.password `JoanBaez456`
+ sfdc.securityToken `ces56arl7apQs56XTddf34X`
+ sfdc.url `https://login.salesforce.com/services/Soap/u/32.0`

**Workday Connector configuration**

+ wdayf.user `wdayf_user`
+ wdayf.password `wdayf_password`
+ wdayf.endpoint `https://{your Workday domain}/ccx/service/{your tenant name}/Revenue_Management/v23.2`

+ wdayf.country `USA`
+ wdayf.state `USA-CA`
+ wdayf.postalCode `90001`
+ wdayf.city `San Francisco`
+ wdayf.street `Main Street 123`
+ wdayf.phone `123-4567`

# API Calls <a name="apicalls"/>
Salesforce imposes limits on the number of API Calls that can be made. However, we make API call to Salesforce only once during migration, so this is not something to worry about.


# Customize It!<a name="customizeit"/>
This brief guide intends to give a high level idea of how this Anypoint Template is built and how you can change it according to your needs.
As mule applications are based on XML files, this page will be organized by describing all the XML that conform the Anypoint Template.
Of course more files will be found such as Test Classes and [Mule Application Files](http://www.mulesoft.org/documentation/display/current/Application+Format), but to keep it simple we will focus on the XMLs.

Here is a list of the main XML files you'll find in this application:

* [config.xml](#configxml)
* [endpoints.xml](#endpointsxml)
* [businessLogic.xml](#businesslogicxml)
* [errorHandling.xml](#errorhandlingxml)


## config.xml<a name="configxml"/>
Configuration for Connectors and [Properties Place Holders](http://www.mulesoft.org/documentation/display/current/Configuring+Properties) are set in this file. **Even you can change the configuration here, all parameters that can be modified here are in properties file, and this is the recommended place to do it so.** Of course if you want to do core changes to the logic you will probably need to modify this file.

In the visual editor they can be found on the *Global Element* tab.


## businessLogic.xml<a name="businesslogicxml"/>
Functional aspect of the Template is implemented on this XML, directed by one flow that will poll for Salesforce creations/updates. The several message processors constitute four high level actions that fully implement the logic of this Template:

1. During the Input stage the Template will go to the Salesforce and query all the existing Accounts that match the filter criteria.
2. During the Process stage, each Salesforce Account ID will be checked in external ID field in Workday (Customer\_Reference\_ID), if it has an existing matching objects in Workday.
3. Then upsert of the relevant Customer fields is performed in Workday
4. Finally during the On Complete stage the Template will logoutput statistics data into the console.



## endpoints.xml<a name="endpointsxml"/>
This file is conformed by a Flow containing the Poll that will periodically query Salesforce for updated/created Accounts that meet the defined criteria in the query and then executing the batch job process with the query results.



## errorHandling.xml<a name="errorhandlingxml"/>
This is the right place to handle how your integration will react depending on the different exceptions. 
This file holds a [Choice Exception Strategy](http://www.mulesoft.org/documentation/display/current/Choice+Exception+Strategy) that is referenced by the main flow in the business logic.



