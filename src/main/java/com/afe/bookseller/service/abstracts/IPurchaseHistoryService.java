package com.afe.bookseller.service.abstracts;

import com.afe.bookseller.model.PurchaseHistory;
import com.afe.bookseller.repository.projection.IPurchaseItem;

import java.util.List;


public interface IPurchaseHistoryService
{
    PurchaseHistory savePurchaseHistory(PurchaseHistory purchaseHistory);

    List<IPurchaseItem> findPurchasedItemsOfUser(Long userId);
}
