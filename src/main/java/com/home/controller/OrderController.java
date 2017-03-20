package com.home.controller;

import com.home.domain.Order;
import com.home.domain.OrderType;
import com.home.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * OrderController is a REST interface to submit & query orders.
 */
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    /*
      To register an order
    */
    @RequestMapping(value = "/v1.0/{userId}/orders", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity register(@PathVariable(value = "userId") int userId, @RequestBody Order order) {
        int orderId = orderService.register(userId, order);
        return new ResponseEntity(orderId, HttpStatus.CREATED);
    }

    /*
      To Cancel an order
     */
    @RequestMapping(value = "/v1.0/{userId}/orders/{orderId}", method = RequestMethod.DELETE)
    public ResponseEntity cancel(@PathVariable(value = "userId") int userId, @PathVariable(value = "orderId") int orderId) {
        HttpStatus httpStatus = null;
        if (orderService.cancel(userId, orderId)) {
            httpStatus = HttpStatus.OK;
        } else {
            httpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity(httpStatus);
    }

    /*
        To get the summary of orders
    */
    @RequestMapping(value = "/v1.0/orders", method = RequestMethod.GET)
    public ResponseEntity cancel(@RequestParam(value = "orderType", required = true) OrderType orderType) {
        return new ResponseEntity(orderService.summary(orderType), HttpStatus.OK);
    }

    @RequestMapping(value = "/v1.0/reset", method = RequestMethod.GET)
    public ResponseEntity reset() {
        orderService.reset();
        return new ResponseEntity(HttpStatus.OK);
    }
}
