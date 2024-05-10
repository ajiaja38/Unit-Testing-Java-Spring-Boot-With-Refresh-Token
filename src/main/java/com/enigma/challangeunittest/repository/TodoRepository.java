package com.enigma.challangeunittest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enigma.challangeunittest.model.entity.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, String> {
  List<Todo> findAllByUserId(String userId);
}
