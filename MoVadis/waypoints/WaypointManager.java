/*
 * Created on 20.02.2006
 *
 */
package waypoints;

import gps.Position;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;

public class WaypointManager {
	private static String fileURL = "file:///e:/waypoints";
	
	private Vector waypoints;

	private Display disp;
	
	public WaypointManager(Display d) {
		waypoints = new Vector();
		this.disp = d;
	}
	
	public void loadWaypoints() throws IOException {
		FileConnection fc = (FileConnection)Connector.open(fileURL);
		InputStream is = fc.openInputStream();
		
		StringBuffer buffer = new StringBuffer();
		int c;
		while ((c = is.read()) != -1) {
			char ch = (char) c;
			if (ch == '\n') {
				Waypoint wp = Waypoint.thaw(buffer.toString());
				addWaypoint(wp);
				buffer.delete(0, buffer.length());
			} else {
				buffer.append(ch);
			}
		}
		is.close();
		
		fc.close();		
	}
	
	public void saveWaypoints() throws IOException {
		FileConnection fc = (FileConnection)Connector.open(fileURL);
		if (! fc.exists()) {
			fc.create();
		}
		OutputStream os = fc.openOutputStream();
		synchronized (waypoints) {
			for (int i=0; i<waypoints.size(); i++) {
				Waypoint wp = (Waypoint) waypoints.elementAt(i);
				String wpline = wp.freeze();
				os.write((wpline+"\n").getBytes());
			}
		}
		
		fc.close();
	}
	
	public void addWaypoint(Waypoint wp) {
		synchronized (waypoints) {
			waypoints.addElement(wp);
		}
	}
	
	public void addWaypoint(String name, Position pos) {
		Waypoint wp = new Waypoint(name, pos);
		addWaypoint(wp);
	}

	public int getSize() {
		synchronized (waypoints) {
			return waypoints.size();
		}
	}
	
	public Waypoint getWaypointAt(int i) {
		synchronized (waypoints) {
			return (Waypoint) waypoints.elementAt(i);
		}
	}
	
	public void clearWaypoints() {
		waypoints.removeAllElements();
	}
}
