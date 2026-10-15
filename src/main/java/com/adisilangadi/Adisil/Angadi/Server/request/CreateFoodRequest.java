package com.adisilangadi.Adisil.Angadi.Server.request;

import com.adisilangadi.Adisil.Angadi.Server.model.Category;
import com.adisilangadi.Adisil.Angadi.Server.model.IngredientsItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFoodRequest {

    private String name;
    private String description;
    private Long price;

    private Category category;
    private List<String> images;

    private Long restaurantId;
    private boolean vegetarian;
    private boolean seasonal;

    private List<IngredientsItem> ingredientsItems;
}
