/*
 * Created on 04.02.2006
 *
 */
package gps;


import java.io.IOException;
import java.io.InputStreamReader;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class GPSConnection extends GPSReceiver implements Runnable {
	private StreamConnection con;
	private String url;
	private boolean reading;
	
	private Thread readThread;
	private InputStreamReader reader;	
	
	public GPSConnection(String url) {
		super();
		
		this.url = url;
		
		reading = true;
	}
	
	public void start() throws IOException {
		con = ((StreamConnection) Connector.open(url, Connector.READ));
		
		reader = new InputStreamReader(con.openInputStream());
		
		readThread = new Thread(this);
		readThread.start();		
	}
	
	public void stop() throws IOException {
		reading = false;
		close();
	}
	
	private synchronized String readLine() throws IOException {
		StringBuffer r = new StringBuffer();
		int input;
		// 13 == carriage return
        while ((input = reader.read()) != 13) {
        	r.append((char)input);
        }
        return r.toString().substring(1, r.length());
	}
	
	private synchronized String readSentence() throws IOException {
		return readLine();
	}
	
	private void read() throws IOException {
		while (reading) {
			String sentence = readSentence();
			informListeners(sentence);
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
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
}
