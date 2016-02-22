package weather.climatenews.Data;

import android.database.Cursor;

import java.util.HashMap;
import java.util.Map;

import weather.climatenews.ForecastFragment;

/**
 *
 * @author Ashish 
 */
public class WeatherProvider {
    public static Map<Integer, String> weatherIcon = new HashMap<Integer, String>();
    public static Map<Integer, String> maxTemp = new HashMap<Integer, String>();
    public static Map<Integer, String> minTemp = new HashMap<Integer, String>();
    public static Map<Integer, String> description = new HashMap<Integer, String>();
    public static Map<Integer, String> windSpeed = new HashMap<Integer, String>();
    public static Map<Integer, String> pressure = new HashMap<Integer, String>();
    public static Map<Integer, String> humidity = new HashMap<Integer, String>();
    public static Map<Integer, String> cloud = new HashMap<Integer, String>();
    public static Map<Integer, String> day = new HashMap<Integer, String>();
    public WeatherProvider(){
    }
}
