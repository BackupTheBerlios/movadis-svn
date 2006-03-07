/*
 * Created on 07.03.2006
 *
 */
package gui;

import gps.Position;
import gps.Position.LatitudeDirection;
import gps.Position.LongitudeDirection;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

import waypoints.Waypoint;
import waypoints.WaypointManager;

public class WaypointInput extends Form {
	private WaypointManager wpm;
	
	private TextField name;
	private TextField latd;
	private TextField latm;
	
	private ChoiceGroup latc;
	
	private TextField lond;
	private TextField lonm;
	
	private ChoiceGroup lonc;

	static Command saveCmd = new Command("Save", Command.OK, 1);
	static Command cancelCmd = new Command("Cancel", Command.CANCEL, 1);
	
	public WaypointInput(WaypointManager wpm) {
		super("Add waypoint");
		this.wpm = wpm;
		
		name = new TextField("Name", "...", 256, TextField.ANY);
		this.append(name);

		this.append(new StringItem("Latitude: ","",StringItem.PLAIN));
		latd = new TextField("Degrees:", "", 2, TextField.NUMERIC);
		latd.setLayout(TextField.LAYOUT_2 | TextField.LAYOUT_NEWLINE_BEFORE);
		latm = new TextField("Minutes:", "", 9, TextField.DECIMAL);
		latm.setLayout(TextField.LAYOUT_2);
		
		latc = new ChoiceGroup("", ChoiceGroup.POPUP);
		latc.append("N", null);
		latc.append("S", null);
		latc.setLayout(ChoiceGroup.LAYOUT_2 | ChoiceGroup.LAYOUT_NEWLINE_AFTER);
		
		this.append(latd);
		this.append(latm);
		this.append(latc);
		
		this.append(new StringItem("Longitude: ",""));
		lond = new TextField("Degrees:", "", 3, TextField.NUMERIC);
		lond.setLayout(TextField.LAYOUT_2 | TextField.LAYOUT_NEWLINE_BEFORE);
		lonm = new TextField("Minutes:", "", 9, TextField.DECIMAL);
		lonm.setLayout(TextField.LAYOUT_2);

		lonc = new ChoiceGroup("", ChoiceGroup.POPUP);
		lonc.append("W", null);
		lonc.append("E", null);
		lonc.setLayout(ChoiceGroup.LAYOUT_2 | ChoiceGroup.LAYOUT_NEWLINE_AFTER);
		
		this.append(lond);
		this.append(lonm);
		this.append(lonc);
		
		this.addCommand(saveCmd);
		this.addCommand(cancelCmd);
		// this.setCommandListener(this);
	}
	
	public WaypointInput(WaypointManager wpm, Waypoint wp) {
		this(wpm);
		this.setTitle("Edit waypoint");
		name.setString(wp.getName());
	}
	
	void saveWaypoint() {
		int dlat = Integer.parseInt(latd.getString());
		double mlat = Double.parseDouble(latm.getString());
		LatitudeDirection lato = (latc.getSelectedIndex() == 0) ? LatitudeDirection.NORTH : LatitudeDirection.SOUTH;
		
		int dlon = Integer.parseInt(lond.getString());
		double mlon = Double.parseDouble(lonm.getString());
		LongitudeDirection lono = (lonc.getSelectedIndex() == 0) ? LongitudeDirection.WEST : LongitudeDirection.EAST;
		
		Position pos = new Position(dlat, mlat, lato, dlon, mlon, lono);
		Waypoint newWp = new Waypoint(name.getString(), pos);
		wpm.addWaypoint(newWp);
	}
	
}
