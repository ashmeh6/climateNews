package weather.climatenews;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import weather.climatenews.Data.DatabaseHelper;
import weather.climatenews.Data.WeatherProvider;

import static java.lang.String.*;

/**
 *
 * @author Ashish 
 */
public class DetailActivityFragment extends Fragment {

    public static Bitmap weatherImg;
    private String day;
    private String date;
    private String description;
    private String maxTemp;
    private String minTemp;
    DatabaseHelper dbHelper;
    public DetailActivityFragment() {
 //       dbHelper = new DatabaseHelper(getContext());
//        Cursor res = dbHelper.getAllData();
  /*      {
            int i=0;
//        DatabaseHelper db = new DatabaseHelper(ForecastFragment);
//        Cursor res = db.getAllData();
            if(res.getCount() == 0){
                return;
            }

            while(res.moveToNext()){
                COL_WEATHER_ICON[i] = res.getString(0);
                COL_MAX_TEMP[i] = res.getString(1);
                COL_MIN_TEMP[i] = res.getString(2);
                COL_DESCRIPTION[i] = res.getString(3);
                COL_WIND_SPEED[i] = res.getString(4);
                COL_PRESSURE[i] = res.getString(5);
                COL_HUMIDITY[i] = res.getString(6);
                COL_CLOUD[i] = res.getString(7);
                i++;
            }
        }
*/


    }

    String getFullDay(String day_)
    {

        if(day_.equals("Sun") || day_.equals("Mon") || day_.equals("Fri")){
            return day_+"day";
        }
        else if(day_.equals("Tue")){
            return "Tuesday";
        }
        else if(day_.equals("Wed")){
            return "Wednesday";
        }
        else if(day_.equals("Thu")){
            return "Thursday";
        }
        else{
            return "Saturday";
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();


        View rootView = inflater.inflate(R.layout.fragment_detail,container);
        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
         //   String forecast = intent.getStringExtra(Intent.EXTRA_TEXT);
            int index = Integer.parseInt(intent.getStringExtra(Intent.EXTRA_TEXT));
       //     String icon = "c"+ForecastFragment.weatherIcon.get(forecast.substring(0, 3));
            String icon = "c"+ WeatherProvider.weatherIcon.get(index);
            TextView dayView = (TextView) rootView.findViewById(R.id.dayView);
            TextView dateView = (TextView) rootView.findViewById(R.id.dateView);
            TextView descriptionView = (TextView) rootView.findViewById(R.id.description);
            TextView maxTempView = (TextView) rootView.findViewById(R.id.MaxTemp);
            TextView minTempView = (TextView) rootView.findViewById(R.id.MinTemp);
            TextView tempView = (TextView) rootView.findViewById(R.id.tempView);
            TextView cityView = (TextView) rootView.findViewById(R.id.city);
            TextView windView = (TextView) rootView.findViewById(R.id.speed);
            TextView pressureView = (TextView) rootView.findViewById(R.id.pressure);
            TextView humidityView = (TextView) rootView.findViewById(R.id.humidity);
            TextView cloudView = (TextView) rootView.findViewById(R.id.cloud);
            ImageView weatherImageView = (ImageView) rootView.findViewById(R.id.weather_icon);
            int imageId = getResources().getIdentifier(icon,"drawable","weather.climatenews");
             weatherImageView.setImageResource(imageId);
     //       day = forecast.substring(0,3);
     //       date = forecast.substring(3,forecast.indexOf(" , "));
     //       description = forecast.substring(forecast.indexOf(",")+1,forecast.indexOf("-"));
     //       maxTemp = forecast.substring(forecast.indexOf("-")+1,forecast.indexOf("/"));
     //       minTemp = forecast.substring(forecast.indexOf("/")+1);


//            dayView.setText(getFullDay(day));

            dayView.setText(getFullDay(WeatherProvider.day.get(index).substring(0,3)));
            dateView.setText(WeatherProvider.day.get(index).substring(4));
            descriptionView.setText(WeatherProvider.description.get(index));
            tempView.setText(WeatherProvider.maxTemp.get(index));
            maxTempView.setText(WeatherProvider.maxTemp.get(index));
            minTempView.setText(WeatherProvider.minTemp.get(index));
            windView.setText(WeatherProvider.windSpeed.get(index)+" meter/sec");
            cityView.setText(ForecastFragment.city.get("city"));
//            cityView.setText("testCity");
            pressureView.setText(WeatherProvider.pressure.get(index)+" hPa");
            cloudView.setText(WeatherProvider.cloud.get(index)+" %");
            humidityView.setText(WeatherProvider.humidity.get(index)+" %");

        }
        return rootView;
    }

    /*
    public class FetchImageTask extends AsyncTask<String, Void, Bitmap> {
        Bitmap mIconValue;
        private ImageView views;

        public FetchImageTask(ImageView views){
            this.views = views;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String link = "http://openweathermap.org/img/w/"+params[0]+".png";
            try {
                URL murl = new URL(link);
                try {
                    mIconValue = BitmapFactory.decodeStream(murl.openConnection().getInputStream());
                    DetailActivityFragment.weatherImg = mIconValue;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
         return mIconValue;
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                this.views.setImageBitmap(result);
            }
        }
    }*/
}
