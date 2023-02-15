package com.stock.prediction.service.controller;

import com.stock.prediction.service.model.customer.CreateCustomerRequest;
import com.stock.prediction.service.model.customer.CreateCustomerResponse;
import com.stock.prediction.service.model.prediction.PredictionRequest;
import com.stock.prediction.service.model.prediction.PredictionResponse;
import com.stock.prediction.service.model.subscription.SubscriptionRequest;
import com.stock.prediction.service.model.subscription.SubscriptionResponse;
import com.stock.prediction.service.services.customer.CustomerService;
import com.stock.prediction.service.services.prediction.PredictionService;
import com.stock.prediction.service.services.subscription.SubscriptionService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1")
@RestController
@OpenAPIDefinition(info = @Info(title = "Stock Prediction Service"), tags = {@Tag(name = "Public"), @Tag(name = "Internal")})
public class CustomerController {

    private final PredictionService predictionService;
    private final SubscriptionService subscriptionService;
    private final CustomerService customerService;

    public CustomerController(PredictionService predictionService, SubscriptionService subscriptionService, CustomerService customerService) {
        this.predictionService = predictionService;
        this.subscriptionService = subscriptionService;
        this.customerService = customerService;
    }

    @Operation(summary = "Public api to predict the price of a stock in a time frame.", tags = {"Public"}, responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PredictionResponse.class)))})
    @PostMapping("/public/customers/{customerId}/predictions/stocks")
    public PredictionResponse predict(@PathVariable("customerId") String customerId, @RequestBody PredictionRequest predictionRequest){
        return this.predictionService.predict(customerId, predictionRequest);
    }

    @Operation(summary = "Public api to create a subscription for a given customer.", tags = {"Public"}, responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubscriptionResponse.class)))})
    @PostMapping("/public/customers/{customerId}/subscriptions")
    public SubscriptionResponse createSubscription(@PathVariable("customerId") String customerId, @RequestBody SubscriptionRequest subscriptionRequest){
        return this.subscriptionService.create(customerId, subscriptionRequest);
    }

    @Operation(summary = "Public api to update a subscription for a given customer.", tags = {"Public"}, responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubscriptionResponse.class)))})
    @PatchMapping("/public/customers/{customerId}/subscriptions")
    public SubscriptionResponse updateSubscription(@PathVariable("customerId") String customerId, @RequestBody SubscriptionRequest subscriptionRequest){
        return this.subscriptionService.update(customerId, subscriptionRequest);
    }

    @Operation(summary = "Internal api called by other internal services to create a customer. Not exposed to the public.", tags = {"Internal"}, responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateCustomerResponse.class)))})
    @PostMapping("/internal/customers")
    public CreateCustomerResponse createCustomer(@RequestBody CreateCustomerRequest createCustomerRequest){
        return this.customerService.create(createCustomerRequest);
    }
}
