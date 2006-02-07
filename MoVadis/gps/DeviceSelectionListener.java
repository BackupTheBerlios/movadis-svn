/*
 * Created on 04.02.2006
 *
 */
package gps;

import javax.bluetooth.RemoteDevice;

public interface DeviceSelectionListener {
	public void DeviceSelectionFinished(boolean success, RemoteDevice dev, String url);
}
