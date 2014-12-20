package com.example.emersontenorio.fredschedulebuilder;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v4.view.GestureDetectorCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by Marcos Souza on 12/15/2014.
 */
public class SearchActivity extends ActionBarActivity {
    ListView list;
    DBAdapter db = new DBAdapter(this);
    String[] menuTitles;
    String[] menuDescriptions;
    String[] menuTimes;
    int[] courseListIds;
    int[] images = {
            R.drawable.ic_computer,
            R.drawable.ic_computer,
            R.drawable.ic_computer,
            R.drawable.ic_computer,
            R.drawable.ic_computer,
            R.drawable.ic_computer,
            R.drawable.ic_computer,
            R.drawable.ic_computer,
            R.drawable.ic_computer,
            R.drawable.ic_computer,
            R.drawable.ic_computer
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_search);



        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
//        mDetector = new GestureDetectorCompat(this,this);
        // Set the gesture detector as the double tap
        // listener.
//        mDetector.setOnDoubleTapListener(this);


        Intent intent = getIntent();

        //String value = intent.getStringExtra("key"); //if it's a string you stored.

        //final Resources res = getResources();//
//        menuTitles = res.getStringArray(R.array.titles);
//        menuDescriptions = res.getStringArray(R.array.descriptions);
//
        //list = (ListView) findViewById(R.id.listView);
//        final Resources res = getResources();

        //VivzAdapter adapter = new VivzAdapter(this, menuTitles, images, menuDescriptions);
        //list.setAdapter(adapter);
        displayAll();
        handleIntent(intent);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }
//        Button btnF = (Button) findViewById(R.id.btnSearch);
//        btnF.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
                DBAdapter myDataBase = new DBAdapter(this);

    private void handleIntent(Intent intent) {
        //use the query to search your data
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            db.open();
                myDataBase.open();
//                myDataBase.loadFileIntoBD();

            Cursor cursor = db.getCourseMatches(query, null);
            ArrayList<String> results = new ArrayList<String>();
                ArrayList<String> allMyCourses = myDataBase.getAllCourses();

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

                    VivzAdapter adapter = new VivzAdapter(getBaseContext(), menuTitles, images, menuDescriptions, /*new*/ courseListIds, menuTimes);
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
//        myDataBase.loadFileIntoBD();

        ArrayList<String> allMyCourses = myDataBase.getAllCourses();//edited MArcos

        myDataBase.close();
//                for (int i = 0; i < allMyCourses.size(); i++) {
//                    Toast.makeText(getBaseContext(), "Course in the DB: " + allMyCourses.get(i), Toast.LENGTH_SHORT).show();
//                }
        menuTitles = new String[allMyCourses.size()];
        menuDescriptions = new String[allMyCourses.size()];
                myDataBase.close();

                menuTitles = new String[allMyCourses.size()];
                menuDescriptions = new String[allMyCourses.size()];
                courseListIds = new int[allMyCourses.size()];
                menuTimes = new String[allMyCourses.size()];

        for (int i = 0; i < allMyCourses.size(); i++) {
            String[] strings = TextUtils.split(allMyCourses.get(i), "/");
            menuTitles[i] = strings[3].trim();
            menuDescriptions[i] = strings[1].trim();
        }
                for (int i = 0; i < allMyCourses.size(); i++) {
                    String[] strings = TextUtils.split(allMyCourses.get(i), "/");
                    menuTitles[i] = strings[3].trim();
                    menuDescriptions[i] = strings[1].trim();
                    courseListIds[i] = Integer.parseInt(strings[0].trim());
                    menuTimes[i] = strings[4].trim();

                }


//                menuTitles = res.getStringArray(R.array.titles);
//                menuDescriptions = res.getStringArray(R.array.descriptions);

        list = (ListView) findViewById(R.id.listView);
                list = (ListView) findViewById(R.id.listView);

//        VivzAdapter adapter = new VivzAdapter(getBaseContext(), menuTitles, images, menuDescriptions);
//        list.setAdapter(adapter);
                VivzAdapter adapter = new VivzAdapter(getBaseContext(), menuTitles, images, menuDescriptions, /*new*/ courseListIds, menuTimes);
                list.setAdapter(adapter);

                registerForContextMenu(list);
//            }
//        });
    }

    private int pos = 0;

    public void setSelectedItem(int position){
        pos = position;
    }

    public int getSelectedItem(){
        return pos;
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v,
//                                    ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.context_menu, menu);
//    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.context_menu, popup.getMenu());
        popup.show();
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        // This activity implements OnMenuItemClickListener
//        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.context_menu);
        popup.show();
    }

    public void doAction(MenuItem item){
        DBAdapter myDataBase = new DBAdapter(this);
        int position = getSelectedItem();


        switch (item.getItemId()) {
            case R.id.add_to_schedule:
                myDataBase.open();
                Toast.makeText(this, "ADDED to schedule", Toast.LENGTH_SHORT).show();

                myDataBase.updateSchedule(position, true);
                myDataBase.close();
//                editNote(info.id);
//                return true;
                break;
            case R.id.remove_from_schedule:
                myDataBase.open();
                Toast.makeText(this, "REMOVED from schedule", Toast.LENGTH_SHORT).show();
                myDataBase.updateSchedule(position, false);
                myDataBase.close();
//                deleteNote(info.id);
//                return true;
                break;
            case R.id.mark_as_done:
                myDataBase.open();
                Toast.makeText(this, "ADDED as DONE", Toast.LENGTH_SHORT).show();
                myDataBase.updateRecord(position, true);
                myDataBase.close();
//                deleteNote(info.id);
//                return true;
                  break;
            case R.id.mark_as_not_done:
                myDataBase.open();
                Toast.makeText(this, "MARKED as NOT DONE", Toast.LENGTH_SHORT).show();
                myDataBase.updateRecord(position, false);
                myDataBase.close();
//                deleteNote(info.id);
//                return true;
                  break;
            default:
                Toast.makeText(this, "DEFAULT", Toast.LENGTH_SHORT).show();
//                return false;//
                break;
        }
    }


