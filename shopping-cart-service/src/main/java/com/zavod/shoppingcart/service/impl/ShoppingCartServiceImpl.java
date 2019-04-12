package com.zavod.shoppingcart.service.impl;

import com.zavod.shoppingcart.entity.Product;
import com.zavod.shoppingcart.exception.NotFoundException;
import com.zavod.shoppingcart.model.CheckProductResponse;
import com.zavod.shoppingcart.model.ReceiptDetailsResponse;
import com.zavod.shoppingcart.model.ReceiptProduct;
import com.zavod.shoppingcart.repository.ProductRepository;
import com.zavod.shoppingcart.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ReceiptDetailsResponse getReceiptDetails(List<Long> barcodes) {
        ReceiptDetailsResponse receiptDetailsResponse = new ReceiptDetailsResponse();

        Map<Long, ProductWithCty> productMap = getProductMapFromListOfRepetedBarcodes(barcodes);

        for (ProductWithCty productWithCty : productMap.values()){
            int cty = productWithCty.getCty();
            Product product = productWithCty.getProduct();

            ReceiptProduct receiptProduct;
            if (product.getDiscountRules() != null){
                receiptProduct = new ReceiptProduct(product.getName(), cty, product.getPricePerUM(), product.getDiscountRules());
            } else {
                receiptProduct = new ReceiptProduct(product.getName(), cty, product.getPricePerUM());
            }
            receiptDetailsResponse.addProduct(receiptProduct);
        }
        return receiptDetailsResponse;
    }

    @Override
    public CheckProductResponse checkProduct(long barcode) {
        Product product = getProductByBarcode(barcode);

        CheckProductResponse checkProductResponse = new CheckProductResponse();
        checkProductResponse.setBarcode(product.getBarcode());
        checkProductResponse.setName(product.getName());
        checkProductResponse.setAvailable(product.isAvailable());
        return checkProductResponse;
    }

    private Product getProductByBarcode(long barcode) {
        Optional<Product> productOptional = productRepository.getProductByBarcode(barcode);

        if (!productOptional.isPresent() || !productOptional.get().isAvailable()){
            throw new NotFoundException("No available product for barcode " + barcode);
        } else {
            return productOptional.get();
        }
    }

    private Map<Long, ProductWithCty> getProductMapFromListOfRepetedBarcodes(List<Long> barcodes){
        Map<Long, ProductWithCty> productMap = new HashMap<>();
        for (Long barcode : barcodes){
            if (productMap.containsKey(barcode)){
                productMap.get(barcode).incrementCty();
            } else {
                Product product = getProductByBarcode(barcode);
                productMap.put(barcode, new ProductWithCty(product));
            }
        }
        return productMap;
    }

    private class ProductWithCty {
        private int cty;
        private Product product;

        public ProductWithCty(Product product){
            this.cty = 1;
            this.product = product;
        }

        public void incrementCty() {
            this.cty++;
        }

        public int getCty() {
            return cty;
        }

        public Product getProduct() {
            return product;
        }
    }
}
