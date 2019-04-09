package com.zavod.shoppingcartservice.service.impl;

import com.zavod.shoppingcartservice.entity.Product;
import com.zavod.shoppingcartservice.exception.NotFoundException;
import com.zavod.shoppingcartservice.model.ReceiptDetails;
import com.zavod.shoppingcartservice.model.ReceiptProduct;
import com.zavod.shoppingcartservice.repository.ProductRepository;
import com.zavod.shoppingcartservice.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ReceiptDetails getReceiptDetails(List<Long> barcodes) {
        ReceiptDetails receiptDetails = new ReceiptDetails();

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
            receiptDetails.addProduct(receiptProduct);
        }
        return receiptDetails;
    }

    private Map<Long, ProductWithCty> getProductMapFromListOfRepetedBarcodes(List<Long> barcodes){
        Map<Long, ProductWithCty> productMap = new HashMap<>();
        for (Long barcode : barcodes){
            if (productMap.containsKey(barcode)){
                productMap.get(barcode).incrementCty();
            } else {
                Product product = productRepository.getProductByBarcode(barcode);

                if (product == null || !product.isAvailable()){
                    throw new NotFoundException("No available product for barcode " + barcode);
                }

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
