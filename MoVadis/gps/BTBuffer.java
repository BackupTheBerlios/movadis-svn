/*
 * Created on 20.02.2006
 *
 */
package gps;

public class BTBuffer {
	private static int FLUSH_THRESHOLD = 8;
	
	private StringBuffer buffer;
	
	public BTBuffer() {
		buffer = new StringBuffer();
	}
	
	public void addData(byte[] input) {
		synchronized (buffer) {
			for (int i=0; i<input.length; i++) {
				if (input[i] != 0) {
					buffer.append((char) input[i]);
				}
			}
		}
	}
	
	public void addData(byte[] input, int bytesRead) {
		synchronized (buffer) {
			if (getNumberOfSentences() > FLUSH_THRESHOLD) {
				buffer.delete(0, buffer.length());
			}
			for (int i=0; i<bytesRead; i++) {
				buffer.append((char) input[i]);
			}
		}
	}
	
	public boolean hasSentence() {
		synchronized (buffer) {
			// Sentences are closed by two LF characters
			int lfi =  buffer.toString().indexOf(10);
			// Both characters (line feed and carriage return) are present and in order
			return (lfi >= 0);
		}
	}
	
	public int getNumberOfSentences() {
		synchronized (buffer) {
			int nSymbols = 0;
			for (int i=0; i<buffer.length(); i++) {
				if (buffer.charAt(i) == '$') {
					nSymbols++;
				}
			}
			return nSymbols;
		}
	}
	
	public String getNextSentence() {
		synchronized (buffer) {
			// Find the first carriage return in the buffer (it closes a sentence)
			int i = buffer.toString().indexOf(10);
			
			//MoVadis.instance.debug("->"+buffer.toString()+"<-\n"+i);
			
			// NMEA closes sentences with CR-LF, so we exclude both characters from the result
			String sentence = buffer.toString().substring(0, i);
			// delete the retrieved data from the buffer
			buffer.delete(0,i+1);
			return sentence;
		}
	}
}
