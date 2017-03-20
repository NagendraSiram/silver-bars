package com.home.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Test class for Order
 */
public class OrderTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void checkOrderObjectIsSerialized() throws Exception {
        Order order = new Order();
        order.setUserId(1);
        order.setOrderId(100);
        order.setPrice(303);
        order.setQuantity(3.5);
        order.setOrderType(OrderType.SELL);

        String expectedJsonString = "{\"orderId\":100,\"quantity\":3.5,\"price\":303,\"orderType\":\"SELL\",\"userId\":1}";
        assertThat(mapper.writeValueAsString(order)).isEqualTo(expectedJsonString);
    }

    @Test
    public void testOrderObjectDeserializeTest() throws Exception {
        String jsonString = "{\"orderId\":100,\"quantity\":3.5,\"price\":303,\"orderType\":\"SELL\",\"userId\":1}";

        Order order = mapper.readValue(jsonString, Order.class);
        assertThat(order.getOrderId()).isEqualTo(100);
        assertThat(order.getQuantity()).isEqualTo(3.5);
        assertThat(order.getPrice()).isEqualTo(303);
        assertThat(order.getOrderType()).isEqualTo(OrderType.SELL);
        assertThat(order.getUserId()).isEqualTo(1);
    }

}