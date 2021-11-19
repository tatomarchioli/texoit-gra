package com.texoit.gra.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.texoit.gra.entity.Movie;
import com.texoit.gra.entity.Producer;
import com.texoit.gra.enums.AwardIntervalProjectionResultSet;
import com.texoit.gra.projection.AwardIntervalProjection;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ProducerRepositoryIntTest {
	
	@Autowired
	private ProducerRepository producerRepository;
	
	@Autowired
	private MovieRepository movieRepository;
	
	private Producer p1;
	
	private Producer p2;
	
	private Movie m1;
	
	private Movie m2;
	
	private Movie m3;
	
	private Movie m4;
	
	@BeforeEach
	void initData() {
		p1 = new Producer("p1");
		
		p2 = new Producer("p2");
		
		producerRepository.saveAll(Arrays.asList(p1, p2));
		
		m1 = new Movie();
		m1.setTitle("m1");
		m1.setYear(2015L);
		m1.setWinner(true);
		m1.setProducers(Collections.singletonList(p1));
		
		m2 = new Movie();
		m2.setTitle("m2");
		m2.setYear(2020L);
		m2.setWinner(true);
		m2.setProducers(Collections.singletonList(p1));
		
		m3 = new Movie();
		m3.setTitle("m3");
		m3.setYear(2020L);
		m3.setWinner(true);
		m3.setProducers(Collections.singletonList(p2));
		
		m4 = new Movie();
		m4.setTitle("m4");
		m4.setYear(2021L);
		m4.setWinner(true);
		m4.setProducers(Collections.singletonList(p2));
		
		movieRepository.saveAll(Arrays.asList(m1, m2, m3, m4));
	}
	
	
	@Test
	public void finWinnersByIntervalTest() {
		
		List<AwardIntervalProjection> items = producerRepository.findWinnersByInterval();		
				
		assertThat(items).hasSize(2);
		
		assertThat(items.get(0))
			.hasFieldOrPropertyWithValue("producer", "p2")
			.hasFieldOrPropertyWithValue("interval", 1L)
			.hasFieldOrPropertyWithValue("previousWin", 2020L)
			.hasFieldOrPropertyWithValue("followingWin", 2021L)
			.hasFieldOrPropertyWithValue("resultSet", AwardIntervalProjectionResultSet.MIN);
		
		assertThat(items.get(1))
			.hasFieldOrPropertyWithValue("producer", "p1")
			.hasFieldOrPropertyWithValue("interval", 5L)
			.hasFieldOrPropertyWithValue("previousWin", 2015L)
			.hasFieldOrPropertyWithValue("followingWin", 2020L)
			.hasFieldOrPropertyWithValue("resultSet", AwardIntervalProjectionResultSet.MAX);
		
	}
	
	@Test
	public void finWinnersByIntervalWithOnlyOneProducerTest() {
		
		movieRepository.deleteAllById(Arrays.asList(m1.getId(), m2.getId()));
		
		List<AwardIntervalProjection> items = producerRepository.findWinnersByInterval();		
				
		assertThat(items).hasSize(1);
		
		assertThat(items.get(0))
			.hasFieldOrPropertyWithValue("producer", "p2")
			.hasFieldOrPropertyWithValue("interval", 1L)
			.hasFieldOrPropertyWithValue("previousWin", 2020L)
			.hasFieldOrPropertyWithValue("followingWin", 2021L)
			.hasFieldOrPropertyWithValue("resultSet", AwardIntervalProjectionResultSet.MIN);;
				
	}
	
	@Test
	public void finWinnersByIntervalIsIgnoringMoviesThatLostTest() {
		
		//Given a movie that did not win the prize
		Movie m = new Movie();
		m.setTitle("new m");
		m.setYear(2016L);
		m.setWinner(false);
		m.setProducers(Collections.singletonList(p1));
		movieRepository.save(m);
		
		List<AwardIntervalProjection> items = producerRepository.findWinnersByInterval();		
		
		//Assert the the list is unchanged
		assertThat(movieRepository.findAll()).hasSize(5);
		assertThat(items).hasSize(2);
		
		assertThat(items.get(0))
			.hasFieldOrPropertyWithValue("producer", "p2")
			.hasFieldOrPropertyWithValue("interval", 1L)
			.hasFieldOrPropertyWithValue("previousWin", 2020L)
			.hasFieldOrPropertyWithValue("followingWin", 2021L)
			.hasFieldOrPropertyWithValue("resultSet", AwardIntervalProjectionResultSet.MIN);
		
		assertThat(items.get(1))
			.hasFieldOrPropertyWithValue("producer", "p1")
			.hasFieldOrPropertyWithValue("interval", 5L)
			.hasFieldOrPropertyWithValue("previousWin", 2015L)
			.hasFieldOrPropertyWithValue("followingWin", 2020L)
			.hasFieldOrPropertyWithValue("resultSet", AwardIntervalProjectionResultSet.MAX);
		
	}
	
	@Test
	public void finWinnersByIntervalIsConsideringMoviesThatWonTest() {
		
		//Given a movie that did win the prize, thus adding an interval for producer 1 of 1 year
		Movie m = new Movie();
		m.setTitle("indicated movie");
		m.setStudios("test");
		m.setYear(2016L);
		m.setWinner(true);
		m.setProducers(Collections.singletonList(p1));
		movieRepository.save(m);
		
		List<AwardIntervalProjection> items = producerRepository.findWinnersByInterval();		
		
		//Assert the the list is now different, since there`s an new 1 year interval to be shown in the min list
		assertThat(items).hasSize(3);
		
		assertThat(items.get(0))
			.hasFieldOrPropertyWithValue("producer", "p1")
			.hasFieldOrPropertyWithValue("interval", 1L)
			.hasFieldOrPropertyWithValue("previousWin", 2015L)
			.hasFieldOrPropertyWithValue("followingWin", 2016L)
			.hasFieldOrPropertyWithValue("resultSet", AwardIntervalProjectionResultSet.MIN);
		
		assertThat(items.get(1))
			.hasFieldOrPropertyWithValue("producer", "p2")
			.hasFieldOrPropertyWithValue("interval", 1L)
			.hasFieldOrPropertyWithValue("previousWin", 2020L)
			.hasFieldOrPropertyWithValue("followingWin", 2021L)
			.hasFieldOrPropertyWithValue("resultSet", AwardIntervalProjectionResultSet.MIN);
		
		assertThat(items.get(2))
			.hasFieldOrPropertyWithValue("producer", "p1")
			.hasFieldOrPropertyWithValue("interval", 4L)
			.hasFieldOrPropertyWithValue("previousWin", 2016L)
			.hasFieldOrPropertyWithValue("followingWin", 2020L)
			.hasFieldOrPropertyWithValue("resultSet", AwardIntervalProjectionResultSet.MAX);
		
	}
	
	
	@Test
	public void finWinnersByIntervalWithIntermediateIntervalsTest() {
		
		//Given a movie that did win the prize, thus creating an interval of two years (2013 to 2015)
		Movie m = new Movie();
		m.setTitle("new m");
		m.setYear(2013L);
		m.setWinner(true);
		m.setProducers(Collections.singletonList(p1));
		movieRepository.save(m);
		
		List<AwardIntervalProjection> items = producerRepository.findWinnersByInterval();		
		
		//Assert the the list is unchanged, since the smallest interval is still 1 year, and the biggest is still 5 years
		assertThat(movieRepository.findAll()).hasSize(5);
		assertThat(items).hasSize(2);
		
		assertThat(items.get(0))
			.hasFieldOrPropertyWithValue("producer", "p2")
			.hasFieldOrPropertyWithValue("interval", 1L)
			.hasFieldOrPropertyWithValue("previousWin", 2020L)
			.hasFieldOrPropertyWithValue("followingWin", 2021L)
			.hasFieldOrPropertyWithValue("resultSet", AwardIntervalProjectionResultSet.MIN);
		
		assertThat(items.get(1))
			.hasFieldOrPropertyWithValue("producer", "p1")
			.hasFieldOrPropertyWithValue("interval", 5L)
			.hasFieldOrPropertyWithValue("previousWin", 2015L)
			.hasFieldOrPropertyWithValue("followingWin", 2020L)
			.hasFieldOrPropertyWithValue("resultSet", AwardIntervalProjectionResultSet.MAX);
		
	}

	@Test
	public void finWinnersByIntervalWithNotEnoughMoviesTes() {
		
		//Given that there are only two movies available, one for each producer
		movieRepository.deleteAllById(Arrays.asList(m1.getId(), m3.getId()));
				
		List<AwardIntervalProjection> items = producerRepository.findWinnersByInterval();		
		
		//Assert the the list is now empty
		assertThat(items).hasSize(0);
		
	}
	
	@Test
	public void finWinnersByIntervalWithNoMoviesTest() {
		
		//Given that there are only no movies available
		movieRepository.deleteAll();
				
		List<AwardIntervalProjection> items = producerRepository.findWinnersByInterval();		
		
		//Assert the the list is now empty
		assertThat(items).hasSize(0);
		
	}
	

}
