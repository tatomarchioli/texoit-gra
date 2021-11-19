package com.texoit.gra.projection;

import com.texoit.gra.enums.AwardIntervalProjectionResultSet;

public interface AwardIntervalProjection {

	String getProducer();
	
	Long getInterval();
	
	Long getPreviousWin();
	
	Long getFollowingWin();
	
	AwardIntervalProjectionResultSet getResultSet();
	
}
