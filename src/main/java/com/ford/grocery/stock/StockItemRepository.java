package com.ford.grocery.stock;

import java.util.Optional;

import com.ford.grocery.stock.StockItem;

public interface StockItemRepository {

    Optional<StockItem> findStockItem(String product);

}
