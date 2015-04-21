
/* 
 How to compile:
 javac -cp twitter4j-core-4.0.3.jar:jmathplot.jar TweetsExtractor.java FrequencyPlot.java
 java -cp .:twitter4j-core-4.0.3.jar:jmathplot.jar TweetsExtractor
 Note that it is assumed that the file twitter4j-core-4.0.3.jar is in the same folder as this file. If its not you have to specify
 the location of the twitter4j-core-4.0.3.jar
*/

//twitter4j docs: http://twitter4j.org/oldjavadocs/4.0.4-SNAPSHOT/index.html

import twitter4j.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


public class TweetsExtractor {
    
    Twitter twitter;
    ArrayList<TweetDate> tweetListDate = new ArrayList<TweetDate>();
    Map<Double, Double> frequency = new TreeMap<Double, Double>();
    double minDateHour = Double.MAX_VALUE;
    

	public TweetsExtractor(){
         twitter = TwitterFactory.getSingleton();
    }

    /**
     * Returns this Date's time value in hours.
     * Need to normalize later
     */
    private double getDateHour(Date date) {
        Calendar tweetCal = Calendar.getInstance();
        tweetCal.setTime(date);

        long seconds = tweetCal.getTimeInMillis()/1000;
        double hour = Math.floor(seconds/3600);
        
        return hour;
    }
    
    public void search(){
        int iter = 0;
        int LIMIT = 30; //
        try {
            
            //Query query = new Query("@gunthermarder"); //referencing person “gunthermarder”.
            Query query = new Query("#svpol");
            query.setCount(100); //Specifies the maximum number of responses per page, (100 is max)
            QueryResult result;
            do {
                result = twitter.search(query);
                //System.out.println("Result size: " + result.getCount());
                
                for (Status status : result.getTweets()) {
                    //System.out.println("@" + status.getUser().getScreenName() + ":" +  status.getText() + " ");
                    
                	double dateHour = getDateHour(status.getCreatedAt());
                	
                	if(dateHour < minDateHour)
                		minDateHour = dateHour;

                	if(frequency.containsKey(dateHour))
                		frequency.put(dateHour, frequency.get(dateHour) + 1.0);
                	else
                		frequency.put(dateHour, 1.0);
                	
                    /* This part works for frequency based on entire days
                    if(!tweetListDate.isEmpty()){
                        Calendar tweetCal = Calendar.getInstance();
                        tweetCal.setTime(tweetListDate.get(tweetListDate.size()-1).date);

                        Calendar statusCal = Calendar.getInstance();
                        statusCal.setTime(status.getCreatedAt());

                        if(tweetCal.get(Calendar.DAY_OF_MONTH) == statusCal.get(Calendar.DAY_OF_MONTH) ) { //Increment num of tweets for this date
                            tweetListDate.get(tweetListDate.size() - 1).numOfTweets++;
                        } else {
                            TweetDate d = new TweetDate();
                            d.numOfTweets = 1;
                            d.date = status.getCreatedAt();
                            tweetListDate.add(d);
                        }
                    } else {    //insert the first element
                        TweetDate d = new TweetDate();
                        d.numOfTweets = 1;
                        d.date = status.getCreatedAt();
                        tweetListDate.add(d);
                    }
                    */
                }
                iter++;
            } while ((query = result.nextQuery()) != null && iter < LIMIT);  //Set limit avoid exceeding the rate limit when requesting from the twitter API
            
            /*
            System.out.println("list size: " + tweetListDate.size());
            for (int i = 0; i < tweetListDate.size(); i++)
                System.out.println("Date:" + tweetListDate.get(i).date + " " + tweetListDate.get(i).numOfTweets);
            */
        }
        catch (TwitterException te) {
            System.out.println("Couldn't connect: " + te);
        }
        
    }
    
    
    

    public static void main(String[] args) {
        
        TweetsExtractor s = new TweetsExtractor();
        s.search();

        double[] x = new double[s.frequency.size()];
        double[] y = new double[s.frequency.size()];

        // Iterate trough hashmap
        Iterator it = s.frequency.entrySet().iterator();
        int i = 0;
        
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            
            x[i] = (double) pair.getKey() - s.minDateHour;
            y[i] = (double) pair.getValue();
            

            //System.out.println("x: " + x[i] + ", y: " + y[i]);
            
            i++;
            it.remove(); // avoids a ConcurrentModificationException
            
        }

        FrequencyPlot plot = new FrequencyPlot(x, y);
        
    }

}
		    
