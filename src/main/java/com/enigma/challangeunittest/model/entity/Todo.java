package com.enigma.challangeunittest.model.entity;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "mst_todo")
public class Todo {
  
  @Id
  private String id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  private Date createdAt;

  private Date updatedAt;

  @ManyToOne
  @JsonIgnore
  @JoinColumn(name = "user_id")
  private User user;

  @PrePersist
  public void prefixId() {
    this.id = "todo-" + UUID.randomUUID();
  }

}
