package com.zavod.shoppingcartservice.controller;

import com.zavod.shoppingcartservice.model.ReceiptDetails;
import com.zavod.shoppingcartservice.service.ShoppingBasketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class ShoppingBasketController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingBasketController.class);

    @Autowired
    private ShoppingBasketService shoppingBasketService;

    @GetMapping("/receipt")
    public ReceiptDetails getReceiptDetails(@RequestParam List<Long> items){
        LOGGER.info("Received getReceiptDetails request for a list of {} items", items.size());

        return shoppingBasketService.getReceiptDetails(items);
    }
}
