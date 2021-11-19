package com.texoit.gra.dto;

import java.util.List;

import lombok.Data;

@Data
public class AwardInterval {
	
	private List<AwardIntervalItem> min;
	
	private List<AwardIntervalItem> max;
	
}
