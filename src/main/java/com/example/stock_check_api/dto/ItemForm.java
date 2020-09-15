package com.example.stock_check_api.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

public class ItemForm implements Serializable {

    private static final long serialVersionUID = -6580513152572425846L;


    @NotEmpty(message = "{NoEmpty.message}")
    @NotNull(message = "{NotNull.message}")
    @Size(max = 30, message = "{Size.message}")
    private String itemName;

    @NotNull(message="{NotNull.message}")
    private LocalDate expireDate;

    @NotNull(message = "{NotNull.message}")
    private LocalDate purchasedOn;

    @NotNull(message = "{NotNull.message}")
    private int categoryId;

    private String image;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String name) {
        this.itemName = name;
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


    public ItemForm() {
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public ItemForm(String itemName, LocalDate expireDate, LocalDate purchasedOn, int categoryId) {
        this(itemName, expireDate, purchasedOn, categoryId, null);
    }


    public ItemForm(String itemName, LocalDate expireDate, LocalDate purchasedOn, int categoryId, String image) {
        this.itemName = itemName;
        this.expireDate = expireDate;
        this.purchasedOn = purchasedOn;
        this.categoryId = categoryId;
        this.image = image;
    }


}
