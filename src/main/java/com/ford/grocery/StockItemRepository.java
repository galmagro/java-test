package com.ford.grocery;

import java.util.Optional;

public interface StockItemRepository {

    Optional<StockItem> findStockItem(String product);

}
