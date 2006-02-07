/*
 * Created on 04.02.2006
 *
 */
package gps;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class GPSConnection implements Runnable {
	private StreamConnection con;
	private String url;
	private boolean reading;
	
	private Vector listeners;
	
	private Thread readThread;
	private InputStreamReader reader;
	
	private class Informer extends Thread {
		private SentenceListener l;
		private String sentence;
		public Informer(SentenceListener l, String sentence) {
			this.l = l;
			this.sentence = sentence;
		}
		public void run() {
			this.l.SentenceReceived(sentence);
		}
	}
	
	
	public GPSConnection(String url) {
		this.url = url;
		
		listeners = new Vector();
		
		reading = true;
	}
	
	public void start() throws IOException {
		con = ((StreamConnection) Connector.open(url, Connector.READ));
		
		reader = new InputStreamReader(con.openInputStream());
		// con = Connector.openInputStream(url);
		
		//read();
		readThread = new Thread(this);
		readThread.start();		
	}
	
	public void addSentenceListener(SentenceListener l) {
		synchronized (listeners) {
			listeners.addElement(l);
		}
	}
	
	private void informListeners(String sentence) {
		synchronized (listeners) {
			Enumeration en = listeners.elements();
			while (en.hasMoreElements()) {
				try {
					// Inform the listener in a seperate thread, so we can get back to reading again
					// We usually only have 1-2, so this should be OK
					//((SentenceListener) en.nextElement()).SentenceReceived(sentence);
					new Informer((SentenceListener) en.nextElement(), sentence).start();
				} catch (Exception e) {}
			}
		}
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
		start();
	}
	
	public void close() {
		reading = false;
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
