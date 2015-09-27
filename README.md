# wmode-challenge
AppDirect Integration Challenge


Demo application that handles AppDirect integration events.

Production instance can be found at http://appdirect.tschoend.com

It is pending approval but should be found on the AppDirect store at https://www.appdirect.com/apps/42708#!overview

Functionality implemented:

- Buy/Upgrade/Cancel subscription
- Assign/Unassign users
- SSO through AppDirect OpenID

### Building:
The framework I used (DropWizard) embeds a jetty server, so the build produces a .jar file. Maven is configured to produce a 
fat jar in the target/ subdirectory.

### Running:
After running `mvn package`, update the configuration example in config/example.yaml. 
Then run `java -jar target/com.tschoend.wmode-challenge-0.0.1-SNAPSHOT.jar server <path to config>`

The application was developed against PostgreSQL 9.3 and has not been tested against other databases.
