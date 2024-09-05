package com.springboot.mongo_atlas.service;

import com.springboot.mongo_atlas.db1.entity.Task;
import com.springboot.mongo_atlas.db1.repo.TaskRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final MongoTemplate mongoTemplate;

    public TaskService(TaskRepository taskRepository, MongoTemplate mongoTemplate) {
        this.taskRepository = taskRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public Task saveTask(Task task) {
        //repo.save if passed with id will behave as upsert and insert will only save the records.
        task.setId(UUID.randomUUID());
        return taskRepository.save(task);
    }

    public List<Task> findAllTask() {
        return taskRepository.findAll();
    }

    public Task findTaskById(UUID taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found."));
    }

    public List<Task> findByAssignee(String assignee) {
        return taskRepository.findByAssignee(assignee);
    }

    public List<Task> findBySeverity(int severity) {
        return taskRepository.findBySeverity(severity);
    }

    public Task updateTask(Task request) {
        Optional<Task> taskOptional = taskRepository.findById(request.getId());
        Task task = taskOptional.orElseThrow(() -> new RuntimeException("Task not found"));
        task.setAssignee(request.getAssignee());
        task.setSeverity(request.getSeverity());
        task.setStoryPoints(request.getStoryPoints());
        task.setDueDate(request.getDueDate());
        task.setDescription(request.getDescription());

        //Update all with given taskId
        mongoTemplate.update(Task.class)
                .matching(Query.query(Criteria.where("taskId").is("")))
                .apply(new Update().set("assignee", "Jack").inc("storyPoints", 2))
                .all();
        return taskRepository.save(task);
    }

    public String deleteById(UUID taskId) {
        taskRepository.deleteById(taskId);

        mongoTemplate.remove(Task.class)
                .matching(Query.query(Criteria.where("taskId").is(taskId)))
                .one();
        return "Task deleted successfully.";
    }

    public void testMongoTemplate() {
        Query query = new Query();
        query.addCriteria(Criteria.where("assignee").is("John Doe"));
        List<Task> tasks = mongoTemplate.find(query, Task.class);

        Query query1 = new Query();
        query1.addCriteria(Criteria.where("severity").lt(10).gt(2));
        List<Task> tasks1 = mongoTemplate.find(query1, Task.class);

        Query query2 = new Query();
        query2.addCriteria(Criteria.where("assignee").regex("^A")); //name starts with A
        List<Task> tasks2 = mongoTemplate.find(query2, Task.class);

        Query query3 = new Query();
        query3.addCriteria(Criteria.where("assignee").regex("e$")); //name ends with e
        List<Task> tasks3 = mongoTemplate.find(query3, Task.class);

        Query query4 = new Query();
        query4.with(Sort.by(Sort.Direction.ASC, "severity"));
        List<Task> tasks4 = mongoTemplate.find(query4, Task.class);

    }
}
