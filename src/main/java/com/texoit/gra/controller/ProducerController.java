package com.texoit.gra.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.texoit.gra.dto.PrizeInterval;
import com.texoit.gra.dto.PrizeIntervalItem;
import com.texoit.gra.projection.PrizeIntervalProjection;
import com.texoit.gra.repository.ProducerRepository;

@RestController
@RequestMapping("producers")
public class ProducerController {

	@Autowired
	private ProducerRepository repository;
	
	@GetMapping("fastest-and-slowest-winners")
    public PrizeInterval fastastAndSlowersWinners(@RequestParam(name = "limit", defaultValue = "2") Integer limit) {
		
		List<PrizeIntervalProjection> items = repository.findFastestAndSlowestWinners(limit);
		
		int half = items.size() / 2 + (items.size() % 2 > 0 ? 1 : 0);
		
		List<PrizeIntervalItem> min = items.subList(0, half)
				.stream().map(this::mapItem)
				.collect(Collectors.toList());
		
		List<PrizeIntervalItem> max = items.stream()
				.skip(half).map(this::mapItem)
				.sorted(Comparator.comparing(PrizeIntervalItem::getInterval).reversed())
				.collect(Collectors.toList());
				
		PrizeInterval interval = new PrizeInterval();
		interval.setMin(min);
		interval.setMax(max);
		return interval;
    }
	
	private PrizeIntervalItem mapItem(PrizeIntervalProjection p) {
		return new PrizeIntervalItem(p.getProducer(), p.getInterval(), p.getPreviousWin(), p.getFollowingWin());
	}
	
}
