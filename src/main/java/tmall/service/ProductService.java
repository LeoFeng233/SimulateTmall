package tmall.service;

import tmall.entity.Category;
import tmall.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> selectProductList(Integer cid);

    Product selectProductById(Integer id);

    void addProduct(Product product);

    void updateProductById(Product product);

    void deleteProductById(Integer id);

    void setFirstProductImage(Product product);

    void setFirstProductImage(List<Product> products);

    void fill(List<Category> categories);

    void fill(Category category);

    void fillByRow(List<Category> categories);

    void setSaleAndReviewNumber(Product product);

    void setSaleAndReviewNumber(List<Product> products);

    List<Product> searchProducts(String keyword);
}
