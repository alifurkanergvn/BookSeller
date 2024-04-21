package com.afe.bookseller.repository.projection;

import java.time.LocalDateTime;

//Alias(takma adlarımızı tanımladığımız yer)
public interface IPurchaseItem {
    String getTitle();
    Double getPrice();
    LocalDateTime getPurchaseTime();
}
