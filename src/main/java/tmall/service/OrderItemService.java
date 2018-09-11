package tmall.service;

import tmall.entity.Order;
import tmall.entity.OrderItem;
import tmall.entity.User;

import java.util.List;

public interface OrderItemService {
    List<OrderItem> selectOrderItemList();

    void addOrderItem(OrderItem orderItem);

    void deleteOrderItemById(Integer id);

    OrderItem selectOrderItemById(Integer id);

    void updateOrderItemById(OrderItem orderItem);

    void fill(List<Order> orders);

    void fill(Order order);

    int getSaleCount(Integer pid);

    List<OrderItem> listByUser(Integer uid);
}
