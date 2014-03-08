package za.co.maiatoday.devart.cogs;

import android.media.FaceDetector;
import android.graphics.*;
import java.util.Random;
/**
 * Created by maia on 2014/03/07.
 */
public class BaseCog {
    protected FaceDetector.Face[] faces;
	int facesFound;
    int[] colors;
	Bitmap in,out;
	
	Random r = new Random(System.currentTimeMillis());
	
    public BaseCog(CogBuilder builder) {
        faces = builder.faces;
		facesFound = faces.length;
        colors = builder.colors;
    }
	
	public Bitmap spin(Bitmap in) {
		out = in.copy(in.getConfig(), true);
		return out;
	}


    public static class CogBuilder {
        private FaceDetector.Face[] faces;
        int[] colors;

        public BaseCog build() {
            return new BaseCog(this);
        }

        CogBuilder setFaces(FaceDetector.Face[] face) {
            this.faces = faces;
            return this;
        }

        CogBuilder setColors(int[] colors) {
            this.colors = colors;
            return this;
        }

    }
}

