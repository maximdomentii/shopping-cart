package com.zavod.shoppingcartservice.service;

import com.zavod.shoppingcartservice.entity.Product;
import com.zavod.shoppingcartservice.exception.NotFoundException;
import com.zavod.shoppingcartservice.model.CheckProductResponse;
import com.zavod.shoppingcartservice.repository.ProductRepository;
import com.zavod.shoppingcartservice.service.impl.ShoppingCartServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ShoppingCartServiceTest {

    @TestConfiguration
    static class ShoppingCartServiceTestContextConfiguration {

        @Bean
        public ShoppingCartService shoppingCartService() {
            return new ShoppingCartServiceImpl();
        }
    }

    @Autowired
    private ShoppingCartService shoppingCartService;

    @MockBean
    private ProductRepository productRepository;

    @Test
    public void whenCheckProductForExistingBarcode_thenReturnCheckProductResponse() {
        Product product = new Product();
        product.setName("Banana");
        product.setBarcode(123456789);
        product.setPricePerUM(new BigDecimal(0.5));
        product.setAvailable(true);

        when(productRepository.getProductByBarcode(product.getBarcode())).thenReturn(Optional.of(product));

        CheckProductResponse checkProductResponse = shoppingCartService.checkProduct(product.getBarcode());

        assertThat(checkProductResponse.getName()).isEqualTo(product.getName());
        assertThat(checkProductResponse.getBarcode()).isEqualTo(product.getBarcode());
        assertThat(checkProductResponse.isAvailable()).isEqualTo(product.isAvailable());
    }

    @Test(expected = NotFoundException.class)
    public void whenCheckProductForMissingBarcode_thenThrowsNotFoundException() {
        when(productRepository.getProductByBarcode(anyLong())).thenReturn(Optional.empty());

        shoppingCartService.checkProduct(anyLong());
    }
}
