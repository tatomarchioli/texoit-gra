package com.texoit.gra.repository;
import org.springframework.data.repository.CrudRepository;

import com.texoit.gra.entity.Movie;

public interface MovieRepository extends CrudRepository<Movie, Long>{

}
