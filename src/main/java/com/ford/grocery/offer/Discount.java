package com.ford.grocery.offer;

public class Discount {

    private double discountAmount;

    private int timesApplied;

    public Discount(final double discountAmount, final int timesApplied) {
        this.discountAmount = discountAmount;
        this.timesApplied = timesApplied;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(final double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getTimesApplied() {
        return timesApplied;
    }

    public void setTimesApplied(final int timesApplied) {
        this.timesApplied = timesApplied;
    }
}
