package za.co.maiatoday.devart.cogs;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.media.FaceDetector;

import java.util.Random;
import java.util.Vector;

import za.co.maiatoday.devart.glitchP5.GlitchFX;

public class GlitchCog extends BaseCog {
    private int magic = 20;
    Random r = new Random();
    Vector<GlitchBlock> blocks = new Vector<GlitchBlock>();

    public GlitchCog(FaceDetector.Face[] face, int facesFound, int[] colors) {
        super(face, facesFound, colors);
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
//        RectF wholePic = new RectF(0, 0, bmpToPost.getWidth(), bmpToPost.getHeight());
//        int xjump = bmpToPost.getWidth()/16;
        int yjump = mOut.getHeight() / 16;
        int dx = r.nextInt(yjump) - yjump / 2;
        int j = 2;
        for (int i = 0; i < in.getWidth(); i += 2 * yjump) {
            GlitchBlock gb = new GlitchBlock();
            gb.bounds = new RectF(i, dx, i + yjump + dx, mOut.getHeight() - yjump - dx);
//            gb.shiftReg = r.nextInt(16);
            gb.shiftReg = j;
            j += 2;
            gb.doShift = true;
            glitchImage(gb, tempglitchfx);
            dx = r.nextInt(yjump * 2) - yjump;

        }
        mOut = tempglitchfx.getBitmap();
        setStatus("glitch ");
        return mOut;
    }

    private void glitchImage(GlitchBlock block, GlitchFX gg) {
        if (gg == null) return;
        if (block == null || block.bounds == null) return;
        gg.open();
        gg.glitch(block.bounds, block.xOffset, block.yOffset, block.doShift, block.shiftReg);
        gg.close();
        mOut = gg.getBitmap();
    }


    private class GlitchBlock {
        /** The bounds of the area to glitch */
        RectF bounds;
        /** The x offset of the glitch shift area */
        int xOffset;
        /** The y offset of the glitch shift area */
        int yOffset;
        /** Must it apply the shift register or not */
        boolean doShift;
        /** number of bits to shift */
        int shiftReg;
    }
}
