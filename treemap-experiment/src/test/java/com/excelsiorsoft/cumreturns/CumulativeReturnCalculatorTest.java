package com.excelsiorsoft.cumreturns;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CumulativeReturnCalculatorTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(CumulativeReturnCalculatorTest.class);
	
	CumulativeReturnCalculator cut = null;
	Calendar calendar = Calendar.getInstance();

	@Before
	public void init() {
		Map<Date, Double> map = new HashMap<>();

		calendar.set(2015, 0, 10);
		Date Jan_10 = calendar.getTime();
		map.put(Jan_10, 0.1d);

		calendar.set(2015, 1, 10);
		Date Feb_10 = calendar.getTime();
		map.put(Feb_10, 0.05d);

		calendar.set(2015, 3, 10);
		Date April_10 = calendar.getTime();
		map.put(April_10, 0.15d);

		calendar.set(2015, 3, 15);
		Date April_15 = calendar.getTime();
		map.put(April_15, -0.10d);

		calendar.set(2015, 5, 15);
		Date June_10 = calendar.getTime();
		map.put(June_10, -0.12d);

		cut = new CumulativeReturnCalculator(map);

	}

	@Test
	public void testGivenScenarios() {
		LOGGER.info("Making sure the CUT is properly initialized and ready for usage: {}",cut.getStorage().toString());
		
		calendar.set(2015, 01, 01);
		Date B_Feb_01 = calendar.getTime();
		
		calendar.set(2015,  01,28);
		Date A_Feb_28 = calendar.getTime();
		assertThat(cut.findCumulativeReturns(B_Feb_01, A_Feb_28)).isEqualTo(0.050000000000000044);
		
		calendar.set(2015,  02,13);
		Date A_Mar_13 = calendar.getTime();
		assertThat(cut.findCumulativeReturns(B_Feb_01, A_Mar_13)).isEqualTo(0.050000000000000044);
		
		calendar.set(2015,  03,30);
		Date A_Apr_30 = calendar.getTime();
		assertThat(cut.findCumulativeReturns(B_Feb_01, A_Apr_30)).isEqualTo(0.0867500000000001);
		
		calendar.set(2015,  04, 8);
		Date A_May_08 = calendar.getTime();
		assertThat(cut.findCumulativeReturns(B_Feb_01, A_May_08)).isEqualTo(0.0867500000000001);
		
		calendar.set(2015,  05,30);
		Date A_June_30 = calendar.getTime();
		assertThat(cut.findCumulativeReturns(B_Feb_01, A_June_30)).isEqualTo(-0.04365999999999992d);

		//tests for the outliers
		
		//AsOf before Base
		calendar.set(2015,  0, 31);
		Date A_Jan_31 = calendar.getTime();
		assertThat(A_Jan_31).isBefore(B_Feb_01);
		assertThat(cut.findCumulativeReturns(B_Feb_01, A_Jan_31)).isEqualTo(null);
		
		//AsOf before the first known mapping
		calendar.set(2014,  0, 31);
		Date A_Jan_31_2014 = calendar.getTime();
		assertThat(A_Jan_31_2014).isBefore(B_Feb_01).isBefore(cut.getStorage().firstKey());
		assertThat(cut.findCumulativeReturns(B_Feb_01, A_Jan_31_2014)).isEqualTo(null);
		
		//Base is after the last known mapping
		Date today = new Date();
		assertThat(today).isAfter(cut.getStorage().lastKey());
		assertThat(cut.findCumulativeReturns(today, A_Jan_31)).isEqualTo(null);
		
	}
	
	@Test
	public void testLargeTimePeriod() {

		Map<Date, Double> lotsOfData = new HashMap<>();

		LocalDate start = LocalDate.of(2000, 1, 1);
		LocalDate stop = LocalDate.of(2016, 10, 28);

		List<LocalDate> dates = new ArrayList<>();

		for (LocalDate d = start; !d.isAfter(stop); d = d.plusDays(1)) {
			Date key = Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant());
			double value = ThreadLocalRandom.current().nextDouble(-1, 1);
			lotsOfData.put(key, value);

		}

		cut = new CumulativeReturnCalculator(lotsOfData);

		Date base = Date.from(LocalDate.of(2016, 03, 24).atStartOfDay(ZoneId.systemDefault()).toInstant());

		Date asOf = Date.from(LocalDate.of(2016, 06, 11).atStartOfDay(ZoneId.systemDefault()).toInstant());

		cut.findCumulativeReturns(base, asOf);
	}

}
