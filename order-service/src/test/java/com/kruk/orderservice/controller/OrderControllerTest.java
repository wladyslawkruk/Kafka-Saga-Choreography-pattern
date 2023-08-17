package com.kruk.orderservice.controller;

import com.kruk.orderservice.domain.Order;
import com.kruk.orderservice.domain.OrderDto;
import com.kruk.orderservice.domain.OrderStatus;
import com.kruk.orderservice.domain.StatusDto;
import com.kruk.orderservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.kruk.orderservice.repository.OrderRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private OrderService orderService;

    @Configuration
    @ComponentScan(basePackageClasses = {OrderController.class})
    public static class TestConf {
    }

    private Order order;

    private Order newOrder;

    private List<Order> orders;

    @BeforeEach
    public void setUp() {
        order = new Order(
                "Moscow, st.Taganskaya 150",
                "Moscow, st.Tulskaya 24",
                "Order #112",
                1500L,
                OrderStatus.REGISTERED,
                1l,
                1500l
        );
        newOrder = new Order(
                "Moscow, st.Taganskaya 150",
                "Moscow, st.Dubininskaya 39",
                "Order #342",
                2450L,
                OrderStatus.REGISTERED,
                1l,
                2450l
        );
        orders = Collections.singletonList(order);
    }

    @Test
    public void listOrders() throws Exception {
        Mockito.when(orderRepository.findAll()).thenReturn(orders);
        mvc.perform(get("/order"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(order.getDescription())));
    }

    @Test
    public void listOrder() throws Exception {
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        mvc.perform(get("/order/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(order.getDescription())));
    }

    @Test
    public void addOrder() throws Exception {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhbGV4IiwiaXNzIjoiaHR0cDovL3NraWx" +
                "sYm94LnJ1IiwiZXhwIjoxNjkxNjkyNTY4LCJpYXQiOjE2OTE2MDYxNjh9" +
                ".Yt0XdE_jLH5BchuL_mUIQ8iFvdqs1c6b1QtK9KCFGTvDXKTD4QKoSh7x4rC9lbdw-neR8Iz2QP3jvVddFdd6jg";
        OrderDto orderDto = new OrderDto(
                "Order #342",
                "Moscow, st.Taganskaya 150",
                "Moscow, st.Dubininskaya 39",
                2450L,
                1L
        );

        Mockito.when(orderService.addOrder(orderDto,token)).thenReturn(Optional.of(newOrder));
        mvc.perform(
                        post("/order")
                                .accept(MediaType.APPLICATION_JSON).header("Authorization","Bearer "+token)
                                .content(
                                        "{\"description\":\"Order #342\",\"departureAddress\":\"Moscow, st.Taganskaya 150\"," +
                                                "\"destinationAddress\":\"Moscow, st.Dubininskaya 39\",\"cost\":2450,\"amount\":1}"
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }

    @Test
    public void updateOrderStatus() throws Exception {
        StatusDto statusDto = new StatusDto(OrderStatus.PAID);
        doNothing().when(orderService).updateOrderStatus(1L, statusDto);
        mvc.perform(
                        patch("/order/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .content("{\"status\": \"PAID\"}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void updateOrderStatusWithError() throws Exception {
        StatusDto statusDto = new StatusDto(OrderStatus.PAID);
        doThrow(new RuntimeException()).when(orderService).updateOrderStatus(1L, statusDto);
        mvc.perform(
                        patch("/order/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .content("{\"status\": \"PAID\"}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotModified());
    }

}
