package com.example.emersontenorio.fredschedulebuilder;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Marcos Souza on 12/15/2014.
 */
public class SearchActivity extends Activity {
    ListView list;
    DBAdapter db = new DBAdapter(this);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        handleIntent(intent);
        //String value = intent.getStringExtra("key"); //if it's a string you stored.

        final Resources res = getResources();

        Button btnF = (Button) findViewById(R.id.btnSearch);
        btnF.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                DBAdapter myDataBase = new DBAdapter(getBaseContext());

                myDataBase.open();
                myDataBase.loadFileIntoBD();

                ArrayList<String> allMyCourses = myDataBase.getAllCourses();//edited MArcos

                myDataBase.close();

                menuTitles = new String[allMyCourses.size()];
                menuDescriptions = new String[allMyCourses.size()];

                for (int i = 0; i < allMyCourses.size(); i++) {
                    String[] strings = TextUtils.split(allMyCourses.get(i), "/");
                    menuTitles[i] = strings[3].trim();
                    menuDescriptions[i] = strings[1].trim();
                }

                list = (ListView) findViewById(R.id.listView);

                VivzAdapter adapter = new VivzAdapter(getBaseContext(), menuTitles, images, menuDescriptions);
                list.setAdapter(adapter);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        //use the query to search your data
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
//            Cursor c = db.getWordMatches(query, null);
            //process Cursor and display results

        }
    }

    static class VivzAdapter extends ArrayAdapter<String>
    {
        Context context;
        int[] images;
        String[] titleArray;
        String[] descriptionArray;
        VivzAdapter(Context c, String[] titles, int imgs[], String[] desc)
        {
            super(c, R.layout.course_list_item, R.id.textView, titles);
            this.context = c;
            this.images = imgs;
            this.titleArray = titles;
            this.descriptionArray = desc;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            View row = convertView;
            if(row==null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.course_list_item, parent, false);
            }

            ImageView myImage = (ImageView) row.findViewById(R.id.imageView);
            TextView myTitle = (TextView) row.findViewById(R.id.textView);
            TextView myDescription = (TextView) row.findViewById(R.id.textView2);

            //change position if you want to change the image
//            myImage.setImageResource(images[position]);
            myTitle.setText(titleArray[position]);
            if (titleArray[position].equals("CSIT")){
                myImage.setImageResource(images[2]);
            } else{
                myImage.setImageResource(images[4]);
            }
            myTitle.setText(descriptionArray[position]);

            return row;
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

        return true;
    }
}
