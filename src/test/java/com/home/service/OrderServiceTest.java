package com.home.service;

import com.home.domain.Order;
import com.home.domain.OrderType;
import javafx.util.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Test class for OrderService class
 */
public class OrderServiceTest {

    private OrderService orderService;

    @Before
    public void setup() {
        orderService = new OrderService();
    }

    @Test
    public void registerAnOrder() {
        int userId = 1;

        Order order = constructOrder(3.5, 303, OrderType.SELL);

        int orderId = orderService.register(userId, order);

        Optional<Order> orderFetched = orderService.get(userId, orderId);

        assertThat(orderFetched.isPresent()).isTrue();
        assertThat(orderFetched.get().getUserId()).isEqualTo(userId);
        assertThat(orderFetched.get().getOrderId()).isEqualTo(orderId);
        assertThat(orderFetched.get().getQuantity()).isEqualTo(order.getQuantity());
        assertThat(orderFetched.get().getPrice()).isEqualTo(order.getPrice());
        assertThat(orderFetched.get().getOrderType()).isEqualTo(order.getOrderType());
    }

    @Test
    public void getOrderThatIsNotPresent() {
        int userId = 1;

        Optional<Order> orderFetched = orderService.get(userId, 100000);

        assertThat(orderFetched.isPresent()).isFalse();
    }

    @Test
    public void getOrderForNonMatchingUserId() {
        int userId = 1;

        Order order = constructOrder(3.5, 303, OrderType.SELL);

        int orderId = orderService.register(userId, order);

        Optional<Order> orderFetched = orderService.get(2, orderId);

        assertThat(orderFetched.isPresent()).isFalse();
    }

    @Test
    public void cancelOrderThatIsPlaced() {
        int userId = 1;

        Order order = constructOrder(3.5, 303, OrderType.SELL);

        int orderId = orderService.register(userId, order);

        boolean cancel = orderService.cancel(userId, orderId);

        assertThat(cancel).isTrue();

        Optional<Order> orderFetched = orderService.get(userId, orderId);

        assertThat(orderFetched.isPresent()).isFalse();
    }

    @Test
    public void cancelOrderThatIsNotBelongToUser() {
        int userId = 1;

        Order order = constructOrder(3.5, 303, OrderType.SELL);

        int orderId = orderService.register(userId, order);

        boolean cancel = orderService.cancel(2, orderId);

        assertThat(cancel).isFalse();
    }

    @Test
    public void cancelANonExistingOrder() {
        int userId = 1;
        int nonExistingOrder = 100000;

        boolean cancel = orderService.cancel(2, nonExistingOrder);

        assertThat(cancel).isFalse();
    }

    @Test
    public void summaryForSell() {
        orderService.register(1, constructOrder(3.5, 325, OrderType.SELL));
        orderService.register(1, constructOrder(1.5, 303, OrderType.SELL));
        orderService.register(1, constructOrder(2.5, 320, OrderType.SELL));
        orderService.register(1, constructOrder(3.5, 303, OrderType.SELL));
        orderService.register(1, constructOrder(6.5, 310, OrderType.SELL));

        List<Pair<Integer, Double>> actualSummary = orderService.summary(OrderType.SELL);

        List<Pair<Integer, Double>> expectedSummary = Arrays.asList(new Pair<>(303, 5.0)
                , new Pair<>(310, 6.5), new Pair<>(320, 2.5), new Pair<>(325, 3.5));

        assertThat(actualSummary).containsExactlyElementsOf(expectedSummary);
    }

    @Test
    public void summaryForBuy() {
        orderService.register(1, constructOrder(3.5, 325, OrderType.BUY));
        orderService.register(1, constructOrder(1.5, 303, OrderType.BUY));
        orderService.register(1, constructOrder(2.5, 320, OrderType.BUY));
        orderService.register(1, constructOrder(3.5, 303, OrderType.BUY));
        orderService.register(1, constructOrder(6.5, 310, OrderType.BUY));

        List<Pair<Integer, Double>> actualSummary = orderService.summary(OrderType.BUY);

        List<Pair<Integer, Double>> expectedSummary = Arrays.asList(new Pair<>(325, 3.5)
                , new Pair<>(320, 2.5), new Pair<>(310, 6.5), new Pair<>(303, 5.0));

        assertThat(actualSummary).containsExactlyElementsOf(expectedSummary);
    }

    private Order constructOrder(double quantity, int price, OrderType orderType) {
        Order order = new Order();
        order.setQuantity(quantity);
        order.setPrice(price);
        order.setOrderType(orderType);
        return order;
    }

    @After
    public void tearDown() {
        orderService.reset();
    }
}