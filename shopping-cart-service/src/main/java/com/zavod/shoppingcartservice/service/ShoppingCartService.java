package com.zavod.shoppingcartservice.service;

import com.zavod.shoppingcartservice.model.CheckProductResponse;
import com.zavod.shoppingcartservice.model.ReceiptDetailsResponse;

import java.util.List;

public interface ShoppingCartService {

    ReceiptDetailsResponse getReceiptDetails(List<Long> barcodes);

    CheckProductResponse checkProduct(long barcode);
}
