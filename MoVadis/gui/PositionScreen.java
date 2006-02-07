/*
 * Created on 04.02.2006
 *
 */
package gui;

import java.io.IOException;

import gps.GPSData;
import gps.GPSDataReceivedListener;
import gps.Position;
import gps.SentenceListener;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.StringItem;

public class PositionScreen extends Form implements SentenceListener, GPSDataReceivedListener {
	private StringItem lat;
	private StringItem lon;
	private StringItem info;
	private StringItem sat;
	private StringItem fix;
	
	public PositionScreen(Display disp) {
		super("MoVadis");
		//this.append("Position");
		Image img = null;
		try {
			img = Image.createImage(this.getClass().getResourceAsStream("../res/movadis.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageItem iitem = new ImageItem("Position", img, ImageItem.PLAIN, "");
		this.append(iitem);
		
		lat = new StringItem("Latitude: ", "?");
		lon = new StringItem("Longitude: ", "?");
		sat = new StringItem("Satellites: ", "?");
		fix = new StringItem("Fix: ", "None");
		info = new StringItem("Info: ", "...");
		
		this.append(lat);
		this.append(lon);
		this.append(sat);
		this.append(fix);
		this.append(info);
	}
	
	public void SentenceReceived(String sentence) {
		this.info.setText(sentence);
		//Alert a = new Alert("DATA!", sentence, null, AlertType.INFO);
		//disp.setCurrent(a, this);
	}
	
	public void GPSDataReceived(GPSData gd) {
		//Alert a = new Alert("posotion changed", p.getLat()+"/"+p.getLon(), null, AlertType.INFO);
		//disp.setCurrent(a, this);
		Position p = gd.getPosition();
		lat.setText(p.getLat()+"�N");
		lon.setText(p.getLon()+"�E");
		
		sat.setText(gd.getNumberOfSatellites()+"");
		
		fix.setText(gd.getFixType().toString());
	}
}