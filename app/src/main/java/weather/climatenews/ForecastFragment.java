package weather.climatenews;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.logging.Logger;


import weather.climatenews.Data.DatabaseHelper;
import weather.climatenews.Data.WeatherProvider;

/**
 *
 * @author Ashish
 */

public class ForecastFragment extends Fragment {
    private Dialog loading;

    static Map<String, String> weatherIcon = new HashMap<String, String>();
    static Map<String, String> cloud = new HashMap<String, String>();
    static Map<String, String> pressure = new HashMap<String, String>();
    static Map<String, String> humidity = new HashMap<String, String>();
    static Map<String, String> speed = new HashMap<String, String>();
    static Map<String, String> city = new HashMap<String, String>();
    static String COL_WEATHER_ICON[], COL_MAX_TEMP[], COL_MIN_TEMP[], COL_DESCRIPTION[], COL_WIND_SPEED[], COL_PRESSURE[], COL_HUMIDITY[], COL_CLOUD[];

    String location = "";
    private ArrayAdapter<String> mForecastAdapter;
    FetchWeatherTask fetchWeatherTask = new FetchWeatherTask();
    private String[] weatherWeekData = {"Waiting For Cloud, ...Please Refresh"};
    DatabaseHelper databaseHelper;
    Cursor weatherCursor;

