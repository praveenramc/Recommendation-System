import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

/* 
 * @author Pragadeesh 
 * 
 * @description
 * Reads the movie_titles.txt file and stores the Movie ID and
 * Movie Name in a HashMap
 * 
 */
public class MovieTitles {

	public static HashMap<Integer, String> movieTitles = new HashMap<Integer, String>();

	String fileName = null;

	MovieTitles(String fileName) {
		this.fileName = fileName;
	}

	public boolean createMovieNameList() {
		
		System.out.println("Creating movie list..");
		UtilityFunctions uf = new UtilityFunctions();
		long startTime = System.currentTimeMillis();
		try {

			FileReader file = new FileReader(fileName);
			BufferedReader br = new BufferedReader(file);

			String line = null;

			while ((line = br.readLine()) != null) {

				StringTokenizer tokens = uf.readALine(line.toLowerCase());

				int skipYearCounter = 0;
				
				int movieID = Integer.parseInt(tokens.nextToken());

				while (tokens.hasMoreTokens()) {
					
					skipYearCounter++;

					if (skipYearCounter == 1) {
						tokens.nextToken();
						continue;
					}

					String Movie = tokens.nextToken();

					if ( ! movieTitles.containsKey(movieID)) {
						movieTitles.put(movieID, Movie);
					}

				}

			}
			
			long endTime = System.currentTimeMillis();
			System.out.println("Total time taken for creating movie list: " + (endTime - startTime)*0.001
	                + " s.");

		} catch (FileNotFoundException fnfe) {
			fnfe.getMessage();

		} catch (IOException ioe) {
			ioe.getMessage();
		}
		
		//System.out.println("Size: "+movieTitles.size());
		return true;
	}
	
	public String getMovieName( int movieId ) {
		String movieName = null;
		
		if( movieTitles.containsKey(movieId)) {
			movieName = movieTitles.get(movieId);
		}
		
		return movieName;
	}
}
