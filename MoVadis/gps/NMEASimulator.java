/*
 * Created on 07.02.2006
 *
 */
package gps;

import java.io.IOException;
import java.util.Random;

public class NMEASimulator extends GPSReceiver implements Runnable {
	private static int INTERVAL = 1000;
	private Position basepos;
	private Thread simThread; 
	private Random random;
	
	private boolean simulating;
	
	public NMEASimulator(Position basepos) {
		this.basepos = basepos;
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

	public void run() {
		while (simulating) {
			// $GPGGA,125532.146,5123.1569,N,00645.0726,E,1,04,09.5,29.0,M,45.7,M,,*52
			
			// TODO extract coordinates from Position object 
			// TODO vary the position randomly
			String lat = "5123.1569";
			String lon = "00645.0726";
			// TODO insert the correct time
			String time = "161805.154";
			// Date date = new Date(System.currentTimeMillis());
			int sats = random.nextInt(6);
			int fix = sats > 0 ? 1 : 0;
			
			// TODO calculate a correct checksum
			String checksum = "*52";
			
			String sentence = "$GPGGA,"+time+","+lat+",N,"+lon+",E,"+fix+","+sats+",09.5,29.0,M,45.7,M,,"+checksum;
			informListeners(sentence);
			try {
				// If this thread has been canceled, we do not have to wait
				if (simulating) Thread.sleep(INTERVAL);
			} catch (InterruptedException e) {}
		}
	}

}
