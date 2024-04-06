package ch.cern.todo.controller;

import ch.cern.todo.dto.CategoryDto;
import ch.cern.todo.exception.CategoryAlreadyExistException;
import ch.cern.todo.exception.CategoryNotFoundException;
import ch.cern.todo.model.Category;
import ch.cern.todo.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CategoryService categoryService;

    @Test
    void createCategorySuccess() throws Exception {
        // given
        String categoryDescription = "category description";
        String name = "category";
        Category category = Category.create(null, name, categoryDescription);
        CategoryDto categoryDto = CategoryDto.builder()
                .name(name)
                .description(categoryDescription)
                .build();
        Mockito.when(categoryService.createCategory(category)).thenReturn(category);

        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(category)));
    }

    @Test
    void createCategoryNameAttributeValidationFailed() throws Exception {
        // given
        String categoryDescription = "category description";
        String name = "";
        Category category = Category.create(null, name, categoryDescription);
        CategoryDto categoryDto = CategoryDto.builder()
                .name(name)
                .description(categoryDescription)
                .build();
        Mockito.when(categoryService.createCategory(category)).thenReturn(category);

        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Category name cannot be blank")));
    }

    @Test
    void createCategoryAlreadyExistException() throws Exception {
        // given
        String categoryDescription = "category description";
        String name = "category";
        Category category = Category.create(null, name, categoryDescription);
        CategoryDto categoryDto = CategoryDto.builder()
                .name(name)
                .description(categoryDescription)
                .build();
        Mockito.when(categoryService.createCategory(category))
                .thenThrow(new CategoryAlreadyExistException("Category with name : " + category.getName() + " already exist"));

        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Category with name : " + category.getName() + " already exist")));
    }

    @Test
    void createCategoryByteLengthValidationFailed() throws Exception {
        // given
        String categoryDescription = "category description";
        String _99CharsPlusEmoji = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789\uD83D\uDE00";
        Category category = Category.create(null, _99CharsPlusEmoji, categoryDescription);
        CategoryDto categoryDto = CategoryDto.builder()
                .name(_99CharsPlusEmoji)
                .description(categoryDescription)
                .build();
        Mockito.when(categoryService.createCategory(category)).thenReturn(category);

        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Category name byte size should not exceed 100 bytes")));
    }

    @Test
    void updateCategorySuccess() throws Exception {
        // given
        String categoryDescription = "category description";
        String name = "category";
        Category category = Category.create(1L, name, categoryDescription);
        CategoryDto categoryDto = CategoryDto.builder()
                .name(name)
                .description(categoryDescription)
                .build();
        Mockito.when(categoryService.updateCategory(category)).thenReturn(category);

        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(category)));
    }

    @Test
    void updateCategoryDescriptionAttributeValidationFailed() throws Exception {
        // given
        String categoryDescription = "    ";
        String name = "category";
        Category category = Category.create(1L, name, categoryDescription);
        CategoryDto categoryDto = CategoryDto.builder()
                .name(name)
                .description(categoryDescription)
                .build();
        Mockito.when(categoryService.updateCategory(category)).thenReturn(category);

        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Category description cannot be blank")));
    }

    @Test
    void updateCategoryNotFoundException() throws Exception {
        // given
        String categoryDescription = "category description";
        String name = "category";
        Category category = Category.create(1L, name, categoryDescription);
        CategoryDto categoryDto = CategoryDto.builder()
                .name(name)
                .description(categoryDescription)
                .build();
        Mockito.when(categoryService.updateCategory(category))
                .thenThrow(new CategoryNotFoundException("Cannot find category with id : " + category.getId()));

        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/categories/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Cannot find category with id : " + category.getId())));
    }

    @Test
    void findCategorySuccess() throws Exception {
        // given
        String categoryDescription = "category description";
        String name = "category";
        Category category = Category.create(1L, name, categoryDescription);

        Mockito.when(categoryService.findCategoryById(1L)).thenReturn(category);

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(category)));
    }

    @Test
    void findCategoryNotFoundException() throws Exception {
        // given
        String categoryDescription = "category description";
        String name = "category";
        Category category = Category.create(1L, name, categoryDescription);

        Mockito.when(categoryService.findCategoryById(1L))
                .thenThrow(new CategoryNotFoundException("Cannot find category with id : " + category.getId()));

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Cannot find category with id : " + category.getId())));
    }

    @Test
    void findAllCategories() throws Exception {
        // given
        String categoryDescription = "category description";
        String name = "category";
        List<Category> categories = Collections.singletonList(Category.create(1L, name, categoryDescription));

        Mockito.when(categoryService.retrieveAllCategories()).thenReturn(categories);

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(categories)));
    }

    @Test
    void deleteCategorySuccess() throws Exception {
        // given
        Mockito.doNothing().when(categoryService).deleteCategoryById(1L);

        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}