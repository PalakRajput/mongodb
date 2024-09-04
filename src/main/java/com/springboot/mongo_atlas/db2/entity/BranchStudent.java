package com.springboot.mongo_atlas.db2.entity;

import java.util.List;

public record BranchStudent(String id, List<Student> students) {
}
