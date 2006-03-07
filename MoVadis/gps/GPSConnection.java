/*
 * Created on 04.02.2006
 *
 */
package gps;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class GPSConnection extends GPSReceiver implements Runnable {
	private static long READ_INTERVAL = 100;
	
	private StreamConnection con;
	private String url;
	private boolean reading;
	
	private Thread readThread;
	private InputStreamReader reader;
	private InputStream is;
	
	private BTBuffer buffer;
	
	public GPSConnection(String url) {
		super();
		this.url = url;
		reading = false;
		buffer = new BTBuffer();
	}
	
	public void start() throws IOException {
		reading = true;
		
		con = ((StreamConnection) Connector.open(url));
		is = con.openInputStream();
		
		readThread = new Thread(this);
		readThread.start();		
	}
	
	public void stop() throws IOException {
		reading = false;
		close();
	}
	
	
	private void fillBuffer() throws IOException {
		while (! buffer.hasSentence()) {
			// We have to read as much as possible
			byte[] input = new byte[is.available()];
			int bytesRead = is.read(input);
			buffer.addData(input, bytesRead);
		}
	}
	
	private void read() {
		while (reading) {
			try {
				// Make sure we have at least one complete sentence 
				fillBuffer();
				// propagate all received sentences to our listeners
				while (buffer.hasSentence()) {
					// fetch the next NMEA sentence, inform listeners,
					// and remove the String it from buffer
					String sentence = buffer.getNextSentence();
					informListeners(sentence);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(READ_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void close() {
		try {
			reader.close();
		} catch (IOException e) {}
	}
	
	public void run() {
		reading = true;
		try {
			read();
		} finally {
			close();
		}
	}
}
