package com.zavod.shoppingcartservice.service.impl;

import com.zavod.shoppingcartservice.entity.Product;
import com.zavod.shoppingcartservice.exception.NotFoundException;
import com.zavod.shoppingcartservice.model.CheckProductResponse;
import com.zavod.shoppingcartservice.model.ReceiptDetailsResposne;
import com.zavod.shoppingcartservice.model.ReceiptProduct;
import com.zavod.shoppingcartservice.repository.ProductRepository;
import com.zavod.shoppingcartservice.service.ShoppingCartService;
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
    public ReceiptDetailsResposne getReceiptDetails(List<Long> barcodes) {
        ReceiptDetailsResposne receiptDetailsResposne = new ReceiptDetailsResposne();

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
            receiptDetailsResposne.addProduct(receiptProduct);
        }
        return receiptDetailsResposne;
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
