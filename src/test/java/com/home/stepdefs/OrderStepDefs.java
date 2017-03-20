package com.home.stepdefs;

import com.home.domain.Order;
import com.home.domain.OrderType;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by nagendra on 19/03/2017.
 */
public class OrderStepDefs extends AbstractDefs {

    int previousCreatedOrderId;

    @Given("^A user (\\d+) registers an (SELL|BUY) order for (\\d+)\\.(\\d+) kg for a price of (\\d+)£$")
    public void a_user_registers_an_order_for_a_price(int userId, String orderType, int beforeDecimal, int afterDecimal, int price) throws Throwable {
        Order order = new Order();
        order.setOrderType(OrderType.valueOf(orderType));
        order.setQuantity(beforeDecimal + afterDecimal / 100);
        order.setPrice(price);

        executePost(userId + "/orders", order);

        previousCreatedOrderId = (Integer) lastResponseEntity.getBody();
    }

    @Then("^the user receives status code of (\\d+)$")
    public void the_user_receives_status_code_of(int statusCode) throws Throwable {
        checkStatusCode(statusCode);
    }

    @Then("^the user receives an order id$")
    public void the_user_receives_an_order_id() throws Throwable {
        checkResponseBodyHasOrderId();
    }

    @When("^the user (\\d+) cancels the registered order$")
    public void the_user_cancels_the_registered_order(int userId) throws Throwable {
        executeDelete(userId + "/orders/" + previousCreatedOrderId);
    }

    @Then("^the user receives as NOT FOUND$")
    public void the_user_receives_as_not_found() throws Throwable {
        assertThat(lastException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
