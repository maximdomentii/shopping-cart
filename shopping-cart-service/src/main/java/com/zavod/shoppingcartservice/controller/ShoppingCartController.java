package com.zavod.shoppingcartservice.controller;

import com.zavod.shoppingcartservice.model.ReceiptDetails;
import com.zavod.shoppingcartservice.service.ShoppingCartService;
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
public class ShoppingCartController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartController.class);

    @Autowired
    private ShoppingCartService shoppingBasketService;

    @GetMapping("/receipt")
    public ReceiptDetails getReceiptDetails(@RequestParam List<Long> barcodes){
        LOGGER.info("Received getReceiptDetails request for a list of {} barcodes", barcodes.size());

        return shoppingBasketService.getReceiptDetails(barcodes);
    }
}
