/*
 * Created on 07.02.2006
 *
 */
package gps;

import gps.Position.LatitudeDirection;
import gps.Position.LongitudeDirection;

import java.io.IOException;
import java.util.Random;

public class NMEASimulator extends GPSReceiver implements Runnable {
	private static int INTERVAL = 1000;
	// Deviation in all directions of latitude and longitude in degrees
	private static double DEVIATION = 0.02;

	private Position startpos;
	private Position basepos;
	private Thread simThread; 
	private Random random;
	
	private boolean simulating;
	
	public NMEASimulator(Position basepos) {
		this.basepos = basepos;
		this.startpos = basepos;
		simThread = new Thread(this);
		random = new Random(System.currentTimeMillis());
		simulating = false;
	}
	
	public void start() throws IOException {
		simulating = true;
		simThread.start();
	}
	
	public void stop() throws IOException {
		simulating = false;
		simThread.interrupt();
	}
	
	private static String formatLongitude(double f) {
		String sf = ("00000")+f+("00000");
		// Where's the dot?
		int i = sf.indexOf(".");
		return sf.substring(i-5, i+5);
	}
	private static String formatLatitude(double d) {
		String sf = ("00000")+d+("00000");
		// Where's the dot?
		int i = sf.indexOf(".");
		return sf.substring(i-4, i+5);
	}
	
	public void run() {
		while (simulating) {
			// NMEA format:
			// $GPGGA,125532.146,5123.1569,N,00645.0726,E,1,04,09.5,29.0,M,45.7,M,,*52
			
			// How many satellites do we have?
			int sats = random.nextInt(6);
			// 1 = GPS
			// 0 = none
			// [ 2 = DGPS (we do not implement that) ]
			int fix = sats > 0 ? 1 : 0;
			
			double lat = basepos.getLatitudeAsDecimal();
			double lon = basepos.getLongitudeAsDecimal();
			
			// If we have a GPS fix, we can vary the position
			if (fix > 0) {
				lon += random.nextFloat()*2*DEVIATION-DEVIATION;
				lat += random.nextFloat()*2*DEVIATION-DEVIATION;
			}
			
			// save the new position
			basepos = new Position(lat, lon);
			
			// format the coordinate strings to the correct format
			String slat = formatLatitude(Math.abs(lat*100));
			String slon = formatLongitude(Math.abs(lon*100));

			// Get the direction
			String dlon = basepos.getLongitudeAsDecimal() > 0 ? LongitudeDirection.WEST.toString() : LongitudeDirection.EAST.toString(); 
			String dlat = basepos.getLatitudeAsDecimal() > 0 ? LatitudeDirection.NORTH.toString() : LatitudeDirection.SOUTH.toString();
			
			// TODO insert the correct time if we have a fix
			String time = "161805.154";
			// Date date = new Date(System.currentTimeMillis());

			
			// TODO calculate a correct checksum
			String checksum = "*52";
			
			String sentence = "$GPGGA,"+time+","+slat+","+dlat+","+slon+","+dlon+","+fix+","+sats+",09.5,29.0,M,45.7,M,,"+checksum;
			
			informListeners(sentence);
			try {
				// If this thread has been canceled, we do not have to wait
				if (simulating) Thread.sleep(INTERVAL);
			} catch (InterruptedException e) {}
		}
	}

}
