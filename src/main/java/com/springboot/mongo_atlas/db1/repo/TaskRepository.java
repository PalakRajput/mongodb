package com.springboot.mongo_atlas.db1.repo;

import com.springboot.mongo_atlas.db1.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends MongoRepository<Task, UUID> {
    List<Task> findByAssignee(String assignee);

    //For multiple fields give comma separated values
    //@Query("{severity: ?0, assignee: ?1, storyPoints: ?2}")
    @Query("{severity: ?0}")
    List<Task> findBySeverity(int severity);

}
