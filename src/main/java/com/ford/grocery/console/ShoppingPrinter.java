package com.ford.grocery.console;

import java.io.PrintStream;

public abstract class ShoppingPrinter<T> {

    protected T printedItem;

    public ShoppingPrinter(T printedItem){
        this.printedItem = printedItem;
    }

    public abstract void printTo(PrintStream stream);

}
