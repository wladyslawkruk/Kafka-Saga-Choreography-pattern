package com.kruk.inventoryservice.controller;

import com.kruk.inventoryservice.domain.Item;
import com.kruk.inventoryservice.dto.ItemDto;
import com.kruk.inventoryservice.repository.InventoryRepository;
import com.kruk.inventoryservice.service.InventoryService;
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
import java.util.Optional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(StockTopUpController.class)
public class InventoryControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private InventoryRepository inventoryRepository;

    @MockBean
    private InventoryService inventoryService;

    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhbGV4IiwiaXNzIjoiaHR0cDovL3NraWx" +
            "sYm94LnJ1IiwiZXhwIjoxNjkxNjkyNTY4LCJpYXQiOjE2OTE2MDYxNjh9" +
            ".Yt0XdE_jLH5BchuL_mUIQ8iFvdqs1c6b1QtK9KCFGTvDXKTD4QKoSh7x4rC9lbdw-neR8Iz2QP3jvVddFdd6jg";

    @Configuration
    @ComponentScan(basePackageClasses = {StockTopUpController.class})
    public static class TestConf {
    }

    private Item item;

    private Item newItem;

    @BeforeEach
    public void setUp() {
        newItem = new Item(
                "condom",
                100L,
                0L
        );



    }

    @Test
    public void topUpStockandCheck() throws Exception {
        ItemDto itemDto = new ItemDto(
                "condom",
                100L,
                0L
        );

        System.out.println(newItem.toString());
        Mockito.when(inventoryService.topUpStock(itemDto)).thenReturn(Optional.of(newItem));
        mvc.perform(
                post("/topupstock")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(
                                "{\"itemName\":\"beer,\"price\":50,\"amount\":10}"
                        ).contentType(MediaType.APPLICATION_JSON)

        ).andExpect(status().isCreated());



    }

}
