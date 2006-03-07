/*
 * Created on 04.02.2006
 *
 */
package gui;

import gps.DeviceSelectionListener;
import gps.GPSConnection;
import gps.GPSDecoder;
import gps.GPSReceiver;
import gps.NMEASimulator;
import gps.Position;
import gps.PositionBuffer;
import gps.SentenceListener;
import gps.Position.LatitudeDirection;
import gps.Position.LongitudeDirection;

import java.io.IOException;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.RemoteDevice;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.sun.midp.io.BufferedConnectionAdapter;

import waypoints.Waypoint;
import waypoints.WaypointManager;


public class MoVadis extends MIDlet implements CommandListener, DeviceSelectionListener, SentenceListener {
	private Display disp;
	
	private PositionScreen pos;
	private WaypointScreen wps;
	
	private GPSDecoder dec;
	private GPSReceiver con;
	
	private PositionBuffer pbuf;
	
	private WaypointManager wpm;
	
	private static Command exitCommand = new Command("Exit", Command.EXIT, 0);
	private static Command connectCommand = new Command("Connect", Command.SCREEN, 0);
	private static Command simCommand = new Command("Simulate", Command.SCREEN, 2);
	
	private static Command wpScreen = new Command("Waypoints", Command.SCREEN, 1);
	
	private static Command loadWps = new Command("Load waypoints", Command.SCREEN, 1);
	private static Command saveWps = new Command("Save waypoints", Command.SCREEN, 1);
	
	private static Command selectWp = new Command("Select", Command.SCREEN, 0);
	private static Command addWp = new Command("Add waypoint", Command.SCREEN, 1);
	private static Command clearWp = new Command("Clear", Command.SCREEN, 2);
	private static Command toPosScreen = new Command("Back", Command.BACK, 0);
	
	private static Command wpQuicksave = new Command("Quicksave", Command.SCREEN, 1);
	
	
	public MoVadis() {
		con = null;
		// disp is set in startApp
		
		dec = new GPSDecoder();
		
		pbuf = new PositionBuffer();
		dec.addGPSDataReceivedListener(pbuf);
		
		wpm = new WaypointManager(disp);
		// wpm.addWaypoint(new Waypoint("Test", new Position(51.231569f, -6.5f)));
		
		wps = new WaypointScreen(wpm);
		wps.setCommandListener(this);
		wps.addCommand(selectWp);
		wps.addCommand(clearWp);
		wps.addCommand(addWp);
		wps.addCommand(toPosScreen);
		wps.addCommand(wpQuicksave);
		wps.addCommand(loadWps);
		wps.addCommand(saveWps);
		
		wpm.addWaypointEventListener(wps);
		
		pos = new PositionScreen(disp, wps);
		dec.addGPSDataReceivedListener(pos);
		wpm.addWaypointEventListener(pos);
		
		pos.addCommand(exitCommand);
		pos.addCommand(connectCommand);
		pos.addCommand(simCommand);
		
		pos.addCommand(wpScreen);
		
		pos.addCommand(wpQuicksave);
		
		pos.setCommandListener(this);
	}
	
	protected void startApp() throws MIDletStateChangeException {
		disp = Display.getDisplay(this);
		disp.setCurrent(pos);
	}

	protected void pauseApp() {
		// TODO Auto-generated method stub
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub

	}

	public void commandAction(Command cmd, Displayable dis) {
		if (cmd == exitCommand) {
			// this.destroyApp(false);
			notifyDestroyed();
		} else if (cmd == connectCommand) {
			Displayable l = null;
			try {
				l = new BTDeviceList(this);
			} catch (BluetoothStateException e) {
				e.printStackTrace();
			}
			disp.setCurrent(l);
		} else if (cmd == simCommand) {
			setDataSource(new NMEASimulator(new Position(51.231569f, -6.5f)));
			Alert a = new Alert("Simulation!");
			disp.setCurrent(a, pos);
		} else if (cmd == loadWps) {
			try {
				wpm.loadWaypoints();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (cmd == saveWps) {
			try {
				wpm.saveWaypoints();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (cmd == wpScreen) {
			wps.update();
			disp.setCurrent(wps);
		} else if (cmd == selectWp) {
			wps.changeCurrentWaypoint();
			Waypoint wp = wps.getWaypointManager().getCurrentWaypoint();
			Alert a = new Alert("Waypoint selected", wp.getName()+" ("+wp.getPosistion().toString()+")", null, AlertType.INFO);
			disp.setCurrent(a, pos);
		} else if (cmd == addWp) {
			WaypointInput wpi = new WaypointInput(wpm);
			wpi.setCommandListener(this);
			disp.setCurrent(wpi);
		} else if (cmd == WaypointInput.saveCmd) {
			WaypointInput wpi = (WaypointInput) dis;
			wpi.saveWaypoint();
			disp.setCurrent(wps);
		} else if (cmd == WaypointInput.cancelCmd) {
			WaypointInput wpi = (WaypointInput) dis;
			disp.setCurrent(wps);
		} else if (cmd == clearWp) {
			wpm.clearWaypoints();
			wps.update();
		} else if (cmd == toPosScreen) {
			disp.setCurrent(pos);
		} else if (cmd == wpQuicksave) {
			if (pbuf.hasPosition()) {
				Position p = pbuf.getLastConfirmedPosition();
				String name = "foobar";
				wpm.addWaypoint(name, p);
				try {
					wpm.saveWaypoints();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				wps.update();
				System.out.println("Saved");
			} else {
				System.out.println("Buffer has no position");
			}
			
		}
	}
	
	private void setDataSource(GPSReceiver rcv) {
		if (con != null) {
			try {
				con.stop();
			} catch (IOException e) {}
		}
		
		con = rcv;
		con.addSentenceListener(this);
		con.addSentenceListener(pos);
		con.addSentenceListener(dec);
		
		try {
			con.start();
		} catch (IOException e) {
			Alert a = new Alert("Connection error", "Unable to connect device.", null, AlertType.ERROR);
			a.setTimeout(Alert.FOREVER);
			disp.setCurrent(a, pos);
		}		
	}
	
	public void DeviceSelectionFinished(boolean success, RemoteDevice dev, String url) {
		if (success) {
			setDataSource(new GPSConnection(url));
			disp.setCurrent(pos);
		} else {
			Alert a = new Alert("Abort", "No device selected.", null, AlertType.ERROR);
			a.setTimeout(Alert.FOREVER);
			disp.setCurrent(a, pos);
		}
	}
	
	// Just for debugging!
	public void SentenceReceived(String sentence) {
		//Alert al = new Alert("Sentence", "->"+toHex(sentence)+"<-", null, AlertType.INFO);
		//Alert al = new Alert("Sentence", "->"+sentence+"<-", null, AlertType.INFO);
		//disp.setCurrent(al, disp.getCurrent());		
	}
	
	private static String toHex(String msg) {
		StringBuffer result = new StringBuffer();
		for (int i=0; i<msg.length(); i++) {
			char c = msg.charAt(i);
			result.append((int)c+"|");
		}
		return result.toString();
	}
}
