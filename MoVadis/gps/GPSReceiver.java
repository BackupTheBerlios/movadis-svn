/*
 * Created on 07.02.2006
 *
 */
package gps;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public abstract class GPSReceiver {
	private Vector listeners;

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

	public GPSReceiver() {
		this.listeners = new Vector();
	}
	
	public abstract void start() throws IOException;
	public abstract void stop() throws IOException;
	
	public void addSentenceListener(SentenceListener l) {
		synchronized (listeners) {
			listeners.addElement(l);
		}
	}

	protected void informListeners(String sentence) {
		System.out.println(sentence);
		synchronized (listeners) {
			Enumeration en = listeners.elements();
			while (en.hasMoreElements()) {
				try {
					System.out.println("Informing");
					// Inform the listener in a seperate thread, so we can get back to reading again
					// We usually only have 1-2, so this should be OK
					//((SentenceListener) en.nextElement()).SentenceReceived(sentence);
					new Informer((SentenceListener) en.nextElement(), sentence).start();
				} catch (Exception e) {}
			}
		}
	}

}