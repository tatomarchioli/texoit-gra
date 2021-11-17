package com.texoit.gra.dto;

import java.util.List;

import lombok.Data;

@Data
public class PrizeInterval {
	
	private List<PrizeIntervalItem> min;
	
	private List<PrizeIntervalItem> max;
	
}
