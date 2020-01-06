package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.domain.Company;
import com.graduation.mainapp.domain.MenuItem;
import com.graduation.mainapp.domain.Order;
import com.graduation.mainapp.domain.User;
import com.graduation.mainapp.dto.CompanyOrdersResponseDTO;
import com.graduation.mainapp.dto.OrderDTO;
import com.graduation.mainapp.dto.UserOrderResponseDTO;
import com.graduation.mainapp.exception.DomainObjectNotFoundException;
import com.graduation.mainapp.repository.OrderRepository;
import com.graduation.mainapp.repository.dao.OrderDAO;
import com.graduation.mainapp.repository.dao.rowmapper.CompanyOrdersRowMapper;
import com.graduation.mainapp.service.MenuItemService;
import com.graduation.mainapp.service.OrderService;
import com.graduation.mainapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class OrderServiceImpl implements OrderService {
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final MenuItemService menuItemService;
    private final OrderDAO orderDAO;

    @Override
    public Order save(OrderDTO orderDTO) throws DomainObjectNotFoundException {
        if (Objects.isNull(orderDTO.getId())) {
            Order order = createOrderObjectForSaving(orderDTO);
            return orderRepository.save(order);
        } else {
            Order orderForUpdating = createOrderObjectForUpdate(orderDTO);
            return orderRepository.save(orderForUpdating);
        }
    }

    private Order createOrderObjectForUpdate(OrderDTO orderDTO) throws DomainObjectNotFoundException {
        Order order = this.findByIdOrThrow(orderDTO.getId());
        MenuItem menuItem = menuItemService.findByIdOrThrow(orderDTO.getMenuItemId());
        order.setComments(orderDTO.getComments());
        order.setMenuItem(menuItem);
        order.setTimePeriod(LocalTime.parse(orderDTO.getTimePeriod(), DateTimeFormatter.ISO_TIME));
        return order;
    }

    @Override
    @Transactional
    public Order findByUser(Long userId) throws DomainObjectNotFoundException {
        User user = userService.findByIdOrThrow(userId);
        return orderRepository.findByUserAndDateOfOrder(user, LocalDate.now());
    }

    @Override
    public UserOrderResponseDTO createUserOrderResponseDTO(Order order) {
        return UserOrderResponseDTO.builder()
                .id(order.getId())
                .menuItemName(order.getMenuItem().getName())
                .timePeriod(order.getTimePeriod().toString())
                .comments(order.getComments())
                .restaurantId(order.getMenuItem().getRestaurant().getId())
                .build();
    }

    @Override
    public Order findByIdOrThrow(Long id) throws DomainObjectNotFoundException {
        return orderRepository.findById(id).orElseThrow(
                () -> new DomainObjectNotFoundException("Order with ID " + id + " was not found"));
    }

    @Override
    public void delete(Long orderId) throws DomainObjectNotFoundException {
        Order order = findByIdOrThrow(orderId);
        orderRepository.delete(order);
    }

    @Override
    public List<CompanyOrdersResponseDTO> getOrdersForCompany(Long companyId) {
        List<CompanyOrdersRowMapper> companyOrders = orderDAO.getOrdersForCompany(companyId);
        return companyOrders.stream().map(companyOrder ->
                CompanyOrdersResponseDTO.builder()
                        .username(companyOrder.getUsername())
                        .restaurantName(companyOrder.getRestaurantName())
                        .menuItemName(companyOrder.getMenuItemName())
                        .menuItemPrice(companyOrder.getMenuItemPrice())
                        .timePeriod(companyOrder.getTimePeriod())
                        .dateOfOrder(companyOrder.getDateOfOrder())
                        .comments(companyOrder.getComments())
                        .build())
                .collect(Collectors.toList());
    }

    private Order createOrderObjectForSaving(OrderDTO orderDTO) throws DomainObjectNotFoundException {
        User user = userService.findByIdOrThrow(orderDTO.getUserId());
        MenuItem menuItem = menuItemService.findByIdOrThrow(orderDTO.getMenuItemId());
        return Order.builder()
                .menuItem(menuItem)
                .user(user)
                .comments(orderDTO.getComments())
                .timePeriod(LocalTime.parse(orderDTO.getTimePeriod(), DateTimeFormatter.ISO_TIME))
                .dateOfOrder(LocalDate.now())
                .build();
    }
}