//    public boolean onMenuItemClick(MenuItem item) {
//        DBAdapter myDataBase = new DBAdapter(this);
//        switch (item.getItemId()) {
//            case R.id.add_to_schedule:
//                myDataBase.open();
////                myDataBase.updateSchedule(((int) info.id), true);
//                myDataBase.close();
////                editNote(info.id);
//                return true;
//            case R.id.remove_from_schedule:
//                myDataBase.open();
////                myDataBase.updateSchedule(((int) info.id), false);
//                myDataBase.close();
////                deleteNote(info.id);
//                return true;
//            case R.id.mark_as_done:
//                myDataBase.open();
////                myDataBase.updateRecord(((int) info.id), true);
//                myDataBase.close();
////                deleteNote(info.id);
//                return true;
//            case R.id.mark_as_not_done:
//                myDataBase.open();
////                myDataBase.updateRecord(((int) info.id), false);
//                myDataBase.close();
////                deleteNote(info.id);
//                return true;
//            default:
//                return false;
//        }
//    }



//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        DBAdapter myDataBase = new DBAdapter(this);
//        switch (item.getItemId()) {
//            case R.id.add_to_schedule:
//                myDataBase.open();
//                myDataBase.updateSchedule(((int) info.id), true);
//                myDataBase.close();
////                editNote(info.id);
//                return true;
//            case R.id.remove_from_schedule:
//                myDataBase.open();
//                myDataBase.updateSchedule(((int) info.id), false);
//                myDataBase.close();
////                deleteNote(info.id);
//                return true;
//            case R.id.mark_as_done:
//                myDataBase.open();
//                myDataBase.updateRecord(((int) info.id), true);
//                myDataBase.close();
////                deleteNote(info.id);
//                return true;
//            case R.id.mark_as_not_done:
//                myDataBase.open();
//                myDataBase.updateRecord(((int) info.id), false);
//                myDataBase.close();
////                deleteNote(info.id);
//                return true;
//            default:
//                return super.onContextItemSelected(item);
//        }
//    }


    class VivzAdapter extends ArrayAdapter<String>
    {
        Context context;
        int[] images;
        String[] titleArray;
        String[] descriptionArray;
//
//        VivzAdapter(Context c, String[] titles, int imgs[], String[] desc) {
//            super(c, R.layout.single_row, R.id.textView, titles);
        int[] courseListArray;
        String[] timeArray;

        VivzAdapter(Context c, String[] titles, int imgs[], String[] desc, int[] courseIds, String[] time) {
            super(c, R.layout.course_list_item, R.id.textView, titles);
            this.context = c;
            this.images = imgs;
            this.titleArray = titles;
            this.descriptionArray = desc;
            this.courseListArray = courseIds;
            this.timeArray = time;
        }

        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
        public View getView(final int position, View convertView, ViewGroup parent){

            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.course_list_item, parent, false);
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
            myDescription.setText(timeArray[position]);

            row.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(getBaseContext(), CourseActivity.class);
                    intent.putExtra("id", position);
                    startActivity(intent);
                    Toast.makeText(context, "Item " + courseListArray[position] + " Clicked! In position: " + position, Toast.LENGTH_SHORT).show();
                }
            });

            row.setOnLongClickListener(new View.OnLongClickListener(){
                public boolean onLongClick(View v){
                    setSelectedItem(position);
                    showPopup(v);
                    return true;
                }
            });

            DBAdapter myDataBase = new DBAdapter(context);
            myDataBase.open();
            Cursor cursor = myDataBase.getRecord(courseListArray[position]);

            if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_SCHEDULED))) == 1){
                row.setBackgroundColor(Color.rgb(54,90,251));
            } else if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBAdapter.COL_DONE))) == 1){
                row.setBackgroundColor(Color.rgb(0, 201, 64));
            }

            myDataBase.close();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

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

//}
