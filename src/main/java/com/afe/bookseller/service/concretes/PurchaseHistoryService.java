package com.afe.bookseller.service.concretes;

import com.afe.bookseller.model.PurchaseHistory;
import com.afe.bookseller.repository.IPurchaseHistoryRepository;
import com.afe.bookseller.repository.projection.IPurchaseItem;
import com.afe.bookseller.service.abstracts.IPurchaseHistoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class PurchaseHistoryService implements IPurchaseHistoryService
{
    private final IPurchaseHistoryRepository purchaseHistoryRepository;

    public PurchaseHistoryService(IPurchaseHistoryRepository purchaseHistoryRepository){
        this.purchaseHistoryRepository = purchaseHistoryRepository;
    }

    @Override
    public PurchaseHistory savePurchaseHistory(PurchaseHistory purchaseHistory){
        purchaseHistory.setPurchaseTime(LocalDateTime.now());

        return purchaseHistoryRepository.save(purchaseHistory);
    }

    @Override
    public List<IPurchaseItem> findPurchasedItemsOfUser(Long userId){
        return purchaseHistoryRepository.findAllPurchasesOfUser(userId);
    }
}
