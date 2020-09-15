package com.example.stock_check_api.response;

public class ItemResponse extends ApiResponse{
    private int id;

    public ItemResponse(int id, String message) {
        super(message);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

