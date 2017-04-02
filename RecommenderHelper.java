import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author Pragadeesh
 * 
 */
public class RecommenderHelper {

	String trainingFileName = null;
	String testingFileName = null;

	/*
	 * @variable averageRating: Stores the rating for a given user Avg.rating =
	 * # of Votes / # of movies voted
	 */
	public static HashMap<Integer, Double> averageRating = new HashMap<Integer, Double>();

	/*
	 * @variable weight: Stores the correlation value for a given user with
	 * respect to other users
	 */

	public static HashMap<Integer, Double> savedWeights = new HashMap<Integer, Double>();

	/*
	 * @variable userMovieRating: Stores the rating of a movie by a given user
	 */
	public static HashMap<String, Double> userMovieRating = new HashMap<String, Double>();

	public static HashMap<Integer, ArrayList<Integer>> movieListForAUser = new HashMap<Integer, ArrayList<Integer>>();
	
	public static HashMap<Integer, ArrayList<Integer>> userListForAMovie = new HashMap<Integer, ArrayList<Integer>>();

	public static ArrayList<Integer> userList = new ArrayList<Integer>();

	UtilityFunctions uf;
	
	RecommenderHelper() {
		
	}
	RecommenderHelper(String trainingFileName, String testingFileName) {
		this.trainingFileName = trainingFileName;
		this.testingFileName = testingFileName;
	}

	/*
	 * This method builds hashmaps which can be helpful in reducing the
	 * computation time.
	 * 
	 * It build the following hashmaps 1. Unique User List 2. Movie Ratings by
	 * each user for a given
	 */
	public void buildUseFulHashMaps() {

		uf = new UtilityFunctions();

		String movieID = null;
		String uID = null;
		double ratings = 0.0;

		String line = null;
		long startTime = System.currentTimeMillis();
		try {
			FileReader file = new FileReader(trainingFileName);
			BufferedReader br = new BufferedReader(file);

			while ((line = br.readLine()) != null) {

				StringTokenizer tokens = uf.readALine(line.toLowerCase());

				while (tokens.hasMoreTokens()) {
					movieID = tokens.nextToken();
					uID = tokens.nextToken();
					ratings = Double.parseDouble(tokens.nextToken());
				}

				buildUserRatings(uID, movieID, ratings);
				createMoviesSeenbyTheUser(Integer.parseInt(uID),
						Integer.parseInt(movieID));
				createUsersWhoSawTheMovie(Integer.parseInt(movieID),Integer.parseInt(uID) );

			}

			long endTime = System.currentTimeMillis();
			System.out
					.println("Total time taken for creating User List, User Rating and movies seen by the user: "
							+ (endTime - startTime) * 0.001 + " s.");
			/*
			 * System.out.println("UserList Size: " + userList.size());
			 * System.out.println("User Movie Rating: " +
			 * userMovieRating.size());
			 */

			/*
			 * Iterator iterator = averageRating.keySet().iterator(); int i=0;
			 * while (iterator.hasNext()) { i++; String key =
			 * iterator.next().toString(); String value =
			 * averageRating.get(key).toString();
			 * 
			 * System.out.println(key + " " + value); if( i >= 10 ) break; }
			 */
			// System.out.println("Rating for 8,1646405: "+
			// userMovieRating.get("16464058"));
		}

		catch (FileNotFoundException fnfe) {
			fnfe.getMessage();
		} catch (IOException ioe) {
			ioe.getMessage();
		}

		uf = null;

	}

	public void buildUserRatings(String uID, String movieID, double ratings) {
		userMovieRating.put(uID + movieID, ratings);
	}

	public void createUniqueUserList() {

		System.out.println("Creating user list...");
		uf = new UtilityFunctions();

		String movieID = null;
		String uID = null;
		double ratings = 0.0;

		String line = null;
		long startTime = System.currentTimeMillis();
		try {
			FileReader file = new FileReader(testingFileName);
			BufferedReader br = new BufferedReader(file);

			while ((line = br.readLine()) != null) {

				StringTokenizer tokens = uf.readALine(line.toLowerCase());

				while (tokens.hasMoreTokens()) {
					movieID = tokens.nextToken();
					uID = tokens.nextToken();
					ratings = Double.parseDouble(tokens.nextToken());
				}

				if (!userList.contains(Integer.parseInt(uID))) {
					userList.add(Integer.parseInt(uID));
				}

			}

			System.out.println("Userlist size:" + userList.size());
			long endTime = System.currentTimeMillis();
			System.out.println("Total time taken for creating User List: "
					+ (endTime - startTime) * 0.001 + " s.");
			/*
			 * System.out.println("UserList Size: " + userList.size());
			 * System.out.println("User Movie Rating: " +
			 * userMovieRating.size());
			 */

			/*
			 * Iterator iterator = averageRating.keySet().iterator(); int i=0;
			 * while (iterator.hasNext()) { i++; String key =
			 * iterator.next().toString(); String value =
			 * averageRating.get(key).toString();
			 * 
			 * System.out.println(key + " " + value); if( i >= 10 ) break; }
			 */
			// System.out.println("Rating for 8,1646405: "+
			// userMovieRating.get("16464058"));
		}

		catch (FileNotFoundException fnfe) {
			fnfe.getMessage();
		} catch (IOException ioe) {
			ioe.getMessage();
		}

		uf = null;

	}

