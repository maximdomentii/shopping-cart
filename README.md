# Shopping Cart

Simple application that calculates the price of a shopping basket.

## Prerequisite

- Java 8 or later
- Maven
- Docker
- docker-compose

## Build and run

1. Get the repo on your local machine
2. cd path_to_repo
3. mvn clean install

    <b>Note:</b> Make sure you can run 'docker ps' first. There can be a problem with permissions denied on docker if 
    you just installed is and using for the first time. If you have used docker before with current user on current 
    machine then should be fine.
 
 4. docker-compose up [--scale SERVICE_NAME=NUM_OF_INSTANCES]
 
    <b>Note:</b> After all containers are up then you can check in the Eureka dashboard, at localhost:8761, 
    registered services. Login with admin/admin. You have to find the zuul-server, auth-service and shopping-cart-service.
    
 5. Now you can access the Shopping Cart client at localhost:8080 to test the application or check the integration tests
    from shopping-cart-service component. 
 
    <b>Note:</b> For the Shopping Cart client you will have to login with cashier/cashier.
    
## Use case

<b>DESCRIPTION</b>
   
Develop a simple program that calculates the price of a shopping basket.
 
<b>REQUIREMENTS</b>
   
1. The output shall be displayed similar to what you would expect to see on a receipt.
2. Items are presented one at a time, in a list, identified by name - for example "Apple" or “Banana".
3. Given a shopping list with items, calculate the total cost of those items.
4. The basket can contain any item multiple times. Items are priced as follows:   
    - Apples are 25 ct. each.
    - Oranges are 30 ct. each.
    - Bananas are 15 ct. each.
    - Papayas are 50 ct. each but are available as 'three for the price of two'.
        
## Implementation
For the above usecase I have implemented a shopping-cart-service with spring-boot, hibernate and postgres.
In postgres I have created a database with 3 tables: product, discount_rule and product_discount_rule_mapping where I 
have stored data and configured discout.
sopping-cart-service expose and API to check if a product is availabe by a barcode and to calculate the receipt for a 
given list of barcodes.
The functionality of the service is tested with unit and integration tests.
    
I also have implemented a shopping-cart-client with ReactJS which provide a user friendly way of consuming the API and 
displaying the receipt.
    
The shopping-cart-service is integrated in a microservice arhitecture with spring-cloud-netflix with Eureka service 
discovery, Zuul intelligent routing and Ribbon load balancing. Zuul is also used as an API Getaway.
    
I have implemented also an addition functionality for cashier authentication with spring-boot, JavaWebToken and 
hibernate and postgres. In postgres are stored the cashier credentials and roles. The authentication is implemented with
JWT. First you have to login with and username and password. The auth-service will check credentials and will provide a 
token which can be used then to access the shopping-cart-service. All request are filtered by the zuul-server which 
check is request contains a valid token.