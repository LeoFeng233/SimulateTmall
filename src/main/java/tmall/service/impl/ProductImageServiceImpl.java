package tmall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tmall.dao.ProductImageMapper;
import tmall.entity.ProductImage;
import tmall.entity.ProductImageExample;
import tmall.service.ProductImageService;

import java.util.List;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageMapper productImageMapper;

    @Autowired
    public ProductImageServiceImpl(ProductImageMapper productImageMapper) {
        this.productImageMapper = productImageMapper;
    }

    @Override
    public List<ProductImage> selectProductImageList(Integer pid, String type) {
        ProductImageExample productImageExample = new ProductImageExample();
        productImageExample.createCriteria().andPidEqualTo(pid).andTypeEqualTo(type);
        productImageExample.setOrderByClause("id DESC");

        return productImageMapper.selectByExample(productImageExample);
    }

    @Override
    public ProductImage selectProductById(Integer id) {
        return productImageMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addProductImage(ProductImage productImage) {
        productImageMapper.insert(productImage);
    }

    @Override
    public void updateProductImageById(ProductImage productImage) {
        productImageMapper.updateByPrimaryKey(productImage);
    }

    @Override
    public void deleteProductImageById(Integer id) {
        productImageMapper.deleteByPrimaryKey(id);
    }
}
