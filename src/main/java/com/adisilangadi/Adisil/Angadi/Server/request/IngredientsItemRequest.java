package com.adisilangadi.Adisil.Angadi.Server.request;

import lombok.Data;

@Data
public class IngredientsItemRequest {

    private String name;
    private Long ingredientCategoryId;
    private Long restaurantId;
}
