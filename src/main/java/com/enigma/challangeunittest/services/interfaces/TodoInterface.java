package com.enigma.challangeunittest.services.interfaces;

import java.util.List;

import com.enigma.challangeunittest.model.dto.req.CreateTodoDto;
import com.enigma.challangeunittest.model.entity.Todo;

public interface TodoInterface {
  Todo createTodo(String userId, CreateTodoDto createTodoDto);
  List<Todo> getAllTodoById(String userId);
  Todo getDetailTodo(String id);
  Todo updateTodo(String id, CreateTodoDto createTodoDto);
  void deleteTodo(String id);
}
