package ch.cern.todo.service.impl;

import ch.cern.todo.exception.CategoryAlreadyExistException;
import ch.cern.todo.exception.CategoryNotFoundException;
import ch.cern.todo.model.Category;
import ch.cern.todo.repository.CategoryRepository;
import ch.cern.todo.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Cannot find category with id : " + id));
    }

    @Override
    public List<Category> retrieveAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new CategoryAlreadyExistException("Category with name : " + category.getName() + " already exist");
        }
        return categoryRepository.saveAndFlush(category);
    }

    @Override
    @Transactional
    public Category updateCategory(Category category) {
        if (!categoryRepository.existsById(category.getId())) {
            throw new CategoryNotFoundException("Cannot find category with id : " + category.getId());
        }
        if (categoryRepository.existsByNameAndIdNot(category.getName(), category.getId())) {
            throw new CategoryAlreadyExistException("Category with name : " + category.getName() + " already taking");
        }

        return categoryRepository.saveAndFlush(category);
    }

    @Override
    @Transactional
    public void deleteCategoryById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException("Cannot find category with id : " + id);
        }
        categoryRepository.deleteById(id);
    }
}
