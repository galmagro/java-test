package com.ford.grocery.console;

import java.io.PrintStream;

import com.ford.grocery.checkout.Receipt;
import com.ford.grocery.offer.Discount;
import com.ford.grocery.stock.StockItem;

public class ReceiptPrinter extends ShoppingPrinter<Receipt> {

    public ReceiptPrinter(final Receipt printedItem) {
        super(printedItem);
    }

    @Override
    public void printTo(final PrintStream stream) {
        for(Receipt.Line line: printedItem) {
            final StockItem stockItem = line.getStockItem();
            stream.printf(
                    "%d %s %s @ %.2f - \t%.2f\n",
                    line.getQuantity(),
                    stockItem.getUnit().name().toLowerCase(),
                    stockItem.getProduct(),
                    stockItem.getCost(),
                    line.lineTotal()
            );
        }

        stream.printf("gross total %.2f\n", printedItem.getGrossTotal());
        stream.println("Discounts --------------------------------");
        for(Discount discount: printedItem.getDiscounts()) {
            stream.printf("-%.2f x %d : -%.2f\n",
                    discount.getUnitDiscount(),
                    discount.getTimesApplied(),
                    discount.getTotalDiscountAmount());
        }
        stream.printf("discounts total %.2f\n", printedItem.getDiscountTotal());
        stream.printf("TOTAL\t %.2f\n\n", printedItem.getTotal());
    }
}
