package com.zavod.shoppingcartservice.service;

import com.zavod.shoppingcartservice.model.CheckProductResponse;
import com.zavod.shoppingcartservice.model.ReceiptDetailsResposne;

import java.util.List;

public interface ShoppingCartService {

    ReceiptDetailsResposne getReceiptDetails(List<Long> barcodes);

    CheckProductResponse checkProduct(long barcode);
}
