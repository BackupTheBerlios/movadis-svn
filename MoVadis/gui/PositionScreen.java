/*
 * Created on 04.02.2006
 *
 */
package gui;

import gps.GPSData;
import gps.GPSDataReceivedListener;
import gps.Position;
import gps.SentenceListener;

import java.io.IOException;
import java.util.Calendar;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.StringItem;

import waypoints.Waypoint;
import waypoints.WaypointEventListener;
import waypoints.WaypointManager;

public class PositionScreen extends Form implements SentenceListener, GPSDataReceivedListener, WaypointEventListener {
	private StringItem lat;
	private StringItem lon;
	// private StringItem time;
	private StringItem sat;
	private StringItem fix;
	
	private StringItem wp;
	private StringItem dist;
	
	private WaypointScreen wps;
	
	public PositionScreen(Display disp, WaypointScreen wps) {
		super("MoVadis");
		this.wps = wps;
		System.out.println("WPS in Position screen constructor: "+wps);
		/*
		Image img = null;
		try {
			img = Image.createImage(this.getClass().getResourceAsStream("../res/movadis-small.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageItem iitem = new ImageItem("Position", img, ImageItem.LAYOUT_LEFT | ImageItem.LAYOUT_TOP | ImageItem.PLAIN, "");
		this.append(iitem);
		*/
		
		lat = new StringItem("Lat: ", "?");
		lon = new StringItem("Lon: ", "?");
		fix = new StringItem("Fix: ", "None");
		fix.setLayout(StringItem.LAYOUT_2 | StringItem.LAYOUT_NEWLINE_BEFORE);
		sat = new StringItem("Satellites: ", "?");
		sat.setLayout(StringItem.LAYOUT_2 | StringItem.LAYOUT_NEWLINE_AFTER);
		// time = new StringItem("Time: ", "...");
		
		wp = new StringItem("Waypoint: ", "...");
		dist = new StringItem("Distance: ", "...");
		
		this.append(lat);
		this.append(lon);
		this.append(fix);
		this.append(sat);
		// this.append(time);
		
		this.append(wp);
		this.append(dist);
	}
	
	private static String formatDouble(double f) {
		String sf = f+"000";
		// Where's the dot?
		int i = sf.indexOf(".");
		if (i >= 0) {
			return sf.substring(0, i+4);
		} else {
			return f+"";
		}
	}
	
	public void SentenceReceived(String sentence) {
		//this.info.setText(sentence);
	}
	
	public void GPSDataReceived(GPSData gd) {
		Position p = gd.getPosition();
		lat.setText(p.getLatitudeAsString());
		lon.setText(p.getLongitudeAsString());
		
		sat.setText(gd.getNumberOfSatellites()+"");
		
		fix.setText(gd.getFixType().toString());
		
		// Calendar c = gd.getTime();
		//time.setText(c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND)+"."+c.get(Calendar.MILLISECOND));
		
		Waypoint swp = wps.getWaypointManager().getCurrentWaypoint();
		if (swp != null) {
			wp.setText(swp.getName());
			Position wpos = swp.getPosistion();
			dist.setText(formatDouble(wpos.distanceTo(p))+" km");// ("+wpos.toString()+")");
		}
	}

	public void waypointSelected(Waypoint wp, WaypointManager wpm) {
		this.wp.setText(wp.getName());
		this.dist.setText("?");
	}

	public void waypointAdded(Waypoint wp, WaypointManager wpm) {}
}
