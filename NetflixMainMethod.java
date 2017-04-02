import java.util.ArrayList;

/* 
 * @author Pragadeesh
 * 
 * @description
 * Starts the various process for Netflix Collaborative Filtering Algorithm
 * Movie Name in a HashMap
 * 
 */
public class NetflixMainMethod {

	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		
		String movieTitles = args[0];
		String trainingFileName = args[1];
		String testingFileName = args[2];
		
		MovieTitles mt = new MovieTitles(movieTitles);
		mt.createMovieNameList();
		
		RecommenderHelper rh = new RecommenderHelper(trainingFileName, testingFileName);
		rh.buildUseFulHashMaps();
		rh.createUniqueUserList();
		rh.calculateAverageRatingForUsers();
		rh.correlationCalculationForEachUser();
		System.out.println("Average rating for user 1744889: "+ rh.getAverageVotesForTheUser(7));
		
		//After running it for the first time uncomment the below line.
		//rh.deserializeWeights();
		
		long endTime = System.currentTimeMillis();
		System.out.println("Total time taken for executing is: " + (endTime - startTime)*0.001
                + " s.");
		
		Recommender r = new Recommender(args[2]);
		//System.out.println("Predicted Weight: "+r.calculatePredictedRating(2152856, 28, 4));
		r.startPredicting();
		System.out.println("Mean Error: "+new MeanAbsoluteError().getMeanAbsoluteError());
		System.out.println("Root Error: "+new RootMeanSquare().getRootMeanSquareError());
		//ArrayList<Integer> list = rh.getUsersWhoSawTheMovie(1104); 
		
		/*for( int i=0; i<list.size(); i++) {
			System.out.println(list.get(i));
		}*/
	}

}
