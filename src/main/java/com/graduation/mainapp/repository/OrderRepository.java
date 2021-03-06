package com.graduation.mainapp.repository;

import com.graduation.mainapp.domain.Order;
import com.graduation.mainapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByUserAndDateOfOrder(User user, LocalDate dateOfOrder);

    void deleteAllByUser(User user);
}
