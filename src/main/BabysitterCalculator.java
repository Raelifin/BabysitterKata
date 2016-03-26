package main;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class BabysitterCalculator {
	
	//These aren't ideal. They're just duplicating data for external reference.
	public final double startPrice, bedtimePrice, afterMidnightPrice;
	public final int startBoundary, bedtime, endBoundary; //Hour of the day
	
	private Map<Integer, Float> hourPrices;
	
	//Ideally I'd have this take a map of intervals <Int,Int> to prices <Double> and populate the hour prices in a more generic way.
	public BabysitterCalculator(float startPrice, float bedtimePrice, float afterMidnightPrice, int startBoundary, int bedtime, int endBoundary) {
		this.startPrice = startPrice;
		this.bedtimePrice = bedtimePrice;
		this.afterMidnightPrice = afterMidnightPrice;
		this.startBoundary = startBoundary;
		this.bedtime = bedtime;
		this.endBoundary = endBoundary;
		
		hourPrices = new HashMap<Integer,Float>();
		IntStream.range(startBoundary, bedtime).forEach(x -> hourPrices.put(x, startPrice));
		IntStream.range(bedtime, 24).forEach(x -> hourPrices.put(x, bedtimePrice));
		IntStream.range(0, endBoundary).forEach(x -> hourPrices.put(x, afterMidnightPrice));
	}
	
	public double getPrice(int startHour, int endHour) {
		try {
			return getListOfHoursWorked(startHour,endHour).mapToDouble(x -> hourPrices.get(x)).sum();
		} catch (NullPointerException e) {
			if (startHour < startBoundary && startHour > endBoundary) {
				throw new IllegalArgumentException("Invalid start time.");
			} else if (endHour < startBoundary && endHour > endBoundary) {
				throw new IllegalArgumentException("Invalid end time.");
			} else {
				throw e;
			}
		}
	}

	private IntStream getListOfHoursWorked(int startHour, int endHour) {
		if (endHour < startHour) { endHour += 24; }
		return IntStream.range(startHour,endHour).map(x -> x%24);
	}

}
