package za.co.maiatoday.devart.cogs;
import android.graphics.*;
import za.co.maiatoday.devart.glitchP5.GlitchFX;

public class GlitchCog extends BaseCog
{
	private int magic = 20;

	@Override
	public Bitmap spin(Bitmap in)
	{
		out = super.spin(in);
		GlitchFX tempglitchfx = new GlitchFX(out);
//        RectF wholePic = new RectF(0, 0, bmpToPost.getWidth(), bmpToPost.getHeight());
//        int xjump = bmpToPost.getWidth()/16;
        int yjump = out.getHeight() / 16;
        int dx = r.nextInt(yjump) - yjump / 2;
        for (int i = 0; i < in.getWidth(); i += 2 * yjump) {
            RectF strip = new RectF(i, dx, i + yjump + dx, out.getHeight() - yjump - dx);
            glitchImage(strip, magic, tempglitchfx);
            dx = r.nextInt(yjump * 2) - yjump;

        }
		return out;
	}
	
	private void glitchImage(RectF bounds, int extraMagic, GlitchFX gg) {
        if (gg == null) return;
        if (extraMagic == 0) {
            extraMagic = magic;
        }
        gg.open();
        gg.glitch((int) bounds.centerX(), (int) bounds.centerY(), (int) bounds.width(), (int) bounds.height(), extraMagic, extraMagic);
        gg.close();
        out = gg.getBitmap();
    }
}
