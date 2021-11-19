package com.texoit.gra.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.texoit.gra.dto.AwardInterval;
import com.texoit.gra.dto.AwardIntervalItem;
import com.texoit.gra.enums.AwardIntervalProjectionResultSet;
import com.texoit.gra.projection.AwardIntervalProjection;
import com.texoit.gra.repository.ProducerRepository;

@RestController
@RequestMapping("producers")
public class ProducerController {

	@Autowired
	private ProducerRepository repository;
	
	@GetMapping("by-interval")
    public AwardInterval byInterval() {
		
		List<AwardIntervalProjection> items = repository.findWinnersByInterval();
		
		List<AwardIntervalItem> min = items
				.stream()
				.filter(i -> i.getResultSet() == AwardIntervalProjectionResultSet.MIN)
				.map(this::mapItem)
				.collect(Collectors.toList());
		
		List<AwardIntervalItem> max = items
				.stream()
				.filter(i -> i.getResultSet() == AwardIntervalProjectionResultSet.MAX)
				.map(this::mapItem)
				.collect(Collectors.toList());
				
		AwardInterval interval = new AwardInterval();
		interval.setMin(min);
		interval.setMax(max);
		return interval;
    }
	
	private AwardIntervalItem mapItem(AwardIntervalProjection p) {
		return new AwardIntervalItem(p.getProducer(), p.getInterval(), p.getPreviousWin(), p.getFollowingWin());
	}
	
}