    private void saveLocation(String data, Context ctx) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(ctx.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            Log.v("Done:", "Done");
        } catch (IOException e) {
            Log.e("ExceptionError", "File write failed: " + e.toString());
        }
    }

    private void saveCity(String data, Context ctx) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(ctx.openFileOutput("weatherCity.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            Log.v("Done:", "Done");
        } catch (IOException e) {
            Log.e("ExceptionError", "File write failed: " + e.toString());
        }
    }
    private String readCity(Context ctx) {
        String ret = "";
        try {
            InputStream inputStream = ctx.openFileInput("weatherCity.txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            return "not";
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return ret;
    }


    private String readLocation(Context ctx) {
        String ret = "";
        try {
            InputStream inputStream = ctx.openFileInput("config.txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            return "not";
         } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return ret;
    }

/*    private boolean initWeatherData(Cursor res){
        int i=0;
        if(res.getCount() == 0){
            return false;
        }

        while(res.moveToNext()){
            COL_WEATHER_ICON[i] = res.getString(1);
            COL_MAX_TEMP[i] = res.getString(2);
            COL_MIN_TEMP[i] = res.getString(3);
            COL_DESCRIPTION[i] = res.getString(4);
            COL_WIND_SPEED[i] = res.getString(5);
            COL_PRESSURE[i] = res.getString(6);
            COL_HUMIDITY[i] = res.getString(7);
            COL_CLOUD[i] = res.getString(8);
            i++;
        }
        return true;
    }*/

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedIntanceState) {
        super.onCreate(savedIntanceState);
        databaseHelper = new DatabaseHelper(getContext());
        weatherCursor = databaseHelper.getAllData();
       // weatherCursor.moveToNext();

        try{
            if(weatherCursor.getCount() != 0){
                int i=0;
                while(weatherCursor.moveToNext()){
                    WeatherProvider.weatherIcon.put(i, weatherCursor.getString(1));
                    WeatherProvider.maxTemp.put(i, weatherCursor.getString(2));
                    WeatherProvider.minTemp.put(i, weatherCursor.getString(3));
                    WeatherProvider.description.put(i, weatherCursor.getString(4));
                    WeatherProvider.windSpeed.put(i, weatherCursor.getString(5));
                    WeatherProvider.pressure.put(i, weatherCursor.getString(6));
                    WeatherProvider.humidity.put(i, weatherCursor.getString(7));
                    WeatherProvider.cloud.put(i, weatherCursor.getString(8));
                    WeatherProvider.day.put(i, weatherCursor.getString(9));
                    i++;
                }
            }
        }catch (Exception e){}
        //TextView adView = findViewById(R.id.ad_view);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        MainActivity.stat = false;
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            FetchWeatherTask fetchWeatherTask = new FetchWeatherTask();
            String Location = readLocation(getContext());
            if (Location.equals("not")) {
                saveLocation("141007", getContext());
            }
            fetchWeatherTask.execute(Location);
            MainActivity.stat = true;
            //saveLocation("141007",this.getContext());
            Toast.makeText(getContext(), readLocation(this.getContext()), Toast.LENGTH_LONG);
            return true;
        }
        if (id == R.id.action_location) {
            final FetchWeatherTask fetchWeatherTask = new FetchWeatherTask();
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("Location Setting");
            alert.setMessage("Enter Postal Code:");

            // Set an EditText view to get user input
            final EditText input = new EditText(getContext());
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = input.getText().toString();
                    if (value.length() != 6) {
                        Snackbar.make(getView(), "Please type Valid PIN-CODE.", Snackbar.LENGTH_LONG)
                                .setActionTextColor(Color.YELLOW).setAction("Action", null).show();
                        return;
                    }
                    saveLocation(value, getContext());
                    Toast.makeText(getContext(), "Location Saved, Just Refreshing...", Toast.LENGTH_LONG).show();
                    MainActivity.stat = true;
                    fetchWeatherTask.execute(value);
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    return;
                }
            });
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String Location = readLocation(getContext());
        String localCity = readCity(getContext());
        if (Location.equals("not")) {
            saveLocation("141007", getContext());
        }

        if(!localCity.equals("not")){city.put("city",localCity);}else{city.put("city",Location);}
        fetchWeatherTask.execute(Location);
        //Toast.makeText(getActivity(),"Refreshing Weather News",Toast.LENGTH_LONG).show();
        //AdView mAdView = (AdView) rootView.findViewById(R.id.ad_view);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.loadAd(adRequest);

     /*   View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        List<String> weekForecast = new ArrayList<String>(Arrays.asList(weatherWeekData));//asList(forecastJsonStr));
        mForecastAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.list_item_forecast, R.id.list_item_textview, weekForecast);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String forecast = mForecastAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                if (forecast.indexOf("...Please Refresh") == -1) {
                    intent.putExtra(Intent.EXTRA_TEXT, forecast);
                    startActivity(intent);
                } else {
                    Snackbar.make(view, "Firstly Ensure proper INTERNET CONNECTION then Refresh from Settings", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.YELLOW).setAction("Action", null).show();
                }
            }
        });*/
        if(databaseHelper.getAllData().getCount()==0){
            return inflater.inflate(R.layout.nodata, container, false);
        }

        View rootView = inflater.inflate(R.layout.list_item_view, container, false);
        TextView dateView[] = new TextView[7];
        TextView[] maxView = new TextView[7];
        TextView minView[] = new TextView[7];
        TextView desView[] = new TextView[7];
        ImageView iconView[] = new ImageView[7];
        for(int i = 0;i<7;i++) {
            dateView[i] = (TextView) rootView.findViewById(getResources().getIdentifier("list_item_date_textview_d" + (i + 1), "id", "weather.climatenews"));
            maxView[i] = (TextView) rootView.findViewById(getResources().getIdentifier("list_item_max_textview_d" + (i + 1), "id", "weather.climatenews"));
            minView[i] = (TextView) rootView.findViewById(getResources().getIdentifier("list_item_min_textview_d" + (i + 1), "id", "weather.climatenews"));
            desView[i] = (TextView) rootView.findViewById(getResources().getIdentifier("list_item_desc_textview_d" + (i + 1), "id", "weather.climatenews"));
            iconView[i] = (ImageView) rootView.findViewById(getResources().getIdentifier("list_item_icon_d" + (i + 1), "id", "weather.climatenews"));
        }

        try{
            String date="";
        for(int i=0;i<7;i++){
            if(i==0){date = "Today,"+WeatherProvider.day.get(i).substring(4);}
            else if(i==1){date = "Tomorrow";}
            else {date = WeatherProvider.day.get(i);}
            dateView[i].setText(date);
            maxView[i].setText(WeatherProvider.maxTemp.get(i));
            minView[i].setText(WeatherProvider.minTemp.get(i));
            desView[i].setText(WeatherProvider.description.get(i));
            int imageId = getResources().getIdentifier("c"+WeatherProvider.weatherIcon.get(i),"drawable","weather.climatenews");
            iconView[i].setImageResource(imageId);
        }}catch(Exception ignored){}
        return rootView;
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String[] > {
      //  String COL_WEATHER_ICON[], COL_MAX_TEMP[], COL_MIN_TEMP[], COL_DESCRIPTION[], COL_WIND_SPEED[], COL_PRESSURE[], COL_HUMIDITY[], COL_CLOUD[];
        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        private String getReadableDateString(long time) {
// Because the API returns a unix timestamp (measured in seconds),
// it must be converted to milliseconds in order to be converted to valid date.
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            return shortenedDateFormat.format(time);
        }

        /**
         * Prepare the weather high/lows for presentation.
         */
        private String formatHighLows(double high, double low) {
// For presentation, assume the user doesn't care about tenths of a degree.
            long roundedHigh = Math.round(high);
            long roundedLow = Math.round(low);
            String highLowStr = roundedHigh + "/" + roundedLow;
            return highLowStr;
        }

        private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
                throws JSONException {
// These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "list";
            final String OWM_WEATHER = "weather";
            final String OWM_TEMPERATURE = "temp";
            final String OWM_ICON = "icon";
            final String OWM_MAX = "max";
            final String OWM_MIN = "min";
            final String OWM_DESCRIPTION = "description";
            final String OWM_CITY = "name";
            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);
//            city = forecastJson.getJSONObject("city").getString("name");
            saveCity(forecastJson.getJSONObject("city").getString("name"),getContext());
// OWM returns daily forecasts based upon the local time of the city that is being
// asked for, which means that we need to know the GMT offset to translate this data
// properly.
// Since this data is also sent in-order and the first day is always the
// current day, we're going to take advantage of that to get a nice
// normalized UTC date for all of our weather.
            Time dayTime = new Time();
            dayTime.setToNow();
// we start at the day returned by local time. Otherwise this is a mess.
            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);
