package com.giridemo.roadrunnerdriver.model;

import java.util.HashMap;
import java.util.Map;

public class GetItemlist {

    public String getHotalName() {
        return hotalName;
    }

    public void setHotalName(String hotalName) {
        this.hotalName = hotalName;
    }

    private String item,hotalName,imageUrl;
    private int amount,minimumquantity;
    private int quantity;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getMinimumquantity() {
        return minimumquantity;
    }

    public void setMinimumquantity(int minimumquantity) {
        this.minimumquantity = minimumquantity;
    }

    public int getPrice_Per_Item() {
        return Price_Per_Item;
    }

    public void setPrice_Per_Item(int price_Per_Item) {
        Price_Per_Item = price_Per_Item;
    }

    private int Price_Per_Item,IsComplementryAvalible,Need_Complementry;
    private Map<String,Integer> complement=new HashMap<>();

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public int getIsComplementryAvalible() {
        return IsComplementryAvalible;
    }

    public void setIsComplementryAvalible(int isComplementryAvalible) {
        IsComplementryAvalible = isComplementryAvalible;
    }

    public int getNeed_Complementry() {
        return Need_Complementry;
    }

    public void setNeed_Complementry(int need_Complementry) {
        Need_Complementry = need_Complementry;
    }

    public Map<String, Integer> getComplement() {
        return complement;
    }

    public void setComplement(Map<String, Integer> complement) {
        this.complement = complement;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
