package com.microservicio.restaurant.domain.constants;

public class CategoryConstants {

    public static final Long APPETIZER = 1L;
    public static final String APPETIZER_NAME = "Entrada";
    public static final String APPETIZER_DESCRIPTION = "Platos que sirven como aperitivo.";

    public static final Long COURSE = 2L;
    public static final String MAIN_COURSE_NAME = "Plato principal";
    public static final String MAIN_COURSE_DESCRIPTION = "Platos que constituyen el plato fuerte o principal de una comida.";

    public static final Long DESSERT = 3L;
    public static final String DESSERT_NAME = "Postre";
    public static final String DESSERT_DESCRIPTION = "Platos dulces o ligeros que se sirven al final de una comida.";

    public CategoryConstants() {
    }
}