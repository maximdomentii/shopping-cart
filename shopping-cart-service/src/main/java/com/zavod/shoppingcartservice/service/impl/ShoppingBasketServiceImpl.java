package com.zavod.shoppingcartservice.service.impl;

import com.zavod.shoppingcartservice.entity.Product;
import com.zavod.shoppingcartservice.exception.NotFoundException;
import com.zavod.shoppingcartservice.model.ReceiptDetails;
import com.zavod.shoppingcartservice.model.ReceiptProduct;
import com.zavod.shoppingcartservice.repository.ProductRepository;
import com.zavod.shoppingcartservice.service.ShoppingBasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingBasketServiceImpl implements ShoppingBasketService {

    @Autowired
    private ProductRepository shoppingBasketRepository;

    @Override
    public ReceiptDetails getReceiptDetails(List<Long> items) {
        ReceiptDetails receiptDetails = new ReceiptDetails();
        for (long itemCode : items){
            Product product = shoppingBasketRepository.getProductByBarcode(itemCode);

            if (product == null || !product.isAvailable()){
                throw new NotFoundException("No available product for barcode " + itemCode);
            }

            receiptDetails.addProduct(new ReceiptProduct(product.getName(), 1, product.getPricePerUM()));
        }
        return receiptDetails;
    }
}
