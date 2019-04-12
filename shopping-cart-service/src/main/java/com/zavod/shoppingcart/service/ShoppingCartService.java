package com.zavod.shoppingcart.service;

import com.zavod.shoppingcart.model.CheckProductResponse;
import com.zavod.shoppingcart.model.ReceiptDetailsResponse;

import java.util.List;

public interface ShoppingCartService {

    ReceiptDetailsResponse getReceiptDetails(List<Long> barcodes);

    CheckProductResponse checkProduct(long barcode);
}
