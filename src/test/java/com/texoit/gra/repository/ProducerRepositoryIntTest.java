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
import com.texoit.gra.projection.PrizeIntervalProjection;

@ExtendWith(SpringExtension.class)
@DataJpaTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
	public void findFastestAndSlowestWinnersTest() {
		
		List<PrizeIntervalProjection> items = producerRepository.findFastestAndSlowestWinners(2);		
				
		assertThat(items).hasSize(2);
		
		assertThat(items.get(0))
			.hasFieldOrPropertyWithValue("producer", "p2")
			.hasFieldOrPropertyWithValue("interval", 1L)
			.hasFieldOrPropertyWithValue("previousWin", 2020L)
			.hasFieldOrPropertyWithValue("followingWin", 2021L);
		
		assertThat(items.get(1))
			.hasFieldOrPropertyWithValue("producer", "p1")
			.hasFieldOrPropertyWithValue("interval", 5L)
			.hasFieldOrPropertyWithValue("previousWin", 2015L)
			.hasFieldOrPropertyWithValue("followingWin", 2020L);
		
	}
	
	@Test
	public void findFastestAndSlowestWinnersWithOnlyOneProducerTest() {
		
		movieRepository.deleteAllById(Arrays.asList(m1.getId(), m2.getId()));
		
		List<PrizeIntervalProjection> items = producerRepository.findFastestAndSlowestWinners(2);		
				
		assertThat(items).hasSize(1);
		
		assertThat(items.get(0))
			.hasFieldOrPropertyWithValue("producer", "p2")
			.hasFieldOrPropertyWithValue("interval", 1L)
			.hasFieldOrPropertyWithValue("previousWin", 2020L)
			.hasFieldOrPropertyWithValue("followingWin", 2021L);
				
	}
	
	@Test
	public void findFastestAndSlowestWinnersIsIgnoringMoviesThatLostTest() {
		
		//Given a movie that did not win the prize
		Movie m = new Movie();
		m.setTitle("new m");
		m.setYear(2016L);
		m.setWinner(false);
		m.setProducers(Collections.singletonList(p1));
		movieRepository.save(m);
		
		List<PrizeIntervalProjection> items = producerRepository.findFastestAndSlowestWinners(2);		
		
		//Assert the the list is unchanged
		assertThat(movieRepository.findAll()).hasSize(5);
		assertThat(items).hasSize(2);
		
		assertThat(items.get(0))
			.hasFieldOrPropertyWithValue("producer", "p2")
			.hasFieldOrPropertyWithValue("interval", 1L)
			.hasFieldOrPropertyWithValue("previousWin", 2020L)
			.hasFieldOrPropertyWithValue("followingWin", 2021L);
		
		assertThat(items.get(1))
			.hasFieldOrPropertyWithValue("producer", "p1")
			.hasFieldOrPropertyWithValue("interval", 5L)
			.hasFieldOrPropertyWithValue("previousWin", 2015L)
			.hasFieldOrPropertyWithValue("followingWin", 2020L);
		
	}
	
	@Test
	public void findFastestAndSlowestWinnersIsConsideringMoviesThatWonTest() {
		
		//Given a movie that did win the prize
		Movie m = new Movie();
		m.setTitle("indicated movie");
		m.setStudios("test");
		m.setYear(2016L);
		m.setWinner(true);
		m.setProducers(Collections.singletonList(p1));
		movieRepository.save(m);
		
		List<PrizeIntervalProjection> items = producerRepository.findFastestAndSlowestWinners(2);		
		
		//Assert the the list is now different
		assertThat(items).hasSize(2);
		
		assertThat(items.get(0))
			.hasFieldOrPropertyWithValue("producer", "p1")
			.hasFieldOrPropertyWithValue("interval", 1L)
			.hasFieldOrPropertyWithValue("previousWin", 2015L)
			.hasFieldOrPropertyWithValue("followingWin", 2016L);
		
		assertThat(items.get(1))
			.hasFieldOrPropertyWithValue("producer", "p2")
			.hasFieldOrPropertyWithValue("interval", 1L)
			.hasFieldOrPropertyWithValue("previousWin", 2020L)
			.hasFieldOrPropertyWithValue("followingWin", 2021L);
		
	}
	
	

}
