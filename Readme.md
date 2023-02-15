# Stock Prediction Service

# Introduction
This is implementation of a home assignment for an interview. Unfortunately nothing materialized with this company, but I put some effort on this project and I want to share the solution.
The exercise is a fictive one, and the assignment information was classified as public. 

# Requirement
**Context**

Imagine the Company designed on algorithm allowing to predict future stocks prices. The solution is rapidly getting popular and decision was made to commercialize it. Company plans to introduce a customer pricing model.
Design and implement a reusable REST API application exposing the prediction mechanism to customers wrapped in a subscription model. 

There are three plans to choose from:
- DEMO
  - call max. 10 different stocks/month
  - max. 1000 calls/month
- SILVER
  - call max. 100 different stocks/month
  - 1 coll/minute
- GOLD
  - call any stock
  - 1 call/10 seconds

**Requirements:**
- REST API takes as input stock ticker and a future time frame for which prediction happens.
- Limit inbound requests according to the plan customer purchased.
- User can have only one subscription at a time. Use user Basic Authorization for simplicity
- User can change the subscription once a month
- Results for a single stock per call are returned Prediction algorithm is implemented by returning a real stock price sourced from live, external REST API and adding a random number from -100 to 100 range for a given time frame. In fact, any external REST API serving numbers would do to mimic the pricing endpoint. Currency is fixed to USD.
- Expectations All requirements are implemented
- Application connects to live, external REST endpoint in order to get
- Solution is runnable and can be tested with postmon, curl etc.
- Solution is written in Java 11, Spring Boot and other, necessary libraries Code is following industry quality standards (TDD, clean code principles, object oriented design)
- Simplicity is the general solution driver. Use mocks andor in-memory solutions etc. if third party needs to be mimicked.

**Bonus**
  - What if the service needs to scale? How would you scale the service?
  - How would you improve already designed solution?

## Service Architecture
The service is a multi maven module project. With 3 modules:
- **stock-prediction-service**
   - contains all business logic related classes 
- **stock-prediction-service-integration-api**
   - contains a well defined set of interfaces with respective dtos. The idea here is to separate the integration layer from business layer with interfaces. This will give us flexibility in the future if we want to change ex: the database. 
- **stock-prediction-service-integration**
   - contains the specific implementation of the api. 

## Running

### Requirements
- java 11
- maven >= 3.0.5 (can work also with older version of maven, but this is the version that i used)

### Steps
- Run ```mvn clean install```
- Run ```java -jar ./stock-prediction-service/target/stock-prediction-service-0.0.1-SNAPSHOT.jar```
- Application should start on port 8080

## Testing
- Access ``http://localhost:8080/swagger-ui/index.html``
- Use username: testUser, and password: S3cret!
- Use internal endpoint to create user: ```POST http://localhost:8080/v1/internal/customers```
- Use public endpoint to create subscription: ```POST http://localhost:8080/v1/public/customers/c85781d9-f8bb-4dee-8563-09372d6fe616/subscriptions```
- Use public endpoint to update subscription: ```PATCH http://localhost:8080/v1/public/customers/c85781d9-f8bb-4dee-8563-09372d6fe616/subscriptions```
- Use public endpoint to predict price: ```POST http://localhost:8080/v1/public/customers/c85781d9-f8bb-4dee-8563-09372d6fe616/predictions/stocks```

For testing with postman you need to add the Authorization header: ```Authorization: Basic dGVzdFVzZXI6UzNjcmV0IQ==```

```
curl --location --request POST 'http://localhost:8080/v1/internal/customers' \
--header 'Authorization: Basic dGVzdFVzZXI6UzNjcmV0IQ==' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=5E802A4D71D03D3BF5D662A67909EB14' \
--data-raw '{
  "firstName": "kristo",
  "lastName": "godari"
}
```

# Bonus Answers
- **What if service needs to scale?**
  - Currently, I'm using in memory solution for rate limiter. This will not work if we have multiple instances and we are in a distributed environment.
  - We need a distributed rate limiter. There are multiple solutions here, like sticky session on load balancer, but this has also a lot of problems.
  - I would use Redis,for bucked storage and management. The drawback of this solution is that we will need to implement transactions for redis operations. It adds a little of complexity but is a more robust solution.
- **How would you scale the service?**
  - I would use docker and then use kubernetes. 
  - We can use kubernetes autoscaling. 
- **How would you improve already designed solution?**
  - hmmm.. I would do a lot of improvements :))
  - Security with Oauth2
  - Retry for external calls + circuit breaker
  - Add health endpoints that can be used by kubernetes to kill a pod if is not unhealthy
  - Expose custom metrics with micrometer for prometheus
  - Enhance logging
  - Improve databse model, to be more optimized. Maybe use NoSQL?
  - Add more unit tests
  - Add functional tests
  - Add contract tests
  - Do performance test to see if the solution is optimized
  - etc..
