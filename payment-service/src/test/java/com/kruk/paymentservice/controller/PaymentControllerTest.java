package com.kruk.paymentservice.controller;

import com.kruk.paymentservice.domain.UserBalance;
import com.kruk.paymentservice.repository.PaymentRepository;
import com.kruk.paymentservice.service.PaymentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.assertj.core.api.Assertions.assertThat;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PaymentRepository paymentRepository;

    @MockBean
    private PaymentService paymentService;

    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhbGV4IiwiaXNzIjoiaHR0cDovL3NraWx" +
            "sYm94LnJ1IiwiZXhwIjoxNjkxNjkyNTY4LCJpYXQiOjE2OTE2MDYxNjh9" +
            ".Yt0XdE_jLH5BchuL_mUIQ8iFvdqs1c6b1QtK9KCFGTvDXKTD4QKoSh7x4rC9lbdw-neR8Iz2QP3jvVddFdd6jg";


    @Configuration
    @ComponentScan(basePackageClasses = {PaymentController.class})
    public static class TestConf {
    }

    private UserBalance userBalance;



    @BeforeEach
    public void setUp() {
        userBalance = new UserBalance(
                1L,
                "alex"
        );


    }

    @Test
    public void topUpBalanceandCheck() throws Exception {
        mvc.perform(
                post("/topup")
                        .accept(MediaType.TEXT_HTML_VALUE)
                        .header("Authorization", "Bearer " + token)
                        .param("amount", String.valueOf(1000)).contentType(MediaType.TEXT_HTML_VALUE)

        ).andExpect(status().isCreated());
    }



}
