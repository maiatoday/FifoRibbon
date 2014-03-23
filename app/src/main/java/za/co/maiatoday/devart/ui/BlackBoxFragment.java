package za.co.maiatoday.devart.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import za.co.maiatoday.devart.util.SelfieStatus;

/**
 * Headless Fragment to do ImageMutating so that it will look after the AsyncTask if the Activity is recreated
 * Created by maia on 2014/03/14.
 */
public class BlackBoxFragment extends Fragment {
    private static String TAG = BlackBoxFragment.class.toString();
    SelfieStatus mSelfie = new SelfieStatus();

    private BlackBoxTask mBlockBoxTask;
    private LoadBitmapTask mLoadUrlTask;
    private boolean mIsDone;

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
        if (mBlockBoxTask != null) {
            mBlockBoxTask.cancel(true);
        }
        if (mLoadUrlTask != null) {
            mLoadUrlTask.cancel(true);
        }
    }

    /**
     * Shake the box to choose different cogs
     */
    public void shake() {
        mSelfie.pickCogs();
    }

    /**
     * Turn the handle to make the cogs change the image
     */
    public void turnHandle() {
        mBlockBoxTask = new BlackBoxTask();
        mBlockBoxTask.execute();
    }

    public String getStatus() {
        return mSelfie.getStatus();
    }

    public Bitmap getBitmap() {
        return mSelfie.getBmpToPost();
    }

    public Bitmap getOriginalBitmap() {
        return mSelfie.getOrig();
    }

    public void setBitmap(Bitmap bitmap) {
        mSelfie.setOrig(bitmap);
    }

    public boolean isDone() {
//        return mSelfie.isProcessDone();
        return mIsDone;
    }

    private class BlackBoxTask extends AsyncTask<Void, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
            mIsDone = false;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            mSelfie.processSelfie();
            return mSelfie.getBmpToPost();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            showProgress(false);
            mIsDone = true;
            statusChange(bitmap, mSelfie.getStatus());
        }
    }

    private class LoadBitmapTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            URL url = null;
            Bitmap bmp = null;
            try {
                url = new URL(params[0]);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            statusChange(bitmap, mSelfie.getStatus());
            setBitmap(bitmap);
//            shake();
//            turnHandle();
        }
    }

    public void makeDefaultImage() {
        PlusFragment p = PlusFragment.getInstance(getActivity());
        String imageUrl = p.getImageUrl();
        String s2 = p.getInfoString();
        String s3 = p.getAnotherString();
        if (!TextUtils.isEmpty(imageUrl)) {
            mLoadUrlTask = new LoadBitmapTask();
            mLoadUrlTask.execute(new String[]{imageUrl});
        }
    }

    public interface BlackBoxStatusChangeListener {
        public void onBBStatusChange(Bitmap bmp, String status);

        public void onShowProgress(boolean show);
    }

    CopyOnWriteArrayList<BlackBoxStatusChangeListener> listeners = new CopyOnWriteArrayList<BlackBoxStatusChangeListener>();

    void register(BlackBoxStatusChangeListener listener) {
        listeners.add(listener);
    }

    void unRegister(BlackBoxStatusChangeListener listener) {
        listeners.remove(listener);
    }

    private void statusChange(Bitmap bitmap, String status) {
        Iterator<BlackBoxStatusChangeListener> it = listeners.iterator();
        while (it.hasNext()) {
            it.next().onBBStatusChange(bitmap, status);
        }

    }

    private void showProgress(boolean show) {
        Iterator<BlackBoxStatusChangeListener> it = listeners.iterator();
        while (it.hasNext()) {
            it.next().onShowProgress(show);
        }

    }

}
