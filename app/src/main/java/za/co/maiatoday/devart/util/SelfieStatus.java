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
import za.co.maiatoday.devart.color.MedianCutQuantizer;

/**
 * Created by maia on 2013/08/22.
 */
public class SelfieStatus {
    private static final int DRIP_COUNT = 20;
    Bitmap bmpToPost;
    String status;
    Bitmap orig;
    boolean processDone = false;

    private static final int MAX_FACES = 5;
    private FaceDetector detector;
    private FaceDetector.Face[] faces;
    int facesFound;

    int[] colors;
	
    Vector<BaseCog> boxOfCogs = new Vector<BaseCog>();

    private int magic = 20;
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

    public void setProcessDone(boolean processDone) {
        this.processDone = processDone;
    }

    public Bitmap getOrig() {
        return orig;
    }

    public void setOrig(Bitmap orig) {
        this.orig = orig;
        this.bmpToPost = orig;
        processDone = false;
        magic = orig.getWidth() / 16;
        detectFaces();
        detectColours();
    }

    public void pickCogs() {
        //TODO pick cogs
//        waysToChange = WaysToChange.rollDice(r);
        boxOfCogs.clear();
        boxOfCogs.add(new DotCog(faces, colors));
        boxOfCogs.add(new GlitchCog(faces, colors));
        boxOfCogs.add(new EyeBlockCog(faces, colors));

    }

    public Bitmap getBmpToPost() {
        return bmpToPost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean processSelfie() {
        if (orig == null) {
            return false;
        }
        if (processDone) {
            return true;
        }
        status = "#autoselfie ";
        Bitmap bmpToPost = BaseCog.copy(orig);
        for (BaseCog cog : boxOfCogs) {
           bmpToPost = cog.spin(bmpToPost, false);
           status += cog.getStatus();
        }
        Log.i("SelfieStatus", status);
        processDone = true;
        return true;
    }

    private void detectColours() {
        MedianCutQuantizer mcq = new MedianCutQuantizer(orig, 10);
        colors = mcq.getQuantizedColorsInt();
    }

    public int[] getColors() {
        return colors;
    }


    private void detectFaces() {
        if (null != orig) {
            int width = orig.getWidth();
            int height = orig.getHeight();

            detector = new FaceDetector(width, height, MAX_FACES);
            faces = new FaceDetector.Face[MAX_FACES];

            Bitmap temp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            Paint ditherPaint = new Paint();
            ditherPaint.setDither(true);

            Canvas canvas = new Canvas();
            canvas.setBitmap(temp);
            canvas.drawBitmap(orig, 0, 0, ditherPaint);

            facesFound = detector.findFaces(temp, faces);

            Log.i("FaceDetector", "Number of faces found: " + facesFound);
        }
    }

}
