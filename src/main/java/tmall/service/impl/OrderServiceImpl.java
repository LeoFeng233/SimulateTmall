package tmall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tmall.dao.OrderMapper;
import tmall.entity.Order;
import tmall.entity.OrderExample;
import tmall.entity.OrderItem;
import tmall.entity.User;
import tmall.service.OrderItemService;
import tmall.service.OrderService;
import tmall.service.UserService;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final UserService userService;
    private final OrderItemService orderItemService;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper, UserService userService, OrderItemService orderItemService) {
        this.orderMapper = orderMapper;
        this.userService = userService;
        this.orderItemService = orderItemService;
    }

    @Override
    public List<Order> selectOrderList() {
        OrderExample orderExample = new OrderExample();
        orderExample.setOrderByClause("id DESC");

        return orderMapper.selectByExample(orderExample);
    }

    @Override
    public void addOrder(Order order) {
        orderMapper.insert(order);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")
    public float addOrder(Order order, List<OrderItem> orderItems) {
        float total = 0;
        addOrder(order);

        for (OrderItem orderItem : orderItems) {
            orderItem.setOid(order.getId());
            orderItemService.updateOrderItemById(orderItem);
            total += orderItem.getProduct().getPromotePrice() * orderItem.getNumber();
        }

        return total;
    }

    @Override
    public void deleteOrderById(Integer id) {
        orderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Order selectOrderById(Integer id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateOrderById(Order order) {
        orderMapper.updateByPrimaryKeySelective(order);
    }

    public void setUser(List<Order> orders) {
        for (Order order : orders)
            setUser(order);
    }

    public void setUser(Order order) {
        int uid = order.getUid();
        User u = userService.selectUserById(uid);
        order.setUser(u);
    }

    @Override
    public List<Order> selectOrderList(Integer uid, String excludedStatus) {
        OrderExample orderExample = new OrderExample();
        orderExample.createCriteria().andUidEqualTo(uid).andStatusNotEqualTo(excludedStatus);
        orderExample.setOrderByClause("id DESC");

        List<Order> orders = orderMapper.selectByExample(orderExample);
        return orders;
    }
}
