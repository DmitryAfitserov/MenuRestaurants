package com.example.developer.test2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.example.developer.test2.model.Restaurants;

import java.io.IOException;

/**
 * Created by developer on 26.02.2017.
 */

public class AsyncTaskFirstData extends AsyncTask<Void, Void, Void > {

    private Context context;

    public AsyncTaskFirstData(Context context){
        this.context = context;
    }
    @Override
    protected Void doInBackground(Void... params) {
        SQLite sqLite = new SQLite(context);
        try {
            sqLite.createDataBase();
            sqLite.openDataBase();
            SQLiteDatabase db = sqLite.getReadableDatabase();
            String query = "select Res.id as id, Logo.file_path as file_path, name as name " +
                    "from ch_restaurant as Res inner join ch_restaurant_logo as Logo " +
                    "on Res.id = Logo.restaurant_id" +
                    " ORDER BY name DESC";
            Cursor cursor = db.rawQuery(query, null);

            Singleton.getState().getListRestaurants().clear();
            for(cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()){
                Restaurants restaurants = new Restaurants();
                restaurants.setId(cursor.getInt(cursor.getColumnIndex("id")));
                restaurants.setNameRestaurant(cursor.getString(cursor.getColumnIndex("name")));
                restaurants.setNameIcon(cursor.getString(cursor.getColumnIndex("file_path")));
                Singleton.getState().setItemRestaurant(restaurants);

            }
            Singleton.getState().createFavoritList(context);
            cursor.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
    }
}
