package ch.cern.todo.service;

import ch.cern.todo.model.Category;

import java.util.List;

public interface CategoryService {
    Category findCategoryByName(String name);
    List<Category> retrieveAllCategories();
    Category persistCategory(Category category);
    Category deleteCategoryByName(String name);
}