	public void calculateAverageRatingForUsers() {

		UtilityFunctions uf = new UtilityFunctions();
		System.out
				.println("Creating the mean vote for every user in the list...");
		uf = new UtilityFunctions();

		HashMap<Integer, Double> noOfVotes = new HashMap<Integer, Double>();
		HashMap<Integer, Integer> moviesVoted = new HashMap<Integer, Integer>();

		try {

			FileReader file = new FileReader(trainingFileName);
			BufferedReader br = new BufferedReader(file);

			String line = null;

			long startTime = System.currentTimeMillis();
			while ((line = br.readLine()) != null) {

				StringTokenizer tokens = uf.readALine(line.toLowerCase());

				while (tokens.hasMoreTokens()) {
					tokens.nextToken();
					int userID = Integer.parseInt(tokens.nextToken());
					double vote = Double.parseDouble(tokens.nextToken());

					if (noOfVotes.containsKey(userID)) {
						noOfVotes.put(userID, noOfVotes.get(userID) + vote);
						moviesVoted.put(userID, moviesVoted.get(userID) + 1);

					}

					else {
						noOfVotes.put(userID, vote);
						moviesVoted.put(userID, 1);
					}

				}

			}

			Iterator iterator = noOfVotes.keySet().iterator();

			while (iterator.hasNext()) {

				String key = iterator.next().toString();
				String value = noOfVotes.get(Integer.parseInt(key)).toString();
				averageRating.put(
						Integer.parseInt(key),
						Double.parseDouble(value)
								/ moviesVoted.get(Integer.parseInt(key)));

			}

			iterator = noOfVotes.keySet().iterator();

			int i = 0;
			while (iterator.hasNext()) {
				i++;
				String key = iterator.next().toString();
				String value = averageRating.get(Integer.parseInt(key))
						.toString();

				System.out.println(key + " " + value);
				if (i >= 10)
					break;
			}
			long endTime = System.currentTimeMillis();

			// System.out.println("Average Rating:"+ averageRating.size());
			System.out.println("Total time for calculating average: "
					+ (endTime - startTime) * 0.001 + " s.");
		} catch (FileNotFoundException fnfe) {
			fnfe.getMessage();

		} catch (IOException ioe) {
			ioe.getMessage();
		}

		/*
		 * Iterator iterator = averageRating.keySet().iterator(); int i=0; while
		 * (iterator.hasNext()) { i++; String key = iterator.next().toString();
		 * String value = averageRating.get(Integer.parseInt(key)).toString();
		 * 
		 * System.out.println(key + " " + value); if( i >= 10 ) break; }
		 */

	}

	public double getAverageVotesForTheUser(int userID) {
		double average = 0.0;

		if (averageRating.containsKey(userID)) {
			average = averageRating.get(userID);
		}

		return average;
	}

	public ArrayList getUsersWhoSawTheMovie(int tragetMovieId) {
		ArrayList<Integer> userList = new ArrayList<Integer>();

		if (userListForAMovie.containsKey(tragetMovieId)) {
			userList = userListForAMovie.get(tragetMovieId);
		}

		else {
			userList = null;
		}
		return userList;
	}

	public void correlationCalculationForEachUser() {
		
		HashMap<Integer, Double> weight = new HashMap<Integer, Double>();
		System.out.println("Calculating correlation factor for each user..");

		long startTime = System.currentTimeMillis();

		for (int activeUser = 0; activeUser < userList.size(); activeUser++) {
			for (int targetUser = 1; targetUser < userList.size(); targetUser++) {
				/*
				 * System.out.println("Calculating correlation for " +
				 * userList.get(activeUser) + " " + userList.get(targetUser));
				 */
				if (userList.get(activeUser) == userList.get(targetUser)) {
					continue;
				}

				if (! weight.containsKey(userList.get(activeUser)
						+ userList.get(targetUser))) {
					double correlation = weightCalculation(userList.get(activeUser),
							userList.get(targetUser));
					weight.put(userList.get(activeUser)+userList.get(targetUser), correlation);
				}

			}
		}

		//serializeWeights(weight);

		long endTime = System.currentTimeMillis();
		System.out
				.println("Time taken for calculating correlation factor for each user: "
						+ (endTime - startTime) * 0.001 + " s.");
	}

