package com.example.emersontenorio.fredschedulebuilder;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Marcos Souza on 12/15/2014.
 */
public class SearchActivity extends Activity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.search_view);

        Intent intent = getIntent();
        handleIntent(intent);
        //String value = intent.getStringExtra("key"); //if it's a string you stored.

        Resources res = getResources();
        menuTitles = res.getStringArray(R.array.titles);
        menuDescriptions = res.getStringArray(R.array.descriptions);

        list = (ListView) findViewById(R.id.listView);

        VivzAdapter adapter = new VivzAdapter(this, menuTitles, images, menuDescriptions);
        list.setAdapter(adapter);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
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
            super(c, R.layout.single_row, R.id.textView, titles);
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
                row = inflater.inflate(R.layout.single_row, parent, false);
            }


            ImageView myImage = (ImageView) row.findViewById(R.id.imageView);
            TextView myTitle = (TextView) row.findViewById(R.id.textView);
            TextView myDescription = (TextView) row.findViewById(R.id.textView2);

            //change position if you want to change the image
            myImage.setImageResource(images[position]);
            myTitle.setText(titleArray[position]);
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
