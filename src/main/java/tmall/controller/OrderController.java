package tmall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import tmall.entity.Order;
import tmall.service.OrderItemService;
import tmall.service.OrderService;
import tmall.util.Page;

import java.util.Date;
import java.util.List;

@Controller
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @Autowired
    public OrderController(OrderService orderService, OrderItemService orderItemService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    @RequestMapping("admin_order_list")
    public String list(Model model, Page page) {
        PageHelper.offsetPage(page.getStart(), page.getCount());

        List<Order> orders = orderService.selectOrderList();
        page.setTotal((int) new PageInfo<>(orders).getTotal());
        orderItemService.fill(orders);

        model.addAttribute("orders", orders);
        model.addAttribute("page", page);

        return "admin/listOrder";
    }

    @RequestMapping("admin_order_delivery")
    public String delivery(Integer id) {
        Order order = orderService.selectOrderById(id);

        if (order.getStatus().equals(orderService.waitDelivery)) {
            order.setDeliveryDate(new Date());
            order.setStatus(orderService.waitConfirm);
            orderService.updateOrderById(order);
        }

        return "redirect:admin_order_list";
    }
}
