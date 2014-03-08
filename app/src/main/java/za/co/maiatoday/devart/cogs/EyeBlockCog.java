package za.co.maiatoday.devart.cogs;
import android.graphics.*;
import android.util.Log;

public class EyeBlockCog extends BaseCog
{

	@Override
	public Bitmap spin(Bitmap in)
	{
		int count = 4;
		super.spin(in);
        Paint drawPaint = new Paint();

        drawPaint.setColor(Color.MAGENTA);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeWidth(1);

        Canvas canvas = new Canvas();
        canvas.setBitmap(out);

        PointF midPoint = new PointF();
        float eyeDistance = 0.0f;
        float confidence = 0.0f;
        int jump = in.getWidth() / 64;

        if (facesFound > 0) {
            for (int index = 0; index < facesFound; ++index) {
                faces[index].getMidPoint(midPoint);
                eyeDistance = faces[index].eyesDistance();
                confidence = faces[index].confidence();

                Log.i("FaceDetector",
					  "Confidence: " + confidence +
					  ", Eye distance: " + eyeDistance +
					  ", Mid Point: (" + midPoint.x + ", " + midPoint.y + ")");
                for (int of = 0; of < count; of += 1) {
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
        }
        return out;
	}
	
}
