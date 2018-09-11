package tmall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import tmall.entity.Category;
import tmall.entity.Product;
import tmall.service.CategoryService;
import tmall.service.ProductService;
import tmall.util.Page;

import java.util.List;

@Controller
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @RequestMapping("admin_product_list")
    public String list(Model model, Integer cid, Page page) {
        PageHelper.offsetPage(page.getStart(), page.getCount());

        List<Product> products = productService.selectProductList(cid);
        productService.setFirstProductImage(products);
        Category category = categoryService.selectCategoryById(cid);

        page.setTotal((int) new PageInfo<>(products).getTotal());
        page.setParam("&cid=" + cid);

        model.addAttribute("products", products);
        model.addAttribute("category", category);
        model.addAttribute("page", page);

        return "admin/listProduct";
    }

    @RequestMapping("admin_product_add")
    public String add(Product product) {
        productService.addProduct(product);

        return "redirect:admin_product_list?cid=" + product.getCid();
    }

    @RequestMapping("admin_product_edit")
    public String edit(Model model, Integer id) {
        Product product = productService.selectProductById(id);
        Category category = categoryService.selectCategoryById(product.getCid());

        model.addAttribute("category", category);
        model.addAttribute("product", product);

        return "admin/editProduct";
    }

    @RequestMapping("admin_product_update")
    public String update(Product product) {
        productService.updateProductById(product);

        return "redirect:admin_product_list?cid=" + product.getCid();
    }

    @RequestMapping("admin_product_delete")
    public String delete(Integer id) {
        int cid = productService.selectProductById(id).getCid();
        productService.deleteProductById(id);

        return "redirect:admin_product_list?cid=" + cid;
    }
}
