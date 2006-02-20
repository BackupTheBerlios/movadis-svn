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
		for (int i=0; i<input.length; i++) {
			buffer.append((char) input[i]);	
		}
	}
	
	public boolean hasSentence() {
		return buffer.toString().indexOf(13) >= 0;
	}
	
	public String getNextSentence() {
		// Find the first newline in the buffer (it closes a sentence)
		int i = buffer.toString().indexOf(13);
		// The first character is a CR, we skip it since it confuses
		// our parsers (we like Unix style newlines)
		String sentence = buffer.toString().substring(1, i);
		// i+1, since we have to delete the newline as well
		buffer.delete(0,i+1);
		return sentence;
	}
}
