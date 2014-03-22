package za.co.maiatoday.devart.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import za.co.maiatoday.devart.R;
import za.co.maiatoday.devart.util.ConnectionDetector;

public class MainActivity extends ActionBarActivity implements MainNavigation  {
    private static final String MAIN_FRAGMENT = "main";
    private static final String INFO_FRAGMENT = "info";
    final String TAG = "MainActivity";
    // Internet Connection detector
    private ConnectionDetector cd;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();
    // Shared Preferences
//    private static SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        PlusFragment.getInstance(this);
        setContentView(R.layout.activity_main);
//        mSharedPreferences = getSharedPreferences(Prefs.PREF_NAME, 0);
        // Start the first fragment.
        // However, if we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
        if (savedInstanceState == null) {
            if (findViewById(R.id.fragment_container) != null) {
                Fragment firstFragment;
                String tag;
                firstFragment = new MainFragment();
                tag = MAIN_FRAGMENT;

                Bundle args = new Bundle();
                args.putParcelable("intent", getIntent());
                // In case this activity was started with special instructions from an Intent,
                // pass the Intent's extras to the fragment as arguments
                firstFragment.setArguments(args);

                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment, tag).commit();
            }
        }
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);    // Shared Preferences
        cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(MainActivity.this, "Internet Connection Error",
                "Please connect to working Internet connection", false);
            // stop executing code by return
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
        case R.id.action_info:
            switchToInfoFragment();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();
//        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        EasyTracker.getInstance(this).activityStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * open the InfoActivity intent
     */
    @Override
    public void switchToInfoFragment() {
//        Intent i = new Intent(this, InfoActivity.class);
//        startActivityForResult(i, REQUEST_INFO);

        // Create fragment and give it an argument specifying the article it should show
        InfoFragment newFragment = new InfoFragment();
        Bundle args = new Bundle();
        newFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Add the fragment to the 'fragment_container' FrameLayout
        transaction.replace(R.id.fragment_container, newFragment, INFO_FRAGMENT);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private void switchToMainFragment() {
        if (findViewById(R.id.fragment_container) != null) {

            FragmentManager manager = getSupportFragmentManager();
            Fragment f = manager.findFragmentByTag(MAIN_FRAGMENT);
            //only replace the fragment with main if it isn't the main fragment already
            if (f == null) {

                // Create an instance of ExampleFragment
                MainFragment newFragment = new MainFragment();

                // In case this activity was started with special instructions from an Intent,
                // pass the Intent's extras to the fragment as arguments
                newFragment.setArguments(getIntent().getExtras());
                manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction transaction = manager.beginTransaction();

                // Add the fragment to the 'fragment_container' FrameLayout
                transaction.replace(R.id.fragment_container, newFragment, MAIN_FRAGMENT);
                // Commit the transaction
                transaction.commit();
            }
        }

    }

    //-------------- opencv test for manager code ----------------
//    final private LoaderCallbackInterface mLoaderCallback = new BaseLoaderCallback(this) {
//        @Override
//        public void onManagerConnected(int status) {
//            switch (status) {
//            case LoaderCallbackInterface.SUCCESS: {
//                Log.i(TAG, "OpenCV loaded successfully");
////                    mOpenCvCameraView.enableView();
//            }
//            break;
//            default: {
//                super.onManagerConnected(status);
//            }
//            break;
//            }
//        }
//    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // No action here, call super to delegate to Fragments
        super.onActivityResult(requestCode, resultCode, data);
        // Because we are using startIntentSenderForResult in the PlusFragment it isn't getting the onActivityResult
        // so I have to pass it here. bleargh
        PlusFragment.getInstance(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
        case PlusFragment.DIALOG_PLAY_SERVICES_ERROR:
            return PlusFragment.getInstance(this).onCreateDialog(id);
        default:
            return super.onCreateDialog(id);
        }
    }


}
