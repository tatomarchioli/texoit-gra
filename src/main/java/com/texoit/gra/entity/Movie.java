package com.texoit.gra.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="movies")
public class Movie {

	@Id
	private String title;

	private Long year;
	
	private String studios;
	
	private String producers;
	
	private boolean winner;
	
}
