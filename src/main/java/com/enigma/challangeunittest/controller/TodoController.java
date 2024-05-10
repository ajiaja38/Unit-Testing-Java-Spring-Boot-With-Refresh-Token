package com.enigma.challangeunittest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enigma.challangeunittest.model.dto.req.CreateTodoDto;
import com.enigma.challangeunittest.model.entity.Todo;
import com.enigma.challangeunittest.services.HeaderService;
import com.enigma.challangeunittest.services.interfaces.TodoInterface;
import com.enigma.challangeunittest.utils.constant.ApiPathConstant;
import com.enigma.challangeunittest.utils.res.Response;
import com.enigma.challangeunittest.utils.res.ResponseMessage;

@RestController
@RequestMapping(
  ApiPathConstant.API +
  ApiPathConstant.VERSION +
  ApiPathConstant.TODO
)
public class TodoController {
  
  @Autowired
  private TodoInterface todoInterface;

  @Autowired
  private HeaderService headerService;

  @PostMapping
  public ResponseEntity<Response<Todo>> createTodoHandler(
    @RequestHeader(HttpHeaders.AUTHORIZATION)
    String authorizationHeader,

    @RequestBody
    CreateTodoDto createTodoDto
  ) {
    return ResponseEntity
    .status(HttpStatus.CREATED)
    .body(
      new Response<Todo>(
        HttpStatus.CREATED.value(),
        "Berhasil Menambahkan Todo",
        this.todoInterface.createTodo(
          this.headerService.userIdByToken(
            authorizationHeader.substring(7)
          ),
          createTodoDto
        )
      )
    );
  }

  @GetMapping
  public ResponseEntity<Response<List<Todo>>> getAllTodoHandler(
    @RequestHeader(HttpHeaders.AUTHORIZATION)
    String authorizationHeader
  ) {
    return ResponseEntity
    .status(HttpStatus.OK)
    .body(
      new Response<List<Todo>>(
        HttpStatus.OK.value(),
        "Berhasil Mendapatkan Semua Todo",
        this.todoInterface.getAllTodoById(
          this.headerService.userIdByToken(authorizationHeader.substring(7))
        )
      )
    );
  }

  @GetMapping("{id}")
  public ResponseEntity<Response<Todo>> getDetailTodoHandler(@PathVariable String id) {
    return ResponseEntity
    .status(HttpStatus.OK)
    .body(
      new Response<>(
        HttpStatus.OK.value(),
        "Berhasil Get Detail Todo",
        this.todoInterface.getDetailTodo(id)
      )
    );
  }

  @PutMapping("{id}")
  public ResponseEntity<Response<Todo>> updateTodoHandler(
    @PathVariable String id,
    @RequestBody CreateTodoDto createTodoDto
  ) {
    return ResponseEntity
    .status(HttpStatus.OK)
    .body(
      new Response<>(
        HttpStatus.OK.value(),
        "Berhasil Update Todo",
        this.todoInterface.updateTodo(id, createTodoDto)
      )
    );
  }

  @DeleteMapping("{id}")
  public ResponseEntity<ResponseMessage> deleteTodoHandler(@PathVariable String id) {
    return ResponseEntity
    .status(HttpStatus.OK)
    .body(
      new ResponseMessage(
        HttpStatus.OK.value(),
        "Berhasil Delete Todo"
      )
    );
  }

}
