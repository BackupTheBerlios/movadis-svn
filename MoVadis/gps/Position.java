/*
 * Created on 04.02.2006
 *
 */
package gps;

public class Position {
	private int degLon;
	private int degLat;
	
	private float minLon;
	private float minLat;
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
	
	public Position(int degLat, float minLat, LatitudeDirection laDir, int degLon, float minLon, LongitudeDirection loDir) {
		// TODO Handle overflow in any direction
		this.degLon = degLon;
		this.minLon = minLon;
		this.loDir = loDir;
		this.degLat = degLat;
		this.minLat = minLat;
		this.laDir = laDir;
	}
	
	public Position(float latitude, float longitude) {
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
	
	protected static float toDecimal(int deg, float min) {
		float result = 0;
		result += deg;
		result += min/60;
		return result;
	}
	
	public float getLatitudeAsFloat() {
		return toDecimal(this.degLat, this.minLat) * (this.laDir == LatitudeDirection.SOUTH ? -1 : 1); 
	}
	
	public float getLongitudeAsFloat() {
		return toDecimal(this.degLon, this.minLon) * (this.loDir == LongitudeDirection.EAST ? -1 : 1); 
	}
	
	private static double pow(double a, int b) {
		double result = 1;
		while (b-- > 0) {
			result *= a;
		}
		return result;
	}
	
	public double distanceTo(Position p) {
		double result = 0;
		
		double lat1 = getLatitudeAsFloat();
		double lon1 = getLongitudeAsFloat();
		
		double lat2 = p.getLatitudeAsFloat();
		double lon2 = p.getLongitudeAsFloat();
		
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

		return result;		
	}
	
}
