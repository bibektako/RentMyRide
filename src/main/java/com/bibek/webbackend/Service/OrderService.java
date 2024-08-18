package com.bibek.webbackend.Service;

import com.bibek.webbackend.Dto.OrderDto;
import com.bibek.webbackend.Dto.OrderUserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {

    String saveOrder(OrderDto orderDto);

    List<OrderUserDto> getAllOrders();

    List<OrderUserDto> getOrdersByUserId(int userId);

    void deleteOrderByOrderId(int orderId);

}
