package com.excelsiorsoft.reservations;

import java.util.Date;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Planning {
	public final TreeMap<Date, Reservation> reservations;

	public Planning() {
		reservations = new TreeMap<Date, Reservation>();
	}

	public void add(Reservation reservation) {
		reservations.put(reservation.from, reservation);
	}

	public Reservation getReservationAt(Date date) {
		Entry<Date, Reservation> entry = reservations.floorEntry(date);
		if (entry == null) {
			return null;
		}
		Reservation reservation = entry.getValue();
		if (!reservation.contains(date)) {
			return null;
		}
		return reservation;
	}
}
