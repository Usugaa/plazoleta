package com.microservicio.restaurant.domain.model;

public class OrderDishes {

    private Long id;
    private Long idOrder;
    private Long idDish;
    private Long amount;

    public OrderDishes(Long id, Long idOrder, Long idDish, Long amount) {
        this.id = id;
        this.idOrder = idOrder;
        this.idDish = idDish;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Long idOrder) {
        this.idOrder = idOrder;
    }

    public Long getIdDish() {
        return idDish;
    }

    public void setIdDish(Long idDish) {
        this.idDish = idDish;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}