/*
 * Created on 04.02.2006
 *
 */
package gps;

// import java.util.Date;

public class Position {
	private double lat;
	private double lon;
	// private Date date;
	
	public Position(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
		// this.date = date;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}
	
	/*
	public Date getDate() {
		return date;
	}
	*/
}
