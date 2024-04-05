package ch.cern.todo.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "task_categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_categories_seq")
    @SequenceGenerator(name = "task_categories_seq", sequenceName = "task_categories_seq", allocationSize = 1)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name", unique = true)
    private String name;

    @Column(name = "category_description")
    private String description;

    public static Category create(String name, String description) {
        return Category.builder()
                .name(name)
                .description(description)
                .build();
    }

    public static Category create(Long id, String name, String description) {
        Category category = create(name, description);
        category.setId(id);
        return category;
    }
}
