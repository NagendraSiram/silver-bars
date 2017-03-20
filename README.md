# Silver bars

* This code is developed using java 1.8 on intellj IDE 

* Minimum System requirement to compile and run the application
1. JDK 1.8
2. Maven
3. Access to internet to download the required artifacts

* In order to compile the code, running junit & cucumber test and package for this project run "mvn clean install" (without quotes) from command line
* To run the application as standalone "java -jar target/silver-bars-1.0-SNAPSHOT"

Design considerations

* Registering an order returns order-id which is used to cancel the already placed order.
* user-id is part of the url.
* price is taken as integer so as in examples.
* Provided a 'reset' REST call for cucumber tests to reset the orders placed.
