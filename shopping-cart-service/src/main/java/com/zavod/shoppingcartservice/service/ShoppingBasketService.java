package com.zavod.shoppingcartservice.service;

import com.zavod.shoppingcartservice.model.ReceiptDetails;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ShoppingBasketService {

    ReceiptDetails getReceiptDetails(List<Long> items);
}
