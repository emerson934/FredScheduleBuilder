package com.example.emersontenorio.fredschedulebuilder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View form = (View) findViewById(R.id.backColor);
        form.setBackgroundColor(Color.argb(150, 100, 100, 100));

        Spinner timeS = (Spinner) findViewById(R.id.timeS);
        Spinner timeE = (Spinner) findViewById(R.id.timeE);
        Spinner days = (Spinner) findViewById(R.id.days);
        Spinner subj = (Spinner) findViewById(R.id.subject);

        ArrayAdapter<CharSequence> adapterT1 = ArrayAdapter.createFromResource(this, R.array.times, android.R.layout.simple_spinner_item);
        adapterT1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapterD1 = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
        adapterD1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapterS1 = ArrayAdapter.createFromResource(this, R.array.subject, android.R.layout.simple_spinner_item);
        adapterS1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        timeS.setAdapter(adapterT1);
        timeE.setAdapter(adapterT1);
        days.setAdapter(adapterD1);
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
                builder.generateRandomSchedules(100, 2300, allDays, "CSIT");
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


