package com.ford.grocery;

public class CheckoutService {

    private StockItemRepository stockItemRepository;

    public CheckoutService(final StockItemRepository stockItemRepository) {
        this.stockItemRepository = stockItemRepository;
    }

    public Receipt checkout(final ShoppingBasket basket) {
        Receipt receipt = new Receipt();
        for(ShoppingBasket.BasketItem basketItem: basket) {
            //will use the product name as the key
            stockItemRepository.findStockItem(basketItem.getProduct())
                    .ifPresent(stockItem -> receipt.addItems(basketItem.getQuantity(), stockItem));
        }
        return receipt;
    }

}
