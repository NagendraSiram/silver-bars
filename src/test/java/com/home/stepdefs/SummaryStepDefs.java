package com.home.stepdefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.domain.Order;
import com.home.domain.OrderType;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by nagendra on 20/03/2017.
 */
public class SummaryStepDefs extends AbstractDefs {

    @Given("^The following orders are registered$")
    public void the_following_orders_are_registered(List<Map<String, String>> maps) {

        maps.stream().forEach(e -> {
            Order order = new Order();
            order.setOrderType(OrderType.valueOf(e.get("orderType")));
            order.setQuantity(Double.parseDouble(e.get("quantity")));
            order.setPrice(Integer.parseInt(e.get("price")));

            executePost(e.get("userId") + "/orders", order);
        });

    }

    @When("^the summary information for (SELL|BUY) is requested$")
    public void the_summary_information_is_requested(String orderType) throws Throwable {
        executeGet("/orders", orderType);
    }

    @Then("^the live dash board should display the following$")
    public void the_live_dashboard_should_display_the_following(List<Map<String, String>> table) throws Throwable {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> response = objectMapper.readValue(lastResponseEntity.getBody().toString(), List.class);

        List<Pair<Integer, Double>> actualSummary = response.stream()
                .map(item -> new Pair<>((Integer) item.get("key"), (Double) item.get("value")))
                .collect(Collectors.toList());

        List<Pair<Integer, Double>> expectedSummary = table.stream()
                .map(row -> new Pair<>(Integer.parseInt(row.get("price")), Double.parseDouble(row.get("quantity"))))
                .collect(Collectors.toList());

        assertThat(actualSummary).containsExactlyElementsOf(expectedSummary);
    }
}
