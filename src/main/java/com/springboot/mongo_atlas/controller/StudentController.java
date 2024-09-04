package com.springboot.mongo_atlas.controller;

import com.springboot.mongo_atlas.db2.entity.Student;
import com.springboot.mongo_atlas.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {


    private final StudentService service;


    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Student>> studentOperations(){
        return ResponseEntity.ok().body(service.studentOperations());
    }
}
