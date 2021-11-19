package com.texoit.gra.controller;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.texoit.gra.enums.AwardIntervalProjectionResultSet;
import com.texoit.gra.projection.AwardIntervalProjection;
import com.texoit.gra.repository.ProducerRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProducerController.class)
public class ProducerControllerIntTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private ProducerRepository repository;
	
	private AwardIntervalProjection i1 = new AwardIntervalProjection() {

		public String getProducer() {
			return "p1";
		}

		public Long getInterval() {
			return 1L;
		}

		public Long getPreviousWin() {
			return 1995L;
		}

		public Long getFollowingWin() {
			return 1996L;
		}

		public AwardIntervalProjectionResultSet getResultSet() {
			return AwardIntervalProjectionResultSet.MIN;
		}
		
	};
	
	private AwardIntervalProjection i2 = new AwardIntervalProjection() {

		public String getProducer() {
			return "p2";
		}

		public Long getInterval() {
			return 1L;
		}

		public Long getPreviousWin() {
			return 2019L;
		}

		public Long getFollowingWin() {
			return 2020L;
		}

		public AwardIntervalProjectionResultSet getResultSet() {
			return AwardIntervalProjectionResultSet.MIN;
		}
		
	};;
	
	private AwardIntervalProjection i3 = new AwardIntervalProjection() {

		public String getProducer() {
			return "p3";
		}

		public Long getInterval() {
			return 6L;
		}

		public Long getPreviousWin() {
			return 2015L;
		}

		public Long getFollowingWin() {
			return 2021L;
		}

		public AwardIntervalProjectionResultSet getResultSet() {
			return AwardIntervalProjectionResultSet.MAX;
		}
		
	};;
	
	private AwardIntervalProjection i4 = new AwardIntervalProjection() {

		public String getProducer() {
			return "p4";
		}

		public Long getInterval() {
			return 6L;
		}

		public Long getPreviousWin() {
			return 2014L;
		}

		public Long getFollowingWin() {
			return 2020L;
		}

		public AwardIntervalProjectionResultSet getResultSet() {
			return AwardIntervalProjectionResultSet.MAX;
		}
		
	};;
	
	@Test
	public void fastastAndSlowersWinnersEmptyResultsTest() throws Exception {
		mvc.perform(get("/producers/by-interval"))
				.andExpect(content().json("{min:[], max:[]}"));
	}
	
	@Test
	public void findWinnersByIntervalTest() throws Exception {
		
		when(repository.findWinnersByInterval()).thenReturn(Arrays.asList(i1, i3));
				
		mvc.perform(get("/producers/by-interval"))
				.andExpect(content().json(
						  "{\"min\": [{"
						+ "\"producer\":\"p1\","
						+ "\"interval\":1,"
						+ "\"previousWin\":1995,"
						+ "\"followingWin\":1996"
						+ "}],\"max\":[{"
						+ "\"producer\":\"p3\","
						+ "\"interval\":6,"
						+ "\"previousWin\":2015,"
						+ "\"followingWin\":2021"
						+ "}]}"));
	}
	
	@Test
	public void findWinnersByIntervalMultipleResultsTest() throws Exception {
		
		when(repository.findWinnersByInterval()).thenReturn(Arrays.asList(i1, i2, i3, i4));
				
		mvc.perform(get("/producers/by-interval"))
				.andExpect(content().json(
						  "{\"min\": [{"
						+ "\"producer\":\"p1\","
						+ "\"interval\":1,"
						+ "\"previousWin\":1995,"
						+ "\"followingWin\":1996"
						+ "},{"
						+ "\"producer\":\"p2\","
						+ "\"interval\":1,"
						+ "\"previousWin\":2019,"
						+ "\"followingWin\":2020"
						+ "}],\"max\":[{"
						+ "\"producer\":\"p3\","
						+ "\"interval\":6,"
						+ "\"previousWin\":2015,"
						+ "\"followingWin\":2021"
						+ "},{"
						+ "\"producer\":\"p4\","
						+ "\"interval\":6,"
						+ "\"previousWin\":2014,"
						+ "\"followingWin\":2020"
						+ "}]}"));
	}
	
}
