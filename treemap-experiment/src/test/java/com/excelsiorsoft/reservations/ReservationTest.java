package com.excelsiorsoft.reservations;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class ReservationTest {

	@Test
	public void test() {
		Date from, to;
		Calendar calendar = Calendar.getInstance();
		 
		calendar.set(2011, 0, 4);
		from = calendar.getTime();
		calendar.set(2011, 0, 7);
		to = calendar.getTime();
		Reservation reserv1MrA = new Reservation(from, to, "Mr. A");
		 
		calendar.set(2011, 0, 10);
		from = calendar.getTime();
		calendar.set(2011, 0, 21);
		to = calendar.getTime();
		Reservation reserv1MrsB = new Reservation(from, to, "Mrs. B");
		 
		calendar.set(2011, 1, 5);
		from = calendar.getTime();
		calendar.set(2011, 1, 18);
		to = calendar.getTime();
		Reservation reserv2MrA = new Reservation(from, to, "Mr. A");
		 
		calendar.set(2011, 1, 20);
		from = calendar.getTime();
		calendar.set(2011, 2, 3);
		to = calendar.getTime();
		Reservation reserv1MrC = new Reservation(from, to, "Mr. C");
	}

}
