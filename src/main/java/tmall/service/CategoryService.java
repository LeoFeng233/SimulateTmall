package tmall.service;

import tmall.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> selectCategoryList();

    void addCategory(Category category);

    void deleteCategoryById(Integer id);

    Category selectCategoryById(Integer id);

    void updateCategoryById(Category category);
}
