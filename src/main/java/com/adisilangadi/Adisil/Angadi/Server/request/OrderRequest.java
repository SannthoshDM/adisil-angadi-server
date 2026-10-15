package com.adisilangadi.Adisil.Angadi.Server.request;

import com.adisilangadi.Adisil.Angadi.Server.model.Address;
import lombok.Data;

@Data
public class OrderRequest {

    private Long restaurantId;
    private Address deliveryAddress;
    private Long totalBillAmount;
}
