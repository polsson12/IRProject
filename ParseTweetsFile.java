import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


final public class ParseTweetsFile {

	private Map<Double, Double> frequency = new TreeMap<Double, Double>();
	
	public ParseTweetsFile(String filename)
	{
		System.out.println("Reading file...");
		
		try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
	        String tweet = br.readLine();
	        
	        while (tweet != null) {

	        	// Append next row to tweet if tweet is multirow
		        while (tweet.indexOf("TweetId3") == -1) {
		        	tweet = tweet + " " + br.readLine();
		        }
	        	
		        // Parse each line
		        parseTweet(tweet);
		        
	        	tweet = br.readLine();
	        }
	        
	    } catch (FileNotFoundException e) {
	    	System.out.println("File doesnt exist");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("Wrong date format");
		}
		
		System.out.println("Done reading file. Writing results...");
		
		saveResult(filename);
		
		System.out.println("Done!");
	}
	
	private void saveResult(String filename)
	{
		// Iterate trough hashmap
        Iterator it = frequency.entrySet().iterator();
        
        try {
			PrintWriter writer = new PrintWriter("results_" + filename, "UTF-8");
			PrintWriter writer1 = new PrintWriter("results_date_" + filename, "UTF-8");
			
	        while (it.hasNext()) {
	        	Map.Entry pair = (Map.Entry)it.next();
	        	//System.out.println();
	        	Date date= new Date((long) ((double)pair.getKey() * 3600 * 1000));
	        	
	        	writer.println(pair.getKey() + " " + pair.getValue());
	        	writer1.println(date + "-" + pair.getValue());
	        }
	        
	        writer.close();
	        writer1.close();
			
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        
	}

	/**
	 * Parses the tweet and adds it to the hashmap
	 * @throws ParseException 
	 */
	private void parseTweet(String tweet) throws ParseException
	{
		String dateStr = getDateStr(tweet);
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
		Date date = formatter.parse(dateStr);
		
		double dateHour = getDateHour(date);
		
		increaseFrequency(dateHour);
	}
	
	private String getDateStr(String tweet)
	{
		int start = tweet.indexOf("TweetDate2:") + ("TweetDate2:").length();
		int end = tweet.indexOf("TweetId3:");
		
		return tweet.substring(start, end);
	}

	
	private void increaseFrequency(double dateHour)
	{
		if(frequency.containsKey(dateHour))
            frequency.put(dateHour, frequency.get(dateHour) + 1.0);
        else
            frequency.put(dateHour, 1.0);
	}

	/**
     * Returns this Date's time value in hours.
     * Need to normalize later
     */
    private double getDateHour(Date date) {
        Calendar tweetCal = Calendar.getInstance();
        tweetCal.setTime(date);
       
        
        long seconds = tweetCal.getTimeInMillis()/1000;
       // System.out.println(Math.floor(seconds/3600));
        return Math.floor(seconds/3600);
        
    }
	
	public static void main(String[] args)
	{
		String filename = "#earthquakeNepal.txt";
		new ParseTweetsFile(filename);
	}

}
