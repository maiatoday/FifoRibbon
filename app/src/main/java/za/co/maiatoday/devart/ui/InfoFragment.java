package za.co.maiatoday.devart.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import za.co.maiatoday.devart.R;

/**
 * Created by maia on 2013/09/01.
 */
public class InfoFragment extends Fragment implements View.OnClickListener{
    private TextView mInfoText;
    private SignInButton mSignInButton;
    private Button mSignOutButton;
    private Button mRevokeButton;
    private TextView mStatus;

    // TODO need to move it into a headless fragment
    // GoogleApiClient wraps our service connection to Google Play services and
    // provides access to the users sign in state and Google's APIs.
    private GoogleApiClient mGoogleApiClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        mInfoText = (TextView) view.findViewById(R.id.lblInfo);
        mSignInButton = (SignInButton) view.findViewById(R.id.sign_in_button);
        mSignOutButton = (Button) view.findViewById(R.id.sign_out_button);
        mRevokeButton = (Button) view.findViewById(R.id.revoke_access_button);
        mStatus = (TextView) view.findViewById(R.id.sign_in_status);
        try {
            String verStr = "Version: " + getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
            TextView version = (TextView) view.findViewById(R.id.lblVersion);
            version.setText(verStr);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Button btnMoreInfo = (Button) view.findViewById(R.id.btnGotoWeb);

        mInfoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/search?q=%23autoselfie&s=typd&f=realtime")));
            }
        });

        btnMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.maiatoday.co.za")));
            }
        });

        mSignInButton.setOnClickListener(this);
        mSignOutButton.setOnClickListener(this);
        mRevokeButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setButtonsView();
    }


    private void setButtonsView() {
        // Hide buttons
    }

    @Override
    public void onClick(View v) {

//    TODO    if (!mGoogleApiClient.isConnecting()) {
            // We only process button clicks when GoogleApiClient is not transitioning
            // between connected and not connected.
            switch (v.getId()) {
            case R.id.sign_in_button:
                mStatus.setText(R.string.status_signing_in);
              //TODO add  resolveSignInError();
                break;
            case R.id.sign_out_button:
                // We clear the default account on sign out so that Google Play
                // services will not return an onConnected callback without user
                // interaction.
                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                mGoogleApiClient.disconnect();
                mGoogleApiClient.connect();
                break;
            case R.id.revoke_access_button:
                // After we revoke permissions for the user with a GoogleApiClient
                // instance, we must discard it and create a new one.
                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                // Our sample has caches no user data from Google+, however we
                // would normally register a callback on revokeAccessAndDisconnect
                // to delete user data so that we comply with Google developer
                // policies.
                Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
              //TODO  mGoogleApiClient = buildGoogleApiClient();
                mGoogleApiClient.connect();
                break;
            }
    //    }
    }
}
