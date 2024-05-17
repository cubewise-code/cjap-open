# OpenCJAPAuth


### Instruction
This project provides an IBM Cognos Analytics (CA) authentication solution based on Custom Java Authentication provider([CJAP](https://www.ibm.com/support/pages/ibm-cognos-custom-authentication)). 



In CJAP authentication flow, a CA provider accepts username and password from login request, executes customized authentication logic to validate user info. The authentication result can be used by IBM Cognos TM1 Applications with IBM Cognos BI security.

To implement CJAP authentication flow, OpenCJAPAuth provides **OpenCJAPAuthBase** project and packaged jar file as a general CJAP Provider. And **AuthHandlers** includes sample projects and packaged jar files to implement specific authentication logic in the provider. 

Project code and packaged jar have been tested with CA version: 11.1, 11.2, 12.0.

---
### How to use
1. Prepare AuthHandler.jar: for **AdminInConfigFile, AzureKeyVault and MicrosoftCredentialManager** cases, directly use AuthHandler.jar in AuthHandler\dist; for **OAuth and AuthService** cases, complete authentication request logic in project and build jar file. 

2. Update configuration in AuthHandler.jar: Open archive with unzip software like 7-Zip, edit conf.properties and save.

3. Put OpenCJAPAuthBase.jar, AuthHandler.jar (and dependency jar files in AuthHandler\dist folder) in [CA install path]\webapps\p2pd\WEB-INF\lib. 

4.  [Create namespace](https://www.ibm.com/docs/en/cognos-analytics/11.1.0?topic=provider-configure-custom-java-authentication-namespace) with:

    Type = **Custom Java Authentication provider**

    Java Class Name = **OpenCJAPAuth.Provider**

5. Test namespace and restart CA.

Notice: use administrator privileges to config and restart CA.

---
### Project update and package

1. Open project with IntelliJ, set SDK (Java version >= 1.8) in Project Settings.

2. Update conf.properties file if needed.

3. Update AuthHandler execute function code if needed.

4. Build Artifacts, generate jar file in output path.

Notice: To build OpenCJAPAuthBase project, need [CA install path]\p2pd\WEB-INF\lib as denpendency. 

---
### AuthHandlers

AuthHandler project and packaged jar for several authentication cases. 

* **AdminInConfigFile**
    Authentication with username & password configured in conf.properties file. It is useful to generate a backend service account.

* **AzureKeyVault**
    Authentication with username in conf.properties and password from Azure Key Vault.

* **AuthService**
    Authentication with http request to a service.

* **OAuth**
    Authentication with OAuth flow:
    1. Jumps to OAuth login page from CA;
    2. Redirect to CA login page with OAuth token;
    3. Provider request user with token;
    
    A Custom CA login page also included to implement frontend OAuth flow.

* **MicrosoftCredentialManager**
    Authentication with username and password stored in Microsoft Credential Manager.


