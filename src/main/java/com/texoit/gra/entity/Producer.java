package com.texoit.gra.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;
import lombok.NonNull;

@Data
@Entity
@Table(name="producers")
public class Producer {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@NonNull
	private String name;
	
	@ManyToMany
	@JoinTable(
			  name = "producer_has_movies", 
			  joinColumns = @JoinColumn(name = "producerId"), 
			  inverseJoinColumns = @JoinColumn(name = "movieId"))
	private List<Movie> movies;
}
