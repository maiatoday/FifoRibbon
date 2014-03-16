package za.co.maiatoday.devart.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import za.co.maiatoday.devart.R;

/**
 * Created by maia on 2014/02/22.
 */
public class PlusFragment extends Fragment implements
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<People.LoadPeopleResult>, PlusClient.OnAccessRevokedListener {
    private static String TAG = PlusFragment.class.toString();

    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;

    private static final int RC_SIGN_IN = 0;

    public static final int DIALOG_PLAY_SERVICES_ERROR = 0;

    private static final String SAVED_PROGRESS = "sign_in_progress";

    // GoogleApiClient wraps our service connection to Google Play services and
    // provides access to the users sign in state and Google's APIs.
    private GoogleApiClient mGoogleApiClient;

    // We use mSignInProgress to track whether user has clicked sign in.
    // mSignInProgress can be one of three values:
    //
    //       STATE_DEFAULT: The default state of the application before the user
    //                      has clicked 'sign in', or after they have clicked
    //                      'sign out'.  In this state we will not attempt to
    //                      resolve sign in errors and so will display our
    //                      Activity in a signed out state.
    //       STATE_SIGN_IN: This state indicates that the user has clicked 'sign
    //                      in', so resolve successive errors preventing sign in
    //                      until the user has successfully authorized an account
    //                      for our app.
    //   STATE_IN_PROGRESS: This state indicates that we have started an intent to
    //                      resolve an error, and so we should not start further
    //                      intents until the current intent completes.
    private int mSignInProgress;

    // Used to store the PendingIntent most recently returned by Google Play
    // services until the user clicks 'sign in'.
    private PendingIntent mSignInIntent;

    // Used to store the error code most recently returned by Google Play services
    // until the user clicks 'sign in'.
    private int mSignInError;
    private Person currentPerson;
    private int peopleCount;


    public boolean isConnected() {
        return mGoogleApiClient.isConnected();
    }

    CopyOnWriteArrayList<PlusStatusChangeListener> listeners = new CopyOnWriteArrayList<PlusStatusChangeListener>();

    private String status = "";

    public String getStatus() {
        return status;
    }


    public PlusFragment() {
    }

    /**
     * Create a new instance of PlusFragment, with parameters passed in a bundle
     */
    public static PlusFragment getInstance(FragmentActivity activity) {

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        PlusFragment fragment = (PlusFragment) fragmentManager
            .findFragmentByTag(PlusFragment.TAG);
        if (fragment == null) {
            fragment = new PlusFragment();
            fragmentManager.beginTransaction().add(fragment,
                PlusFragment.TAG).commit();
        }

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mSignInProgress = savedInstanceState
                .getInt(SAVED_PROGRESS, STATE_DEFAULT);
        }

        // Keep Fragment around
        setRetainInstance(true);
        mGoogleApiClient = buildGoogleApiClient();
    }

    private GoogleApiClient buildGoogleApiClient() {
        // When we build the GoogleApiClient we specify where connected and
        // connection failed callbacks should be returned, which Google APIs our
        // app uses and which OAuth 2.0 scopes our app requests.
        return new GoogleApiClient.Builder(getActivity())
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(Plus.API, null)
            .addScope(Plus.SCOPE_PLUS_LOGIN)
            .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_PROGRESS, mSignInProgress);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {

        Log.d(TAG, "onActivityResult resultCode" + resultCode);
        switch (requestCode) {
        case RC_SIGN_IN:
            if (resultCode == Activity.RESULT_OK) {
                // If the error resolution was successful we should continue
                // processing errors.
                mSignInProgress = STATE_SIGN_IN;
            } else {
                // If the error resolution was not successful or the user canceled,
                // we should stop processing errors.
                mSignInProgress = STATE_DEFAULT;
            }

            if (!mGoogleApiClient.isConnecting()) {
                // If Google Play services resolved the issue with a dialog then
                // onStart is not called so we need to re-attempt connection here.
                mGoogleApiClient.connect();
            }
            break;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "onConnected");
        // Indicate that the sign in process is complete.
        // Retrieve some profile information to personalize our app for the user.
        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            String personName = currentPerson.getDisplayName();
            setStatus(String.format(
                getResources().getString(R.string.signed_in_as),
                personName));
//            Person.Image personPhoto = currentPerson.getImage();
//            String personGooglePlusProfile = currentPerson.getUrl();
        }

        Plus.PeopleApi.loadVisible(mGoogleApiClient, null)
            .setResultCallback(this);
        mSignInProgress = STATE_DEFAULT;
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason.
        // We call connect() to attempt to re-establish the connection or get a
        // ConnectionResult that we can attempt to resolve.
      //TODO fix  mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
            + connectionResult.getErrorCode());
        if (mSignInProgress != STATE_IN_PROGRESS) {
            // We do not have an intent in progress so we should store the latest
            // error resolution intent for use when the sign in button is clicked.
            mSignInIntent = connectionResult.getResolution();
            mSignInError = connectionResult.getErrorCode();

            if (mSignInProgress == STATE_SIGN_IN) {
                // STATE_SIGN_IN indicates the user already clicked the sign in button
                // so we should continue processing errors until the user is signed in
                // or they click cancel.
                resolveSignInError();
            }
        }
        if (getActivity() != null) {
            setStatus(getString(R.string.status_signed_out));
        }

    }

    /* Starts an appropriate intent or dialog for user interaction to resolve
  * the current error preventing the user from being signed in.  This could
  * be a dialog allowing the user to select an account, an activity allowing
  * the user to consent to the permissions being requested by your app, a
  * setting to enable device networking, etc.
  */
    private void resolveSignInError() {
        Log.d(TAG, "resolveSignInError");
        if (mSignInIntent != null) {
            // We have an intent which will allow our user to sign in or
            // resolve an error.  For example if the user needs to
            // select an account to sign in with, or if they need to consent
            // to the permissions your app is requesting.

            try {
                // Send the pending intent that we stored on the most recent
                // OnConnectionFailed callback.  This will allow the user to
                // resolve the error currently preventing our connection to
                // Google Play services.
                mSignInProgress = STATE_IN_PROGRESS;
                // NB Remember to pass the onActivityResult from the main activity to this fragment
                // because it won't be routed here automatically
                getActivity().startIntentSenderForResult(mSignInIntent.getIntentSender(),
                    RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                Log.i(TAG, "Sign in intent could not be sent: "
                    + e.getLocalizedMessage());
                // The intent was canceled before it was sent.  Attempt to connect to
                // get an updated ConnectionResult.
                mSignInProgress = STATE_SIGN_IN;
                mGoogleApiClient.connect();
            }
        } else {
            // Google Play services wasn't able to provide an intent for some
            // error types, so we show the default Google Play services error
            // dialog which may still start an intent on our behalf if the
            // user can resolve the issue.
            getActivity().showDialog(DIALOG_PLAY_SERVICES_ERROR);
        }
    }

    public void signIn() {
        if (!mGoogleApiClient.isConnecting()) {
            Log.d(TAG, "signIn");
            resolveSignInError();
        }
    }

    public void signOut() {
        if (!mGoogleApiClient.isConnecting()) {
            Log.d(TAG, "signOut");
            // We clear the default account on sign out so that Google Play
            // services will not return an onConnected callback without user
            // interaction.
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }

    public void revoke() {
        if (!mGoogleApiClient.isConnecting()) {
            Log.d(TAG, "revoke");
            // After we revoke permissions for the user with a GoogleApiClient
            // instance, we must discard it and create a new one.
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            // Our sample has caches no user data from Google+, however we
            // would normally register a callback on revokeAccessAndDisconnect
            // to delete user data so that we comply with Google developer
            // policies.
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
            mGoogleApiClient = buildGoogleApiClient();
            mGoogleApiClient.connect();
        }
    }



    protected Dialog onCreateDialog(int id) {
        switch(id) {
        case DIALOG_PLAY_SERVICES_ERROR:
            if (GooglePlayServicesUtil.isUserRecoverableError(mSignInError)) {
                return GooglePlayServicesUtil.getErrorDialog(
                    mSignInError,
                    getActivity(),
                    RC_SIGN_IN,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Log.e(TAG, "Google Play services resolution cancelled");
                            mSignInProgress = STATE_DEFAULT;
                            setStatus(getString(R.string.status_signed_out));
                        }
                    });
            } else {
                return new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.play_services_error)
                    .setPositiveButton(R.string.close,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e(TAG, "Google Play services error could not be "
                                    + "resolved: " + mSignInError);
                                mSignInProgress = STATE_DEFAULT;
                                setStatus(getString(R.string.status_signed_out));
                            }
                        }).create();
            }
        default:
            return null;
        }
    }

    private void setStatus(final String msg) {
        this.status = msg;
        //now tell any listeners that the status changed
        Iterator<PlusStatusChangeListener> it = listeners.iterator();
        while(it.hasNext()){
            it.next().onPlusStatusChange(mGoogleApiClient.isConnected(), status);
        }
    }

    @Override
    public void onResult(People.LoadPeopleResult peopleData) {
        if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
            PersonBuffer personBuffer = peopleData.getPersonBuffer();
            try {
                peopleCount = personBuffer.getCount();
//                for (int i = 0; i < peopleCount; i++) {
//                    Log.d(TAG, "Display name: " + personBuffer.get(i).getDisplayName());
//                }
            } finally {
                personBuffer.close();
            }
        } else {
            Log.e(TAG, "Error requesting visible circles: " + peopleData.getStatus());
        }
    }

    @Override
    public void onAccessRevoked(ConnectionResult connectionResult) {

        currentPerson = null;
        peopleCount = 0;

    }

    public interface PlusStatusChangeListener {
        public void onPlusStatusChange(boolean isConnected, String status);
    }

    void register(PlusStatusChangeListener listener) {
        listeners.add(listener);
    }

    void unRegister(PlusStatusChangeListener listener) {
        listeners.remove(listener);
    }

    /**
     * Get an image asset for this user, either the image or the cover image
     * @return
     */
    public String getImageUrl() {
        String res = "";
        if (currentPerson != null) {
            if (currentPerson.hasImage()) {
                res = currentPerson.getImage().getUrl();
            } else if (currentPerson.hasCover()) {
                res = currentPerson.getCover().getCoverPhoto().getUrl();
            }
        }
        return res;
    }

    public String getInfoString() {
        String res = "";
        if (currentPerson != null) {
            if (currentPerson.hasNickname()) {
                res = currentPerson.getNickname();
            } else if (currentPerson.hasDisplayName()) {
                res = currentPerson.getDisplayName();
            } else if (currentPerson.hasName()) {
                res = currentPerson.getName().getFormatted();
            } else if (currentPerson.hasTagline()) {
                res = currentPerson.getTagline();
            }
        }
        return res;
    }

    public String getAnotherString() {
        String res = "";
        if (currentPerson != null) {
            if (currentPerson.hasUrl()) {
                res = currentPerson.getUrl();
            } else if (currentPerson.hasLanguage()) {
                res = currentPerson.getLanguage();
            }
        }
        return res;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

}
