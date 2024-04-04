package ch.cern.todo.service.impl;

import ch.cern.todo.exception.CategoryNotFoundException;
import ch.cern.todo.model.Category;
import ch.cern.todo.repository.CategoryRepository;
import ch.cern.todo.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category findCategoryByName(String name) {
        return categoryRepository.findCategoryByName(name)
                .orElseThrow(() -> new CategoryNotFoundException("Cannot find category with name : " + name));
    }

    @Override
    public List<Category> retrieveAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category persistCategory(Category category) {
        return categoryRepository.saveAndFlush(category);
    }

    @Override
    public Category deleteCategoryByName(String name) {
        return categoryRepository.deleteByName(name)
                .orElseThrow(() -> new CategoryNotFoundException("Cannot find category with name : " + name));
    }
}
