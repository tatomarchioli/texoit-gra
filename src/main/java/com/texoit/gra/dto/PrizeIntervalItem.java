package com.texoit.gra.dto;

import lombok.Data;

@Data
public class PrizeIntervalItem {
	
	private String producer;
	
	private Long interval;
	
	private Long previousWin;
	
	private Long followingWin;
}
