package za.co.maiatoday.devart.util;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.List;
import java.util.Vector;

/**
 * Created by maia on 2014/02/28.
 *
 * lifted some javascript here https://github.com/lokesh/color-thief/blob/master/js/color-thief.js
 * Thanks Lokesh Dhakar
 */
public class ColourThief {
    int count = 10;
    List<Integer> palette = new Vector<Integer>();

    public List<Integer> getPalette() {
        return palette;
    }

    public int getColour(Bitmap image, int quality) {
        getPalette(image, count, quality);
        if (palette.size() > 0) {
            return palette.get(0);
        }
        return Color.RED;
    }


    public List<Integer> getPalette(Bitmap image, int colourCount, int quality) {
//        // Create custom CanvasImage object
//        var image      = new CanvasImage(sourceImage);
//        var imageData  = image.getImageData();
//        var pixels     = imageData.data;
//        var pixelCount = image.getPixelCount();
//
//        // Store the RGB values in an array format suitable for quantize function
//        var pixelArray = [];
//        for (var i = 0, offset, r, g, b, a; i < pixelCount; i = i + quality) {
//            offset = i * 4;
//            r = pixels[offset + 0];
//            g = pixels[offset + 1];
//            b = pixels[offset + 2];
//            a = pixels[offset + 3];
//            // If pixel is mostly opaque and not white
//            if (a >= 125) {
//                if (!(r > 250 && g > 250 && b > 250)) {
//                    pixelArray.push([r, g, b]);
//                }
//            }
//        }
//
//        // Send array to quantize function which clusters values
//        // using median cut algorithm
//        var cmap    = MMCQ.quantize(pixelArray, colorCount);
//        var palette = cmap.palette();
//
//        // Clean up
//        image.removeCanvas();
        return palette;
    }




}
