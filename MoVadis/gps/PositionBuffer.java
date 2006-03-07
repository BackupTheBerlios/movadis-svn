/*
 * Created on 21.02.2006
 *
 */
package gps;

public class PositionBuffer implements GPSDataReceivedListener {
	private Position lastPosition;
	
	public boolean hasPosition() {
		return lastPosition != null;
	}
	
	public Position getLastConfirmedPosition() {
		return lastPosition;
	}
	
	public void GPSDataReceived(GPSData gd) {
		System.out.println("Got GPS info, fix is "+gd.getFixType());
		if (gd.getFixType() != GPSLockType.NONE) {
			lastPosition = gd.getPosition();
		}
	}
}
