package com.texoit.gra.repository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.texoit.gra.entity.Movie;
import com.texoit.gra.projection.PrizeIntervalProjection;

public interface MovieRepository extends CrudRepository<Movie, Long>{
		@Query(value =
	           "SELECT p.name as producer, 2021 as interval, 2000 as previousWin, 3000 as followingWin " +
	           "FROM " +
	           "Producer p " + 
	           "Join Movie m ")
	    List<PrizeIntervalProjection> findAllTest();
}
