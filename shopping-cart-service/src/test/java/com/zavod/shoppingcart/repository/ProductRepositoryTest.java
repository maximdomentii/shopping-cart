package com.zavod.shoppingcart.repository;

import com.zavod.shoppingcart.entity.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void whenGetProductByExistingBarcode_thenReturnProduct() {
        Product product = new Product();
        product.setName("Banana");
        product.setBarcode(123456789);
        product.setPricePerUM(new BigDecimal(0.5));
        product.setAvailable(true);
        entityManager.persist(product);
        entityManager.flush();

        Optional<Product> result = productRepository.getProductByBarcode(product.getBarcode());

        assertThat(result.get().getName()).isEqualTo(product.getName());
        assertThat(result.get().getBarcode()).isEqualTo(product.getBarcode());
        assertThat(result.get().getPricePerUM()).isEqualTo(product.getPricePerUM());
        assertThat(result.get().isAvailable()).isEqualTo(product.isAvailable());

    }

    @Test
    public void whenGetProductByMissingBarcode_thenReturnAbsent() {
        Optional<Product> result = productRepository.getProductByBarcode(123456789L);

        assertThat(result.isPresent()).isEqualTo(false);

    }
}