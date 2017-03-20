package com.home.domain;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Order represents order placed by the user
 */
@JsonPropertyOrder({"orderId", "quantity", "price", "orderType", "userId"})
public class Order {

    private int orderId;
    private int userId;
    private double quantity;
    private int price;
    private OrderType orderType;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (orderId != order.orderId) return false;
        if (userId != order.userId) return false;
        if (Double.compare(order.quantity, quantity) != 0) return false;
        if (price != order.price) return false;
        return orderType == order.orderType;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = orderId;
        result = 31 * result + userId;
        temp = Double.doubleToLongBits(quantity);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + price;
        result = 31 * result + orderType.hashCode();
        return result;
    }
}
