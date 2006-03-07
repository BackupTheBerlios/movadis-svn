/*
 * Created on 07.03.2006
 *
 */
package waypoints;


public interface WaypointEventListener {
	public void waypointSelected(Waypoint wp, WaypointManager wpm);
	public void waypointAdded(Waypoint wp, WaypointManager wpm);
}
