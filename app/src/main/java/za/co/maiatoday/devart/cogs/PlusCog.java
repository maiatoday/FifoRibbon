package za.co.maiatoday.devart.cogs;

import android.graphics.Bitmap;
import android.media.FaceDetector;

public class PlusCog extends BaseCog {
    public PlusCog(FaceDetector.Face[] face, int[] colors) {
        super(face, colors);
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
