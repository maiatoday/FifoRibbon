package za.co.maiatoday.devart.cogs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.util.Log;

import java.util.Vector;

public class EyeBlockCog extends BaseCog {

    public EyeBlockCog(Vector<FaceDetector.Face> faces, int[] colors) {
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
        int rectangleCount = 4;
        Paint drawPaint = new Paint();

        drawPaint.setColor(Color.MAGENTA);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeWidth(1);

        Canvas canvas = new Canvas();
        canvas.setBitmap(mOut);

        PointF midPoint = new PointF();
        float eyeDistance = 0.0f;
        float confidence = 0.0f;
        int jump = in.getWidth() / 64;

        for (FaceDetector.Face face : faces) {
            face.getMidPoint(midPoint);
            eyeDistance = face.eyesDistance();
            confidence = face.confidence();

            Log.i("FaceDetector",
                "Confidence: " + confidence +
                    ", Eye distance: " + eyeDistance +
                    ", Mid Point: (" + midPoint.x + ", " + midPoint.y + ")"
            );
            for (int of = 0; of < rectangleCount; of += 1) {
                if (of % 2 == 0) {
                    drawPaint.setColor(Color.MAGENTA);
                } else if (of % 3 == 0) {
                    drawPaint.setColor(Color.RED);
                } else {
                    drawPaint.setColor(Color.YELLOW);
                }
                canvas.drawRect((int) midPoint.x - eyeDistance + of,
                    (int) midPoint.y - eyeDistance / 2 + of * jump,
                    (int) midPoint.x + eyeDistance + of * jump,
                    (int) midPoint.y + eyeDistance / 2 + of * jump, drawPaint);
            }
        }
        setStatus("eye ");
        return mOut;
    }

}
