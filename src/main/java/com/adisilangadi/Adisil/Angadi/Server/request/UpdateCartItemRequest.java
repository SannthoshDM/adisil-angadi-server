package com.adisilangadi.Adisil.Angadi.Server.request;

import lombok.Data;

@Data
public class UpdateCartItemRequest {

    private Long cartItemId;
    private int quantity;
}
