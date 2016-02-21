package weather.climatenews;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import weather.climatenews.Data.DatabaseHelper;

/**
 *
 * @author Ashish 
 */

public class MainActivity extends AppCompatActivity {
    static  boolean stat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        stat = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getActionBar().setTitle("Undo");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setIcon(R.drawable.climatenews);
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().add(R.id.container, new ForecastFragment()).commit();

        }
    }

    public void open1(View view){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor cursor = databaseHelper.getAllData();
        if(cursor.getCount()!=0){
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, "0");
        startActivity(intent);}
    }
    public void open2(View view){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor cursor = databaseHelper.getAllData();
        if(cursor.getCount()!=0){
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, "1");
            startActivity(intent);}
    }
    public void open3(View view){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor cursor = databaseHelper.getAllData();
        if(cursor.getCount()!=0){
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, "2");
            startActivity(intent);}
    }
    public void open4(View view){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor cursor = databaseHelper.getAllData();
        if(cursor.getCount()!=0){
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, "3");
            startActivity(intent);}
    }
    public void open5(View view){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor cursor = databaseHelper.getAllData();
        if(cursor.getCount()!=0){
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, "4");
            startActivity(intent);}
    }
    public void open6(View view){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor cursor = databaseHelper.getAllData();
        if(cursor.getCount()!=0){
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, "5");
            startActivity(intent);}
    }
    public void open7(View view){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor cursor = databaseHelper.getAllData();
        if(cursor.getCount()!=0){
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, "6");
            startActivity(intent);}
    }

}
