package com.texoit.gra.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="movies")
public class Movie {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(unique = true, nullable = false)
	private String title;

	private Long year;
	
	private String studios;
	
	@ManyToMany
	@JoinTable(
			  name = "producer_has_movies", 
			  joinColumns = @JoinColumn(name = "movieId"), 
			  inverseJoinColumns = @JoinColumn(name = "producerId"))
	private List<Producer> producers;
	
	private boolean winner;
	
}
