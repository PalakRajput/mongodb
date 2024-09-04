package com.springboot.mongo_atlas.service;

import com.springboot.mongo_atlas.db2.entity.BranchStudent;
import com.springboot.mongo_atlas.db2.entity.BranchStudentCount;
import com.springboot.mongo_atlas.db2.entity.Student;
import com.springboot.mongo_atlas.db2.repo.StudentRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Service
public class StudentService {


    private final StudentRepository studentRepository;
    private final MongoTemplate mongoTemplate;

    public StudentService(StudentRepository studentRepository, @Qualifier("db2Template") MongoTemplate mongoTemplate) {
        this.studentRepository = studentRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public List<Student> studentOperations() {
        List<Student> students;


        BasicQuery query = new BasicQuery("{ age : { $lt : 50 } }");
        students = mongoTemplate.find(query, Student.class);

        BasicQuery query1 = new BasicQuery("{$and: [ {'courseDetails.branch': \"ECE\"}, {'age': {$lte: 26}} ] }");
        students = mongoTemplate.find(query1, Student.class);

        //Projection
        Query query3 = new Query();
        query3.fields().include("name", "lastName", "courseDetails.branch").exclude("id");
        students = mongoTemplate.find(query3, Student.class);


        //Aggregation

        //Count of students for each branch
        GroupOperation groupByBranch = Aggregation.group("courseDetails.branch")
                .count().as("total");
        // Aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(groupByBranch);
        // Execute aggregation
        AggregationResults<BranchStudentCount> result = mongoTemplate.aggregate(aggregation, "demo", BranchStudentCount.class);
        System.out.println("Count of students per branch: ");
        System.out.println(result.getMappedResults());

        //Get list of Student names for each branch
        GroupOperation groupOperation = Aggregation.group("courseDetails.branch")
                .push("$$ROOT").as("students");
        SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "age");
        Aggregation aggregation1 = Aggregation.newAggregation(groupOperation, sortOperation);
        AggregationResults<BranchStudent> results = mongoTemplate.aggregate(aggregation1, "demo", BranchStudent.class);
        System.out.println("Students grouped by branch: ");
        System.out.println(results.getMappedResults());

        //Average age of students
        GroupOperation go = Aggregation.group().avg("age").as("averageAge");
        Aggregation aggregation2 = Aggregation.newAggregation(go);
        AggregationResults<Map> averageAge = mongoTemplate.aggregate(aggregation2, "demo", Map.class);

        // Extract the average age from the result because db returns a document, and we need to extract result from that document object
        if (!result.getMappedResults().isEmpty()) {
            Map<String, Object> resultMap = averageAge.getMappedResults().get(0);

            System.out.println("Average age of students: " + resultMap.get("averageAge"));
        }

        //Match operation
        MatchOperation matchOperation = Aggregation.match(Criteria.where("age").is(25));
        SortOperation sortOperation1 = Aggregation.sort(Sort.Direction.DESC, "name").and(Sort.Direction.ASC, "lastName");
        Aggregation aggregation3 = newAggregation(matchOperation, sortOperation1);
        AggregationResults<Student> res = mongoTemplate.aggregate(aggregation3, "demo", Student.class);
        System.out.println("Students with age = 25 and name sorted in descending order and lastName sorted in ascending order: " + res.getMappedResults());


        //unwind operation
        MatchOperation matchOperation1 = Aggregation.match(Criteria.where("hobbies").exists(true));
        UnwindOperation unwindOperation = Aggregation.unwind("hobbies");
        GroupOperation groupOperation1 = Aggregation.group("courseDetails.branch").count().as("total");
        Aggregation aggregation4 = newAggregation(matchOperation1, unwindOperation, groupOperation1);
        AggregationResults<BranchStudentCount> hobbiesCountByBranch = mongoTemplate.aggregate(aggregation4, "demo", BranchStudentCount.class);
        System.out.println("Hobbies count by branch: " + hobbiesCountByBranch.getMappedResults());

        return students;
    }
}
