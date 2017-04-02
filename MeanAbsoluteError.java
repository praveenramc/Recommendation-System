
public class MeanAbsoluteError {
	
	public static double error = 0.0;
	public static int counter = 0;
	
	public void addValues(double predictedRating, double actualRating) {
		//System.out.println("mean delta"+Math.abs(predictedRating - actualRating));
		error += Math.abs(predictedRating - actualRating);
		//System.out.println("mean error"+error);
		counter++;
	}
	
	public double getMeanAbsoluteError() {
		return error/counter;
	}
}
