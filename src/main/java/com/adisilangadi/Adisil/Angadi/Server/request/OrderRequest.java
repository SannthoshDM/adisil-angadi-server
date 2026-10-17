package com.adisilangadi.Adisil.Angadi.Server.request;

import com.adisilangadi.Adisil.Angadi.Server.model.Address;
import lombok.Data;

@Data
public class OrderRequest {

    private Long restaurantId;
    private Long addressId;
    private Long totalBillAmount;
}
