package com.example.stock_check_api.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "item",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"itemName", "user_id"})
    })
public class Item {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String itemName;

    @Column(nullable = false)
    private LocalDate expireDate;

    @Column(nullable = false)
    private LocalDate purchasedOn;


    @Column(name = "image")
    private String image;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;


    public Item() {

    }

    public Item(String itemName, LocalDate expireDate, LocalDate purchasedOn, User user, Category category) {
        this(itemName, expireDate, purchasedOn, null, user, category);
    }



    public Item(String itemName, LocalDate expireDate, LocalDate purchasedOn, String image, User user, Category category) {
        this.itemName = itemName;
        this.expireDate = expireDate;
        this.purchasedOn = purchasedOn;
        this.image = image;
        this.user = user;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public LocalDate getPurchasedOn() {
        return purchasedOn;
    }

    public void setPurchasedOn(LocalDate purchasedOn) {
        this.purchasedOn = purchasedOn;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}


