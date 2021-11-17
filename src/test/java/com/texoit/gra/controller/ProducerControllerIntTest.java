package com.texoit.gra.controller;


import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
	
	@Mock
	private PrizeIntervalItem i1;
	
	@Mock
	private PrizeIntervalItem i2;
	
	@Mock
	private PrizeIntervalItem i3;
	
	@Mock
	private PrizeIntervalItem i4;
	

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
				.andExpect(content().json("{\"min\":[{\"producer\":null,\"interval\":0,\"previousWin\":0,\"followingWin\":0},{\"producer\":null,\"interval\":0,\"previousWin\":0,\"followingWin\":0}],\"max\":[{\"producer\":null,\"interval\":0,\"previousWin\":0,\"followingWin\":0},{\"producer\":null,\"interval\":0,\"previousWin\":0,\"followingWin\":0}]}"));
	}
	
	@Test
	public void fastastAndSlowersWinnersOddResultsTest() throws Exception {
		
		when(repository.findFastestAndSlowestWinners(2)).thenReturn(Arrays.asList(i1, i2, i3));
				
		mvc.perform(get("/producers/fastest-and-slowest-winners"))
				.andExpect(content().json("{\"min\":[{\"producer\":null,\"interval\":0,\"previousWin\":0,\"followingWin\":0},{\"producer\":null,\"interval\":0,\"previousWin\":0,\"followingWin\":0}],\"max\":[{\"producer\":null,\"interval\":0,\"previousWin\":0,\"followingWin\":0}]}"));
	}
	
}
