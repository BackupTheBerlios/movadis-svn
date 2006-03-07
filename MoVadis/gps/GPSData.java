/*
 * Created on 07.02.2006
 *
 */
package gps;

import java.util.Calendar;

public class GPSData {
	private Position position;
	private GPSLockType fixType;
	private int numberOfSatellites;
	private Calendar time;
	
	public GPSData(Position pos, GPSLockType fix, int sats, Calendar time) {
		this.time = time;
		this.position = pos;
		this.fixType = fix;
		this.numberOfSatellites = sats;
	}

	public GPSLockType getFixType() {
		return fixType;
	}

	public int getNumberOfSatellites() {
		return numberOfSatellites;
	}

	public Position getPosition() {
		return position;
	}
	
	public Calendar getTime() {
		return time;
	}
	
}
