
/* 
 How to compile:
 javac -cp twitter4j-core-4.0.3.jar TweetsExtractor.java
 java -cp .:twitter4j-core-4.0.3.jar TweetsExtractor
 Note that it is assumed that the file twitter4j-core-4.0.3.jar is in the same folder as this file. If its not you have to specify
 the location of the twitter4j-core-4.0.3.jar
*/

//twitter4j docs: http://twitter4j.org/oldjavadocs/4.0.4-SNAPSHOT/index.html

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.*;
import java.util.ArrayList;


public class TweetsExtractor {
    
    Twitter twitter;
    ArrayList<TweetDate> tweetListDate = new ArrayList<TweetDate>();
    
    
    
    public TweetsExtractor(){
         twitter = TwitterFactory.getSingleton();
    }
    
    public void search(){
        int iter = 0;
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
                    
                    if(!tweetListDate.isEmpty()){
                        if(tweetListDate.get(tweetListDate.size()-1).date == status.getCreatedAt() ) { //Increment num of tweets for this date
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
                }
                iter++;
            } while ((query = result.nextQuery()) != null && iter < 300);  //Set iter to a max limit to avoid exceeding the rate limit
            
            
            System.out.println("list size: " + tweetListDate.size());
            for (int i = 0; i < tweetListDate.size(); i++)
                System.out.println("Date:" + tweetListDate.get(i).date + " " + tweetListDate.get(i).numOfTweets);
            
        }
        catch (TwitterException te) {
            System.out.println("Couldn't connect: " + te);
        }
        
    }
    
    
    

    public static void main(String[] args) {
        
        TweetsExtractor s = new TweetsExtractor();
        s.search();
        
    }

}
		    
