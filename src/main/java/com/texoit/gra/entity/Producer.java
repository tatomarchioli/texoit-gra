package com.texoit.gra.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

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
	@Column(unique = true, nullable = false)
	private String name;
	
	@ManyToMany(mappedBy = "producers", fetch = FetchType.LAZY)
	private List<Movie> movies;
}
