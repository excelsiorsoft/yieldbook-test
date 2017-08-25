package com.excelsiorsoft.cumreturns;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.junit.Test;

public class Tests {

	@Test
	public void testOrdering() {
		
		TreeMap<Date, Double> treemap = new TreeMap<>();
		
		Date fixDate;
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(2015, 0, 10);
		Date Jan_10 = calendar.getTime();
		treemap.put(Jan_10,0.1d);
		
		calendar.set(2015,  1, 10);
		Date Feb_10 = calendar.getTime();
		treemap.put(Feb_10,0.05d);
		
		calendar.set(2015,  3, 10);
		Date April_10 = calendar.getTime();
		treemap.put(April_10,0.15d);
		
		calendar.set(2015,  3, 15);
		Date April_15 = calendar.getTime();
		treemap.put(April_15,-0.10d);
		
		calendar.set(2015,  5, 15);
		Date June_10 = calendar.getTime();
		treemap.put(June_10,-0.12d);
		
		System.out.println(treemap.descendingKeySet());
		
		Date today = new Date();
		assertThat(today).isAfter(treemap.lastKey());
		
		calendar.set(2015, 01, 01);
		Date B_Feb_1 = calendar.getTime();
			
		assertThat(treemap.ceilingEntry(B_Feb_1).getValue()).isEqualTo(.05d);
		assertThat(treemap.ceilingEntry(B_Feb_1).getKey()).isEqualTo(Feb_10);
		
		calendar.set(2015, 5, 30);
		Date A_June_30 = calendar.getTime();
		
		assertThat(treemap.floorEntry(A_June_30).getValue()).isEqualTo(-.12d);
		assertThat(treemap.floorEntry(A_June_30).getKey()).isEqualTo(June_10);
		
		//get range: From B_Feb_1 to A_June_30
		NavigableMap<Date, Double> B_Feb_1_to_A_June_30 = treemap.subMap(treemap.ceilingKey(B_Feb_1), true, treemap.floorKey(A_June_30), true);
		System.out.println(B_Feb_1_to_A_June_30);
		
		//calculate cum return
		Double accumulator = 1.0d;
		for(Map.Entry<Date, Double> entry : B_Feb_1_to_A_June_30.entrySet()) {
			accumulator*=1.0+entry.getValue();
			
		}
		double rate = (accumulator-1.0);
		System.out.println("B=Feb 1, 2015 <|> A=June 30, 2015: "+rate);
		
		
	}

}
