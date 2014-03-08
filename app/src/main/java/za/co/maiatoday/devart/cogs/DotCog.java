package za.co.maiatoday.devart.cogs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * cog to draw psuedo benday dots
 */
public class DotCog extends BaseCog {

    public DotCog(CogBuilder builder) {
        super(builder);
    }

    @Override
    public Bitmap spin(Bitmap in) {

        super.spin(in);

        Paint drawPaint = new Paint();
        int radius = in.getWidth() / 200;
        drawPaint.setStyle(Paint.Style.FILL);

        Canvas canvas = new Canvas();
        canvas.setBitmap(out);
        int newBlack = Color.argb(0xff, 0x16, 0x16, 0x16);
        for (int x = 0; x < in.getWidth(); x += radius * 4) {
            for (int y = 0; y < in.getHeight(); y += radius * 4) {
                int p = in.getPixel(x, y);
//                int pixelAlpha=   Color.alpha(p);
//                int red =  Color.red(p);
//                int green=    Color.green(p);
//                int blue=    Color.blue(p);
//                int newColor = Color.argb(pixelAlpha, red + 5, green + 5, blue + 5);
                if (p == Color.BLACK) {
                    drawPaint.setColor(newBlack);
                    canvas.drawCircle(x, y, radius, drawPaint);
                } else if (p == Color.RED) {
                    drawPaint.setColor(Color.MAGENTA);
                    canvas.drawCircle(x, y, radius, drawPaint);
                }
            }
        }

        return out;
    }

}
