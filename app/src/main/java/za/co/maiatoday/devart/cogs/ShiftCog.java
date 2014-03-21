package za.co.maiatoday.devart.cogs;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.media.FaceDetector;
import android.util.Log;

import java.util.Vector;

import za.co.maiatoday.devart.glitchP5.GlitchFX;

public class ShiftCog extends GlitchCog {

    public ShiftCog(Vector<FaceDetector.Face> faces, int[] colors) {
        super(faces, colors);
    }

    @Override
    public Bitmap spin(Bitmap in, boolean copy) {
        this.mIn = in;
        if (copy) {
            this.mOut = copy(mIn);
        } else {
            this.mOut = mIn;
        }
        GlitchFX tempglitchfx = new GlitchFX(in);
        blocks.clear();

        int w = mOut.getWidth();
        int h = mOut.getHeight();
        int xJump = w / 8;
        int dx = r.nextInt(xJump / 2);
        int blockh = r.nextInt(h) + h/2;
        for (int x = 0; x < w; x += 2 * xJump) {
                GlitchBlock gb = new GlitchBlock();
                gb.bounds = new RectF(x, 0+dx, x + xJump + dx, blockh-dx);
                gb.shiftReg = 2;
                gb.noShift = true;
                blocks.add(gb);
                dx = xJump - r.nextInt(xJump * 2);
        }
        for (GlitchBlock b : blocks) {
            Log.d("GlitchBlock", b.toString());
            glitchImage(b, tempglitchfx);
        }
        mOut = tempglitchfx.getBitmap();
        setStatus("shift ");
        return mOut;
    }
}
