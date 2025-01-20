package com.microservicio.restaurant.infraestructure.configuration;

import com.microservicio.restaurant.application.mapper.*;
import com.microservicio.restaurant.domain.api.IDishServicePort;
import com.microservicio.restaurant.domain.api.IOrderServicePort;
import com.microservicio.restaurant.domain.api.IRestaurantServicePort;
import com.microservicio.restaurant.domain.spi.*;
import com.microservicio.restaurant.domain.usecase.DishUseCase;
import com.microservicio.restaurant.domain.usecase.OrderUseCase;
import com.microservicio.restaurant.domain.usecase.RestaurantUseCase;
import com.microservicio.restaurant.infraestructure.input.client.TraceabilityFeignClient;
import com.microservicio.restaurant.infraestructure.input.client.UserFeignClient;
import com.microservicio.restaurant.infraestructure.output.jpa.adapter.*;
import com.microservicio.restaurant.infraestructure.output.jpa.mapper.DishEntityMapper;
import com.microservicio.restaurant.infraestructure.output.jpa.mapper.RestaurantEntityMapper;
import com.microservicio.restaurant.infraestructure.output.jpa.repository.IDishRepository;
import com.microservicio.restaurant.infraestructure.output.jpa.repository.IRestaurantRepository;
import com.microservicio.restaurant.infraestructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IRestaurantRepository restaurantRepository;
    private final RestaurantEntityMapper restaurantEntityMapper;
    private final IDishRepository dishRepository;
    private final DishEntityMapper dishEntityMapper;
    private final UserFeignClient userFeignClient;
    private final TraceabilityFeignClient traceabilityFeignClient;
    private final JwtService jwtService;

    @Bean
    public IRestaurantePersistencePort restaurantePersistencePort() {
        return new RestaurantJpaAdapter(restaurantRepository, restaurantEntityMapper);
    }

    @Bean
    public IUserPersistencePort userPersistencePort() {
        return new UserAdapter(userFeignClient);
    }

    @Bean
    public ITraceabilityPersistencePort traceabilityPersistencePort() {
        return new TraceabilityAdapter(traceabilityFeignClient);
    }

    @Bean
    public TraceabilityMapper traceabilityMapper() {
        return new TraceabilityMapper();
    }

    @Bean
    public IRestaurantServicePort restaurantServicePort(
            IRestaurantePersistencePort restaurantePersistencePort,
            IUserPersistencePort userPersistencePort,
            ITraceabilityPersistencePort traceabilityPersistencePort) {
        return new RestaurantUseCase(restaurantePersistencePort, userPersistencePort, traceabilityPersistencePort);
    }

    @Bean
    public IDishPersistencePort dishPersistencePort() {
        return new DishJpaAdapter(dishRepository, dishEntityMapper, jwtService);
    }

    @Bean
    public IDishServicePort dishServicePort() {
        return new DishUseCase(dishPersistencePort());
    }

    @Bean
    public RestaurantResponseMapper restaurantResponseMapper(){
        return new RestaurantResponseMapper();
    }

    @Bean
    public DishRequestMapper dishRequestMapper() {
        return new DishRequestMapper();
    }

    @Bean
    public OrderRequestMapper orderRequestMapper(){
        return new OrderRequestMapper();
    }

    @Bean
    public OrderDishesRequestMapper orderDishesRequestMapper(){
        return new OrderDishesRequestMapper();
    }

    @Bean
    public IOrderPersistencePort orderPersistencePort(OrderJpaAdapter orderJpaAdapter) {
        return orderJpaAdapter;
    }

    @Bean
    public IOrderServicePort orderServicePort(IOrderPersistencePort orderPersistencePort, IMessagePersistencePort messagePersistencePort, ITraceabilityPersistencePort traceabilityPersistencePort) {
        return new OrderUseCase(orderPersistencePort, messagePersistencePort, traceabilityPersistencePort);
    }
}