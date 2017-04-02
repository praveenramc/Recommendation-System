import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author Pragadeesh
 * 
 * @description Predicts the vote P(a,j) where a is the active user and and the
 *              j is the movie for which the vote has to be predicted.
 * 
 */
public class Recommender {

	RecommenderHelper rh = new RecommenderHelper();
	MeanAbsoluteError mae = new MeanAbsoluteError();
	RootMeanSquare rms = new RootMeanSquare();
	UtilityFunctions uf = null;

	String testingFileName = null;

	public Recommender(String fileName) {
		this.testingFileName = fileName;
	}

	public void startPredicting() {

		System.out.println("Starting to predict the user votes...");
		uf = new UtilityFunctions();

		int movieID = 0;
		int uID = 0;
		double ratings = 0.0;

		String line = null;
		long startTime = System.currentTimeMillis();
		try {
			
			int linecount = 0;
			FileReader file = new FileReader(testingFileName);
			BufferedReader br = new BufferedReader(file);

			while ((line = br.readLine()) != null) {
				System.out.println("Line"+linecount++);
				StringTokenizer tokens = uf.readALine(line.toLowerCase());

				while (tokens.hasMoreTokens()) {
					movieID = Integer.parseInt(tokens.nextToken());
					uID = Integer.parseInt(tokens.nextToken());
					ratings = Double.parseDouble(tokens.nextToken());
				}

				calculatePredictedRating(uID, movieID, ratings);

			}

			long endTime = System.currentTimeMillis();
			System.out.println("Total time taken for Prediction: "
					+ (endTime - startTime) * 0.001 + " s.");

		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public double calculatePredictedRating(int userID, int movieID,
			double actualRating) {
		long startTime = System.currentTimeMillis();
		double predictedRating = 0.0;
		double activeAverage = RecommenderHelper.averageRating.get(userID);
		// System.out.println(activeAverage);
		ArrayList<Integer> list = rh.getUsersWhoSawTheMovie(movieID);

		double weightSum = 0;
		double summation = 0.0;
		double savedWeight = 0.0;
		
		for (int iterator = 0; iterator < list.size(); iterator++) {

			double userAverage = rh.getAverageVotesForTheUser(list
					.get(iterator));
			double userVote = rh.getRatingByUserForAMovie(list.get(iterator),
					movieID);
			
			if( RecommenderHelper.savedWeights.get(userID
					+ list.get(iterator)) == null ) {
				savedWeight = 0.6;
				weightSum += savedWeight;
			}
			
			else {
				savedWeight = RecommenderHelper.savedWeights.get(userID
						+ list.get(iterator));
				weightSum += savedWeight ;
			}
			
			summation += savedWeight * (userVote - userAverage);

		}

		predictedRating = activeAverage + (1 / weightSum) * summation;

		mae.addValues(predictedRating, actualRating);
		rms.addValues(predictedRating, actualRating);

		long endTime = System.currentTimeMillis();
		/*System.out.println("Total time taken for prediction is: "
				+ (endTime - startTime) * 0.001 + " s.");*/
		return predictedRating;
	}
	
	

}
