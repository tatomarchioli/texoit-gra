package com.texoit.gra.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
	
	private String title;

	private Long year;
	
	private String studios;
	
	@ManyToMany(mappedBy = "movies", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private List<Producer> producers;
	
	private boolean winner;
	
}
