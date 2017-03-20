package com.home.controller;

import com.home.domain.Order;
import com.home.domain.OrderType;
import com.home.service.OrderService;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Mock Test class for OrderController.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class OrderControllerTest {

    private MockMvc mvc;

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    public void registerAnOrder() throws Exception {
        int userId = 1;

        when(orderService.register(eq(userId), any(Order.class))).thenReturn(1001);

        String jsonRequestBody = "{" +
                "\"quantity\": 3.5," +
                "\"price\": 303," +
                "\"orderType\": \"SELL\"" +
                "}";
        mvc.perform(MockMvcRequestBuilders
                .post(String.format("/v1.0/%s/orders", Integer.toString(userId)))
                .content(jsonRequestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string("1001"));

        verify(orderService, times(1)).register(eq(userId), any(Order.class));
    }

    @Test
    public void cancelAnExistingOrder() throws Exception {
        int userId = 1;
        int orderId = 100;

        when(orderService.cancel(eq(userId), eq(orderId))).thenReturn(Boolean.TRUE);

        mvc.perform(MockMvcRequestBuilders
                .delete(String.format("/v1.0/%s/orders/%s", Integer.toString(userId), Integer.toString(orderId)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(orderService, times(1)).cancel(eq(userId), eq(orderId));
    }

    @Test
    public void cancelNonExistingOrder() throws Exception {
        int userId = 1;
        int orderId = 100;

        when(orderService.cancel(eq(userId), eq(orderId))).thenReturn(Boolean.FALSE);

        mvc.perform(MockMvcRequestBuilders
                .delete(String.format("/v1.0/%s/orders/%s", Integer.toString(userId), Integer.toString(orderId)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(orderService, times(1)).cancel(eq(userId), eq(orderId));
    }


    @Test
    public void summaryForSell() throws Exception {

        when(orderService.summary(OrderType.SELL)).thenReturn(Arrays.asList(new Pair<>(303, 5.0)
                , new Pair<>(310, 6.5), new Pair<>(320, 2.5), new Pair<>(325, 3.5)));

        String expectedJson = "[{\"key\":303,\"value\":5.0},{\"key\":310,\"value\":6.5},{\"key\":320,\"value\":2.5},{\"key\":325,\"value\":3.5}]";

        mvc.perform(MockMvcRequestBuilders
                .get("/v1.0/orders")
                .param("orderType", "SELL")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(expectedJson));

        verify(orderService, times(1)).summary(OrderType.SELL);
    }

    @Test
    public void summaryForBuy() throws Exception {

        when(orderService.summary(OrderType.BUY)).thenReturn(Arrays.asList(new Pair<>(325, 3.5)
                , new Pair<>(320, 2.5), new Pair<>(310, 6.5), new Pair<>(303, 5.0)));

        String expectedJson = "[{\"key\":325,\"value\":3.5},{\"key\":320,\"value\":2.5},{\"key\":310,\"value\":6.5},{\"key\":303,\"value\":5.0}]";

        mvc.perform(MockMvcRequestBuilders
                .get("/v1.0/orders")
                .param("orderType", "BUY")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(expectedJson));

        verify(orderService, times(1)).summary(OrderType.BUY);
    }

    @Test
    public void summaryWhenInvalidOrderTypeIsPassed() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                .get("/v1.0/orders")
                .param("orderType", "INVALID")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void reset() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/v1.0/reset")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(orderService, times(1)).reset();
    }
}