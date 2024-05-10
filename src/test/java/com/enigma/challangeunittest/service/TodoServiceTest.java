package com.enigma.challangeunittest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.enigma.challangeunittest.model.dto.req.CreateTodoDto;
import com.enigma.challangeunittest.model.entity.Todo;
import com.enigma.challangeunittest.model.entity.User;
import com.enigma.challangeunittest.repository.TodoRepository;
import com.enigma.challangeunittest.services.TodoService;
import com.enigma.challangeunittest.services.interfaces.UserInterface;

public class TodoServiceTest {
  
  @Mock
  private TodoRepository todoRepository;

  @Mock
  private UserInterface userInterface;

  @InjectMocks
  private TodoService todoService;

  @BeforeEach
  public void setUpTest() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("Successfully create todo")
  public void successCreateTodoTest() {
    User user = User.builder().id("user-1").build(); 
    
    Todo expectedTodo = Todo.builder()
      .id("todo-1")
      .title("todo title")
      .description("todo description")
      .createdAt(new Date())
      .updatedAt(new Date())
      .user(user)
      .build();

    CreateTodoDto createTodoDto = CreateTodoDto.builder()
      .title("Title Todo")
      .description("todo description")
      .build();

    when(this.userInterface.getProfile(Mockito.anyString())).thenReturn(user);
                                               
    when(this.todoRepository.save(Mockito.any(Todo.class))).thenReturn(expectedTodo);
    
    Todo result = this.todoService.createTodo(user.getId(), createTodoDto);

    assertEquals(user.getId(), result.getUser().getId(), "User harus terdaftar");
    assertEquals(expectedTodo.getId(), result.getId(), "Id todo harus sama");
    assertEquals(expectedTodo.getTitle(), result.getTitle(), "title todo harus sama");
    assertEquals(expectedTodo.getDescription(), result.getDescription(), "deskripsi todo harus sama");
    assertEquals(expectedTodo.getCreatedAt(), result.getCreatedAt(), "tanggal dibuat harus sama");
    assertEquals(expectedTodo.getUpdatedAt(), result.getUpdatedAt(), "tanggal update harus sama");
    assertEquals(expectedTodo.getUser().getId(), result.getUser().getId(), "id user harus sama");
  }

  @Test
  @DisplayName("Failed to create todo, User not found")
  public void failCreateTodoUserNotFoundTest() {
    when(this.userInterface.getProfile(Mockito.anyString())).thenReturn(null);

    CreateTodoDto createTodoDto = CreateTodoDto.builder()
      .title("Title Todo")
      .description("todo description")
      .build();

    Todo result = this.todoService.createTodo("invalid user id", createTodoDto);

    assertNull(result, "Gagal create todo user tidak ditemukan");
  }

  @Test
  @DisplayName("Get All Todo with existing data successfully")
  public void successGetAllExistingTodo() {
    User expectedUser = User.builder().id("user-1").build(); 

    List<Todo> expectedTodos = new ArrayList<>();
    expectedTodos.add(
      Todo.builder()
        .title("todo 1")
        .description("desc todo 1")
        .createdAt(new Date())
        .updatedAt(new Date())
        .user(expectedUser)
        .build()
    );
    expectedTodos.add(
      Todo.builder()
        .title("todo 2")
        .description("desc todo 2")
        .createdAt(new Date())
        .updatedAt(new Date())
        .user(expectedUser)
        .build()
    );

    when(this.todoRepository.findAllByUserId(expectedUser.getId())).thenReturn(expectedTodos);

    List<Todo> result = this.todoService.getAllTodoById(expectedUser.getId());

    assertEquals(expectedTodos.size(), result.size(), "Total jumlah todo harus sama");

    for (int i = 0; i < expectedTodos.size(); i++) {
      assertEquals(expectedTodos.get(i), result.get(i), "Berhasil get todo dengan existing expected todo list");
    }
    
  }

  @Test
  @DisplayName("Get empty todo")
  public void successGetEmptyTodo() {
    User expectedUser = User.builder().id("user-1").build();

    when(this.todoRepository.findAllByUserId(expectedUser.getId())).thenReturn(new ArrayList<>());

    List<Todo> result = this.todoService.getAllTodoById(expectedUser.getId());

    assertEquals(0, result.size(), "Belum ada todo yang diinputkan");
  }

}
