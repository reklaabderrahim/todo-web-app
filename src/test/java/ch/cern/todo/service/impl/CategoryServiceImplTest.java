package ch.cern.todo.service.impl;

import ch.cern.todo.exception.CategoryAlreadyExistException;
import ch.cern.todo.exception.CategoryNotFoundException;
import ch.cern.todo.exception.CategoryWithAssociatedTasksException;
import ch.cern.todo.model.Category;
import ch.cern.todo.repository.CategoryRepository;
import ch.cern.todo.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    public void retrieveAllCategories() {
        // given
        String name = "name";
        String description = "description";
        Category category = Category.create(name, description);

        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(category));

        // when
        List<Category> categories = categoryService.retrieveAllCategories();

        // then
        assertThat(categories.size()).isEqualTo(1);
        assertThat(categories.get(0).getName()).isEqualTo(name);
        assertThat(categories.get(0).getDescription()).isEqualTo(description);

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void createCategorySuccess() {
        // given
        String name = "name";
        String description = "description";
        Category category = Category.create(name, description);

        when(categoryRepository.existsByName(category.getName())).thenReturn(false);
        when(categoryRepository.saveAndFlush(category)).thenReturn(category);

        // when
        Category createdCategory = categoryService.createCategory(category);

        // then
        assertThat(createdCategory.getName()).isEqualTo(name);
        assertThat(createdCategory.getDescription()).isEqualTo(description);

        verify(categoryRepository, times(1)).saveAndFlush(category);
    }

    @Test
    public void createCategoryAlreadyExistException() {
        // given
        String name = "name";
        String description = "description";
        Category category = Category.create(name, description);

        when(categoryRepository.existsByName(category.getName())).thenReturn(true); // if true -> throws CategoryAlreadyExistException

        // then
        assertThrows(CategoryAlreadyExistException.class, () -> categoryService.createCategory(category));

        verify(categoryRepository, times(0)).saveAndFlush(category);
    }

    @Test
    public void updateCategorySuccess() {
        // given
        Long id = 1L;
        String name = "name";
        String description = "description";
        Category category = Category.create(id, name, description);

        when(categoryRepository.existsById(category.getId())).thenReturn(true);
        when(categoryRepository.existsByNameAndIdNot(category.getName(), category.getId())).thenReturn(false);
        when(categoryRepository.saveAndFlush(category)).thenReturn(category);

        // when
        Category createdCategory = categoryService.updateCategory(category);

        // then
        assertThat(createdCategory.getName()).isEqualTo(name);
        assertThat(createdCategory.getDescription()).isEqualTo(description);

        verify(categoryRepository, times(1)).saveAndFlush(category);
    }

    @Test
    public void updateCategoryNotFoundException() {
        // given
        Long id = 1L;
        String name = "name";
        String description = "description";
        Category category = Category.create(id, name, description);

        when(categoryRepository.existsById(category.getId())).thenReturn(false);

        // when
        assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(category));

        // then
        verify(categoryRepository, times(0)).saveAndFlush(category);
    }

    @Test
    public void updateCategoryExistInAnotherOneException() {
        // given
        Long id = 1L;
        String name = "name";
        String description = "description";
        Category category = Category.create(id, name, description);

        when(categoryRepository.existsById(category.getId())).thenReturn(true);
        when(categoryRepository.existsByNameAndIdNot(category.getName(), category.getId())).thenReturn(true);

        // when
        assertThrows(CategoryAlreadyExistException.class, () -> categoryService.updateCategory(category));

        // then
        verify(categoryRepository, times(0)).saveAndFlush(category);
    }

    @Test
    public void findCategorySuccess() {
        // given
        Long id = 1L;
        String name = "name";
        String description = "description";
        Category category = Category.create(id, name, description);

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        // when
        Category createdCategory = categoryService.findCategoryById(category.getId());

        // then
        assertThat(createdCategory.getName()).isEqualTo(name);
        assertThat(createdCategory.getDescription()).isEqualTo(description);

        verify(categoryRepository, times(1)).findById(category.getId());
    }

    @Test
    public void findCategoryNotFoundException() {
        // given
        Long id = 1L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        // when
        assertThrows(CategoryNotFoundException.class, () -> categoryService.findCategoryById(id));

        // then
        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    public void deleteCategorySuccess() {
        // given
        Long id = 1L;
        when(categoryRepository.existsById(id)).thenReturn(true);
        when(taskRepository.existsByCategoryId(id)).thenReturn(false);

        // when
        categoryService.deleteCategoryById(id);

        // then
        verify(categoryRepository, times(1)).deleteById(id);
    }

    @Test
    public void deleteCategoryNotFoundException() {
        // given
        Long id = 1L;
        when(categoryRepository.existsById(id)).thenReturn(false);

        // when
        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategoryById(id));

        // then
        verify(categoryRepository, times(0)).deleteById(id);
    }

    @Test
    public void deleteCategoryRelatedToTasksException() {
        // given
        Long id = 1L;
        when(categoryRepository.existsById(id)).thenReturn(true);
        when(taskRepository.existsByCategoryId(id)).thenReturn(true);

        // when
        assertThrows(CategoryWithAssociatedTasksException.class, () -> categoryService.deleteCategoryById(id));

        // then
        verify(categoryRepository, times(0)).deleteById(id);
    }
}