	public double weightCalculation(int activeUser, int targetUser) {

		// double defaultRating = 0.0;
		double numeratorSum = 0.0;
		double bottomSumActive = 0.0;
		double bottomSumTarget = 0.0;

		ArrayList<Integer> movieList = new ArrayList<Integer>();
		movieList = getMoviesSeenByTheUser(activeUser);

		double meanOfActiveUser = averageRating.get(activeUser);
		double meanOfTargetUser = averageRating.get(targetUser);

		for (int iterator = 0; iterator < movieList.size(); iterator++) {
			double activeRating = (getRatingByUserForAMovie(activeUser,
					movieList.get(iterator)) - meanOfActiveUser);
			double targetRating = (getRatingByUserForAMovie(targetUser,
					movieList.get(iterator)) - meanOfTargetUser);

			numeratorSum += activeRating * targetRating;
			bottomSumActive += Math.pow(activeRating, 2);
			bottomSumTarget += Math.pow(targetRating, 2);
		}

		if (bottomSumActive != 0 && bottomSumTarget != 0) {
			/*
			 * System.out.println((numeratorSum) / Math.sqrt(bottomSumActive *
			 * bottomSumTarget));
			 */
			return (numeratorSum)
					/ Math.sqrt(bottomSumActive * bottomSumTarget);
		} else
			return 1;
	}

	public void createUsersWhoSawTheMovie(int movieID, int userID) {

		if (userListForAMovie.containsKey(movieID)) {
			ArrayList<Integer> currentList = new ArrayList<Integer>();
			currentList = userListForAMovie.get(movieID);
			currentList.add(userID);
			userListForAMovie.put(movieID, currentList);
		} else {
			ArrayList<Integer> userId = new ArrayList<Integer>();
			userId.add(userID);
			userListForAMovie.put(movieID, userId);
		}
	}
	
	public void createMoviesSeenbyTheUser(int userID, int movieID) {

		if (movieListForAUser.containsKey(userID)) {
			ArrayList<Integer> currentList = new ArrayList<Integer>();
			currentList = movieListForAUser.get(userID);
			currentList.add(movieID);
			movieListForAUser.put(userID, currentList);
		} else {
			ArrayList<Integer> movieId = new ArrayList<Integer>();
			movieId.add(movieID);
			movieListForAUser.put(userID, movieId);
		}
	}

	public ArrayList getMoviesSeenByTheUser(int userID) {
		ArrayList<Integer> movieList = new ArrayList<Integer>();

		if (movieListForAUser.containsKey(userID)) {
			movieList = movieListForAUser.get(userID);
		}

		else {
			movieList = null;
		}
		return movieList;

	}

	private void serializeWeights(HashMap weigths) {
		
		try {
			FileOutputStream fos = new FileOutputStream("weightMap.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(weigths);
			oos.close();
		} catch (Exception e) {

		} finally {

		}

	}
	
	public void deserializeWeights() {
		
		System.out.println("Before deserialization: "+ savedWeights.size() );
		long startTime = System.currentTimeMillis();
		try {
			 FileInputStream fis = new FileInputStream("weightMap.ser");
		     ObjectInputStream ois = new ObjectInputStream(fis);
		     savedWeights = (HashMap) ois.readObject();
		     ois.close();
		    // System.out.println(savedWeights);
		     System.out.println("After deserialization: "+ savedWeights.size() );
		     long endTime = System.currentTimeMillis();
				System.out.println("Total time taken for executing is: " + (endTime - startTime)*0.001
		                + " s.");
		} catch( Exception e ) {
			e.getMessage();
		}
	}

	public double getRatingByUserForAMovie(int userID, int movieID) {
		double rating = 0.0;
		String umID = Integer.toString(userID) + Integer.toString(movieID);
		if (userMovieRating.containsKey(umID)) {
			rating = userMovieRating.get(umID);
		} else {
			rating = 1.0;
		}
		return rating;

	}
	
	public HashMap getSavedWeights() {
		return savedWeights;
	}
	
	public ArrayList getUserList() {
		return userList;
	}
}
