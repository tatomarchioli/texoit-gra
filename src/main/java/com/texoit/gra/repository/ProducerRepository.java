package com.texoit.gra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.texoit.gra.entity.Producer;
import com.texoit.gra.projection.AwardIntervalProjection;

public interface ProducerRepository extends CrudRepository<Producer, Long>{

	@Query(nativeQuery = true, 
			value = 
			"WITH data AS ( " +
			" WITH scan_plan AS( " +
			"  SELECT " +
			"  p.name AS \"producer\", " +
			"  COALESCE(LAG(m.year) OVER(PARTITION BY p.id ORDER BY p.id ASC, m.year ASC), 0) AS \"previousWin\", " +
			"  m.year AS \"followingWin\", " +
			"  m.year - COALESCE(LAG(m.year) OVER(PARTITION BY p.id ORDER BY p.id ASC, m.year ASC), m.year) AS \"interval\" " +
			"  FROM PRODUCERS p " +
			"  INNER JOIN PRODUCER_HAS_MOVIES h ON h.producer_id = p.id " +
			"  INNER JOIN MOVIES m  ON m.id = h.movie_id  AND m.winner = 1 " +
			"  ORDER BY p.id asc, m.year ASC " +
			" ) " +
			" SELECT *, " +
			" RANK() OVER (ORDER BY \"interval\" ASC) AS \"min\", " +
			" RANK() OVER (ORDER BY \"interval\" DESC) AS \"max\" " +
			" FROM scan_plan " +
			" WHERE \"interval\" > 0 " +
			" ORDER BY \"interval\" ASC, \"previousWin\" ASC, \"producer\" ASC" +
			") " +
			"SELECT \"producer\", \"previousWin\", \"followingWin\", \"interval\", " +
			"case when \"min\" = 1 then 'MIN' else 'MAX' end as \"resultSet\"" +
			"FROM data " +
			"WHERE (\"min\" = 1 OR \"max\" = 1)")
	List<AwardIntervalProjection> findWinnersByInterval();
}
