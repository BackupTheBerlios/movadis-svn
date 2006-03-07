/*
 * Created on 21.02.2006
 *
 */
package gui;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.List;

import waypoints.Waypoint;
import waypoints.WaypointManager;

public class WaypointScreen extends List {
	private WaypointManager wpm;
	private Display disp;
	
	public WaypointScreen(Display disp, WaypointManager wpm) {
		super("Waypoints", List.EXCLUSIVE);
		this.wpm = wpm;
		this.disp = disp;
		update();
	}
	
	public void update() {
		this.deleteAll();
		int size = wpm.getSize();
		for (int i=0; i<size; i++) {
			this.append(wpm.getWaypointAt(i).getName(), null);
			this.setSelectedIndex(i, false);
		}
	}
	
	public Waypoint getSelectedWaypoint() {
		int i = this.getSelectedIndex();
		return wpm.getWaypointAt(i);
	}
	
}
