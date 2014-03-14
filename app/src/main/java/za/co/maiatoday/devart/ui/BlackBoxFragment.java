package za.co.maiatoday.devart.ui;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.ImageView;
import android.widget.TextView;

import za.co.maiatoday.devart.util.SelfieStatus;

/**
 * Headless Fragment to do ImageMutating so that it will look after the AsyncTask if the Activity is recreated
 * Created by maia on 2014/03/14.
 *
 */
public class BlackBoxFragment extends Fragment {
    private static String TAG = BlackBoxFragment.class.toString();
    SelfieStatus selfie = new SelfieStatus();

    ImageView mImageView;
    TextView mTextView;
    private BlackBoxTask blackBoxTask;

    public BlackBoxFragment() {
    }

    /**
     * Create a new instance of PlusFragment, with parameters passed in a bundle
     */
    public static BlackBoxFragment getInstance(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        BlackBoxFragment fragment = (BlackBoxFragment) fragmentManager
            .findFragmentByTag(BlackBoxFragment.TAG);
        if (fragment == null) {
            fragment = new BlackBoxFragment();
            fragmentManager.beginTransaction().add(fragment,
                BlackBoxFragment.TAG).commit();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (savedInstanceState != null) {
//        }

        // Keep Fragment around
        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (blackBoxTask != null) {
            blackBoxTask.cancel(true);
        }
    }

    /**
     * Shake the box to choose different cogs
     */
    public void shake() {
        selfie.setProcessDone(false);
        selfie.pickCogs();
    }

    /**
     * Turn the handle to make the cogs change the image
     */
    public void turnHandle() {
        blackBoxTask = new BlackBoxTask();
        blackBoxTask.execute();
    }

    /**
     * Set the imageView which the box must update to see the changes
     * @param imageView
     */
    public void setImageView(ImageView imageView) {
        this.mImageView = imageView;
    }

    /**
     * Set the textView which the box must update to see the generated status
     * @param textView
     */
    public void setTextView(TextView textView) {
        this.mTextView = textView;
    }

    public String getStatus() {
        return selfie.getStatus();
    }

    public Bitmap getBitmap() {
        return selfie.getBmpToPost();
    }

    public Bitmap getOriginalBitmap() {
        return selfie.getOrig();
    }

    public void setBitmap(Bitmap bitmap) {
        selfie.setOrig(bitmap);
    }

    public boolean isDone() {
        return selfie.isProcessDone();
    }

    private class BlackBoxTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {
            selfie.processSelfie();
            return selfie.getBmpToPost();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (mImageView != null) {
                mImageView.setImageBitmap(bitmap);
            }
            if (mTextView != null) {
                mTextView.setText(selfie.getStatus());
            }
        }
    }

}
