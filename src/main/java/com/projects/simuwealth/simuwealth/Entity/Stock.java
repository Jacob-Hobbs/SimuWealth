package com.projects.simuwealth.simuwealth.Entity;

import jakarta.persistence.*;

@Entity
@Table(name= "stock")
public class Stock {

    @Id
    @Column(name = "stock_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int stock_Id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "purchase_price")
    private double purchasePrice;

    public Stock() {
    }

    public Stock(int stock_Id, User user, String symbol, double purchasePrice) {
        this.stock_Id = stock_Id;
        this.user = user;
        this.symbol = symbol;
        this.purchasePrice = purchasePrice;
    }

    public int getStock_Id() {
        return stock_Id;
    }

    public void setStock_Id(int stock_Id) {
        this.stock_Id = stock_Id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "stock_Id=" + stock_Id +
                ", user=" + user +
                ", symbol='" + symbol + '\'' +
                ", purchasePrice=" + purchasePrice +
                '}';
    }

}
