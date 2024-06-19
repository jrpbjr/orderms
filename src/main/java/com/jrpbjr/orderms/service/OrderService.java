package com.jrpbjr.orderms.service;

import com.jrpbjr.orderms.entity.OrderEntity;
import com.jrpbjr.orderms.entity.OrderItem;
import com.jrpbjr.orderms.listener.dto.OrderCreatedEvent;
import org.springframework.stereotype.Service;
import com.jrpbjr.orderms.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void save(OrderCreatedEvent event) {
       var entity = new OrderEntity();
       entity.setOrderId(event.codigoPedido());
       entity.setCustomerId(event.codigoPedido());
       entity.setTotal(getTotal(event));
       entity.setItems(getOrderItems(event));

       orderRepository.save(entity);

    }

    private BigDecimal getTotal(OrderCreatedEvent event) {
        return event.itens().stream()
                .map(i -> i.preco().multiply(BigDecimal.valueOf(i.quantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static List<OrderItem> getOrderItems(OrderCreatedEvent event) {
        return event.itens().stream()
                .map(i -> new OrderItem(i.produto(), i.quantidade(), i.preco())).toList();
    }

}
