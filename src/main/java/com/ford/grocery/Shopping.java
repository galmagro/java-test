package com.ford.grocery;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;

import com.ford.grocery.checkout.CheckoutService;
import com.ford.grocery.checkout.Receipt;
import com.ford.grocery.console.ReceiptPrinter;
import com.ford.grocery.console.ShoppingBasketPrinter;
import com.ford.grocery.offer.memory.InMemoryOfferRepository;
import com.ford.grocery.stock.ItemUnitType;
import com.ford.grocery.stock.json.JsonStockItemRepository;

public class Shopping {

    private final static Scanner SCANNER = new Scanner(System.in);

    private final static CheckoutService CHECKOUT = new CheckoutService(new JsonStockItemRepository(), new InMemoryOfferRepository());

    private final static String VALID_PRODUCTS = "soup|bread|milk|apples";

    public static void main(String[] args) {

        ShoppingBasket basket;

        while (true) {
            System.out.println("choose an option");
            System.out.println("1 - start a new shopping basket");
            System.out.println("2 - exit");
            System.out.println("selected option?: ");

            int selectedOption = readInteger();

            switch (selectedOption){
                case 1:
                    basket = startBasket();
                    basketLoop(basket);
                    break;

                case 2:
                    System.out.println("goodbye!");
                    return;
            }
        }

    }

    private static int readInteger() {
        try {
            return SCANNER.nextInt();
        } catch (Exception e) {
            SCANNER.skip("\\w*");
            System.out.println("incorrect number, please try again");
            return readInteger();
        }
    }

    private static void basketLoop(final ShoppingBasket basket) {
        int option;

        do {
            System.out.println("Basket options");

            System.out.println("1 - add item to current basket");
            System.out.println("2 - checkout current basket");
            System.out.println("selected option?: ");
            option = readInteger();

            switch (option) {
                case 1:
                    addBasketItem(basket);
                    break;
                case 2:
                    checkoutBasket(basket);
                    break;
            }

        } while (option != 2);
    }

    private static void checkoutBasket(final ShoppingBasket basket) {
        System.out.println("Basket items");
        System.out.println("---------------------");
        ShoppingBasketPrinter printer = new ShoppingBasketPrinter(basket);
        printer.printTo(System.out);

        final Receipt receipt = CHECKOUT.checkout(basket);

        System.out.println("Receipt");
        System.out.println("---------------------");
        ReceiptPrinter receiptPrinter = new ReceiptPrinter(receipt);
        receiptPrinter.printTo(System.out);
    }

    private static void addBasketItem(final ShoppingBasket basket) {
        System.out.println("indicate amount and product to be added to basket using format `amount unitype product`." +
                "\n (ex: 1 tin soup, 3 single apples)");
        int amount = readInteger();
        final ItemUnitType unitType = readUnitType();
        if(unitType == null) {
            addBasketItem(basket);
            return;
        }
        String product = readProduct();
        if(product == null) {
            addBasketItem(basket);
            return;
        }
        basket.add(amount, unitType, product);
    }

    private static String readProduct() {
        try {
            final String product = SCANNER.next();
            if(!product.matches(VALID_PRODUCTS)) {
                SCANNER.nextLine();
                System.out.println("invalid product, valid products are " + VALID_PRODUCTS);
            }
            return product;
        } catch (Exception e) {
            SCANNER.nextLine();
        }
        return null;
    }

    private static ShoppingBasket startBasket() {
        System.out.println("basket to be bought in how many days (enter a number or zero for today) ?");
        int numDays = readInteger();
        return new ShoppingBasket(LocalDate.now().plusDays(numDays));
    }

    private static ItemUnitType readUnitType() {
        try {
            return ItemUnitType.valueOf(SCANNER.next().toUpperCase());
        } catch (Exception e) {
            System.out.println("invalid unit type , valid values are tin,loaf,bottle and single");
            System.out.println("please try again");
            SCANNER.nextLine();
            return null;
        }
    }
}
