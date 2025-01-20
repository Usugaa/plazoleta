package com.restaurant.plazoleta.domain.constants;


public class RoleConstants {

    public static final Long ADMINISTRADOR = 1L;
    public static final String ADMINISTRADOR_NAME = "ADMINISTRADOR";
    public static final String ADMINISTRADOR_DESCRIPTION = "Administrador del sistema";

    public static final Long PROPIETARIO = 2L;
    public static final String PROPIETARIO_NAME = "OWNER";
    public static final String PROPIETARIO_DESCRIPTION = "Propietario de un restaurante";

    public static final Long EMPLEADO = 3L;
    public static final String EMPLEADO_NAME = "EMPLOYEE";
    public static final String EMPLEADO_DESCRIPTION = "Empleado de un restaurante";

    public static final Long CLIENTE = 4L;
    public static final String CLIENTE_NAME = "CLIENT";
    public static final String CLIENTE_DESCRIPTION = "Cliente que realiza pedidos";

    private RoleConstants() {
    }
}
