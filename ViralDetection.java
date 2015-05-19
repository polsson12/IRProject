import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.LinkedList;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ViralDetection {

    public static void main(String args[]) {
        String filename = args[0];
        normaliseFrequency(filename);
    }

    /**
     * Returns this Date's time value in hours.
     * Need to normalize later
     */
    private static double getDateHour(Date date) {
        Calendar tweetCal = Calendar.getInstance();
        tweetCal.setTime(date);

        long seconds = tweetCal.getTimeInMillis()/1000;
        // System.out.println(Math.floor(seconds/3600));
        return Math.floor(seconds/3600);
    }

    /**
     * Normalizes, plots normalized graph and predicts when it looks 
     * like the topic is going viral.
     */
    private static void normaliseFrequency(String filename) {
        try {
            // Get the starting time of the event, needs to be put in manually
            /*String dateStr = "Tue Apr 21 20:45:00 CEST 2015";
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.ENGLISH);
            Date date = formatter.parse(dateStr);

            double startTime = getDateHour(date);*/

            // Prepare the graph plot
            ViralFrequencyPlot plot = new ViralFrequencyPlot();

            // Read data into a map
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String[] tweetDateFreq = null;
            String line = br.readLine();

            Map<Double, Double> inputData = new TreeMap<Double, Double>();
            
            double totalTweets = 0;

            boolean viralityFound = false;

            double startTime = Double.parseDouble(line.split(" ")[0]); // Get the first value in set as start time

            while (line != null) {
                tweetDateFreq = line.split(" ");
                inputData.put(Double.parseDouble(tweetDateFreq[0]) - startTime, Double.parseDouble(tweetDateFreq[1]));
                totalTweets += Double.parseDouble(tweetDateFreq[1]);
                line = br.readLine();
            }

            for(int j = 0; j < 2; j++) {

                if(j == 1) inputData = normalize(inputData);  // "Normalize" the data for the second plot

                // Put the data into double arrays for plotting
                double[] hours = new double[inputData.size()];
                double[] freqs = new double[inputData.size()];
                int i = 0;

                double prevValue = 0;

                for(Map.Entry<Double, Double> entry : inputData.entrySet()) {

                    if(j == 1 && !viralityFound) { // Detect virality
                        /* Virality is not predicted within the first 20 values of the 
                        set since those are needed to calibrate the detection.
                            Virality is then defined as when a value in the normalized set is 
                        50% bigger than the previous value.*/
                        if (i > 20 && entry.getValue() / prevValue > 1.5) {
                            System.out.println("Trending at t " + entry.getKey() + " hours");
                            viralityFound = true;
                        }
                    }

                    prevValue = entry.getValue();

                    hours[i] = entry.getKey();
                    freqs[i] = entry.getValue();

                    i++;
                }

                plot.addPlot("Plot" + j, hours, freqs);
            }

            plot.showPlot();  // Send it to be plotted

        } catch (FileNotFoundException e) {
            System.out.println("File doesnt exist");
        } catch (IOException e) {
            e.printStackTrace();
        }/* catch (ParseException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * Returns a "normalized" set, each value is the average of 
     * the highest and lowest value seen up to this point
     */
    private static Map<Double, Double> normalize(Map<Double, Double> inputData) {
        Map<Double, Double> f = new TreeMap<Double, Double>();
        
        double hiLoAverage = 0;
        double highest = 0;
        double lowest = 0;

        for(Map.Entry<Double, Double> entry : inputData.entrySet()) {

            if(entry.getValue() > highest) {
                highest = entry.getValue();
            } else if(entry.getValue() < lowest) {
                lowest = entry.getValue();
            }

            hiLoAverage = (highest + lowest) / 2;
            f.put(entry.getKey(), hiLoAverage);
        }
        return f;
    }
}

