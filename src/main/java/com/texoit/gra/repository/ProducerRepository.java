package com.texoit.gra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.texoit.gra.entity.Producer;
import com.texoit.gra.projection.PrizeIntervalProjection;

public interface ProducerRepository extends CrudRepository<Producer, Long>{

	@Query(nativeQuery = true, 
			value = 
			"WITH data as ( " +
			" WITH scan_plan AS( " +
			"  SELECT " +
			"  p.name AS \"producer\", " +
			"  COALESCE(LAG(m.year) OVER(PARTITION BY p.id  ORDER BY p.id), 0) as \"previousWin\", " +
			"  m.year AS \"followingWin\", " +
			"  m.year - COALESCE(LAG(m.year) OVER(PARTITION BY p.id  ORDER BY p.id), m.year) as \"interval\" " +
			"  FROM PRODUCERS p " +
			"  INNER JOIN PRODUCER_HAS_MOVIES h ON h.producer_id = p.id " +
			"  INNER JOIN MOVIES m  ON m.id = h.movie_id  AND m.winner = 1 " +
			"  ORDER BY p.id asc, m.year ASC " +
			" ) " +
			" SELECT *, " +
			" ROW_NUMBER() OVER (ORDER BY \"interval\" ASC) AS first_row, " +
			" ROW_NUMBER() OVER (ORDER BY \"interval\" DESC) AS last_row " +
			" FROM scan_plan " +
			" WHERE \"interval\" > 0 " +
			" ORDER BY \"interval\" ASC, \"previousWin\" ASC" +
			") " +
			"SELECT \"producer\", \"previousWin\", \"followingWin\", \"interval\" " +
			"FROM data " +
			"WHERE (first_row <= :limit OR last_row <= :limit)")
	List<PrizeIntervalProjection> findFastestAndSlowestWinners(@Param("limit") Integer limit);
}