// now we work exclusively in UTC
            dayTime = new Time();
            String[] resultStrs = new String[numDays];
            for (int i = 0; i < weatherArray.length(); i++) {
// For now, using the format "Day, description, hi/low"

                String day;
                String description;
                String icon;
                String highAndLow;
// Get the JSON object representing the day
                JSONObject dayForecast = weatherArray.getJSONObject(i);
                //speed = dayForecast.getString("speed");
// The date/time is returned as a long. We need to convert that
// into something human-readable, since most people won't read "1400356800" as
// "this saturday".
                long dateTime;
// Cheating to convert this to UTC time, which is what we want anyhow
                dateTime = dayTime.setJulianDay(julianStartDay + i);
                day = getReadableDateString(dateTime);
                String dayN = day.substring(0,3)+","+day.substring(3);
// description is in a child array called "weather", which is 1 element long.
                JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                description = weatherObject.getString(OWM_DESCRIPTION);
                icon = weatherObject.getString(OWM_ICON);
                //city = dayForecast.get();
// Temperatures are in a child object called "temp". Try not to name variables
// "temp" when working with temperature. It confuses everybody.
                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                double high = temperatureObject.getDouble(OWM_MAX);
                double low = temperatureObject.getDouble(OWM_MIN);
                highAndLow = formatHighLows(high, low);
                resultStrs[i] = day + " , " + description + " - " + highAndLow;
                String currentDay = day.substring(0, 3);
                weatherIcon.put(currentDay, icon);
                speed.put(currentDay, dayForecast.getString("speed"));
                cloud.put(currentDay, dayForecast.getString("clouds"));
                pressure.put(currentDay, dayForecast.getString("pressure"));
                humidity.put(currentDay, dayForecast.getString("humidity"));

                int highInt = (int) high;
                int lowInt = (int) low;
                databaseHelper.insertData(i,icon, String.valueOf(highInt)+"°", String.valueOf(lowInt)+"°", description, dayForecast.getString("speed"), dayForecast.getString("pressure"), dayForecast.getString("humidity"), dayForecast.getString("clouds"), dayN);

            }

            return resultStrs;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(getContext(), "", "Loading Weather...");
        }

        @Override
        protected String[] doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            String format = "json";
            String units = "metric";
            String appid = "74f924e50a25ecea26d08fdf300833c4";
            String numDays = "7";

            try {
                final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "mode";
                final String UNIT_PARAM = "units";
                final String DAYS_PARAM = "cnt";
                final String APPID_PARAM = "APPID";
                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNIT_PARAM, units)
                        .appendQueryParameter(APPID_PARAM, appid)
                        .appendQueryParameter(DAYS_PARAM, numDays).build();
                URL url = new URL(builtUri.toString());
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + ",\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                loading.dismiss();
//                Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                // Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    loading.dismiss();
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            try {
                weatherWeekData = getWeatherDataFromJson(forecastJsonStr, Integer.parseInt(numDays));
                loading.dismiss();
                return params;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String[] result) {
            loading.dismiss();
            if (result != null) {
                if(MainActivity.stat){
                    city.put("city", readCity(getContext()));
                    getFragmentManager().beginTransaction().replace(R.id.container,new ForecastFragment()).commit();
                    MainActivity.stat = false;
                    //loading.dismiss();
                }

            }
        }
    }
}