package ch.cern.todo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tasks_seq")
    @SequenceGenerator(name = "tasks_seq", sequenceName = "tasks_seq", allocationSize = 1)
    @Column(name = "task_id")
    private Long id;

    @Column(name = "task_name")
    private String name;

    @Column(name = "task_description")
    private String description;

    private LocalDateTime deadline;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "category_id")
    private Category category;

    public static Task create(String name, String description, LocalDateTime deadline) {
        return Task.builder()
                .name(name)
                .description(description)
                .deadline(deadline)
                .build();
    }

    public static Task create(Long id, String name, String description, LocalDateTime deadline) {
        Task task = create(name, description, deadline);
        task.setId(id);
        return task;
    }

    public static Task linkToCategory(Task task, Category category) {
        task.setCategory(category);
        return task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(deadline, task.deadline) && Objects.equals(category, task.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, deadline, category);
    }
}
