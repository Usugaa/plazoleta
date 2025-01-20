package com.microservicio.restaurant.infraestructure.output.jpa.mapper;

import com.microservicio.restaurant.domain.model.Dish;
import com.microservicio.restaurant.infraestructure.output.jpa.entity.CategoryEntity;
import com.microservicio.restaurant.infraestructure.output.jpa.entity.DishEntity;
import com.microservicio.restaurant.infraestructure.output.jpa.entity.RestaurantEntity;
import org.springframework.stereotype.Component;

@Component
public class DishEntityMapper {

    public DishEntity toEntity(Dish dish) {
        if (dish == null) {
            return null;
        }

        DishEntity dishEntity = new DishEntity();
        dishEntity.setId(dish.getId());
        dishEntity.setName(dish.getName());
        dishEntity.setDescription(dish.getDescription());
        dishEntity.setPrice(dish.getPrice());
        dishEntity.setUrlImage(dish.getUrlImage());
        dishEntity.setActive(dish.isActive());

        // Asegúrate de que las categorías y restaurantes existan
        if (dish.getIdCategory() != null) {
            CategoryEntity category = new CategoryEntity();
            category.setId(dish.getIdCategory());  // Aquí pasas el ID de la categoría
            dishEntity.setCategory(category);
        }

        if (dish.getIdRestaurant() != null) {
            RestaurantEntity restaurant = new RestaurantEntity();
            restaurant.setId(dish.getIdRestaurant());  // Aquí pasas el ID del restaurante
            dishEntity.setRestaurant(restaurant);
        }

        return dishEntity;
    }

    public Dish toDomain(DishEntity dishEntity) {
        if (dishEntity == null) {
            return null;
        }

        Dish dish = new Dish();
        dish.setId(dishEntity.getId());
        dish.setName(dishEntity.getName());
        dish.setDescription(dishEntity.getDescription());
        dish.setPrice(dishEntity.getPrice());
        dish.setUrlImage(dishEntity.getUrlImage());
        dish.setActive(dishEntity.isActive());

        if (dishEntity.getCategory() != null) {
            dish.setIdCategory(dishEntity.getCategory().getId());
        }

        if (dishEntity.getRestaurant() != null) {
            dish.setIdRestaurant(dishEntity.getRestaurant().getId());
        }

        return dish;
    }
}