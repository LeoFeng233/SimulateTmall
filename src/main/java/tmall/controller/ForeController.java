package tmall.controller;

import com.github.pagehelper.PageHelper;
import comparator.*;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;
import tmall.entity.*;
import tmall.service.*;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
public class ForeController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final UserService userService;
    private final ProductImageService productImageService;
    private final PropertyValueService propertyValueService;
    private final ReviewService reviewService;
    private final OrderItemService orderItemService;
    private final OrderService orderService;

    @Autowired
    public ForeController(CategoryService categoryService, ProductService productService, UserService userService, ProductImageService productImageService, PropertyValueService propertyValueService, ReviewService reviewService, OrderItemService orderItemService, OrderService orderService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.userService = userService;
        this.productImageService = productImageService;
        this.propertyValueService = propertyValueService;
        this.reviewService = reviewService;
        this.orderItemService = orderItemService;
        this.orderService = orderService;
    }

    @RequestMapping("forehome")
    public String home(Model model) {
        List<Category> categories = categoryService.selectCategoryList();
        productService.fill(categories);
        productService.fillByRow(categories);

        model.addAttribute("categories", categories);
        return "fore/home";
    }

    @RequestMapping("foreregister")
    public String register(Model model, User user) {
        String name = user.getName();

        name = HtmlUtils.htmlEscape(name);
        user.setName(name);

        if (userService.isExist(name)) {
            String msg = "用户名已经被使用,不能使用";
            model.addAttribute("msg", msg);
            model.addAttribute("user", null);

            return "fore/register";
        }
        userService.addUser(user);

        return "redirect:registerSuccessPage";
    }

    @RequestMapping("forelogin")
    public String login(String name, String password, Model model, HttpSession session) {
        name = HtmlUtils.htmlEscape(name);
        User user = userService.selectUserByNameAndPassword(name, password);

        if (user == null) {
            model.addAttribute("msg", "账号或密码错误");

            return "redirect:forehome";
        }
        session.setAttribute("user", user);
        return "redirect:forehome";
    }

    @RequestMapping("forelogout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:forehome";
    }

    @RequestMapping("foreproduct")
    public String product(Model model, Integer pid) {
        Product product = productService.selectProductById(pid);

        List<ProductImage> productSingleImages = productImageService.selectProductImageList(pid, productImageService.type_single);
        List<ProductImage> productDetailImages = productImageService.selectProductImageList(pid, productImageService.type_detail);
        product.setProductSingleImages(productSingleImages);
        product.setProductDetailImages(productDetailImages);

        List<PropertyValue> propertyValues = propertyValueService.selectPropertyValueList(pid);
        List<Review> reviews = reviewService.selectReviewListByPid(pid);
        productService.setSaleAndReviewNumber(product);

        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        model.addAttribute("propertyValues", propertyValues);

        return "fore/product";
    }

    @RequestMapping("forecheckLogin")
    @ResponseBody
    public String checkLogin(HttpSession session) {
        User user = (User) session.getAttribute("user");

        return user == null ? "fail" : "success";
    }

    @RequestMapping("foreloginAjax")
    @ResponseBody
    public String loginAjax(String name, String password, HttpSession session) {
        name = HtmlUtils.htmlEscape(name);
        User user = userService.selectUserByNameAndPassword(name, password);

        if (null == user)
            return "fail";
        session.setAttribute("user", user);
        return "success";
    }

    @RequestMapping("forecategory")
    public String category(Integer cid, String sort, Model model) {
        Category category = categoryService.selectCategoryById(cid);
        productService.fill(category);
        productService.setSaleAndReviewNumber(category.getProducts());

        if (null != sort) {
            switch (sort) {
                case "review":
                    Collections.sort(category.getProducts(), new ProductReviewComparator());
                    break;
                case "date":
                    Collections.sort(category.getProducts(), new ProductDateComparator());
                    break;

                case "saleCount":
                    Collections.sort(category.getProducts(), new ProductSaleCountComparator());
                    break;

                case "price":
                    Collections.sort(category.getProducts(), new ProductPriceComparator());
                    break;

                case "all":
                    Collections.sort(category.getProducts(), new ProductAllComparator());
                    break;
            }
        }

        model.addAttribute("category", category);
        return "fore/category";
    }

    @RequestMapping("foresearch")
    public String search(String keyword, Model model) {
        PageHelper.offsetPage(0, 20);
        List<Product> products = productService.searchProducts(keyword);

        model.addAttribute("products", products);
        return "fore/searchResult";
    }

    @RequestMapping("forebuyone")
    public String buyNow(Integer pid, Integer num, HttpSession session) {
        Product product = productService.selectProductById(pid);
        User user = (User) session.getAttribute("user");

        List<OrderItem> orderItems = orderItemService.listByUser(user.getId());

        int orderItemId = 0;
        boolean found = false;
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getPid().intValue() == product.getId().intValue()) {
                orderItem.setNumber(orderItem.getNumber() + num);
                found = true;
                orderItemId = orderItem.getId();
                break;
            }
        }

        if (!found) {
            OrderItem orderItem = new OrderItem();
            orderItem.setNumber(num);
            orderItem.setPid(pid);
            orderItem.setUid(user.getId());
            orderItemService.addOrderItem(orderItem);
            orderItemId = orderItem.getId();
        }
        return "redirect:forebuy?oiid=" + orderItemId;
    }

    @RequestMapping("forebuy")
    public String buy(Model model, Integer[] oiid, HttpSession session) {
        List<OrderItem> orderItems = new ArrayList<>();
        float total = 0;

        for (Integer id : oiid) {
            OrderItem orderItem = orderItemService.selectOrderItemById(id);
            total += orderItem.getProduct().getPromotePrice() * orderItem.getNumber();
            orderItems.add(orderItem);
        }

        session.setAttribute("orderItems", orderItems);
        model.addAttribute("total", total);
        return "fore/buy";
    }

    @RequestMapping("foreaddCart")
    @ResponseBody
    public String addCart(Integer pid, Integer num, HttpSession session) {
        Product product = productService.selectProductById(pid);
        User user = (User) session.getAttribute("user");

        List<OrderItem> orderItems = orderItemService.listByUser(user.getId());

        boolean found = false;
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getPid().intValue() == product.getId().intValue()) {
                orderItem.setNumber(orderItem.getNumber() + num);
                found = true;
                break;
            }
        }

        if (!found) {
            OrderItem orderItem = new OrderItem();
            orderItem.setNumber(num);
            orderItem.setPid(pid);
            orderItem.setUid(user.getId());
            orderItemService.addOrderItem(orderItem);
        }

        return "success";
    }

    @RequestMapping("forecart")
    public String cart(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<OrderItem> orderItems = orderItemService.listByUser(user.getId());
        model.addAttribute("orderItems", orderItems);

        return "fore/cart";
    }

    @RequestMapping("foredeleteOrderItem")
    @ResponseBody
    public String deleteOrderItem(HttpSession session, @RequestParam(value = "oiid") Integer orderItemId) {
        User user = (User) session.getAttribute("user");
        if (null == user)
            return "fail";

        orderItemService.deleteOrderItemById(orderItemId);
        return "success";
    }

    @RequestMapping("forecreateOrder")
    public String createOrder(Model model, Order order, HttpSession session) {
        User user = (User) session.getAttribute("user");
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + RandomUtils.nextInt(0, 10000);
        order.setCreateDate(new Date());
        order.setOrderCode(orderCode);
        order.setUid(user.getId());
        order.setStatus(OrderService.waitPay);
        List<OrderItem> orderItems = (List<OrderItem>) session.getAttribute("orderItems");

        float total = orderService.addOrder(order, orderItems);
        return "redirect:forealipay?oid=" + order.getId() + "&total=" + total;
    }

    @RequestMapping("forebought")
    public String bought(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<Order> orders = orderService.selectOrderList(user.getId(), OrderService.delete);

        orderItemService.fill(orders);

        model.addAttribute("orders", orders);

        return "fore/bought";
    }

    @RequestMapping("forepayed")
    public String payed(Model model, Integer oid, float total) {
        Order order = orderService.selectOrderById(oid);
        order.setStatus(OrderService.waitDelivery);
        order.setPayDate(new Date());
        order.setTotal(total);

        orderService.updateOrderById(order);
        model.addAttribute("order", order);
        return "fore/payed";
    }

    @RequestMapping("/foreconfirmPay")
    public String confirmPay(Integer oid, Model model) {
        Order order = orderService.selectOrderById(oid);
        orderItemService.fill(order);

        model.addAttribute("order", order);
        return "fore/confirmPay";
    }

    @RequestMapping("foreorderConfirmed")
    public String orderConfirmed(Integer oid) {
        Order order = orderService.selectOrderById(oid);
        order.setStatus(OrderService.waitReview);
        order.setConfirmDate(new Date());
        orderService.updateOrderById(order);

        return "fore/orderConfirmed";
    }

    @RequestMapping("foredeleteOrder")
    @ResponseBody
    public String deleteOrder(Model model, Integer oid) {
        Order order = orderService.selectOrderById(oid);
        order.setStatus(OrderService.delete);
        orderService.updateOrderById(order);
        return "success";
    }

    @RequestMapping("forereview")
    public String review(Integer oid, Model model) {
        Order order = orderService.selectOrderById(oid);
        orderItemService.fill(order);
        Product product = order.getOrderItems().get(0).getProduct();
        List<Review> reviews = reviewService.selectReviewListByPid(product.getId());
        productService.setSaleAndReviewNumber(product);

        model.addAttribute("reviews", reviews);
        model.addAttribute("product", product);
        model.addAttribute("order", order);

        return "fore/review";
    }

    @RequestMapping("foredoreview")
    public String doReview(Integer oid, Integer pid, HttpSession session, String content) {
        Order order = orderService.selectOrderById(oid);
        order.setStatus(OrderService.finish);
        orderService.updateOrderById(order);

        Product product = productService.selectProductById(pid);
        content = HtmlUtils.htmlEscape(content);

        User user = (User) session.getAttribute("user");
        Review review = new Review();
        review.setUser(user);
        review.setContent(content);
        review.setCreateDate(new Date());
        review.setPid(pid);
        review.setUid(user.getId());
        reviewService.addReview(review);


        return "redirect:forereview?oid=" + oid + "&showonly=true";
    }
}

