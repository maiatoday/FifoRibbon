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

import za.co.maiatoday.devart.R;

/**
 * Created by maia on 2013/09/01.
 */
public class InfoFragment extends Fragment implements View.OnClickListener, PlusFragment.PlusStatusChangeListener {
    private TextView mInfoText;
    private SignInButton mSignInButton;
    private Button mSignOutButton;
    private Button mRevokeButton;
    private TextView mStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/s/%23autoselfie")));
            }
        });

        btnMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://devart.withgoogle.com/#/project/16858063")));
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
        PlusFragment plusFragment = PlusFragment.getInstance(getActivity());
        setButtonsView(plusFragment.isConnected(), plusFragment.getStatus());
        plusFragment.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        PlusFragment plusFragment = PlusFragment.getInstance(getActivity());
        plusFragment.unRegister(this);
    }

    private void setButtonsView(boolean isConnected, String msg) {

        mSignInButton.setEnabled(!isConnected);
        mSignOutButton.setEnabled(isConnected);
        mRevokeButton.setEnabled(isConnected);
        mStatus.setText(msg);
    }

    @Override
    public void onClick(View v) {
        // We only process button clicks when GoogleApiClient is not transitioning
        // between connected and not connected.
        PlusFragment plusFragment = PlusFragment.getInstance(getActivity());
        switch (v.getId()) {
        case R.id.sign_in_button:
            mStatus.setText(R.string.status_signing_in);
            plusFragment.signIn();
            break;
        case R.id.sign_out_button:
            plusFragment.signOut();
            break;
        case R.id.revoke_access_button:
            plusFragment.revoke();
            break;
        }
    }

    @Override
    public void onPlusStatusChange(boolean isConnected, String status) {
        setButtonsView(isConnected, status);
    }
}
