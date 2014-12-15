package com.example.emersontenorio.fredschedulebuilder;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


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

        //Image Downloading and Fetching
        new Thread() {
            public void run() {
                try {
                    URL url = new URL("http://wanderingoak.net/bridge.png");
                    HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

                    if (httpCon.getResponseCode() != 200) {
                        throw new Exception("Failed to Connect");
                    }

                    InputStream is = httpCon.getInputStream();
                    final Bitmap bitmap = BitmapFactory.decodeStream(is);
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            ImageView iv = (ImageView) findViewById(R.id.main_image);
                            iv.setImageBitmap(bitmap);
                        }
                    });
                } catch (Exception e) {
                    Log.e("Image", " Failed to load image", e);
                }
            }
        }.start();
        //Ending Downloading and Fetching Image

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //Start test
        String[] daysA = new String[]{"M", "W", "F"};
        String[] daysB = new String[]{"M", "W", "F"};
        String[] daysC = new String[]{"M", "W", "F"};
        String[] daysD = new String[]{"T", "TH"};
        String[] daysE = new String[]{"T", "TH"};
        String[] daysF = new String[]{"T", "TH"};

        final Course courseA = new Course(830, 1030, daysA);
        final Course courseB = new Course(1040, 1150, daysB);
        final Course courseC = new Course(1140, 1230, daysC);
        final Course courseD = new Course(830, 1030, daysD);
        final Course courseE = new Course(1040, 1150, daysE);
        final Course courseF = new Course(1140, 1230, daysF);

        Button btnA = (Button) findViewById(R.id.btnA);
        btnA.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String resultA = Builder.addClass(courseA);
                Toast.makeText(getBaseContext(), resultA, Toast.LENGTH_SHORT).show();
            }
        });

        Button btnB = (Button) findViewById(R.id.btnB);
        btnB.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String resultB = Builder.addClass(courseB);
                Toast.makeText(getBaseContext(), resultB, Toast.LENGTH_SHORT).show();
            }
        });

        Button btnC = (Button) findViewById(R.id.btnC);
        btnC.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String resultC = Builder.addClass(courseC);
                Toast.makeText(getBaseContext(), resultC, Toast.LENGTH_SHORT).show();
            }
        });

        Button btnD = (Button) findViewById(R.id.btnD);
        btnA.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String resultD = Builder.addClass(courseD);
                Toast.makeText(getBaseContext(), resultD, Toast.LENGTH_SHORT).show();
            }
        });

        Button btnE = (Button) findViewById(R.id.btnE);
        btnB.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String resultE = Builder.addClass(courseE);
                Toast.makeText(getBaseContext(), resultE, Toast.LENGTH_SHORT).show();
            }
        });

        Button btnF = (Button) findViewById(R.id.btnF);
        btnC.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String resultF = Builder.addClass(courseF);
                Toast.makeText(getBaseContext(), resultF, Toast.LENGTH_SHORT).show();
            }
        });

        //End of Test

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


    /**
     * Created by Marcos Souza on 12/12/2014.
     */
//    public class UpdateApp extends AsyncTask<String, Integer, Bitmap> {
//        @Override
//        protected void onPreExecute() {
//            //Setup
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... params) {
//            try {
//                URL url = new URL(params[0]);
//                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
//
//                if (httpCon.getResponseCode() != 200) {
//                    throw new Exception("Failed to Connect");
//                }
//
//                InputStream is = httpCon.getInputStream();
//                return BitmapFactory.decodeStream(is);
//            } catch (Exception e) {
//                Log.e("Image", " Failed to load image", e);
//            }
//            return null;
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            //Update a progress bar
//            super.onProgressUpdate(values);
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap img) {
//            ImageView iv = (ImageView) findViewById(R.id.main_image);
//            if(iv!=null && img!=null){
//                iv.setImageBitmap(img);
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            //Handle what to do if you cancel this task
//            //You should not be able to cancel this task anyway
//            super.onCancelled();
//        }
//    }
}


