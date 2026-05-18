import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;



public class szamolasok {

    public static Boolean akadaly_Hely (int x, int y) {
        return (x > 30 && x < 38) && (y > 6 && y < 9);
    }

    //jobb x nel kell hatot levonni
    public static void tiltott_Hely (String[] tomb, int x, int y) {
        System.out.println("felso y: "+y+"\nalso y: "+(y + tomb.length-1)+"\nbal x:" +(x-6)+"\njobb x: "+(x+tomb[0].length()));
    }

    public static void tiltott_Hely_Nagy_G(String[] tomb, int x, int y) {
        int felso_y = y;
        int also_y  = y + tomb.length;              // A tömb alja utáni első üres sor
        int bal_x   = x;
        int jobb_x  = x + tomb[0].length();         // A tömb jobb széle utáni első üres oszlop

        System.out.println("--- Objektum határai a képernyőn ---");
        System.out.println("Felső határ (Y): " + felso_y);
        System.out.println("Alsó határ  (Y): " + also_y);
        System.out.println("Bal szél    (X): " + bal_x);
        System.out.println("Jobb szél   (X): " + jobb_x);
    }
    public static void main(String[] args) throws IOException, InterruptedException {



        String[] kisHaz = {
                "   /\\   ",
                "  /  \\  ",
                " [____] "
        };
        int x = 30;
        int y = 6;
        tiltott_Hely_Nagy_G(kisHaz, x, y);



        // 1. Képernyő előkészítése
        TerminalSize size = new TerminalSize(180, 50); // 80 oszlop széles, 24 sor magas
        DefaultTerminalFactory factory = new DefaultTerminalFactory().setInitialTerminalSize(size); // Itt adod meg a kezdő méretet
        Terminal terminal = factory.createTerminal();
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();
        screen.setCursorPosition(null); // Kurzor elrejtése

        TextGraphics tg = screen.newTextGraphics();
        //Kezdőpont
        int playerX = 80;
        int playerY = 30;
        boolean isRunning = true;

        //Player
        String karakter = "(<*-*)<";

        //fal megrajzolasa
        int felso_X = 15; // 150 szer
        int felso_Y = 5;

        int also_X = 15; // 150 szer
        int also_Y = 44;

        int bal_X = 14;
        int bal_Y = 5; //38 szor

        int jobb_X = 165;
        int jobb_Y = 5; //38 szor

        while (isRunning) {
            screen.clear();

            int hozzaadas = 0;
            for (int i = 0; i < 149; i++) {
                hozzaadas++;
                tg.putString(felso_X + hozzaadas, felso_Y, "=");
                tg.putString(also_X + hozzaadas, also_Y, "=");
            }
            hozzaadas = 0;
            for (int i = 0; i < 38; i++) {
                hozzaadas++;
                tg.putString(bal_X, bal_Y  + hozzaadas, "||");
                tg.putString(jobb_X, jobb_Y  + hozzaadas, "||");
            }
            screen.refresh();

            //szin
            TextGraphics textGraphics = screen.newTextGraphics();
            textGraphics.setForegroundColor(TextColor.ANSI.MAGENTA_BRIGHT);

            //karakter kiírása
            textGraphics.putString(new TerminalPosition(playerX, playerY), karakter);

            screen.refresh();

            //Karakter mozgÁs
            KeyStroke gombnyomas = screen.readInput();
            if (gombnyomas != null) {
                if (gombnyomas.getKeyType() == KeyType.Escape) {
                    isRunning = false;
                } else if (gombnyomas.getKeyType() == KeyType.Character) {
                    char betu = Character.toLowerCase(gombnyomas.getCharacter());

                    switch (betu) {
                        case 'w':
                            if (akadaly_Hely(playerX, playerY - 1)) {
                                playerY--;
                            }
                            break;
                        case 's':
                            if (akadaly_Hely(playerX, playerY + 1)) {
                                playerY++;
                            }
                            break;
                        case 'a':
                            if (akadaly_Hely(playerX - 1, playerY)) {
                                playerX--;
                            }
                            break;
                        case 'd':
                            if (akadaly_Hely(playerX + 1, playerY)) {
                                playerX++;
                            }
                            break;
                    }
                }
            }
        }







    }
}
