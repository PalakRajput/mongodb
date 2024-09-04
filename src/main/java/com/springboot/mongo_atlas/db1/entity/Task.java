package com.springboot.mongo_atlas.db1.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "tasks")
public class Task {
    @Id
    private UUID id;
    private String title;
    private String description;
    private int severity;
    private LocalDate dueDate;
    private String assignee;
    private int storyPoints;

}
