package tmall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import tmall.entity.Category;
import tmall.service.CategoryService;
import tmall.util.ImageUtil;
import tmall.util.Page;
import tmall.util.UploadImage;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping("admin_category_list")
    public String list(Model model, Page page) {
        PageHelper.offsetPage(page.getStart(), page.getCount());
        List<Category> categories = categoryService.selectCategoryList();
        int total = (int) new PageInfo<>(categories).getTotal();
        page.setTotal(total);
        model.addAttribute("categories", categories);
        model.addAttribute("page", page);

        return "admin/listCategory";
    }

    @RequestMapping("admin_category_add")
    public String add(Category category, UploadImage uploadImage, HttpSession session) throws IOException {
        categoryService.addCategory(category);

        //获取文件路径
        File imageFolder = new File(session.getServletContext().getRealPath("resources/img/category"));
        File image = new File(imageFolder, category.getId() + "jpg");
        if (!imageFolder.exists())
            imageFolder.mkdirs();
        uploadImage.getImage().transferTo(image);
        BufferedImage bufferedImage = ImageUtil.change2jpg(image);
        ImageIO.write(bufferedImage, "jpg", image);

        return "redirect:/admin_category_list";
    }

    @RequestMapping("admin_category_delete")
    public String delete(Integer id, HttpSession session) {
        categoryService.deleteCategoryById(id);

        File imageFolder = new File(session.getServletContext().getRealPath("resources/img/category"));
        File image = new File(imageFolder, id + ".jpg");
        if (image.exists())
            image.delete();

        return "redirect:/admin_category_list";
    }

    @RequestMapping("admin_category_edit")
    public String edit(Model model, Integer id) {
        Category category = categoryService.selectCategoryById(id);
        model.addAttribute("category", category);

        return "admin/editCategory";
    }

    @RequestMapping("admin_category_update")
    public String update(Category category, UploadImage uploadImage, HttpSession session) throws IOException {
        categoryService.updateCategoryById(category);

        File imageFolder = new File(session.getServletContext().getRealPath("resources/img/category"));
        File image = new File(imageFolder, category.getId() + ".jpg");
        if (!imageFolder.exists())
            imageFolder.mkdirs();
        uploadImage.getImage().transferTo(image);

        return "redirect:/admin_category_list";
    }


}
