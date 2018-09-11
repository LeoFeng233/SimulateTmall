package tmall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import tmall.entity.Category;
import tmall.entity.Property;
import tmall.service.CategoryService;
import tmall.service.PropertyService;
import tmall.util.Page;

import java.util.List;

@Controller
public class PropertyController {

    private final PropertyService propertyService;
    private final CategoryService categoryService;

    @Autowired
    public PropertyController(PropertyService propertyService, CategoryService categoryService) {
        this.propertyService = propertyService;
        this.categoryService = categoryService;
    }


    @RequestMapping("admin_property_list")
    public String list(Model model, Integer cid, Page page) {
        Category category = categoryService.selectCategoryById(cid);

        PageHelper.offsetPage(page.getStart(), page.getCount());
        List<Property> properties = propertyService.selectPropertyList(cid);
        int total = (int) new PageInfo<>(properties).getTotal();

        page.setTotal(total);
        page.setParam("&cid=" + cid);

        model.addAttribute("properties", properties);
        model.addAttribute("category", category);
        model.addAttribute("page", page);

        return "admin/listProperty";
    }

    @RequestMapping("admin_property_add")
    public String add(Property property) {
        propertyService.addProperty(property);

        return "redirect:admin_property_list?cid=" + property.getCid();
    }

    @RequestMapping("admin_property_edit")
    public String edit(Model model, Integer id) {
        Property property = propertyService.selectPropertyById(id);
        Category category = categoryService.selectCategoryById(property.getCid());

        model.addAttribute("category", category);
        model.addAttribute("property", property);

        return "admin/editProperty";
    }

    @RequestMapping("admin_property_update")
    public String update(Property property) {
        propertyService.updatePropertyById(property);

        return "redirect:admin_property_list?cid=" + property.getCid();
    }

    @RequestMapping("admin_property_delete")
    public String delete(Integer id) {
        int cid = propertyService.selectPropertyById(id).getCid();
        propertyService.deletePropertyById(id);

        return "redirect:admin_property_list?cid=" + cid;
    }
}
