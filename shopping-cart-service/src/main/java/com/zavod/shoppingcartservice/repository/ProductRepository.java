package com.zavod.shoppingcartservice.repository;

import com.zavod.shoppingcartservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    public Product getProductByBarcode(long barcode);
}
