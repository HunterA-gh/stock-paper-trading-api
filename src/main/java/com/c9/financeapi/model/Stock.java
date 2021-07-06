package com.c9.financeapi.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private Integer quantity;

    @Transient
    private Double priceAtTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private User user;

    public Stock() {
    }

    public Stock(String name, Integer quantity, Double priceAtTransaction, User user) {
        super();
        this.name = name;
        this.quantity = quantity;
        this.priceAtTransaction = priceAtTransaction;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPriceAtTransaction() {
        return priceAtTransaction;
    }

    public void setPriceAtTransaction(Double priceAtTransaction) {
        this.priceAtTransaction = priceAtTransaction;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
