package com.example.emersontenorio.fredschedulebuilder;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Marcos Souza on 12/15/2014.
 */
public class SearchActivity extends ActionBarActivity {
    ListView list;
    String[] menuTitles;
    String[] menuDescriptions;
    int[] images = {
            R.drawable.csit2,
            R.drawable.csit3,
            R.drawable.csit4,
            R.drawable.mobile1,
            R.drawable.mobile3,
            R.drawable.csit2,
            R.drawable.csit3,
            R.drawable.csit4,
            R.drawable.mobile1,
            R.drawable.mobile3,
            R.drawable.csit2
    };
    DBAdapter db = new DBAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.search_view);
        //String value = intent.getStringExtra("key"); //if it's a string you stored.

        //final Resources res = getResources();//
//        menuTitles = res.getStringArray(R.array.titles);
//        menuDescriptions = res.getStringArray(R.array.descriptions);
//
        //list = (ListView) findViewById(R.id.listView);

        //VivzAdapter adapter = new VivzAdapter(this, menuTitles, images, menuDescriptions);
        //list.setAdapter(adapter);
        displayAll();
        handleIntent(getIntent());
    }
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        //use the query to search your data
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            db.open();

            Cursor cursor = db.getCourseMatches(query, null);
            ArrayList<String> results = new ArrayList<String>();

            //process Cursor and display results
            if(cursor != null) {

                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        String id = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_ID));
                        String title = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_TITLE));
                        String scheduled = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_SCHEDULED));
                        String subject = cursor.getString(cursor.getColumnIndex(DBAdapter.COL_SUBJECT));
                        results.add(id + " / " + title + " / schedule " + scheduled + " / " + subject);
                        cursor.moveToNext();
                    }
                    menuTitles = new String[results.size()];
                    menuDescriptions = new String[results.size()];

                    for (int i = 0; i < results.size(); i++) {
                        String[] strings = TextUtils.split(results.get(i), "/");
                        menuTitles[i] = strings[3].trim();
                        menuDescriptions[i] = strings[1].trim();
                    }
                    list = (ListView) findViewById(R.id.listView);

                    VivzAdapter adapter = new VivzAdapter(getBaseContext(), menuTitles, images, menuDescriptions);
                    list.setAdapter(adapter);
                }
                cursor.close();
            }
            else
            {
                list.setAdapter(null);
            }
            db.close();
        } else {
            displayAll();
        }
    }

   public void displayAll(){
        DBAdapter myDataBase = new DBAdapter(getBaseContext());

        myDataBase.open();
        myDataBase.loadDictionary();

        ArrayList<String> allMyCourses = myDataBase.getAllCourses();//edited MArcos

        myDataBase.close();
//                for (int i = 0; i < allMyCourses.size(); i++) {
//                    Toast.makeText(getBaseContext(), "Course in the DB: " + allMyCourses.get(i), Toast.LENGTH_SHORT).show();
//                }
        menuTitles = new String[allMyCourses.size()];
        menuDescriptions = new String[allMyCourses.size()];

        for (int i = 0; i < allMyCourses.size(); i++) {
            String[] strings = TextUtils.split(allMyCourses.get(i), "/");
            menuTitles[i] = strings[3].trim();
            menuDescriptions[i] = strings[1].trim();
        }


//                menuTitles = res.getStringArray(R.array.titles);
//                menuDescriptions = res.getStringArray(R.array.descriptions);

        list = (ListView) findViewById(R.id.listView);

        VivzAdapter adapter = new VivzAdapter(getBaseContext(), menuTitles, images, menuDescriptions);
        list.setAdapter(adapter);
    }



    static class VivzAdapter extends ArrayAdapter<String> {
        Context context;
        int[] images;
        String[] titleArray;
        String[] descriptionArray;

        VivzAdapter(Context c, String[] titles, int imgs[], String[] desc) {
            super(c, R.layout.single_row, R.id.textView, titles);
            this.context = c;
            this.images = imgs;
            this.titleArray = titles;
            this.descriptionArray = desc;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.single_row, parent, false);
            }


            ImageView myImage = (ImageView) row.findViewById(R.id.imageView);
            TextView myTitle = (TextView) row.findViewById(R.id.textView);
            TextView myDescription = (TextView) row.findViewById(R.id.textView2);

            //change position if you want to change the image
//            myImage.setImageResource(images[position]);
            myTitle.setText(titleArray[position]);
            if (titleArray[position].equals("CSIT")) {
                myImage.setImageResource(images[2]);
            } else {
                myImage.setImageResource(images[4]);
            }
            myTitle.setText(descriptionArray[position]);

            return row;
//            return super.getView(position, convertView, parent);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        System.out.println("Chamou!");
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.options_menu, menu);
//
//        // Associate searchable configuration with the SearchView
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//
//        return true;
//    }

    public boolean onActionViewCollapsed(MenuItem item) {
        //do what you want to when close the sesarchview
        //remember to return true;
        displayAll();
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.search), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                displayAll();
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });
        return true;
    }

}