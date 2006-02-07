/*
 * Created on 07.02.2006
 *
 */
package gps;

public class GPSLockType {
	public static final GPSLockType NONE = new GPSLockType(0);
	public static final GPSLockType GPS = new GPSLockType(1);
	public static final GPSLockType DGPS = new GPSLockType(2);
	
	private int n;

	private GPSLockType(int n) {
		this.n = n;
	}
	
	public static GPSLockType parseType(int n) {
		if (n == 0) return NONE;
		else if (n == 1) return GPS;
		else if (n == 2) return DGPS;
		return null;
	}
	
	public String toString() {
		if (n == 0) {
			return "NONE";
		} else if (n == 1) {
			return "GPS";
		} else if (n == 2) {
			return "DGPS";
		}
		return null;
	}
}