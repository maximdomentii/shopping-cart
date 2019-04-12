package com.zavod.shoppingcartservice.integration.test;

import com.zavod.shoppingcartservice.entity.DiscountRule;
import com.zavod.shoppingcartservice.entity.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ShoppingCartControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private EntityManager entityManager;

    @Before
    public void setUp(){
        Product product1 = new Product();
        product1.setName("Apple");
        product1.setBarcode(111);
        product1.setPricePerUM(new BigDecimal(0.25));
        product1.setAvailable(true);
        entityManager.persist(product1);

        Product product2 = new Product();
        product2.setName("Orange");
        product2.setBarcode(222);
        product2.setPricePerUM(new BigDecimal(0.30));
        product2.setAvailable(true);
        entityManager.persist(product2);

        Product product3 = new Product();
        product3.setName("Bannana");
        product3.setBarcode(333);
        product3.setPricePerUM(new BigDecimal(0.15));
        product3.setAvailable(true);
        entityManager.persist(product3);

        Product product4 = new Product();
        product4.setName("Papaya");
        product4.setBarcode(444);
        product4.setPricePerUM(new BigDecimal(0.50));
        product4.setAvailable(true);

        DiscountRule discountRule = new DiscountRule();
        discountRule.setDescription("three for the price of two");
        discountRule.setNumberOfItems(3);
        discountRule.setDiscountPercentage(100);
        discountRule.setEnable(true);

        product4.setDiscountRules(new HashSet<>(Arrays.asList(discountRule)));
        entityManager.persist(product4);
    }

    @Test
    public void whenCheckForExistingProduct_thenReturnCheckProductResposneAsJson() throws Exception {
        mvc.perform(get("/shopcart/checkProduct/111")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Apple")))
                .andExpect(jsonPath("$.barcode", is(111)))
                .andExpect(jsonPath("$.available", is(true)));

        mvc.perform(get("/shopcart/checkProduct/222")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Orange")))
                .andExpect(jsonPath("$.barcode", is(222)))
                .andExpect(jsonPath("$.available", is(true)));

        mvc.perform(get("/shopcart/checkProduct/333")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Bannana")))
                .andExpect(jsonPath("$.barcode", is(333)))
                .andExpect(jsonPath("$.available", is(true)));

        mvc.perform(get("/shopcart/checkProduct/444")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Papaya")))
                .andExpect(jsonPath("$.barcode", is(444)))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    public void whenCheckForMissingProduct_thenReturn404() throws Exception {
        mvc.perform(get("/shopcart/checkProduct/123456789")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetReceiptDetailsForExistingProductsWithThreeForPriceOfTwoDiscount_thenReturnReceiptAsJson()
            throws Exception {

        mvc.perform(get("/shopcart/receipt?barcodes=444,444,444")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasSize(1)))
                .andExpect(jsonPath("$.products[0].name", is("Papaya")))
                .andExpect(jsonPath("$.products[0].cty", is(3)))
                .andExpect(jsonPath("$.products[0].priceForOneProduct", is(0.5)))
                .andExpect(jsonPath("$.products[0].priceForAllProducts", is(1.5)))
                .andExpect(jsonPath("$.products[0].totalDiscount", is(0.5)))
                .andExpect(jsonPath("$.products[0].discounts", hasSize(1)))
                .andExpect(jsonPath("$.products[0].discounts[0].discountRuleName",
                        is("three for the price of two")))
                .andExpect(jsonPath("$.products[0].discounts[0].discountedAmount", is(0.5)))
                .andExpect(jsonPath("$.subTotal", is(1.5)))
                .andExpect(jsonPath("$.totalDiscount", is(0.5)))
                .andExpect(jsonPath("$.total", is(1.0)));
    }

    @Test
    public void whenGetReceiptDetailsForUnavailableProduct_thenReturn404() throws Exception {
        mvc.perform(get("/shopcart/receipt?barcodes=123456789")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetReceiptDetailsForEmptyListOfBarcodes_thenReturnReceiptAsJsonWithNoProducts() throws Exception {
        mvc.perform(get("/shopcart/receipt?barcodes=")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasSize(0)))
                .andExpect(jsonPath("$.subTotal", is(0)))
                .andExpect(jsonPath("$.totalDiscount", is(0)))
                .andExpect(jsonPath("$.total", is(0)));
    }

    @Test
    public void whenGetReceiptDetailsForBadRequest_thenReturn400() throws Exception {
        mvc.perform(get("/shopcart/receipt")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
