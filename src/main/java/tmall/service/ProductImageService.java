package tmall.service;

import tmall.entity.ProductImage;

import java.util.List;

public interface ProductImageService {

    String type_single = "type_single";
    String type_detail = "type_detail";

    List<ProductImage> selectProductImageList(Integer pid, String type);

    ProductImage selectProductById(Integer id);

    void addProductImage(ProductImage productImage);

    void updateProductImageById(ProductImage productImage);

    void deleteProductImageById(Integer id);
}
