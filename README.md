
# Anypoint Template: Salesforce to Workday Financials Account to Customer Broadcast	

<!-- Header (start) -->

<!-- Header (end) -->

# License Agreement
This template is subject to the conditions of the <a href="https://s3.amazonaws.com/templates-examples/AnypointTemplateLicense.pdf">MuleSoft License Agreement</a>. Review the terms of the license before downloading and using this template. You can use this template for free with the Mule Enterprise Edition, CloudHub, or as a trial in Anypoint Studio. 
# Use Case
<!-- Use Case (start) -->
As a Salesforce admin I want to synchronize Accounts and Customers between Salesfoce and Workday.

This template serves as a foundation for setting an online sync of Accounts from SalesForce instance to Customer in Workday instance. Every time there is a new Account or a change in an already existing one with type stating with the string Customer, the integration will poll for changes in SalesForce source instance and it will be responsible for updating the Customer in the target Workday instance.

Requirements have been set not only to be used as examples, but also to establish a starting point to adapt your integration to your requirements.

As implemented, this template leverage the Mule batch module

The integration is triggered by a scheduler defined in the flow that is going to trigger the application, querying newest SalesForce updates/creations matching a filter criteria and executing the batch job.
During the Process stage, each Salesforce Account is checked, if it has an existing matching Customer in the Workday instance.
The last step of the Process stage will inserts or updates the Customers in Workday.
Finally during the On Complete stage the Template logs output statistics data into the console.
<!-- Use Case (end) -->

# Considerations
<!-- Default Considerations (start) -->

<!-- Default Considerations (end) -->

<!-- Considerations (start) -->
To make this template run, there are certain preconditions that must be considered. All of them deal with the preparations in both source (Salesforce) and destination (Workday) systems, that must be made for the template to run smoothly. 
**Failing to do so could lead to unexpected behavior of the template.**

This template illustrates the broadcast use case between Salesforce and a Workday, thus it requires a Workday instance to work.
<!-- Considerations (end) -->



## Salesforce Considerations

Here's what you need to know about Salesforce to get this template to work:

- Where can I check that the field configuration for my Salesforce instance is the right one? See: <a href="https://help.salesforce.com/HTViewHelpDoc?id=checking_field_accessibility_for_a_particular_field.htm&language=en_US">Salesforce: Checking Field Accessibility for a Particular Field</a>.
- Can I modify the Field Access Settings? How? See: <a href="https://help.salesforce.com/HTViewHelpDoc?id=modifying_field_access_settings.htm&language=en_US">Salesforce: Modifying Field Access Settings</a>.

### As a Data Source

If the user who configured the template for the source system does not have at least *read only* permissions for the fields that are fetched, then an *InvalidFieldFault* API fault displays.

