package com.texoit.gra.controller;


import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

import com.texoit.gra.dto.PrizeIntervalItem;
import com.texoit.gra.repository.ProducerRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProducerController.class)
public class ProducerControllerIntTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private ProducerRepository repository;
	
	private PrizeIntervalItem i1 = new PrizeIntervalItem("p1", 1L, 1995L, 1996L);
	
	private PrizeIntervalItem i2 = new PrizeIntervalItem("p2", 2L, 2019L, 2021L);
	
	private PrizeIntervalItem i3 = new PrizeIntervalItem("p3", 5L, 2015L, 2020L);
	
	private PrizeIntervalItem i4 = new PrizeIntervalItem("p4", 6L, 2015L, 2021L);
	

	@Test
	public void fastastAndSlowersWinnersDefaultParametersTest() throws Exception {
		mvc.perform(get("/producers/fastest-and-slowest-winners"));
		verify(repository, times(1)).findFastestAndSlowestWinners(2);
	}
	
	@Test
	public void fastastAndSlowersWinnersCustomParametersTest() throws Exception {
		mvc.perform(get("/producers/fastest-and-slowest-winners?limit=4"));
		verify(repository, times(1)).findFastestAndSlowestWinners(4);
	}
	
	@Test
	public void fastastAndSlowersWinnersEmptyResultsTest() throws Exception {
		mvc.perform(get("/producers/fastest-and-slowest-winners"))
				.andExpect(content().json("{min:[], max:[]}"));
	}
	
	@Test
	public void fastastAndSlowersWinnersEvenResultsTest() throws Exception {
		
		when(repository.findFastestAndSlowestWinners(2)).thenReturn(Arrays.asList(i1, i2, i3, i4));
				
		mvc.perform(get("/producers/fastest-and-slowest-winners"))
				.andExpect(content().json(
						  "{\"min\": [{"
						+ "\"producer\":\"p1\","
						+ "\"interval\":1,"
						+ "\"previousWin\":1995,"
						+ "\"followingWin\":1996"
						+ "},{"
						+ "\"producer\":\"p2\","
						+ "\"interval\":2,"
						+ "\"previousWin\":2019,"
						+ "\"followingWin\":2021"
						+ "}],\"max\":[{"
						+ "\"producer\":\"p4\","
						+ "\"interval\":6,"
						+ "\"previousWin\":2015,"
						+ "\"followingWin\":2021"
						+ "},{"
						+ "\"producer\":\"p3\","
						+ "\"interval\":5,"
						+ "\"previousWin\":2015,"
						+ "\"followingWin\":2020"
						+ "}]}"));
	}
	
	@Test
	public void fastastAndSlowersWinnersOddResultsTest() throws Exception {
		
		when(repository.findFastestAndSlowestWinners(2)).thenReturn(Arrays.asList(i1, i2, i3));
				
		mvc.perform(get("/producers/fastest-and-slowest-winners"))
				.andExpect(content().json(
						  "{\"min\": [{"
						+ "\"producer\":\"p1\","
						+ "\"interval\":1,"
						+ "\"previousWin\":1995,"
						+ "\"followingWin\":1996"
						+ "},{"
						+ "\"producer\":\"p2\","
						+ "\"interval\":2,"
						+ "\"previousWin\":2019,"
						+ "\"followingWin\":2021"
						+ "}],\"max\":[{"
						+ "\"producer\":\"p3\","
						+ "\"interval\":5,"
						+ "\"previousWin\":2015,"
						+ "\"followingWin\":2020"
						+ "}]}"));
	}
	
}
