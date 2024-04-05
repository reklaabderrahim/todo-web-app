package ch.cern.todo.service;

import ch.cern.todo.model.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    Category findCategoryById(Long id);
    List<Category> retrieveAllCategories();
    Category createCategory(Category category);
    Category updateCategory(Category category);
    void deleteCategoryById(Long id);
}
