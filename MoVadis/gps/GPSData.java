/*
 * Created on 07.02.2006
 *
 */
package gps;

public class GPSData {
	private Position position;
	private GPSLockType fixType;
	private int numberOfSatellites;
	
	public GPSData(Position pos, GPSLockType fix, int sats) {
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
}
