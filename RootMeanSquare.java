/**
 * @author Pragadeesh
 *
 */
public class RootMeanSquare {
	
	public static double error = 0.0;
	public static int counter = 0;
	
	public void addValues(double predictedRating, double actualRating) {
		double delta = predictedRating - actualRating;
		//System.out.println("Delta"+delta);
		error += Math.pow(delta, 2);
		//System.out.println("error"+error);
		counter++;
	}
	
	public double getRootMeanSquareError() {
		return error/counter;
	}

}
