// package utils;

// import java.awt.*;
// import java.io.FileInputStream;
// import java.io.InputStream;

// public class FontLoader {
//     public static Font loadFont(String path, float size, int style) {
//         try {
//             InputStream is = new FileInputStream("src/assets/Montserrat-Regular.ttf");
//             Font font = Font.createFont(Font.TRUETYPE_FONT, is);
//             System.out.println(font.getFontName());
//             return font.deriveFont(style, size);
//         } catch (Exception e) {
//             // fallback default font
//             return new Font("SansSerif", style, (int) size);
//         }
//     }
// }