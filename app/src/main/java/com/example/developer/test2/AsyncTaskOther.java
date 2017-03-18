package com.example.developer.test2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.example.developer.test2.model.Dishes;
import com.example.developer.test2.model.PartDish;

import java.io.IOException;
import java.util.List;

/**
 * Created by developer on 27.02.2017.
 */

public class AsyncTaskOther extends AsyncTask<Void,Void,Void> {
    private List<Dishes> data;
    private List<PartDish> dataPartDishes;
    private String id;
    private boolean isDishes;
    private CallBackAsyncTask callBackAsyncTask;

    public AsyncTaskOther(CallBackAsyncTask callBackAsyncTask, List data, String id, boolean isDishes){
        this.callBackAsyncTask = callBackAsyncTask;
        if(isDishes){
            this.data = data;
        } else {
            this.dataPartDishes = data;
        }
        this.id = id;
        this.isDishes = isDishes;
    }

    @Override
    protected Void doInBackground(Void... params) {
        SQLite sqLite = new SQLite(Singleton.getState().getContext());
        try {
            sqLite.createDataBase();
            sqLite.openDataBase();
            SQLiteDatabase db = sqLite.getReadableDatabase();
            if(isDishes){
                String query = "select Item.name as name_dish, Cat.name as name_category, " +
                        "Item.id as id_dish " +
                        "from ch_item as Item inner join ch_category as Cat " +
                        "on Item.category_id = Cat.id "+
                        " where restaurant_id = ?";
                Cursor cursor = db.rawQuery(query, new String[]{id});

                for(cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()){
                    Dishes dish = new Dishes();
                    dish.setNameDishes(cursor.getString(cursor.getColumnIndex("name_dish")));
                    dish.setNameCategory(cursor.getString(cursor.getColumnIndex("name_category")));
                    dish.setId(cursor.getString(cursor.getColumnIndex("id_dish")));
                    data.add(dish);
                }
                cursor.close();
            } else {
                String[] colums = new String[]{"serving", "calories", "total_fat", "saturated_fat",
                "trans_fats", "cholesterol", "sodium", "carbs"};
                String[] tag = new String[]{"Serving", "Calories", "Total fat", "Saturated fat",
                        "Trans fats", "Cholesterol", "Sodium", "Carbs"};
                Cursor cursor = db.query("ch_item", colums, "id = ?", new String[]{id}, null, null, null);
                Log.d("EEE", "cursor = " + cursor.getCount());
                cursor.moveToFirst();
                for(int i = 0; i < colums.length; i++){
                    PartDish partDish = new PartDish();
                    partDish.setName(tag[i]);
                    String value = cursor.getString(cursor.getColumnIndex(colums[i]));
                    if(value == null){
                        value = "0";
                    }
                    partDish.setValue(value);
                    dataPartDishes.add(partDish);
                }
                cursor.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        callBackAsyncTask.notifyAdapter();
    }
}
