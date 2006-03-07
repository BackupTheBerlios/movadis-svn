/*
 * Created on 07.03.2006
 *
 */
package gui;

import javax.microedition.lcdui.ChoiceGroup;
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
		
		latc = new ChoiceGroup("", ChoiceGroup.EXCLUSIVE);
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

		lonc = new ChoiceGroup("", ChoiceGroup.EXCLUSIVE);
		lonc.append("W", null);
		lonc.append("E", null);
		lonc.setLayout(ChoiceGroup.LAYOUT_2 | ChoiceGroup.LAYOUT_NEWLINE_AFTER);
		
		this.append(lond);
		this.append(lonm);
		this.append(lonc);
	}
	
	public WaypointInput(WaypointManager wpm, Waypoint wp) {
		this(wpm);
		this.setTitle("Edit waypoint");
		name.setString(wp.getName());
	}
}
