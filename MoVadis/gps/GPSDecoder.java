/*
 * Created on 04.02.2006
 *
 */
package gps;

import gps.Position.LatitudeDirection;
import gps.Position.LongitudeDirection;

import java.util.Enumeration;
import java.util.Vector;

public class GPSDecoder implements SentenceListener {
	private Vector listeners;
	
	public GPSDecoder() {
		listeners = new Vector();
	}
	
	public void addGPSDataReceivedListener(GPSDataReceivedListener l) {
		synchronized (listeners) {
			listeners.addElement(l);
		}
	}
	
	protected void informListeners(GPSData gd) {
		synchronized (listeners) {
			Enumeration en = listeners.elements();
			while (en.hasMoreElements()) {
				GPSDataReceivedListener r = (GPSDataReceivedListener) en.nextElement();
				r.GPSDataReceived(gd);
			}
		}
	}
	
	public void SentenceReceived(String sentence) {
		// $GPGGA,125532.146,5123.1569,N,00645.0726,E,1,04,09.5,29.0,M,45.7,M,,*52
		Vector strings = split(sentence, ',');
		
		String first = (String)strings.elementAt(0);
		
		if (first.equals("$GPGGA")) {
			//String time = (String)strings.elementAt(1);
			String lat = (String)strings.elementAt(2);
			String lon = (String)strings.elementAt(4);
			/*
			 * 0 no fix
			 * 1 GPS fix
			 * 2 DGPS fix
			 */
			int fixType = Integer.parseInt( (String)strings.elementAt(6) );
			// Number of tracked satellites
			int sats = Integer.parseInt( (String)strings.elementAt(7) );
			
			if (!(lat.equals("")||lon.equals(""))) {
				int degLat = Integer.parseInt(lat.substring(0,2));
				float mLat = Float.parseFloat(lat.substring(2,9));
				int degLon = Integer.parseInt(lon.substring(0,3));
				float mLon = Float.parseFloat(lon.substring(3,10));
				
				String latIn = (String)strings.elementAt(3);
				String lonIn = (String)strings.elementAt(5);
				LatitudeDirection latDir = latIn.equals("N") ? LatitudeDirection.NORTH : LatitudeDirection.SOUTH;
				LongitudeDirection lonDir = lonIn.equals("E") ? LongitudeDirection.EAST : LongitudeDirection.WEST;
				
				Position p = new Position(degLat, mLat, latDir, degLon, mLon, lonDir);
				GPSLockType gl = GPSLockType.parseType(fixType);
				GPSData gd = new GPSData(p, gl, sats);
				informListeners(gd);
			}
		}
	}
	
	protected static Vector split(String input, char separator) {
		Vector result = new Vector();
		StringBuffer b = new StringBuffer();
		int i = 0;
		while (i < input.length()) {
			char c = input.charAt(i);
			if (c == separator) {
				result.addElement(b.toString());
				b = new StringBuffer();
			} else {
				b.append(c);
			}
			i++;
		}
		result.addElement(b.toString());
		return result;
	}

}
