package tmall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tmall.entity.Category;
import tmall.entity.Product;
import tmall.entity.PropertyValue;
import tmall.service.CategoryService;
import tmall.service.ProductService;
import tmall.service.PropertyValueService;

import java.util.List;

@Controller
public class PropertyValueController {

    private final PropertyValueService propertyValueService;
    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public PropertyValueController(PropertyValueService propertyValueService, ProductService productService, CategoryService categoryService) {
        this.propertyValueService = propertyValueService;
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @RequestMapping("admin_propertyValue_edit")
    public String edit(Model model, Integer pid) {
        Product product = productService.selectProductById(pid);
        Category category = categoryService.selectCategoryById(product.getCid());

        propertyValueService.init(product);
        List<PropertyValue> propertyValues = propertyValueService.selectPropertyValueList(product.getId());
        model.addAttribute("propertyValues", propertyValues);
        model.addAttribute("product", product);
        model.addAttribute("category", category);

        return "admin/editPropertyValue";
    }

    @RequestMapping("admin_propertyValue_update")
    @ResponseBody
    public String update(PropertyValue propertyValue) {
        propertyValueService.addPropertyValue(propertyValue);

        return "success";
    }
}
