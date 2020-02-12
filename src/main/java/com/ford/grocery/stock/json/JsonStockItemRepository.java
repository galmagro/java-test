package com.ford.grocery.stock.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ford.grocery.stock.StockItem;
import com.ford.grocery.stock.StockItemRepository;

public class JsonStockItemRepository implements StockItemRepository {

    private List<StockItem> stockItems = new LinkedList<>();

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public Optional<StockItem> findStockItem(final String product) {
        if(stockItems.isEmpty()){
            loadItems();
        }
        return stockItems.stream()
                .filter(stockItem -> stockItem.getProduct().equals(product))
                .findFirst();
    }

    private void loadItems() {
        try {
            final InputStream stockItemsJson = getClass().getClassLoader().getResourceAsStream("stock-items.json");
            stockItems = OBJECT_MAPPER.readValue(stockItemsJson, new TypeReference<List<StockItem>>() {});
        } catch (IOException e) {
            throw new RuntimeException("could not load stock items", e);
        }
    }
}
