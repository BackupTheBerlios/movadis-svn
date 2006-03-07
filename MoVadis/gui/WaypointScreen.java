/*
 * Created on 21.02.2006
 *
 */
package gui;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.List;

import waypoints.Waypoint;
import waypoints.WaypointEventListener;
import waypoints.WaypointManager;

public class WaypointScreen extends List implements WaypointEventListener {
	private WaypointManager wpm;
	
	public WaypointScreen(WaypointManager wpm) {
		super("Waypoints", List.EXCLUSIVE);
		this.wpm = wpm;
		update();
	}
	
	public void update() {
		this.deleteAll();
		int size = wpm.getSize();
		for (int i=0; i<size; i++) {
			Waypoint wp = wpm.getWaypointAt(i);
			this.append(wp.getName(), null);
			this.setSelectedIndex(i, (wpm.getCurrentWaypoint() == wp));
		}
	}
	
	public void changeCurrentWaypoint() {
		wpm.selectWaypoint(wpm.getWaypointAt(this.getSelectedIndex()));
		// We will receive an event notification
		// update();
	}
	
	public WaypointManager getWaypointManager() {
		return wpm;
	}

	public void waypointSelected(Waypoint wp, WaypointManager wpm) {
		update();
	}

	public void waypointAdded(Waypoint wp, WaypointManager wpm) {
		update();
	}
	
}
