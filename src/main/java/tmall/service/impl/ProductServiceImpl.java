package tmall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tmall.dao.ProductMapper;
import tmall.entity.Category;
import tmall.entity.Product;
import tmall.entity.ProductExample;
import tmall.entity.ProductImage;
import tmall.service.OrderItemService;
import tmall.service.ProductImageService;
import tmall.service.ProductService;
import tmall.service.ReviewService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ProductImageService productImageService;
    private final ReviewService reviewService;

    @Autowired
    public void setOrderItemService(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    private OrderItemService orderItemService;

    @Autowired
    public ProductServiceImpl(ProductMapper productMapper, ProductImageService productImageService, ReviewService reviewService) {
        this.productMapper = productMapper;
        this.productImageService = productImageService;
        this.reviewService = reviewService;
    }

    @Override
    public List<Product> selectProductList(Integer cid) {
        ProductExample productExample = new ProductExample();
        productExample.createCriteria().andCidEqualTo(cid);
        productExample.setOrderByClause("id DESC");

        return productMapper.selectByExample(productExample);
    }

    @Override
    public Product selectProductById(Integer id) {
        return productMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addProduct(Product product) {
        productMapper.insert(product);
    }

    @Override
    public void updateProductById(Product product) {
        productMapper.updateByPrimaryKey(product);
    }

    @Override
    public void deleteProductById(Integer id) {
        productMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void setFirstProductImage(Product product) {
        List<ProductImage> productImages = productImageService.selectProductImageList(product.getId(), ProductImageService.type_single);
        if (!productImages.isEmpty()) {
            ProductImage productImage = productImages.get(0);
            product.setFirstProductImage(productImage);
        }
    }

    @Override
    public void setFirstProductImage(List<Product> products) {
        for (Product product : products) {
            setFirstProductImage(product);
        }
    }

    @Override
    public void fill(List<Category> categories) {
        for (Category category : categories) {
            fill(category);
        }
    }

    @Override
    public void fill(Category category) {
        List<Product> products = selectProductList(category.getId());
        setFirstProductImage(products);
        category.setProducts(products);
    }

    @Override
    public void fillByRow(List<Category> categories) {
        int productNumberEachRow = 8;

        for (Category category : categories) {
            List<Product> products = category.getProducts();
            if (products == null) {
                products = selectProductList(category.getId());
            }
            setFirstProductImage(products);

            List<List<Product>> productsByRow = new ArrayList<>();
            for (int i = 0; i < products.size(); i += productNumberEachRow) {
                int size = i + productNumberEachRow;
                size = size > products.size() ? products.size() : size;
                List<Product> productsOfEachRow = products.subList(i, size);
                productsByRow.add(productsOfEachRow);
            }

            category.setProductsByRow(productsByRow);
        }
    }

    @Override
    public void setSaleAndReviewNumber(Product product) {
        product.setSaleCount(orderItemService.getSaleCount(product.getId()));
        product.setReviewCount(reviewService.getCount(product.getId()));
    }

    @Override
    public void setSaleAndReviewNumber(List<Product> products) {
        for (Product product : products) {
            setSaleAndReviewNumber(product);
        }
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        ProductExample productExample = new ProductExample();
        productExample.createCriteria().andNameLike("%" + keyword + "%");

        List<Product> products = productMapper.selectByExample(productExample);
        setFirstProductImage(products);
        setSaleAndReviewNumber(products);

        return products;
    }
}
