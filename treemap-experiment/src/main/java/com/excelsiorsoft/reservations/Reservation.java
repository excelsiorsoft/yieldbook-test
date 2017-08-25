package com.excelsiorsoft.reservations;

import java.util.Date;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;




public class Reservation {
    public Date from;
    public Date to;
    public String occupant;
 
    public Reservation(Date from, Date to, String occupant) {
        this.from = from;
        this.to = to;
        this.occupant = occupant;
    }
 
    public boolean contains(Date date) {
        return !(from.after(date) || to.before(date));
    }
 
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("from", from)
            .add("to", to)
            .add("occupant", occupant)
            .toString();
    }
 
    @Override
    public int hashCode() {
        return Objects.hashCode(from, to, occupant);
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Reservation)) {
            return false;
        }
        Reservation reservation = (Reservation) obj;
        return java.util.Objects.equals(from, reservation.from)
            && java.util.Objects.equals(to, reservation.to)
            && java.util.Objects.equals(occupant, reservation.occupant);
    }
 
}