
class Matrix {
		
	static final int X = 0, Y = 1, Z = 2;
	
	public static float[] rot2D(float angle) {
		
		float[] m = {
			
			1, 0,
			0, 1,
		};

		m[0] =  (float) Math.cos(angle); 
		m[1] = -(float) Math.sin(angle);
		m[2] =  (float) Math.sin(angle);
		m[3] =  (float) Math.cos(angle);

		return  m;
	}

}
