package test;

import junit.framework.TestCase;
import main.BabysitterCalculator;

import org.junit.Test;

//This kata simulates a babysitter working and getting paid for one night. 
//The rules are pretty straight forward:
//
//The babysitter 
//- starts no earlier than 5:00PM
//- leaves no later than 4:00AM
//- gets paid $12/hour from start-time to bedtime
//- gets paid $8/hour from bedtime to midnight
//- gets paid $16/hour from midnight to end of job
//- gets paid for full hours (no fractional hours)
//
//
//Feature:
//As a babysitter
//In order to get paid for 1 night of work
//I want to calculate my nightly charge

public class BabysitterTest extends TestCase {
	
	private BabysitterCalculator makeACalculator() {
		return new BabysitterCalculator(12, 8, 16, 17, 21, 4);
	}
	
	@Test
	public void testCanGetPrice() {
		BabysitterCalculator calc = makeACalculator();
		
		double price = calc.getPrice(calc.startBoundary, calc.bedtime);
		
		assertTrue("Price should be postive.", price > 0);
	}
	
	@Test
	public void testGetsStartTimePriceForOneHourOfWork() {
		BabysitterCalculator calc = makeACalculator();
		
		double price =  calc.getPrice(calc.startBoundary, calc.startBoundary+1);
		
		assertEquals("Price before bedtime should be "+calc.startPrice+".", calc.startPrice, price);
	}
	
	@Test
	public void testGetsTwiceStartTimePriceForTwoHoursOfWork() {
		BabysitterCalculator calc = makeACalculator();
		
		double price =  calc.getPrice(calc.startBoundary, calc.startBoundary+2);
		
		assertEquals("Price should be "+(2*calc.startPrice)+".", calc.startPrice*2, price);
	}
	
	@Test
	public void testGetsBedtimePriceForOneHourOfWorkAfterBedtime() {
		BabysitterCalculator calc = makeACalculator();
		
		double price =  calc.getPrice(calc.bedtime, calc.bedtime+1);
		
		assertEquals("Price should be "+calc.bedtimePrice+".", calc.bedtimePrice, price);
	}
	
	@Test
	public void testGetsAfterMidnightPriceForOneHourOfWorkAfterMidnight() {
		BabysitterCalculator calc = makeACalculator();
		
		double price =  calc.getPrice(1, 2);
		
		assertEquals("Price should be "+calc.afterMidnightPrice+".", calc.afterMidnightPrice, price);
	}
	
	@Test
	public void testMaximumPriceIsRight() {
		BabysitterCalculator calc = makeACalculator();
		double targetPrice = calc.startPrice*(calc.bedtime-17) + calc.bedtimePrice*(24-calc.bedtime) + calc.afterMidnightPrice*4;
		
		double price =  calc.getPrice(calc.startBoundary, calc.endBoundary);
		
		assertEquals("Price should be "+targetPrice+".", targetPrice, price);
	}
	
	@Test
	public void testThrowsExceptionWhenStartBeforeFive() {
		BabysitterCalculator calc = makeACalculator();
		
		Exception exception = null;
		try {
			calc.getPrice(calc.startBoundary-1, calc.bedtime);
		} catch (IllegalArgumentException e) {
			exception = e;
		}
		
		assertTrue("Should throw exception when start time is before 5pm.", exception != null);
	}
	
	@Test
	public void testThrowsExceptionWhenEndAfterFour() {
		BabysitterCalculator calc = makeACalculator();
		
		Exception exception = null;
		try {
			calc.getPrice(calc.bedtime, calc.endBoundary+1);
		} catch (IllegalArgumentException e) {
			exception = e;
		}
		
		assertTrue("Should throw exception when end time is after 4am.", exception != null);
	}

}
