package com.adisilangadi.Adisil.Angadi.Server.repository;

import com.adisilangadi.Adisil.Angadi.Server.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {


}
