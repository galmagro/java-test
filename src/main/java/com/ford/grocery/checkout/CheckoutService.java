package com.ford.grocery.checkout;

import static java.lang.String.format;

import com.ford.grocery.ShoppingBasket;
import com.ford.grocery.stock.StockItem;
import com.ford.grocery.stock.StockItemRepository;

public class CheckoutService {

    private StockItemRepository stockItemRepository;

    public CheckoutService(final StockItemRepository stockItemRepository) {
        this.stockItemRepository = stockItemRepository;
    }

    public Receipt checkout(final ShoppingBasket basket) {
        Receipt receipt = new Receipt();
        for(ShoppingBasket.BasketItem basketItem: basket) {
            //will use the product name as the key
            stockItemRepository
                    .findStockItem(basketItem.getProduct())
                    .ifPresent(stockItem -> this.addItemToReceipt(stockItem, basketItem, receipt));
        }
        return receipt;
    }

    private void addItemToReceipt(final StockItem stockItem, final ShoppingBasket.BasketItem basketItem, final Receipt receipt ) {
        if(stockItem.getUnit().equals(basketItem.getUnit())){
            receipt.addItems(basketItem.getQuantity(), stockItem);
        } else {
            throw new IllegalArgumentException(format("invalid unit {} for product {}", basketItem.getUnit(), basketItem.getProduct()));
        }
    }

}
