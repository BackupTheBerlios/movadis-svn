/*
 * Created on 04.02.2006
 *
 */
package gui;

import java.io.IOException;

import gps.DeviceSelectionListener;
import gps.GPSConnection;
import gps.GPSData;
import gps.GPSDataReceivedListener;
import gps.GPSDecoder;
import gps.SentenceListener;

import javax.bluetooth.RemoteDevice;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class MoVadis extends MIDlet implements CommandListener, DeviceSelectionListener {
	private Display disp;
	
	private PositionScreen pos;
	
	private GPSDecoder dec;
	private GPSConnection con;
	
	private static Command exitCommand = new Command("Exit", Command.EXIT, 0);
	private static Command connectCommand = new Command("Connect", Command.SCREEN, 0);
	
	public MoVadis() {
		con = null;
		disp = Display.getDisplay(this);
		
		pos = new PositionScreen(disp);
		pos.addCommand(exitCommand);
		pos.addCommand(connectCommand);
		
		pos.setCommandListener(this);
		
		dec = new GPSDecoder();
		dec.addGPSDataReceivedListener(pos);
	}
	
	protected void startApp() throws MIDletStateChangeException {
		//TextBox txt = new TextBox("Eingabe:", "", 256, 0);
		//disp.setCurrent(txt);
		/*
		Map m = new Map();
		disp.setCurrent(m);
		m.place(1, new Coordinate(50, 100));
		m.place(2, new Coordinate(100, 200));
		m.place(3, new Coordinate(75, 150));
		*/
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
			Displayable l = new BTDeviceList(this);
			disp.setCurrent(l);
		}
	}

	public void DeviceSelectionFinished(boolean success, RemoteDevice dev, String url) {
		if (success) {
			con = new GPSConnection(url);
			con.addSentenceListener(pos);
			con.addSentenceListener(dec);
			
			try {
				con.start();
			} catch (IOException e) {
				Alert a = new Alert("Connection error", "Unable to connect device at "+url+".", null, AlertType.ERROR);
				a.setTimeout(Alert.FOREVER);
				disp.setCurrent(a, pos);
			}
			disp.setCurrent(pos);
		} else {
			Alert a = new Alert("Abort", "No device selected.", null, AlertType.ERROR);
			a.setTimeout(Alert.FOREVER);
			disp.setCurrent(a, pos);
		}
	}
	
}
