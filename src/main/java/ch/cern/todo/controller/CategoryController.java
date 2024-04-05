package ch.cern.todo.controller;

import ch.cern.todo.dto.CategoryDto;
import ch.cern.todo.model.Category;
import ch.cern.todo.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDto categoryDto) {
        Category category = Category.create(categoryDto.getName(), categoryDto.getDescription());
        return new ResponseEntity<>(
                categoryService.createCategory(category),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable Long id) {
        Category category = Category.create(id, categoryDto.getName(), categoryDto.getDescription());
        return new ResponseEntity<>(
                categoryService.updateCategory(category),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> findCategoryById(@PathVariable Long id) {
        return new ResponseEntity<>(
                categoryService.findCategoryById(id),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<Category>> findAllCategories() {
        return new ResponseEntity<>(
                categoryService.retrieveAllCategories(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok().build();
    }
}
