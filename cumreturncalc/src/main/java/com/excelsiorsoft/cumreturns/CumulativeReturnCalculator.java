/**
 * 
 */
package com.excelsiorsoft.cumreturns;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encapsulates the logic of cumulative return calculation as described in {@link #findCumulativeReturns(Date, Date)}
 * 
 * @author Simeon
 *
 */
public class CumulativeReturnCalculator {
		
	private static final Logger LOGGER = LoggerFactory.getLogger(CumulativeReturnCalculator.class);
	private final Map<Date, Double> storage;
	
	public TreeMap<Date, Double> getStorage() {
		return (TreeMap<Date, Double>) storage;
	}

	/**
	 * Construct an instance of calculator given available daily returns
	 * 
	 * @param dailyReturns an <i>immutable</i> collection of daily return values
	 */
	public CumulativeReturnCalculator(final Map<Date, Double> dailyReturns) {
		//we want an efficient navigable data structure on which we can do range filtering  
		this.storage = new TreeMap<Date, Double>(dailyReturns);		
	}
	
	/**
	 * Given the base date to start from and an asOf date for which the RoR is sought, calculate corresponding cumulative rate of return using the following formula:
	 * </br></br>
	 * The cumulative return for a specific <b>as of date A</b> with <b>base date B</b> is:</br></br>
	 * 
	 * <b>CR = [(1+DR1)*(1+DR2)*(1+DR3)*...]-1.0</b>, where</br></br>
	 * CR = cumulative return,</br></br>
	 * DR1, DR2, DR3, ... are all daily returns from B through A (<b>excluding B and including A</b>).</br></br>
	 * 
	 * @param base starting date
	 * @param asOf asOf date
	 * @return
	 */
	public Double findCumulativeReturns(Date base/*exclusive*/, Date asOf/*inclusive*/) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		NumberFormat numFormat = new DecimalFormat("#0.00000");
		LOGGER.info("Calculating cumulative return for [Base={}, AsOf={}]", format.format(base), format.format(asOf));
		
		TreeMap<Date, Double> fixings = (TreeMap<Date, Double>) storage;
		
		//filter out requests with incorrect args
		if(asOf.before(base)) {
			LOGGER.info("asOf before base, results in 'null'");
			return null;};
		/*if(base.after(fixings.lastKey())) {
			LOGGER.info("base after the known range of provided fixings, results in 'null'");
			return null;}
		if(asOf.before(fixings.firstKey())) {
			LOGGER.info("asOf before the known range of provided fixings, results in 'null'");
			return null;}*/
		
		//obtaining the interval to operate with
		NavigableMap<Date, Double> interval = fixings.subMap(fixings.ceilingKey(base), true, fixings.floorKey(asOf), true);
		
		
		Double accumulator = 1.0d;
		for(Map.Entry<Date, Double> entry : interval.entrySet()) {
			
			double dr = entry.getValue();
			Date key = entry.getKey();
			accumulator*=1.0+dr;
			//LOGGER.info("1.0+DR[{}]{}",format.format(key),dr);
			System.out.print("(1.0+DR["+format.format(key)+"]=1+("+numFormat.format(dr)+"))");
		}
		System.out.println("");
		double result = accumulator-1.0;
		LOGGER.info("Calculated cumulative RoR: {}", result);
		System.out.println("-------------------------------------------------------------------------------------------------------------");
		return (accumulator-1.0);

	}

}
