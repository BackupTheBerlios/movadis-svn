/*
 * Created on 20.02.2006
 *
 */
package gps;

public class BTBuffer {
	private StringBuffer buffer;
	
	public BTBuffer() {
		buffer = new StringBuffer();
	}
	
	public void addData(byte[] input) {
		synchronized (buffer) {
			for (int i=0; i<input.length; i++) {
				buffer.append((char) input[i]);	
			}
		}
	}
	
	public boolean hasSentence() {
		synchronized (buffer) {
			// Sentences are closed by two characters, CR (0d/13) and LF (0a/10)
			int lfi =  buffer.toString().indexOf(13);
			int cri =  buffer.toString().indexOf(10);
			// Both characters (line feed and carriage return) are present and in order
			return (lfi >= 0 && cri >= 0) && (lfi == cri+1);
		}
	}
	
	public String getNextSentence() {
		synchronized (buffer) {
			// Find the first carriage return in the buffer (it closes a sentence)
			int i = buffer.toString().indexOf(10);
			// NMEA closes sentences with CR-LF, so we exclude both characters from the result
			String sentence = buffer.toString().substring(0, i-1);
			// delete the retrieved data from the buffer
			buffer.delete(0,i);
			return sentence;
		}
	}
}
