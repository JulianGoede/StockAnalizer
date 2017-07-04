import junit.framework.TestCase;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Created by Julian on 04.07.2017.
 * Create some random Stock data. Is mainly used
 * to check weather JSONObjects/Arrays work as intended.
 */
public class StockTest extends TestCase {

    private double[][] data;
    private JSONArray jsonData;

    @Override
    /*
     * Creates random data and stores it into the 2d array data.
     * Additionally creates a JSONArray to which we can apply our methods to
     */
    public void setUp() throws Exception {
        super.setUp();
        data = new double[4][28];
        StringBuilder sb = new StringBuilder(); // we build a string in json format
        Locale.setDefault(Locale.US); // so we don't mess up ',' and '.' when working with doubles in string
        sb.append("{\"data\":[");
        String s = "2017-2-%d";
        Random rng = new Random();
        for (int day = 1; day <= 28; day++) {
            // append some random data
            data[Stock.HIGH_COL - 1][day - 1] = rng.nextDouble() * 100;
            data[Stock.LOW_COL - 1][day - 1] = data[Stock.HIGH_COL - 1][day - 1] * (1 - rng.nextDouble()); // smaller then high
            data[Stock.LAST_COL - 1][day - 1] = rng.nextDouble() * 100;
            data[Stock.PREV_COL - 1][day - 1] = rng.nextDouble() * 100;
            sb.append(String.format("[\"%s\",%.3f,%.3f,%.3f,%.3f,%.3f]", String.format(s, day),
                    data[Stock.HIGH_COL - 1][day - 1], data[Stock.LOW_COL - 1][day - 1],
                    data[Stock.LAST_COL - 1][day - 1], data[Stock.PREV_COL - 1][day - 1],
                    rng.nextDouble() * 100));
            if (day < 28)
                sb.append(",");
            else
                sb.append("]}");
        }
        jsonData = new JSONObject(sb.toString()).getJSONArray("data");
    }

    public void testFindLowDate() throws Exception {
        double minVal = Arrays.stream(data[Stock.LOW_COL - 1]).min().getAsDouble();
        int day = IntStream.range(0, 27)
                .filter(i -> Double.compare(data[Stock.LOW_COL - 1][i], minVal) == 0)
                .findFirst().getAsInt() + 1;
        String expectedDay = String.format("2017-2-%d", day);
        assertEquals(expectedDay, Stock.findLowDate(jsonData));
        System.out.println(expectedDay);
    }

    public void testFindMaxDate() throws Exception {
        double maxVal = Arrays.stream(data[Stock.HIGH_COL - 1]).max().getAsDouble();
        int day = IntStream.range(0, 27)
                .filter(i -> Double.compare(data[Stock.HIGH_COL - 1][i], maxVal) == 0)
                .findFirst().getAsInt() + 1;
        String expectedDay = String.format("2017-2-%d", day);
        assertEquals(expectedDay, Stock.findMaxDate(jsonData));
    }

    public void testHighFluctuationDate() throws Exception {
        // store the fluctuation of consecutive days in an array
        double[] fluctuationArr = IntStream.range(0, 27)
                .mapToDouble(i -> Math.abs(data[Stock.LAST_COL - 1][i] - data[Stock.PREV_COL - 1][i])).toArray();
        double maxFluctuation = Arrays.stream(fluctuationArr).max().getAsDouble();
        int day = IntStream.range(0, 27)
                .filter(i -> Double.compare(fluctuationArr[i], maxFluctuation) == 0)
                .findFirst().getAsInt() + 1;
        String expectedDay = String.format("2017-2-%d", day);
        assertEquals(expectedDay, Stock.highFluctuationDate(jsonData));
    }

    public void testAverageStockPrice() throws Exception {
        double expectedAvg = Arrays.stream(data[Stock.LAST_COL - 1]).average().getAsDouble();
        assertEquals(expectedAvg, Stock.averageStockPrice(jsonData), 0.0001);
    }

}