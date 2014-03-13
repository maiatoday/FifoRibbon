package za.co.maiatoday.devart.cogs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.FaceDetector;

import java.util.Vector;

/**
 * cog to draw psuedo benday dots
 */
public class DotCog extends BaseCog {

    Vector<float[]> colorHSV = new Vector<float[]>();
    public DotCog(FaceDetector.Face[] face, int facesFound, int[] colors) {
        super(face, facesFound, colors);
        for (int i = 0; i< colors.length; i++) {
            float[] hsv = new float[3];
            Color.colorToHSV(colors[i], hsv);
            colorHSV.add(hsv);
        }
    }

    @Override
    public Bitmap spin(Bitmap in, boolean copy) {
        this.mIn = in;
        if (copy) {
            this.mOut = copy(mIn);
        } else {
            this.mOut = mIn;
        }
        Paint drawPaint = new Paint();
        int radius = mIn.getWidth() / 200;
        drawPaint.setStyle(Paint.Style.FILL);

        Canvas canvas = new Canvas();
        canvas.setBitmap(mOut);
        int newBlack = Color.argb(0xff, 0x16, 0x16, 0x16);
        for (int x = 0; x < mIn.getWidth(); x += radius * 4) {
            for (int y = 0; y < mIn.getHeight(); y += radius * 4) {
                int p = mIn.getPixel(x, y);
//                int pixelAlpha=   Color.alpha(p);
//                int red =  Color.red(p);
//                int green=    Color.green(p);
//                int blue=    Color.blue(p);
//                int newColor = Color.argb(pixelAlpha, red + 5, green + 5, blue + 5);
                float[] pHSV = new float[3];
                Color.colorToHSV(p, pHSV);
                for (float[] hsv : colorHSV ) {
                   if ((pHSV[0] == hsv[0] || pHSV[1] == hsv[1] || pHSV[2] == hsv[2]))  {
                       drawPaint.setColor(Color.MAGENTA);
                       canvas.drawCircle(x, y, radius, drawPaint);
                   }
                }

//                if (p == Color.BLACK) {
//                    drawPaint.setColor(newBlack);
//                    canvas.drawCircle(x, y, radius, drawPaint);
//                } else if (p == Color.RED) {
//                    drawPaint.setColor(Color.MAGENTA);
//                    canvas.drawCircle(x, y, radius, drawPaint);
//                }
            }
        }
        setStatus("dot ");

        return mOut;
    }

}
