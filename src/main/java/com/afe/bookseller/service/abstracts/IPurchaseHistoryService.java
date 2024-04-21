package com.afe.bookseller.service.abstracts;

import com.afe.bookseller.model.PurchaseHistory;
import com.afe.bookseller.repository.projection.IPurchaseItem;

import java.util.List;

/**
 * @author sa
 * @date 3.07.2021
 * @time 18:14
 */
public interface IPurchaseHistoryService
{
    PurchaseHistory savePurchaseHistory(PurchaseHistory purchaseHistory);

    List<IPurchaseItem> findPurchasedItemsOfUser(Long userId);
}
