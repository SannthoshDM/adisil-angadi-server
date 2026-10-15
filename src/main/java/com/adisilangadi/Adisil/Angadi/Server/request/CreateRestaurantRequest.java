package com.adisilangadi.Adisil.Angadi.Server.request;

import com.adisilangadi.Adisil.Angadi.Server.model.Address;
import com.adisilangadi.Adisil.Angadi.Server.model.ContactInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRestaurantRequest {

    private Long id;
    private String name;
    private String description;
    private String cuisineType;
    private Address address;
    private ContactInformation contactInformation;
    private String openingHours;
    private List<String> images;
}
