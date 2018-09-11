package tmall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tmall.dao.CategoryMapper;
import tmall.entity.Category;
import tmall.entity.CategoryExample;
import tmall.service.CategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<Category> selectCategoryList() {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("id desc");

        return categoryMapper.selectByExample(categoryExample);
    }

    @Override
    public void addCategory(Category category) {
        categoryMapper.insertSelective(category);
    }

    @Override
    public void deleteCategoryById(Integer id) {
        categoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Category selectCategoryById(Integer id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateCategoryById(Category category) {
        categoryMapper.updateByPrimaryKey(category);
    }
}
