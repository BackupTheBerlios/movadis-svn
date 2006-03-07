/*
 * Created on 04.02.2006
 *
 */
package gps;


public class Position {
	private int degLon;
	private int degLat;
	
	private double minLon;
	private double minLat;
	private LongitudeDirection loDir;
	private LatitudeDirection laDir;
	
	public abstract static class GeoDirection {
		private char dir;
		protected GeoDirection(char c) {
			this.dir = c;
		}
		public String toString() {
			return ""+dir;
		}
	}
	
	public static final class LongitudeDirection extends GeoDirection {
		public static final LongitudeDirection WEST = new LongitudeDirection('W');
		public static final LongitudeDirection EAST = new LongitudeDirection('E');		
		
		private LongitudeDirection(char c) {
			super(c);
		}
	}

	public static final class LatitudeDirection extends GeoDirection {
		public static final LatitudeDirection NORTH = new LatitudeDirection('N');
		public static final LatitudeDirection SOUTH = new LatitudeDirection('S');
		
		private LatitudeDirection(char c) {
			super(c);
		}
	}
	
	public Position(int degLat, double minLat, LatitudeDirection laDir, int degLon, double minLon, LongitudeDirection loDir) {
		// TODO Handle overflow in any direction
		this.degLon = degLon;
		this.minLon = minLon;
		this.loDir = loDir;
		this.degLat = degLat;
		this.minLat = minLat;
		this.laDir = laDir;
	}
	
	public Position(double latitude, double longitude) {
		this(Math.abs((int)latitude), Math.abs((latitude-(int)latitude)*60), latitude > 0 ? LatitudeDirection.NORTH : LatitudeDirection.SOUTH, Math.abs((int)longitude), Math.abs(((longitude-(int)longitude))*60), longitude > 0 ? LongitudeDirection.WEST : LongitudeDirection.EAST);
	}
	
	public String getLatitudeAsString() {
		return degLat+"° "+minLat+"' "+laDir;
	}

	public String getLongitudeAsString() {
		return degLon+"° "+minLon+"' "+loDir;
	}
	
	public String toString() {
		return getLatitudeAsString()+"/"+getLongitudeAsString();
	}
	
	protected static double toDecimal(int deg, double min) {
		double result = 0;
		result += deg;
		result += min/60;
		return result;
	}
	
	public double getLatitudeAsDecimal() {
		return toDecimal(this.degLat, this.minLat) * (this.laDir == LatitudeDirection.SOUTH ? -1 : 1); 
	}
	
	public double getLongitudeAsDecimal() {
		return toDecimal(this.degLon, this.minLon) * (this.loDir == LongitudeDirection.EAST ? -1 : 1); 
	}
	
	public double distanceTo(Position p) {
		double result = 0;
		
		double lat1 = getLatitudeAsDecimal();
		double lon1 = getLongitudeAsDecimal();
		
		double lat2 = p.getLatitudeAsDecimal();
		double lon2 = p.getLongitudeAsDecimal();
		
		// TODO Implement a proper calculation
		// -> d=2*asin(sqrt((sin((lat1-lat2)/2))^2 + cos(lat1)*cos(lat2)*(sin((lon1-lon2)/2))^2))
		
		//double root = pow( pow(Math.sin((lat1-lat2)/2),2) + Math.cos(lat1)*Math.cos(lat2)*(Math.sin((lon1-lon2)/2)) ,2);
		//result = 2*Trigonometry.asin(Math.toRadians(Math.sqrt(root)));
		
		//		L1  =  	latitude at the first point (degrees)
		//		L2 	= 	latitude at the second point (degrees)
		//		G1 	= 	longitude at the first point (degrees)
		//		G2 	= 	longitude at the second point (degrees)
		//		DG 	= 	longitude of the second point minus longitude of the first point (degrees)
		//		DL 	= 	latitude of the second point minus latitude of the first point (degrees)
		//		D 	= 	computed distance (km)
		// 1 minute of arc is 1 nautical mile
		// 1 nautical mile is 1.852 km
		// D = 1.852 * 60 * ARCOS ( SIN(L1) * SIN(L2) + COS(L1) * COS(L2) * COS(DG))
		
		//double t = Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2-lon1);
		//double distance = 1.852 * 60 * Float11.acos(t);
		
		//result = distance;
		
		// Let's assume the earth is a flat surface
		
		double latd = (lat2-lat1)*40000/360;
		double lond = Math.cos((lat1+lat2)/2)*(lon2-lon1)*40000/360;
		
		double distance = Math.sqrt(latd*latd + lond*lond);
		
		result = distance;
		
		return result;		
	}
	
	public String freeze() {
		return getLatitudeAsDecimal()+"/"+getLongitudeAsDecimal();
	}
	
	public static Position thaw(String data) {
		int i = data.indexOf('/');
		String lat = data.substring(0, i);
		String lon = data.substring(i+1, data.length());
		double flat = Double.parseDouble(lat);
		double flon = Double.parseDouble(lon);
		return new Position(flat, flon);
	}
	
}
