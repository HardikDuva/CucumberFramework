# README #

This framework is capable of execution on the command line using maven, or through TestNG using the suite files.

### What is this repository for? ###

* Quick summary
	This framework drives cucumber feature files through web browsers using Selenium WebDriver, Selenium Grid and Docker. The tests are executed in parallel using TestNG and Grid.

### How do I get set up? ###

* Summary of set up
	-You will need to have Chrome, Firefox, Edge and any other browsers installed on the system in their default locations (Should be managed by the company, request any you don't have installed). 
	-You will also need the WebDriver for that browser or saved in the dependencies/webdrivers/grid/jar folder with the selenium grid jar (if you plan to run grid locally).
	-If you have docker set-up in your machine then run compose file located in dependencies/Docker/docker.compose.yml and then you can execute your xml file
	-You will need Maven and Java 17+ installed on the system, and of course an IDE of some kind (we recommend IntelliJ IDEA but the choie is yours)
* Configuration
	The configuration files: 
		* logback.xml
			use to configure the way logback logs. For instance setting the logging level.
		* fw_config.properties
			use to set the length of static waits, filepaths, and URI's for reports, artefacts and grid
* Dependencies
	* Selenium WebDriver
	* Selenium Grid (Docker)
	* Java
	* Maven
	* Logback
	* Rest-Assured
	* TestNG
	* Cucumber
	* Docker
	
* How to run tests
	Tests can be executed from the command line using maven.
	- i.e. mvn test (this will execute all regression tests)
	Tests can also be executed for any suite by using the following syntax
	- Alternatively you can execute any of the Suite XML files from the IDE directly
	Tests are expecting there to be a Grid up and running with a node they can use
	- This is currently achieved using Docker on the local Machine
	- To run a grid locally use the packaged batch files and provide the appropriate jar and WebDrivers
* Deployment instructions
	- mvn install
	- mvn test (optionally include suite xml file name here)

### Who do I talk to? ###

* Developer
	- Hardik Duva - Automation Engineer

