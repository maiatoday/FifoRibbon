package za.co.maiatoday.devart.cogs;

import android.graphics.Bitmap;
import android.media.FaceDetector;

import java.util.Random;
/**
 * Created by maia on 2014/03/07.
 */
public abstract class BaseCog {
    protected FaceDetector.Face[] faces;
	int facesFound;
    int[] colors;
	Bitmap mIn, mOut;
	
	Random r = new Random(System.currentTimeMillis());
    private String mStatus;

    public BaseCog(FaceDetector.Face[] face, int facesFound, int[] colors) {
        this.faces = face;
        this.facesFound = facesFound;
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

