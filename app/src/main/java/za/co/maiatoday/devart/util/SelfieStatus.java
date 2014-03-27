package za.co.maiatoday.devart.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.FaceDetector;
import android.util.Log;

import java.util.Random;
import java.util.Vector;

import za.co.maiatoday.devart.cogs.BaseCog;
import za.co.maiatoday.devart.cogs.DotCog;
import za.co.maiatoday.devart.cogs.EyeBlockCog;
import za.co.maiatoday.devart.cogs.GlitchCog;
import za.co.maiatoday.devart.cogs.ShiftCog;
import za.co.maiatoday.devart.color.MedianCutQuantizer;

/**
 * Created by maia on 2013/08/22.
 */
public class SelfieStatus {
    Bitmap bmpToPost;
    String mStatus;
    Bitmap orig;
    boolean processDone = false;

    private static final int MAX_FACES = 5;
    private FaceDetector mDetector;
    private Vector<FaceDetector.Face> mFaces = new Vector<FaceDetector.Face>();

    int[] mColors;
	
    Vector<BaseCog> boxOfCogs = new Vector<BaseCog>();

    private WaysToChange waysToChange = WaysToChange.EIGHT_BIT_ROY;

    Random r = new Random(System.currentTimeMillis());

    enum WaysToChange {
        CANNNY_KONNY,
        EIGHT_BIT_ROY,
        ANOTHER_ONE_FOR_LUCK,
        NO_POINT_IN_HOLDING_ON,
        GLITCHY_GOODNESS_DELUXE,
        LINES;
        private static final int size = WaysToChange.values().length;

        public static WaysToChange rollDice(Random r) {
            //Roll the dice to see which technique to use
            return WaysToChange.values()[r.nextInt(size - 1)];
//            return LINES;
        }

    }

    void SelfieStatus() {
    }

//    public void setProcessDone(boolean processDone) {
//        this.processDone = processDone;
//    }

    public Bitmap getOrig() {
        return orig;
    }

    public void setOrig(Bitmap orig) {
        this.orig = orig;
        this.bmpToPost = orig;
//        processDone = false;
        mFaces = detectFaces(orig);
        detectColours();
    }

    public void pickCogs() {
        //TODO pick cogs
//        waysToChange = WaysToChange.rollDice(r);
        boxOfCogs.clear();
        int cogLoop = r.nextInt(2);
        boxOfCogs.add(new DotCog(mFaces, mColors));
        for (int i = 0; i <= cogLoop; i++ ) {
            boxOfCogs.add(new GlitchCog(mFaces, mColors));
            boxOfCogs.add(new EyeBlockCog(mFaces, mColors));
        }
        boxOfCogs.add(new EyeBlockCog(mFaces, mColors));
        boxOfCogs.add(new DotCog(mFaces, mColors));

    }

    public Bitmap getBmpToPost() {
        return bmpToPost;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }

//    public boolean isProcessDone() {
//        return processDone;
//    }

    public boolean processSelfie() {
        if (orig == null) {
            return false;
        }
//        if (processDone) {
//            return true;
//        }
        StringBuilder status = new StringBuilder(140);
        status.append("#autoselfie #devart ");
        bmpToPost = BaseCog.copy(orig);
        for (BaseCog cog : boxOfCogs) {
           bmpToPost = cog.spin(bmpToPost, false);
           status.append(cog.getStatus());
        }
        Vector<FaceDetector.Face> facesLeft = detectFaces(bmpToPost);
        if (facesLeft.size() > 0) {
            ShiftCog sc = new ShiftCog(mFaces, mColors);
            bmpToPost = sc.spin(bmpToPost, false);
            status.append(sc.getStatus());
        }
        Log.i("SelfieStatus", status.toString());
        mStatus = status.toString();
//        processDone = true;
        return true;
    }

    private void detectColours() {
        MedianCutQuantizer mcq = new MedianCutQuantizer(orig, 10);
        mColors = mcq.getQuantizedColorsInt();
    }

    public int[] getmColors() {
        return mColors;
    }


    private Vector<FaceDetector.Face> detectFaces(Bitmap orig) {
        Vector<FaceDetector.Face> faces = new Vector<FaceDetector.Face>();
        if (null != orig) {
            int width = orig.getWidth();
            int height = orig.getHeight();

            mDetector = new FaceDetector(width, height, MAX_FACES);
            FaceDetector.Face[] tempFaces = new FaceDetector.Face[MAX_FACES];

            Bitmap temp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            Paint ditherPaint = new Paint();
            ditherPaint.setDither(true);

            Canvas canvas = new Canvas();
            canvas.setBitmap(temp);
            canvas.drawBitmap(orig, 0, 0, ditherPaint);

            int facesFound = mDetector.findFaces(temp, tempFaces);

            Log.i("FaceDetector", "Number of mFaces found: " + facesFound);
            for (int i = 0; i < facesFound; i++) {
                faces.add(tempFaces[i]);
            }
        }
        return faces;
    }

}
