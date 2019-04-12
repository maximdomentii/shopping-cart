package com.zavod.shoppingcartservice.controller;

import com.zavod.shoppingcartservice.model.CheckProductResponse;
import com.zavod.shoppingcartservice.model.ReceiptDetailsResponse;
import com.zavod.shoppingcartservice.service.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shopcart")
public class ShoppingCartController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartController.class);

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/receipt")
    public ReceiptDetailsResponse getReceiptDetails(@RequestParam List<Long> barcodes){
        LOGGER.info("Received getReceiptDetails request for a list of {} barcodes", barcodes.size());

        return shoppingCartService.getReceiptDetails(barcodes);
    }

    @GetMapping("/checkProduct/{barcode}")
    public CheckProductResponse checkProduct(@PathVariable("barcode") Long barcode){
        LOGGER.info("Received checkProduct request for barcode", barcode);

        return shoppingCartService.checkProduct(barcode);
    }
}
