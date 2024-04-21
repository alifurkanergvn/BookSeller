package com.afe.bookseller.controller;

import com.afe.bookseller.model.PurchaseHistory;
import com.afe.bookseller.security.UserPrincipal;
import com.afe.bookseller.service.abstracts.IPurchaseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/purchase-history")//pre-path
public class PurchaseHistoryController {
    @Autowired
    private IPurchaseHistoryService purchaseHistoryService;

    @PostMapping //api/purchase-history
    public ResponseEntity<?> savePurchaseHistory(@RequestBody PurchaseHistory purchaseHistory) {
        return new ResponseEntity<>(purchaseHistoryService.savePurchaseHistory(purchaseHistory), HttpStatus.CREATED);
    }

    //Kimliği dogrulanmıs satıcının tum satın alma ıslemlerını cagırmak ısıtıyoruz.
    // Bu sebeple kimligi dorulanmıs kullanıcıya once erısmemiz gerekiyor
    @GetMapping //api/purchase-history
    public ResponseEntity<?> getAllPurchasesOfUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(purchaseHistoryService.findPurchasedItemsOfUser(userPrincipal.getId()));
    }
}
