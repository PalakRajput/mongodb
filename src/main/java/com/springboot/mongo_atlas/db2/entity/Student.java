package com.springboot.mongo_atlas.db2.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "demo")
public class Student {
    @Id
    private int id;
    private String name;
    private String lastName;
    private int graduationYear;
    private boolean isPassed;
    private CourseDetails courseDetails;
    private int age;
    private List<String> hobbies;

}

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
class CourseDetails {
    private String branch;
    private String rollNumber;
}

