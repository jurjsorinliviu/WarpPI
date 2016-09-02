package org.warp.engine;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.warp.picalculator.Main;
import org.warp.picalculator.Utils;

/**
 *
 * @author andreacv
 */
public class RAWFont {

    public boolean[][] rawchars;
    public int[] chars32;
    public long[] chars64;
    public static final boolean is64 = true;
    public int minBound = 10;
    public int maxBound = 9599;
    public int charW;
    public int charH;
    public int charS;

    public void create(String name) {
    	try {
			loadFont("/font_"+name+".rft");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
        if (is64) {
            chars64 = new long[maxBound-minBound];
        } else {
            chars32 = new int[(maxBound-minBound)*2];
        }
        for (int charIndex = 0; charIndex < maxBound-minBound; charIndex++) {
            if (is64) {
                boolean[] currentChar = rawchars[charIndex];
                if (currentChar == null) {
                    chars64[charIndex] = 0x1FFFFFFFFFFFL;
                } else {
                    long result = 0, l = charS;
                    for (int i = 0; i < l; ++i) {
                        result = (result << 1) | (currentChar[i] ? 1L : 0L);
                    }
                    chars64[charIndex] = result;
                }
            } else {
                boolean[] currentChar = rawchars[charIndex];
                if (currentChar == null) {
                    chars32[charIndex*2] = 0x1FFFFFFF;
                    chars32[(charIndex*2)+1] = 0xFFFF;
                } else {
                    int result1 = 0, result2 = 0, l1 = 29, l2 = currentChar.length;
                    for (int i = 0; i < l1; ++i) {
                        result1 = (result1 << 1) + (currentChar[i] ? 1 : 0);
                    }
                    for (int i = l1; i < l2; ++i) {
                        result2 = (result2 << 1) + (currentChar[i] ? 1 : 0);
                    }
                    chars32[charIndex*2] = result1;
                    chars32[(charIndex*2)+1] = result2;
                }
            }
        }
        
        Object obj = new Object();
        WeakReference<Object> ref = new WeakReference<>(obj);
        obj = null;
        while (ref.get() != null) {
            System.gc();
        }
    }

	private void loadFont(String string) throws IOException {
		URL res = Main.instance.getClass().getResource(string);
		int[] file = Utils.realBytes(Utils.convertStreamToByteArray(res.openStream(), res.getFile().length()));
		int filelength = file.length;
		if (filelength >= 16) {
			if (file[0x0] == 114 && file[0x1] == 97 && file[0x2] == 119 && file[0x3] == 0xFF && file[0x6] == 0xFF && file[0xB] == 0xFF) {
				charW = file[0x4];
				charH = file[0x5];
				charS = charW*charH;
				minBound = file[0x7] << 24 | file[0x8] << 16 | file[0x9] << 8 | file[0xA];
				maxBound = file[0xC] << 24 | file[0xD] << 16 | file[0xE] << 8 | file[0xF];
				if (maxBound <= minBound) {
					maxBound = 9599; //TODO remove it: temp fix
				}
		        rawchars = new boolean[maxBound-minBound][];
		        int index = 0x10;
		        while (index < filelength) {
		        	int charIndex = file[index] << 8 | file[index+1];
		        	boolean[] rawchar = new boolean[charS];
		        	int charbytescount = (int) Math.ceil(charS/8)+1;
		        	int currentBit = 0;
		        	for (int i = 0; i <= charbytescount; i++) {
		        		for (int bit = 0; bit < 8; bit++) {
		        			if (currentBit >= charS) {
		        				break;
		        			}
		        			rawchar[i*8+bit] = (((file[index + 2 + i] >> (7-bit)) & 0x1)>=1)?true:false;
		        			currentBit++;
		        		}
		        	}
		        	rawchars[charIndex - minBound] = rawchar;
		        	index += 2 + charbytescount;
		        }
			} else {
				throw new IOException();
			}
		} else {
			throw new IOException();
		}
	}

	public int[] getCharIndexes(String txt) {
        final int l = txt.length();
        int[] indexes = new int[l];
        char[] chars = txt.toCharArray();
        for (int i = 0; i < l; i++) {
            indexes[i] = (chars[i] & 0xFFFF)-minBound;
        }
        return indexes;
    }

    @SuppressWarnings("unused")
	private void saveArray(int[] screen, String coutputpng) {
        BufferedImage bi = new BufferedImage(300, 200, BufferedImage.TYPE_INT_RGB);
        final int[] a = ((DataBufferInt) bi.getRaster().getDataBuffer()).getData();
        System.arraycopy(screen, 0, a, 0, screen.length);
        try {
            ImageIO.write(bi, "PNG", new File(coutputpng));
        } catch (IOException ex) {
            Logger.getLogger(RAWFont.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void drawText(int[] screen, int[] screenSize, int x, int y, int[] text, int color) {
    	final int screenLength = screen.length;
        int screenPos = 0;
        if (is64) {
            long res = 0;
            final int l = text.length;
            for (int i = 0; i < l; i++) {
                long chr = chars64[text[i]];
                int dx = 0;
                int dy = 0;
                for (int j = charS-1; j >= 0; j--) {
                	res = (chr >> j) & 1;
                	screenPos = (x + dx) + (y + dy) * screenSize[0];
                    if (res == 1 & screenLength > screenPos) {
                    	screen[screenPos] = color;
                    }
                    dx++;
                    if (dx >= charW) {
                    	dx = 0;
                        dy++;
                    }
                }
                x+=charW+1;
            }
        } else {
            int res = 0;
            final int l = text.length;
            for (int i = 0; i < l; i++) {
                final int charIndex = text[i]*2;
                int chrP1 = chars32[charIndex];
                int chrP2 = chars32[charIndex+1];
                for (int dx = 0; dx < charW; dx++) {
                    for (int dy = 0; dy < charH; dy++) {
                        int bit = dx + dy * charW;
                        if (bit < 29) {
                            res = chrP1 >> (28-bit) & 1;
                        } else {
                            res = chrP2 >> (12-bit) & 1;
                        }
                        screenPos = x + (i * (charW + 1)) + dx + (y + dy) * screenSize[0];
                        if (res == 1 & screenLength > res) {
                            screen[screenPos] = color;
                        }
                    }
                }
            }
        }
    }
}
