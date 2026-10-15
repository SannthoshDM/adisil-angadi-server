package com.adisilangadi.Adisil.Angadi.Server.specification;

import com.adisilangadi.Adisil.Angadi.Server.model.Restaurant;
import org.springframework.data.jpa.domain.Specification;

public class RestaurantSpecification {

    public static Specification<Restaurant> hasKeyword(String keyword) {

        return (root, query, criteriaBuilder) -> {

            if (keyword == null || keyword.isBlank()) {
                return null;
            }

            String searchKeyword = "%" + keyword.toLowerCase() + "%";

            return criteriaBuilder.or(

                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")),
                            searchKeyword
                    ),

                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("description")),
                            searchKeyword
                    ),

                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("cuisineType")),
                            searchKeyword
                    )
            );
        };
    }
}
