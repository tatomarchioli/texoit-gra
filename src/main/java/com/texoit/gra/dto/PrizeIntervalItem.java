package com.texoit.gra.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.texoit.gra.projection.PrizeIntervalProjection;

import lombok.Data;
import lombok.NonNull;

@Data
@JsonPropertyOrder({ "producer", "interval", "previousWin", "followingWin"})
public class PrizeIntervalItem implements PrizeIntervalProjection {

	@NonNull
	private String producer;
	
	@NonNull
	private Long interval;
	
	@NonNull
	private Long previousWin; 
	
	@NonNull
	private Long followingWin;
	
}
