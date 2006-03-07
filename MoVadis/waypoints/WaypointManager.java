/*
 * Created on 20.02.2006
 *
 */
package waypoints;

import gps.Position;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Display;

public class WaypointManager {
	private static String fileURL = "file:///e:/waypoints";
	
	private Vector waypoints;
	private Waypoint currentWaypoint;
	
	private Vector eventListeners;
	
	public WaypointManager(Display d) {
		waypoints = new Vector();
		eventListeners = new Vector();
	}
	
	public void addWaypointEventListener(WaypointEventListener wpsl) {
		eventListeners.addElement(wpsl);
	}
	
	protected void informSelectListeners() {
		Enumeration e = eventListeners.elements();
		while (e.hasMoreElements()) {
			WaypointEventListener wpsl = (WaypointEventListener) e.nextElement();
			wpsl.waypointSelected(currentWaypoint, this);
		}
	}
	
	protected void informAddListeners(Waypoint added) {
		Enumeration e = eventListeners.elements();
		while (e.hasMoreElements()) {
			WaypointEventListener wpsl = (WaypointEventListener) e.nextElement();
			wpsl.waypointAdded(added, this);
		}
	}
	
	public void loadWaypoints() throws IOException {
		FileConnection fc = (FileConnection)Connector.open(fileURL, Connector.READ);
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
		FileConnection fc = (FileConnection)Connector.open(fileURL, Connector.WRITE);
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
		informAddListeners(wp);
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
		synchronized (waypoints) {
			waypoints.removeAllElements();
			currentWaypoint = null;
			informSelectListeners();
		}
	}
	
	public void selectWaypoint(Waypoint wp) {
		currentWaypoint = wp;
		informSelectListeners();
	}
	
	public Waypoint getCurrentWaypoint() {
		return currentWaypoint;
	}
}
