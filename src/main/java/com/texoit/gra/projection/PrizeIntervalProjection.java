package com.texoit.gra.projection;

public interface PrizeIntervalProjection {

	String getProducer();
	
	Long getInterval();
	
	Long getPreviousWin();
	
	Long getFollowingWin();
	
}
