package com.example.emersontenorio.fredschedulebuilder;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, AdapterView.OnItemSelectedListener {
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    int timeStart = 0;
    int timeEnd = 1380;
    String subject = "CSIT";

    int counterTimes = 0;



    //Spinner get Value
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {


//        Toast.makeText(this, "ID: " + parent.getItemIdAtPosition(pos), Toast.LENGTH_SHORT).show();
        // An item was selected. You can retrieve the selected item using

//
//        System.out.println("parent ID: " + parent.);
//        System.out.println("View ID: " + parent.getSelectedView().getId());
        String content = parent.getItemAtPosition(pos).toString();
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
        String[] item = TextUtils.split(content, " ");
        if(item.length > 1){//time
            counterTimes++;
//            System.out.println("First IF");
            String[] time = TextUtils.split(item[0], ":");
            int hour   = Integer.parseInt(time[0]);
            int minute = Integer.parseInt(time[1]);

//            System.out.println("hour: " + hour);
//            System.out.println("minute: " + minute);
//            int timeConverted = hour*60 + minute;
            int timeConverted = hour*100 + minute;
            if(item[1].equals("PM") && hour != 12){
//                timeConverted += 12*60;
                timeConverted += 12*100;
//                System.out.println("PM");
            }

            if (counterTimes % 2 == 1){
                timeStart = timeConverted;
            } else {
                timeEnd = timeConverted;
            }

//            if (view.getId() == R.id.timeS){
//                System.out.println("Start: " + R.id.timeS + ", " + view.getId());
//                timeStart = timeConverted;
//            } else if(view.getId() == R.id.timeE){
//                System.out.println("Start: " + R.id.timeE + ", " + view.getId());
//                timeEnd = timeConverted;
//            } else{
//                System.out.println("WHY?");
//                System.out.println("Start: " + R.id.timeS + ", " + view.getId()+ ", " + R.id.timeE);
//            }
        } else if (view.getId() == R.id.timeS){
            subject = item[0];
//            System.out.println("ELSE");
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    //End Spinner getting Value

    public void clearDone(View v){
        DBAdapter myDB = new DBAdapter(this);
        myDB.open();
        int numRows = myDB.countRows();

        for (int i = 0; i < myDB.countRows(); i++) {
            myDB.updateRecord(i, false);
        }
        myDB.close();

        Toast.makeText(this, "All courses set as NOT DONE!", Toast.LENGTH_SHORT).show();
    }

    public void clearSchedule(View v){
        DBAdapter myDB = new DBAdapter(this);
        myDB.open();
        int numRows = myDB.countRows();

        for (int i = 0; i < myDB.countRows(); i++) {
            myDB.updateSchedule(i, false);
        }
        myDB.close();

        Toast.makeText(this, "All courses REMOVED from the Schedule!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View form = (View) findViewById(R.id.backColor);
        form.setBackgroundColor(Color.argb(150, 100, 100, 100));

//        View formColor = (View) findViewById(R.id.formColor);
//        formColor.setBackgroundColor(Color.argb(50, 100, 100, 100));

        Spinner timeS = (Spinner) findViewById(R.id.timeS);
        final Spinner timeE = (Spinner) findViewById(R.id.timeE);
//        Spinner days = (Spinner) findViewById(R.id.days);
        Spinner subj = (Spinner) findViewById(R.id.subject);

        timeS.setOnItemSelectedListener(this);
        timeE.setOnItemSelectedListener(this);
//        days.setOnItemSelectedListener(this);
        subj.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapterT1 = ArrayAdapter.createFromResource(this, R.array.times, android.R.layout.simple_spinner_item);
        adapterT1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        ArrayAdapter<CharSequence> adapterD1 = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
//        adapterD1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapterS1 = ArrayAdapter.createFromResource(this, R.array.subject, android.R.layout.simple_spinner_item);
        adapterS1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        timeS.setAdapter(adapterT1);
        timeE.setAdapter(adapterT1);
//        days.setAdapter(adapterD1);
        subj.setAdapter(adapterS1);


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        final String[] allDays = new String[]{"M", "W", "F", "T", "TH"};

        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                //intent.putExtra("key", value); //Optional parameters
                MainActivity.this.startActivity(intent);
            }
        });

        Button btnSchedule = (Button) findViewById(R.id.btnSchedule);
        btnSchedule.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
                //intent.putExtra("key", value); //Optional parameters
                MainActivity.this.startActivity(intent);
            }
        });

        Button btnRandom = (Button) findViewById(R.id.btnRandom);
        btnRandom.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Builder builder = new Builder(getBaseContext());
//                builder.generateRandomSchedules(100, 2300, allDays, "CSIT");
                if (timeStart > timeEnd){
                    int aux = timeStart;
                    timeStart = timeEnd;
                    timeEnd = aux;
                }
                builder.generateRandomSchedules(timeStart, timeEnd, allDays, subject);
                Toast.makeText(getBaseContext(), "Start: " + timeStart + ", End: " + timeEnd + ", Subject: " + subject, Toast.LENGTH_SHORT).show();

                Toast.makeText(getBaseContext(), "Courses ADDED to Schedule!", Toast.LENGTH_SHORT).show();
//                ArrayList<Course> classes = builder.getCourses();
//                int scheduleSize = builder.getNumberCourses();
//                for (int i = 0; i < scheduleSize; i++) {
//                    if (classes.get(i).name != null){
//                        Toast.makeText(getBaseContext(), "Class: " + classes.get(i).subject +  " " + classes.get(i).course_number + "/  "  + classes.get(i).name + " in: " + i, Toast.LENGTH_SHORT).show();
//                    } else{
//                        Toast.makeText(getBaseContext(), "Name null in: " + i, Toast.LENGTH_SHORT).show();
//                    }
//                }
            }
        });
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return inflater.inflate(R.layout.fragment_main, container, false);//rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}


