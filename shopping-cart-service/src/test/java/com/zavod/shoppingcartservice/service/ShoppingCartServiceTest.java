package com.zavod.shoppingcartservice.service;

import com.zavod.shoppingcartservice.entity.DiscountRule;
import com.zavod.shoppingcartservice.entity.Product;
import com.zavod.shoppingcartservice.exception.NotFoundException;
import com.zavod.shoppingcartservice.model.CheckProductResponse;
import com.zavod.shoppingcartservice.model.ReceiptDetailsResponse;
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
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
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

    @Test
    public void whenGetReceiptDetailsForEmtyList_thenReturnEmptyReceiptDetailsResponse() {
        List<Long> barcodes = new ArrayList<>();

        ReceiptDetailsResponse receiptDetailsResponse = shoppingCartService.getReceiptDetails(barcodes);

        assertThat(receiptDetailsResponse).isNotNull();
        assertThat(receiptDetailsResponse.getProducts().isEmpty()).isTrue();
        assertThat(receiptDetailsResponse.getSubTotal()).isEqualTo(BigDecimal.ZERO);
        assertThat(receiptDetailsResponse.getTotal()).isEqualTo(BigDecimal.ZERO);
        assertThat(receiptDetailsResponse.getTotalDiscount()).isEqualTo(BigDecimal.ZERO);

    }

    @Test(expected = NotFoundException.class)
    public void whenGetReceiptDetailsForUnavailableBarcode_thenThrowNotFoundException() {
        when(productRepository.getProductByBarcode(anyLong())).thenReturn(Optional.empty());

        shoppingCartService.getReceiptDetails(new ArrayList<>(Arrays.asList(111L)));
    }

    @Test(expected = NotFoundException.class)
    public void whenGetReceiptDetailsForAvailableAndUnavailableBarcodes_thenThrowNotFoundException() {
        long barcode1 = 111L;
        long barcode2 = 222L;
        Product product = new Product();
        product.setName("Banana");
        product.setBarcode(barcode1);
        product.setPricePerUM(new BigDecimal(0.5));
        product.setAvailable(true);

        when(productRepository.getProductByBarcode(barcode1)).thenReturn(Optional.of(product));
        when(productRepository.getProductByBarcode(barcode2)).thenReturn(Optional.empty());

        shoppingCartService.getReceiptDetails(new ArrayList<>(Arrays.asList(barcode1, barcode2)));
    }

    @Test
    public void whenGetReceiptDetailsForAnAvailableBarcode_thenReturnReceiptDetailsResponse() {
        long barcode = 111L;
        Product product = new Product();
        product.setName("Banana");
        product.setBarcode(barcode);
        product.setPricePerUM(new BigDecimal(0.5));
        product.setAvailable(true);

        when(productRepository.getProductByBarcode(barcode)).thenReturn(Optional.of(product));

        ReceiptDetailsResponse receiptDetailsResponse = shoppingCartService.getReceiptDetails(
                new ArrayList<>(Arrays.asList(barcode)));

        assertThat(receiptDetailsResponse).isNotNull();
        assertThat(receiptDetailsResponse.getProducts().size()).isEqualTo(1);
        assertThat(receiptDetailsResponse.getProducts().get(0).getCty()).isEqualTo(1);
        assertThat(receiptDetailsResponse.getProducts().get(0).getName()).isEqualTo(product.getName());
        assertThat(receiptDetailsResponse.getProducts().get(0).getPriceForOneProduct())
                .isEqualTo(product.getPricePerUM());
        assertThat(receiptDetailsResponse.getProducts().get(0).getPriceForAllProducts())
                .isEqualTo(product.getPricePerUM());
        assertThat(receiptDetailsResponse.getSubTotal()).isEqualTo(product.getPricePerUM());
        assertThat(receiptDetailsResponse.getTotal()).isEqualTo(product.getPricePerUM());
        assertThat(receiptDetailsResponse.getTotalDiscount()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void whenGetReceiptDetailsForThreeProductsWithThreeForPriceOfTwoDiscount_thenReturnReceiptDetailsResponse() {
        long barcode = 111L;
        Product product = new Product();
        product.setName("Banana");
        product.setBarcode(barcode);
        product.setPricePerUM(new BigDecimal(0.5));
        product.setAvailable(true);

        DiscountRule discountRule = new DiscountRule();
        discountRule.setDescription("three for price of two");
        discountRule.setDiscountPercentage(100);
        discountRule.setEnable(true);
        discountRule.setNumberOfItems(3);
        product.setDiscountRules(new HashSet<>(Arrays.asList(discountRule)));

        when(productRepository.getProductByBarcode(barcode)).thenReturn(Optional.of(product));

        ReceiptDetailsResponse receiptDetailsResponse = shoppingCartService.getReceiptDetails(
                new ArrayList<>(Arrays.asList(barcode, barcode, barcode)));

        assertThat(receiptDetailsResponse).isNotNull();
        assertThat(receiptDetailsResponse.getProducts().size()).isEqualTo(1);
        assertThat(receiptDetailsResponse.getProducts().get(0).getCty()).isEqualTo(3);
        assertThat(receiptDetailsResponse.getProducts().get(0).getName()).isEqualTo(product.getName());
        assertThat(receiptDetailsResponse.getProducts().get(0).getPriceForOneProduct())
                .isEqualTo(product.getPricePerUM());
        assertThat(receiptDetailsResponse.getProducts().get(0).getPriceForAllProducts())
                .isEqualTo(product.getPricePerUM().multiply(new BigDecimal(3)));
        assertThat(receiptDetailsResponse.getProducts().get(0).getDiscounts().size()).isEqualTo(1);
        assertThat(receiptDetailsResponse.getProducts().get(0).getDiscounts().get(0).getDiscountRuleName())
                .isEqualTo(discountRule.getDescription());
        assertThat(receiptDetailsResponse.getProducts().get(0).getTotalDiscount()).isEqualTo(product.getPricePerUM());
        assertThat(receiptDetailsResponse.getSubTotal())
                .isEqualTo(product.getPricePerUM().multiply(new BigDecimal(3)));
        assertThat(receiptDetailsResponse.getTotalDiscount()).isEqualTo(product.getPricePerUM());
        assertThat(receiptDetailsResponse.getTotal())
                .isEqualTo((product.getPricePerUM().multiply(new BigDecimal(3)).subtract(product.getPricePerUM())));
    }

    @Test
    public void whenGetReceiptDetailsForTwoProductsWithDifferentDiscounts_thenReturnReceiptDetailsResponse() {
        long barcode1 = 111L;
        Product product1 = new Product();
        product1.setName("Banana");
        product1.setBarcode(barcode1);
        product1.setPricePerUM(new BigDecimal(0.5));
        product1.setAvailable(true);

        DiscountRule discountRule1 = new DiscountRule();
        discountRule1.setDiscountRuleId(1);
        discountRule1.setDescription("three for price of two");
        discountRule1.setDiscountPercentage(100);
        discountRule1.setEnable(true);
        discountRule1.setNumberOfItems(3);
        product1.setDiscountRules(new HashSet<>(Arrays.asList(discountRule1)));

        when(productRepository.getProductByBarcode(barcode1)).thenReturn(Optional.of(product1));

        long barcode2 = 222L;
        Product product2 = new Product();
        product2.setName("Orange");
        product2.setBarcode(barcode2);
        product2.setPricePerUM(new BigDecimal(0.25));
        product2.setAvailable(true);

        DiscountRule discountRule2 = new DiscountRule();
        discountRule2.setDiscountRuleId(2);
        discountRule2.setDescription("50 % discount");
        discountRule2.setDiscountPercentage(50);
        discountRule2.setEnable(true);
        discountRule2.setNumberOfItems(1);
        product2.setDiscountRules(new HashSet<>(Arrays.asList(discountRule2)));

        when(productRepository.getProductByBarcode(barcode2)).thenReturn(Optional.of(product2));

        ReceiptDetailsResponse receiptDetailsResponse = shoppingCartService.getReceiptDetails(
                new ArrayList<>(Arrays.asList(barcode1, barcode1, barcode1, barcode2)));

        assertThat(receiptDetailsResponse).isNotNull();
        assertThat(receiptDetailsResponse.getProducts().size()).isEqualTo(2);
        assertThat(receiptDetailsResponse.getSubTotal())
                .isEqualTo(product1.getPricePerUM().multiply(new BigDecimal(3)).add(product2.getPricePerUM()));
        assertThat(receiptDetailsResponse.getTotalDiscount())
                .isEqualTo(product1.getPricePerUM().add((product2.getPricePerUM().divide(new BigDecimal(2)))));
        assertThat(receiptDetailsResponse.getTotal())
                .isEqualTo((product1.getPricePerUM().multiply(new BigDecimal(3)).subtract(product1.getPricePerUM()))
                .add((product2.getPricePerUM().divide(new BigDecimal(2)))));
    }

    @Test
    public void whenGetReceiptDetailsForOneTypeOfProductsWithTwoCumulativeDiscounts_thenReturnReceiptDetailsResponse() {
        long barcode = 111L;
        Product product = new Product();
        product.setName("Banana");
        product.setBarcode(barcode);
        product.setPricePerUM(new BigDecimal(0.5));
        product.setAvailable(true);

        DiscountRule discountRule1 = new DiscountRule();
        discountRule1.setDiscountRuleId(1);
        discountRule1.setDescription("three for price of two");
        discountRule1.setDiscountPercentage(100);
        discountRule1.setEnable(true);
        discountRule1.setNumberOfItems(3);

        DiscountRule discountRule2 = new DiscountRule();
        discountRule2.setDiscountRuleId(2);
        discountRule2.setDescription("50 % discount");
        discountRule2.setDiscountPercentage(50);
        discountRule2.setEnable(true);
        discountRule2.setNumberOfItems(1);
        product.setDiscountRules(new HashSet<>(Arrays.asList(discountRule1, discountRule2)));

        when(productRepository.getProductByBarcode(barcode)).thenReturn(Optional.of(product));

        ReceiptDetailsResponse receiptDetailsResponse = shoppingCartService.getReceiptDetails(
                new ArrayList<>(Arrays.asList(barcode, barcode, barcode)));

        assertThat(receiptDetailsResponse).isNotNull();
        assertThat(receiptDetailsResponse.getProducts().size()).isEqualTo(1);
        assertThat(receiptDetailsResponse.getProducts().get(0).getCty()).isEqualTo(3);
        assertThat(receiptDetailsResponse.getProducts().get(0).getName()).isEqualTo(product.getName());
        assertThat(receiptDetailsResponse.getProducts().get(0).getPriceForOneProduct())
                .isEqualTo(product.getPricePerUM());
        assertThat(receiptDetailsResponse.getProducts().get(0).getPriceForAllProducts())
                .isEqualTo(product.getPricePerUM().multiply(new BigDecimal(3)));
        assertThat(receiptDetailsResponse.getProducts().get(0).getDiscounts().size()).isEqualTo(2);
        assertThat(receiptDetailsResponse.getProducts().get(0).getTotalDiscount())
                .isEqualTo(product.getPricePerUM().add(
                        (product.getPricePerUM().divide(new BigDecimal(2))).multiply(new BigDecimal(3))));
        assertThat(receiptDetailsResponse.getSubTotal())
                .isEqualTo(product.getPricePerUM().multiply(new BigDecimal(3)));
        assertThat(receiptDetailsResponse.getTotalDiscount())
                .isEqualTo(receiptDetailsResponse.getProducts().get(0).getTotalDiscount());
        assertThat(receiptDetailsResponse.getTotal())
                .isEqualTo((product.getPricePerUM().multiply(new BigDecimal(3)).subtract(
                        receiptDetailsResponse.getProducts().get(0).getTotalDiscount())));
    }

    @Test
    public void whenGetReceiptDetailsForProductsWithDisabledDiscount_thenReturnReceiptDetailsResponse() {
        long barcode = 111L;
        Product product = new Product();
        product.setName("Banana");
        product.setBarcode(barcode);
        product.setPricePerUM(new BigDecimal(0.5));
        product.setAvailable(true);

        DiscountRule discountRule = new DiscountRule();
        discountRule.setDescription("three for price of two");
        discountRule.setDiscountPercentage(100);
        discountRule.setEnable(false);
        discountRule.setNumberOfItems(3);
        product.setDiscountRules(new HashSet<>(Arrays.asList(discountRule)));

        when(productRepository.getProductByBarcode(barcode)).thenReturn(Optional.of(product));

        ReceiptDetailsResponse receiptDetailsResponse = shoppingCartService.getReceiptDetails(
                new ArrayList<>(Arrays.asList(barcode, barcode, barcode)));

        assertThat(receiptDetailsResponse).isNotNull();
        assertThat(receiptDetailsResponse.getProducts().size()).isEqualTo(1);
        assertThat(receiptDetailsResponse.getProducts().get(0).getCty()).isEqualTo(3);
        assertThat(receiptDetailsResponse.getProducts().get(0).getName()).isEqualTo(product.getName());
        assertThat(receiptDetailsResponse.getProducts().get(0).getPriceForOneProduct())
                .isEqualTo(product.getPricePerUM());
        assertThat(receiptDetailsResponse.getProducts().get(0).getPriceForAllProducts())
                .isEqualTo(product.getPricePerUM().multiply(new BigDecimal(3)));
        assertThat(receiptDetailsResponse.getProducts().get(0).getDiscounts().size()).isEqualTo(0);
        assertThat(receiptDetailsResponse.getProducts().get(0).getTotalDiscount()).isEqualTo(BigDecimal.ZERO);
        assertThat(receiptDetailsResponse.getSubTotal())
                .isEqualTo(product.getPricePerUM().multiply(new BigDecimal(3)));
        assertThat(receiptDetailsResponse.getTotalDiscount()).isEqualTo(BigDecimal.ZERO);
        assertThat(receiptDetailsResponse.getTotal())
                .isEqualTo(product.getPricePerUM().multiply(new BigDecimal(3)));
    }

    @Test
    public void whenGetReceiptDetailsForProductsWithUnsatisfiedDiscount_thenReturnReceiptDetailsResponse() {
        long barcode = 111L;
        Product product = new Product();
        product.setName("Banana");
        product.setBarcode(barcode);
        product.setPricePerUM(new BigDecimal(0.5));
        product.setAvailable(true);

        DiscountRule discountRule = new DiscountRule();
        discountRule.setDescription("three for price of two");
        discountRule.setDiscountPercentage(100);
        discountRule.setEnable(true);
        discountRule.setNumberOfItems(3);
        product.setDiscountRules(new HashSet<>(Arrays.asList(discountRule)));

        when(productRepository.getProductByBarcode(barcode)).thenReturn(Optional.of(product));

        ReceiptDetailsResponse receiptDetailsResponse = shoppingCartService.getReceiptDetails(
                new ArrayList<>(Arrays.asList(barcode)));

        assertThat(receiptDetailsResponse).isNotNull();
        assertThat(receiptDetailsResponse.getProducts().size()).isEqualTo(1);
        assertThat(receiptDetailsResponse.getProducts().get(0).getCty()).isEqualTo(1);
        assertThat(receiptDetailsResponse.getProducts().get(0).getName()).isEqualTo(product.getName());
        assertThat(receiptDetailsResponse.getProducts().get(0).getPriceForOneProduct())
                .isEqualTo(product.getPricePerUM());
        assertThat(receiptDetailsResponse.getProducts().get(0).getPriceForAllProducts())
                .isEqualTo(product.getPricePerUM());
        assertThat(receiptDetailsResponse.getProducts().get(0).getDiscounts().size()).isEqualTo(1);
        assertThat(receiptDetailsResponse.getProducts().get(0).getTotalDiscount())
                .isEqualTo(BigDecimal.ZERO.setScale(1));
        assertThat(receiptDetailsResponse.getSubTotal()).isEqualTo(product.getPricePerUM());
        assertThat(receiptDetailsResponse.getTotalDiscount()).isEqualTo(BigDecimal.ZERO.setScale(1));
        assertThat(receiptDetailsResponse.getTotal()).isEqualTo(product.getPricePerUM());
    }
}
