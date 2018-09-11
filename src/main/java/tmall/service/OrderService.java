package tmall.service;

import tmall.entity.Order;
import tmall.entity.OrderItem;

import java.util.List;

public interface OrderService {
    String waitPay = "waitPay";
    String waitDelivery = "waitDelivery";
    String waitConfirm = "waitConfirm";
    String waitReview = "waitReview";
    String finish = "finish";
    String delete = "delete";

    List<Order> selectOrderList();

    void addOrder(Order order);

    float addOrder(Order order, List<OrderItem> orderItems);

    void deleteOrderById(Integer id);

    Order selectOrderById(Integer id);

    void updateOrderById(Order order);

    List<Order> selectOrderList(Integer uid, String excludedStatus);
}
