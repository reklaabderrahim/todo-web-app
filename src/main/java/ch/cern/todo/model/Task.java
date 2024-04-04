package ch.cern.todo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_categories_seq")
    @SequenceGenerator(name = "task_categories_seq", sequenceName = "task_categories_seq", allocationSize = 1)
    @Column(name = "task_id")
    private Long id;

    @Column(name = "task_name")
    private String name;

    @Column(name = "task_description")
    private String description;

    private LocalDateTime deadline;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
