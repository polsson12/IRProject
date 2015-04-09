
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


public class TweetsExtractor{
    
    Twitter twitter;
    
    
    
    public TweetsExtractor(){
         twitter = TwitterFactory.getSingleton();
    }
    
    public void search(){
        try {
            
            //Query query = new Query("@gunthermarder"); //referencing person “gunthermarder”.
            Query query = new Query("@from:gunthermarder"); //sent from person “gunthermarder”.
            query.setCount(100); //Specifies the maximum number of responses per page, (100 is max)
            QueryResult result = twitter.search(query);
            System.out.println("Result size: " + result.getCount());
            
            for (Status status : result.getTweets()) {
                System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
            }
            
        }
        catch (TwitterException te) {
            System.out.println("Couldn't connect: " + te);
        }
    
    }
    

    public static void main(String[] args) {
        
        TweetsExtractor s = new TweetsExtractor();
        ts.search();
        
    }

}
		    