```
java.lang.RuntimeException: [InvalidFieldFault [ApiQueryFault 
[ApiFault  exceptionCode='INVALID_FIELD'
exceptionMessage='Account.Phone, Account.Rating, Account.RecordTypeId, 
Account.ShippingCity
^
ERROR at Row:1:Column:486
No such column 'RecordTypeId' on entity 'Account'. If you are attempting to 
use a custom field, be sure to append the '__c' after the custom field name. 
Reference your WSDL or the describe call for the appropriate names.'
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

When creating new Workday customer, one of existing customer categories must be chosen. In order to do this, this template uses lookup table with pre-defined mappings between Salesforce data (Industry) and Workday Customer Category ID.
Workday customer categories can be user defined, therefore the mapping must be edited by user manually before running this template.

To view all the Workday Customer Categories, please type "customer categories" into search field in Workday system and select Customer Categories report.
In the report you should see all the customer categories available. Select Integration IDs -> View Integration IDs from available actions of each customer
category and update the function *industryToCustomerCategoryMapping* in the template's DataWeave component 'Prepare put customer request' manually.
The default customer category ID can be edited inside the same DataWeave component in the row where customer category ID is set. This category will be used if the source Account's Industry
value does not match any defined mappings in the function.

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

+ [Download Anypoint Studio](https://www.mulesoft.com/platform/studio)
+ [Download Mule runtime](https://www.mulesoft.com/lp/dl/mule-esb-enterprise)

**Note:** Anypoint Studio requires JDK 8.
<!-- Where to download (start) -->

<!-- Where to download (end) -->

### Importing a Template into Studio
In Studio, click the Exchange X icon in the upper left of the taskbar, log in with your Anypoint Platform credentials, search for the template, and click Open.
<!-- Importing into Studio (start) -->

<!-- Importing into Studio (end) -->

### Running on Studio
After you import your template into Anypoint Studio, follow these steps to run it:

+ Locate the properties file `mule.dev.properties`, in src/main/resources.
+ Complete all the properties required as per the examples in the "Properties to Configure" section.
+ Right click the template project folder.
+ Hover your mouse over `Run as`.
+ Click `Mule Application (configure)`.
+ Inside the dialog, select Environment and set the variable `mule.env` to the value `dev`.
+ Click `Run`.
<!-- Running on Studio (start) -->

<!-- Running on Studio (end) -->

### Running on Mule Standalone
Update the properties in one of the property files, for example in mule.prod.properties, and run your app with a corresponding environment variable. In this example, use `mule.env=prod`. 


## Running on CloudHub
When creating your application in CloudHub, go to Runtime Manager > Manage Application > Properties to set the environment variables listed in "Properties to Configure" as well as the mule.env value.
<!-- Running on Cloudhub (start) -->
While [creating your application on CloudHub](http://www.mulesoft.org/documentation/display/current/Hello+World+on+CloudHub) (Or you can do it later as a next step), you need to go to Deployment > Advanced to set all environment variables detailed in **Properties to be configured** as well as the **mule.env**. 
Follow other steps defined [here](#runonpremise) and once your app is all set and started, there is no need to do anything else. Every time a Account (with type starting with Customer) is created or modified, it will be automatically synchronised to supplied Workday instance.
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

+ scheduler.frequency= `60000`
+ scheduler.start.delay= `500`
+ watermark.default.expression ` (now() - |PT5M|) as String` or ` 2018-01-02T15:53:00Z`

**Salesforce Connector Configuration**

+ sfdc.username `joan.baez@orgb`
+ sfdc.password `JoanBaez456`
+ sfdc.securityToken `ces56arl7apQs56XTddf34X`

**Workday Connector Configuration**

+ wdayf.username `joan`
+ wdayf.tenant `acme_pt1`
+ wdayf.password `joanPass123`
+ wdayf.host `your_impl-cc.workday.com`

+ wdayf.country `USA`
+ wdayf.state `USA-CA`
+ wdayf.postalCode `90001`
+ wdayf.city `San Francisco`
+ wdayf.street `Main Street 123`
+ wdayf.phone `123-4567`
<!-- Application Configuration (end) -->

# API Calls
<!-- API Calls (start) -->
Salesforce imposes limits on the number of API Calls that can be made. However, we make API call to Salesforce only once during migration, so this is not something to worry about.
<!-- API Calls (end) -->

# Customize It!
This brief guide provides a high level understanding of how this template is built and how you can change it according to your needs. As Mule applications are based on XML files, this page describes the XML files used with this template. More files are available such as test classes and Mule application files, but to keep it simple, we focus on these XML files:

* config.xml
* businessLogic.xml
* endpoints.xml
* errorHandling.xml<!-- Customize it (start) -->

<!-- Customize it (end) -->

## config.xml
<!-- Default Config XML (start) -->
This file provides the configuration for connectors and configuration properties. Only change this file to make core changes to the connector processing logic. Otherwise, all parameters that can be modified should instead be in a properties file, which is the recommended place to make changes.<!-- Default Config XML (end) -->

<!-- Config XML (start) -->

<!-- Config XML (end) -->

## businessLogic.xml
<!-- Default Business Logic XML (start) -->
Functional aspect of the Template is implemented on this XML, directed by one flow that will poll for Salesforce creations/updates. The several message processors constitute four high level actions that fully implement the logic of this Template:

1. Firstly the Template will go to the Salesforce and query all the existing Accounts that match the filter criteria.
2. During the Process stage, each Salesforce Account ID is checked in external ID field in Workday (Customer\_Reference\_ID), if it has an existing matching objects in Workday.
3. Then upsert of the relevant Customer fields is performed in Workday
4. Finally during the On Complete stage the Template logs output statistics data into the console.<!-- Default Business Logic XML (end) -->

<!-- Business Logic XML (start) -->

<!-- Business Logic XML (end) -->

## endpoints.xml
<!-- Default Endpoints XML (start) -->
This file is conformed by a Flow containing the Scheduler that will periodically query Salesforce for updated/created Accounts that meet the defined criteria in the query and then executing the batch job process with the query results.<!-- Default Endpoints XML (end) -->

<!-- Endpoints XML (start) -->

<!-- Endpoints XML (end) -->

## errorHandling.xml
<!-- Default Error Handling XML (start) -->
This file handles how your integration reacts depending on the different exceptions. This file provides error handling that is referenced by the main flow in the business logic.<!-- Default Error Handling XML (end) -->

<!-- Error Handling XML (start) -->

<!-- Error Handling XML (end) -->

<!-- Extras (start) -->

<!-- Extras (end) -->
