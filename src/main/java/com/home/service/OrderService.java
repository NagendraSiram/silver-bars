package com.home.service;

import com.home.domain.Order;
import com.home.domain.OrderType;
import javafx.util.Pair;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * OrderService accepts new orders, create orders and also provide query services
 */
@Component
public class OrderService {

    /*
        To Generate unique id for order
     */
    private static AtomicInteger idGenerator = new AtomicInteger(1);

    /*
     Holds the orders
     */
    private static final List<Order> orders = Collections.synchronizedList(new LinkedList<Order>());

    public int register(int userId, Order order) {
        order.setUserId(userId);
        order.setOrderId(idGenerator.incrementAndGet());
        orders.add(order);
        return order.getOrderId();
    }

    public boolean cancel(int userId, int orderId) {
        return orders.removeIf(filterByUserId(userId).and(filterByOrderId(orderId)));
    }

    public Optional<Order> get(int userId, int orderId) {
        return orders.stream()
                .filter(filterByUserId(userId).and(filterByOrderId(orderId)))
                .findAny();
    }

    public List<Pair<Integer, Double>> summary(OrderType orderType) {
        Map<Integer, Double> ordersOfType = orders.stream()
                .filter(o -> orderType.equals(o.getOrderType()))
                .collect(Collectors.groupingBy(Order::getPrice, Collectors.summingDouble(Order::getQuantity)));

        return ordersOfType.entrySet()
                .stream()
                .sorted(getComparator(orderType))
                .map(e -> new Pair<>(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    /*
        This method is used for Junit tests to reset the static maps
    */
    public void reset() {
        orders.clear();
    }

    private Predicate<Order> filterByUserId(int userId) {
        return o -> userId == o.getUserId();
    }

    private Predicate<Order> filterByOrderId(int orderId) {
        return o -> orderId == o.getOrderId();
    }

    private Comparator<Map.Entry<Integer, Double>> getComparator(OrderType orderType) {
        Comparator<Map.Entry<Integer, Double>> comparator = Comparator.comparing(Map.Entry::getKey);
        if (orderType == OrderType.SELL) {
            return comparator;
        } else {
            return comparator.reversed();
        }
    }
}
