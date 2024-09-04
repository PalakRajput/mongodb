package com.springboot.mongo_atlas.db2.repo;

import com.springboot.mongo_atlas.db2.entity.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends MongoRepository<Student, Integer> {

    //Projection to find only names of students.
    @Query(value="{}", fields="{name : 1, _id : 0}")
    List<Student> findAllStudentsName();
}
