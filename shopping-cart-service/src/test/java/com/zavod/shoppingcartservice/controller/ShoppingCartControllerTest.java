package com.zavod.shoppingcartservice.controller;

import com.zavod.shoppingcartservice.exception.NotFoundException;
import com.zavod.shoppingcartservice.model.CheckProductResponse;
import com.zavod.shoppingcartservice.service.ShoppingCartService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ShoppingCartController.class)
public class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ShoppingCartService shoppingCartService;

    @Test
    public void whenCheckForExistingProduct_thenReturnCheckProductResposneAsJson() throws Exception {
        CheckProductResponse checkProductResponse = new CheckProductResponse();
        checkProductResponse.setName("Banana");
        checkProductResponse.setBarcode(123456789);
        checkProductResponse.setAvailable(true);

        when(shoppingCartService.checkProduct(checkProductResponse.getBarcode())).thenReturn(checkProductResponse);

        mvc.perform(get("/shopcart/checkProduct/"+checkProductResponse.getBarcode())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(checkProductResponse.getName())))
                .andExpect(jsonPath("$.barcode", is((int)checkProductResponse.getBarcode())))
                .andExpect(jsonPath("$.available", is(checkProductResponse.isAvailable())));
    }

    @Test
    public void whenCheckForMissingProduct_thenReturn404() throws Exception {
        when(shoppingCartService.checkProduct(anyLong())).thenThrow(new NotFoundException(""));

        mvc.perform(get("/shopcart/checkProduct/123456789")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}


