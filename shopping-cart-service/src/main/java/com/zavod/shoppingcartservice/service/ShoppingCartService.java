package com.zavod.shoppingcartservice.service;

import com.zavod.shoppingcartservice.model.ReceiptDetails;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ShoppingCartService {

    ReceiptDetails getReceiptDetails(List<Long> barcodes);
}
