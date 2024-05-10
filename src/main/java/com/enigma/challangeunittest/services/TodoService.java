package com.enigma.challangeunittest.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.enigma.challangeunittest.model.dto.req.CreateTodoDto;
import com.enigma.challangeunittest.model.entity.Todo;
import com.enigma.challangeunittest.model.entity.User;
import com.enigma.challangeunittest.repository.TodoRepository;
import com.enigma.challangeunittest.services.interfaces.TodoInterface;
import com.enigma.challangeunittest.services.interfaces.UserInterface;

@Service
public class TodoService implements TodoInterface {
  
  @Autowired
  private TodoRepository todoRepository;

  @Autowired
  private UserInterface userInterface;
  

  @Override
  public Todo createTodo(String userId, CreateTodoDto createTodoDto) {
    System.out.println(createTodoDto);
    
    User user = this.userInterface.getProfile(userId);

    Todo todo = Todo.builder()
    .title(createTodoDto.getTitle())
    .description(createTodoDto.getDescription())
    .createdAt(new Date())
    .updatedAt(new Date())
    .user(user)
    .build();

    return this.todoRepository.save(todo);
  }

  @Override
  public List<Todo> getAllTodoById(String userId) {
    return this.todoRepository.findAllByUserId(userId);
  }

  @Override
  public Todo getDetailTodo(String id) {
    return this.todoRepository.findById(id).orElseThrow(
      () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo Tidak Ditemukan")
    );
  }

  @Override
  public Todo updateTodo(String id, CreateTodoDto createTodoDto) {
    Todo existingTodo = this.todoRepository.findById(id).orElseThrow(
      () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo Tidak Ditemukan")
    );

    existingTodo.setTitle(createTodoDto.getTitle());
    existingTodo.setDescription(createTodoDto.getDescription());
    existingTodo.setUpdatedAt(new Date());

    return this.todoRepository.save(existingTodo);
  }

  @Override
  public void deleteTodo(String id) {
    if (!this.todoRepository.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo Tidak Ditemukan");
    }

    this.todoRepository.deleteById(id);
  }
  
}
