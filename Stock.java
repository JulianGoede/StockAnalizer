import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by Julian on 03.07.2017.
 * On the foundation of org.json from https://github.com/stleary/JSON-java
 * this class parses json files from url and analyzes stock data
 */
public class Stock {

    private static final String test_url = "https://www.quandl.com/api/v3/datasets/SSE/HYQ.json?start_date=2017-01-01&end_date=2017-01-31";

    // column ids
    public static final int DATE_COL = 0;
    public static final int HIGH_COL = 1;
    public static final int LOW_COL = 2;
    public static final int LAST_COL = 3;
    public static final int PREV_COL = 4;
    public static final int VOL_COL = 5;


    /**
     * Loads the passed url link and creates a JSONObject out of it.
     *
     * @param link url of the website
     * @return a JSONObject that stores the data of the URL
     */
    public static JSONObject loadURL(String link) throws IOException {
        URL url = new URL(link);
        // read data from url
        Scanner in = new Scanner(url.openStream());
        StringBuilder sb = new StringBuilder();
        while (in.hasNext())
            sb.append(in.nextLine());
        in.close();

        // now we stored the raw string into a handy jObject
        return new JSONObject(sb.toString());
    }

    /**
     * Returns the date of the lowest stock price.
     *
     * @param data stock data
     */
    public static String findLowDate(JSONArray data) {
        double low = Double.MAX_VALUE; // with the assumption that stock data doesn't exceed double range
        int lowDay = -1;
        int day = 0;
        Iterator<Object> it = data.iterator();
        while (it.hasNext()) {
            // iterate through each day
            JSONArray dayData = (JSONArray) it.next();
            double lowPrice = dayData.getDouble(LOW_COL);
            if (lowPrice < low) {
                lowDay = day;
                low = lowPrice;
            }
            day++;
        }
        return ((JSONArray) data.get(lowDay)).get(DATE_COL).toString();
    }

    /**
     * Returns the date of the highest stock price.
     * Note that this method makes use of lambda expressions which makes the code much more concise.
     */
    public static String findMaxDate(JSONArray data) {
        // find the row (day) which maximizes the column 1 ("high")
        ArrayList topDay = (ArrayList) data.toList().stream()
                .max(Comparator.comparingDouble(value -> (double) ((ArrayList) value).get(HIGH_COL)))
                .get();
        return topDay.get(DATE_COL).toString();
    }

    /**
     * Returns the date of the day with the highest deviation to the last days stock price.
     */
    public static String highFluctuationDate(JSONArray data) {
        ArrayList highFluctuationDate = (ArrayList) data.toList().stream()
                .max(Comparator.comparingDouble(value ->
                        Math.abs((double) ((ArrayList) value).get(LAST_COL) - (double) ((ArrayList) value).get(PREV_COL))))
                .get();
        return highFluctuationDate.get(DATE_COL).toString();
    }

    /*
    * Returns the average stock price at the end of the day.
     */
    public static double averageStockPrice(JSONArray data) {
        return data.toList().stream()
                .mapToDouble(value -> (double) ((ArrayList) value).get(LAST_COL))
                .sum() / data.length();
    }

    public static void main(String[] args) {
        try {
            JSONObject jObject = loadURL(args.length == 0 ? test_url : args[0]);
            JSONObject dataset = (JSONObject) jObject.get("dataset");
            JSONArray data = dataset.getJSONArray("data");

            System.out.println(String.format("Date of the lowest stock index: %s", findLowDate(data)));
            System.out.println(String.format("Date of the highest stock index: %s", findMaxDate(data)));
            System.out.println(String.format("Date with the highest deviation from the previous day: %s",
                    highFluctuationDate(data)));

            Locale.setDefault(Locale.US);
            System.out.println(String.format("Average stock index at the end of the day: %.3f",
                    averageStockPrice(data)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
