package za.co.maiatoday.devart.cogs;

import android.media.FaceDetector;

/**
 * Created by maia on 2014/03/07.
 */
public class BaseCog {
    private FaceDetector.Face[] faces;
    int[] colors;

    public BaseCog(CogBuilder builder) {
        faces = builder.faces;
        colors = builder.colors;
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

