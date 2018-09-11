package tmall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import tmall.entity.Category;
import tmall.entity.Product;
import tmall.entity.ProductImage;
import tmall.service.CategoryService;
import tmall.service.ProductImageService;
import tmall.service.ProductService;
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
public class ProductImageController {

    private final ProductImageService productImageService;
    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductImageController(ProductImageService productImageService, ProductService productService, CategoryService categoryService) {
        this.productImageService = productImageService;
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @RequestMapping("admin_productImage_list")
    public String list(Model model, Integer pid) {

        List<ProductImage> pisSingle = productImageService.selectProductImageList(pid, ProductImageService.type_single);
        List<ProductImage> pisDetail = productImageService.selectProductImageList(pid, ProductImageService.type_detail);
        Product product = productService.selectProductById(pid);
        Category category = categoryService.selectCategoryById(product.getCid());


        model.addAttribute("category", category);
        model.addAttribute("product", product);
        model.addAttribute("pisSingle", pisSingle);
        model.addAttribute("pisDetail", pisDetail);

        return "admin/listProductImage";
    }

    @RequestMapping("admin_productImage_add")
    public String add(ProductImage productImage, UploadImage uploadImage, HttpSession session) throws IOException {
        productImageService.addProductImage(productImage);

        String imageName = productImage.getId() + ".jpg";
        String imageFolder_small = null;
        String imageFolder_middle = null;
        //获取文件路径
        String imageFolder = null;

        if (productImageService.type_single.equals(productImage.getType())) {
            imageFolder = session.getServletContext().getRealPath("resources/img/productSingle");
            imageFolder_small = session.getServletContext().getRealPath("resources/img/productSingle_small");
            imageFolder_middle = session.getServletContext().getRealPath("resources/img/productSingle_middle");
        } else {
            imageFolder = session.getServletContext().getRealPath("resources/img/productDetail");
        }

        File image = new File(imageFolder, imageName);
        image.getParentFile().mkdirs();

        uploadImage.getImage().transferTo(image);
        BufferedImage bufferedImage = ImageUtil.change2jpg(image);
        ImageIO.write(bufferedImage, "jpg", image);

        if (productImageService.type_single.equals(productImage.getType())) {
            File f_small = new File(imageFolder_small, imageName);
            File f_middle = new File(imageFolder_middle, imageName);

            ImageUtil.resizeImage(image, 56, 56, f_small);
            ImageUtil.resizeImage(image, 217, 190, f_middle);
        }

        return "redirect:admin_productImage_list?pid=" + productImage.getPid();
    }

    @RequestMapping("admin_productImage_delete")
    public String delete(Integer id, HttpSession session) {
        ProductImage productImage = productImageService.selectProductById(id);

        String imageName = id + ".jpg";
        String imageFolder;
        String imageFolder_small = null;
        String imageFolder_middle = null;

        if (productImageService.type_single.equals(productImage.getType())) {
            imageFolder = session.getServletContext().getRealPath("resources/img/productSingle");
            imageFolder_small = session.getServletContext().getRealPath("img/productSingle_small");
            imageFolder_middle = session.getServletContext().getRealPath("img/productSingle_middle");
            new File(imageFolder, imageName).delete();
            new File(imageFolder_small, imageName).delete();
            new File(imageFolder_middle, imageName).delete();
        } else {
            imageFolder = session.getServletContext().getRealPath("resources/img/productDetail");
            new File(imageFolder, imageName).delete();
        }

        productImageService.deleteProductImageById(id);

        return "redirect:admin_productImage_list?pid=" + productImage.getPid();
    }
}
