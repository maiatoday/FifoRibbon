package za.co.maiatoday.devart.cogs;

import android.graphics.Bitmap;
import android.media.FaceDetector;

import java.util.Random;
import java.util.Vector;

/**
 * Created by maia on 2014/03/07.
 */
public abstract class BaseCog {
    protected Vector<FaceDetector.Face> faces;
    int[] colors;
	Bitmap mIn, mOut;
	
	Random r = new Random(System.currentTimeMillis());
    private String mStatus;

    public BaseCog(Vector<FaceDetector.Face> faces, int[] colors) {
        this.faces = faces;
        this.colors = colors;

    }
	
	abstract public Bitmap spin(Bitmap in, boolean copy);

    public static Bitmap copy(Bitmap in) {
       return in.copy(in.getConfig(), true);
    }

    public String getStatus() {
        return mStatus;
    }

    protected void setStatus(String status) {
        this.mStatus = status;
    }
}

