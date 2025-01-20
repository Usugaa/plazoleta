package com.microservicio.restaurant.domain.usecase;

import com.microservicio.restaurant.domain.api.IDishServicePort;
import com.microservicio.restaurant.domain.constants.CategoryConstants;
import com.microservicio.restaurant.domain.model.Category;
import com.microservicio.restaurant.domain.model.Dish;
import com.microservicio.restaurant.domain.spi.IDishPersistencePort;

import java.util.List;
import java.util.Optional;


public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;

    public DishUseCase(IDishPersistencePort dishPersistencePort) {
        this.dishPersistencePort = dishPersistencePort;
    }

    @Override
    public Dish saveDish(Dish dish) {
        Category category = validateCategory(dish.getIdCategory());
        if (category == null) {
            return null;
        }

        dish.setIdCategory(category.getId());
        return dishPersistencePort.saveDish(dish);
    }

    @Override
    public Optional<Dish> findDishById(Long id) {
        return dishPersistencePort.findDishById(id);
    }

    @Override
    public Dish updateDish(Long id, String description, Long price) {
        return dishPersistencePort.updateDish(id, description, price);
    }


    @Override
    public Dish updateDishStatus(Long id, boolean active) {
        Optional<Dish> optionalDish = dishPersistencePort.findDishById(id);
        if (optionalDish.isEmpty()) {
            return null;
        }

        return dishPersistencePort.updateDishStatus(id, active);
    }

    @Override
    public List<Dish> getDishesByRestaurant(Long idRestaurant) {
        return dishPersistencePort.findDishesByRestaurant(idRestaurant);
    }


    private Category validateCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        if (categoryId.equals(CategoryConstants.APPETIZER)) {
            return new Category(CategoryConstants.APPETIZER, CategoryConstants.APPETIZER_NAME, CategoryConstants.APPETIZER_DESCRIPTION);
        } else if (categoryId.equals(CategoryConstants.COURSE)) {
            return new Category(CategoryConstants.COURSE, CategoryConstants.MAIN_COURSE_NAME, CategoryConstants.MAIN_COURSE_DESCRIPTION);
        } else if (categoryId.equals(CategoryConstants.DESSERT)) {
            return new Category(CategoryConstants.DESSERT, CategoryConstants.DESSERT_NAME, CategoryConstants.DESSERT_DESCRIPTION);
        } else {
            return null;
        }
    }
}
