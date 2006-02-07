/*
 * Created on 04.02.2006
 *
 */
package gui;
import gps.DeviceSelectionListener;

import java.io.IOException;
import java.util.Vector;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

public class BTDeviceList extends List implements DiscoveryListener, CommandListener {
	private LocalDevice local;
	private DiscoveryAgent da;
	
	private static Command okCommand = new Command("OK", Command.OK, 0); 
	private static Command cancelCommand = new Command("Cancel", Command.CANCEL, 0);
	
	private Vector devices;
	private DeviceSelectionListener listener;
	
	public BTDeviceList(DeviceSelectionListener listener) {
		super("Bluetooth", List.EXCLUSIVE);
		this.setCommandListener(this);
		
		this.listener = listener;
		
		this.addCommand(okCommand);
		this.addCommand(cancelCommand);
		
		devices = new Vector();
		try {
			local = LocalDevice.getLocalDevice();
			da = local.getDiscoveryAgent();
			da.startInquiry(DiscoveryAgent.GIAC, this);
		} catch (BluetoothStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deviceDiscovered(RemoteDevice dev, DeviceClass devclass) {
		String name = null;
		try {
			name = dev.getFriendlyName(true);
			devices.addElement(dev);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.append(name, null);
	}

	public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
		String url = servRecord[0].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, true);
		
		listener.DeviceSelectionFinished(true, servRecord[0].getHostDevice(), url);
	}

	public void serviceSearchCompleted(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void inquiryCompleted(int arg0) {
		// TODO Auto-generated method stub
		
	}

	public void commandAction(Command cmd, Displayable d) {
		boolean success = false;
		RemoteDevice dev = null;
		this.removeCommand(okCommand);
		if (cmd == okCommand) {
			if (getSelectedIndex() >= 0) {
				dev = (RemoteDevice) devices.elementAt(this.getSelectedIndex());
				
				UUID []uuidSet = {new UUID(0x1101)};
				try {
					da.searchServices(null, uuidSet, dev, this);
				} catch (BluetoothStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				success = true;
			}
		} else if (cmd == cancelCommand) {
		}
		
		if (! success) {
			listener.DeviceSelectionFinished(false, null, "");
		}
	}
}
