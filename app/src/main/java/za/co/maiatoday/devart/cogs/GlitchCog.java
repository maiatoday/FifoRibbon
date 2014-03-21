package za.co.maiatoday.devart.cogs;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.FaceDetector;
import android.util.Log;

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
        blocks.clear();
        int ruleOfThirds = in.getHeight()/3;

        int w = mOut.getWidth();
        int h = mOut.getHeight();
        int xJump = w / 8;
        int yJump = h / 8;
        int dx = r.nextInt(xJump/2);
        int dy = r.nextInt(yJump/2);
        int j = 2;
        for (int x = 0; x < w; x += 2 * xJump) {
            for (int y = 0; y < h; y += 2 * yJump) {
                GlitchBlock gb = new GlitchBlock();
                gb.bounds = new RectF(x, y, x + xJump + dx, y + yJump + dy);
                gb.shiftReg = j;
                gb.noShift = nearFace(gb.bounds);
                blocks.add(gb);
                j += 2;
                dx = xJump - r.nextInt(xJump * 2);
				dy = yJump - r.nextInt(yJump * 2);			
            }
        }
        for (GlitchBlock b : blocks) {
           Log.d("GlitchBlock", b.toString());
            glitchImage(b, tempglitchfx);
        }
        mOut = tempglitchfx.getBitmap();
        setStatus("glitch ");
        return mOut;
    }

    /**
     * Checks if the rectangle described by bounds is near a detected face
     * @param bounds
     * @return
     */
    private boolean nearFace(RectF bounds) {
        if (facesFound > 0) {
            for (int index = 0; index < facesFound; ++index) {
                //TODO what about confidence?
                PointF midPoint = new PointF();
                faces[index].getMidPoint(midPoint);
                return bounds.contains(midPoint.x, midPoint.y);
            }
        }
        return false;
    }


    private void glitchImage(GlitchBlock block, GlitchFX gg) {
        if (gg == null) return;
        if (block == null || block.bounds == null) return;
        gg.open();
        gg.glitch(block.bounds, block.xOffset, block.yOffset, block.noShift, block.shiftReg);
        gg.close();
        mOut = gg.getBitmap();
    }


    /**
     * contains detail about the block in the image that will be glitched
     */
    private class GlitchBlock {
        /** The bounds of the area to glitch */
        RectF bounds;
        /** The x offset of the glitch shift area */
        int xOffset;
        /** The y offset of the glitch shift area */
        int yOffset;
        /** Must it apply the shift register or not */
        boolean noShift;
        /** number of bits to shift */
        int shiftReg;

        @Override
        public String toString() {
            return bounds.toString() + " noShift " + noShift + " shiftReg " + shiftReg;
        }
    }
}
