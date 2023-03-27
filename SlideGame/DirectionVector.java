package SlideGame;

public enum DirectionVector 
{

	EAST(+1, 0), 
	NORTHEAST(+1, -1), 
	NORTHWEST(0, -1), 
    WEST(-1, 0), 
    SOUTHWEST(-1, +1), 
    SOUTHEAST(0, +1),;
	
	private int q, r;

	DirectionVector(int q, int r) 
	{
		
		this.q = q;
		this.r = r;
		
		
		
	} 
	
	public static int amountOfDirections() 
	{
		
		return 6;
		
	}

	public int getQ() {
		return q;
	}


	public int getR() {
		return r;
	}


	
	
}
