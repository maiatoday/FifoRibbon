package za.co.maiatoday.devart.cogs;

import android.graphics.Bitmap;
import android.media.FaceDetector;

import java.util.Vector;

public class PlusCog extends BaseCog {
    public PlusCog(Vector<FaceDetector.Face> faces, int[] colors) {
        super(faces,colors);
    }

    @Override
    public Bitmap spin(Bitmap in, boolean copy) {
        this.mIn = in;
        if (copy) {
            this.mOut = copy(mIn);
        } else {
            this.mOut = mIn;
        }
        return mOut;
    }
}
