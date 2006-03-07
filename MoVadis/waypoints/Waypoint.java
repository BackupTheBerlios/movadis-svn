/*
 * Created on 20.02.2006
 *
 */
package waypoints;

import gps.Position;

public class Waypoint {
	private String name;
	private Position posistion;
	
	public Waypoint(String name, Position pos) {
		this.name = name;
		this.posistion = pos;
	}

	public String getName() {
		return name;
	}

	public Position getPosistion() {
		return posistion;
	}
	
	public String freeze() {
		return posistion.freeze()+" "+getName();
	}
	
	public static Waypoint thaw(String data) {
		int i = data.indexOf(' ');
		String spos = data.substring(0, i);
		String name = data.substring(i+1, data.length());
		
		Position pos = Position.thaw(spos);
		return new Waypoint(name, pos);
	}
}
