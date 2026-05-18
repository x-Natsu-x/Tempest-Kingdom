import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.RandomAccess;
import java.util.Scanner;
import com.googlecode.lanterna.SGR;
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



public class Tempest {

  //karakter lekeres cuccok
  public static String Keresett_ertek (String file_Path, String keresni_valo) {
    String visszaadott_Ertek = "";
    try {
      RandomAccessFile Adatok = new RandomAccessFile(file_Path,"r");
      String sor;
      while ((sor=Adatok.readLine()) != null) {
        String[] parts = sor.split("=");
        if (parts.length >= 2) {
          if (parts[0].trim().equals(keresni_valo)) {
            visszaadott_Ertek = parts[1].trim();
          }
        }
      }
      Adatok.close();

    } catch (IOException e) {
      System.out.println("Hiba "+e.getMessage());
    }
    return  visszaadott_Ertek;
  }

  public static String weapon_Dmg (String file_Path, String hasznalati_Fegyver) {
    String weapon_dmg = "";
    try {
      String sor;
      RandomAccessFile adatok = new RandomAccessFile(file_Path, "r");
      while ((sor = adatok.readLine()) != null) {
        if (sor.split("=")[0].trim().equals(hasznalati_Fegyver)) {
          weapon_dmg = sor.split("=")[1].trim();
        }
      }
      adatok.close();
    } catch (IOException e) {
      System.out.println("Hiba");
    }
    return weapon_dmg;
  }

  //kiiratas cuccok
  public static String kiiratas(String szo) {
    String[] tomb = szo.split("");
    ArrayList<String> Tomb = new ArrayList<>(Arrays.asList(tomb));
    int szamlalo = 0;
    for (String i:Tomb) {
      if (i.equals(".")) {
        szamlalo++;
      }
    }
    if (szamlalo >= 2) {
      int index = 0;
      for (int i = 0; i < Tomb.size(); i++) {
        if (Tomb.get(i).equals(".")) {
          index = i;
          break;
        }
      }
      Tomb.set(index, "[");
      Tomb.set((index+1), "]");
      String uj_Szo = "";
      for (String i:Tomb) {
        uj_Szo+=i;
      }
      return uj_Szo;
    } else {
      return szo;
    }
  }





  //boss cuccok
  public static String Boss_Hp (String file_Path, String boss_Adat) {
    String Boss_vissza_Adat = "";
    try {
      RandomAccessFile B_Adatok = new RandomAccessFile(file_Path, "r");
      String sor;
      while ((sor = B_Adatok.readLine()) != null) {
        if (sor.split("=")[0].trim().equals(boss_Adat)) {
          Boss_vissza_Adat = sor.split("=")[1].trim();
        }
      }
      B_Adatok.close();
    } catch (IOException e) {
      System.out.println("Hiba");
    }
    return Boss_vissza_Adat;
  }


  public static Boolean akadaly_Hely (int x, int y) {
      return (x > 15 && x < 159) && (y > 5 && y < 44);
  }

  /*
  switch (betu) {
    case 'w':
      if (playerY ) {
        playerY--;
      }

      break;
    case 's':
      if (playerY ) {
        playerY++;
      }
      break;
    case 'a':
      if (playerX ) {
        playerX--;
      }
      break;
    case 'd':
      if (playerX ) {
        playerX++;
      }
      break;
  }
}

   */

  //fő kód
  public static void main(String[] args) throws IOException, InterruptedException {
    Scanner scanner = new Scanner(System.in);
    String file_Path = "adatok_tarolasa.txt";
    String hasznalati_Fegyver = "equipped_Weapon";
    String hasznalati_Pancel = "equipped_Armor";
    String karakter_Hp = "charakter_hp";
    String boss_HP = "HP";
    String boss_DMG = "DMG";
    String boss_NAME = "NAME";

    // 1. Képernyő előkészítése
    TerminalSize size = new TerminalSize(180, 50); // 80 oszlop széles, 24 sor magas
    DefaultTerminalFactory factory = new DefaultTerminalFactory().setInitialTerminalSize(size); // Itt adod meg a kezdő méretet
    Terminal terminal = factory.createTerminal();
    Screen screen = new TerminalScreen(terminal);
    screen.startScreen();
    screen.setCursorPosition(null); // Kurzor elrejtése

    TextGraphics tg = screen.newTextGraphics();


    /*
    // 2. ASCII Art rajzolása (soronként)
    String[] Tempest_logo = {
            "===========================================================",
            "  _______  ______  __  __  _____  ______   _____  _______ ",
            " |__   __||  ____||  \\/  ||  __ \\|  ____| / ____||__   __|",
            "    | |   | |__   | \\  / || |__) | |__   | (___     | |   ",
            "    | |   |  __|  | |\\/| ||  ___/|  __|   \\___ \\    | |   ",
            "    | |   | |____ | |  | || |    | |____  ____) |   | |   ",
            "    |_|   |______||_|  |_||_|    |______||_____/    |_|   ",
            "==========================================================",
            "                  V E R S I O N  1 . 0                    "
    };

    //logo kirajzolasa
    tg.setForegroundColor(TextColor.ANSI.CYAN);
    for (int i = 0; i < Tempest_logo.length; i++) {
      tg.putString(60, i + 10, Tempest_logo[i]);
    }
    screen.refresh();

    //a jatek elinditasa
    boolean visible = true;
    while (true) {
      // Billentyűzet ellenőrzése (nem várakozik, csak megnézi van-e gombnyomás)
      KeyStroke keyStroke = screen.pollInput();
      if (keyStroke != null) {
        if (keyStroke.getKeyType() == KeyType.Enter) {
          break;
        }
      } // Ha bármit megnyomtak, kilép a ciklusból

      if (visible) {
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(60, 24, " » Játék kezdete! (Nyomj entert) ");
      } else {
        tg.putString(60, 24, "                                          ");
      }

      screen.refresh();
      visible = !visible;
      Thread.sleep(500);
    }

    tg.putString(60, 24, "                                          ");
    screen.refresh();
    int darab = 0;
    int szin = 0;
    String start = "Játék indítása: ............";

    //betolto kep
    while (darab < 2) {
      if (szin == 0) {
        tg.setForegroundColor(TextColor.ANSI.RED);
      } else {
        tg.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);
      }
      tg.putString(75, 26, start);
      screen.refresh();
      Thread.sleep(400);

      start = kiiratas(start);
      tg.putString(75, 26, start);
      screen.refresh();
      Thread.sleep(400);

      if (szin == 0) {
        tg.setForegroundColor(TextColor.ANSI.YELLOW);

      } else {
        tg.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
      }

      start = kiiratas(start);
      tg.putString(75, 26, start);
      screen.refresh();
      Thread.sleep(400);

      start = kiiratas(start);
      tg.putString(75, 26, start);
      screen.refresh();
      Thread.sleep(400);

      darab++;
      szin ++;
    }

    String[] Start_logo = {
            "===========================================================",
            "      _____   _______                _____    _______ ",
            "     / ____| |__   __|      /\\      |  __ \\  |__   __|",
            "    | (___      | |        /  \\     | |__) |    | |   ",
            "     \\___ \\     | |       / /\\ \\    |  _  /     | |   ",
            "     ____) |    | |      / ____ \\   | | \\ \\     | |   ",
            "    |_____/     |_|     /_/    \\_\\  |_|  \\_\\    |_|   ",
            "                                                      ",
            "===========================================================",
            "            A J Á T É K  E L K E Z D Ő D Ö T T            "
    };

    screen.clear();
    screen.refresh();

    //start felirat lehet mar kicsit sok
    tg.setForegroundColor(TextColor.ANSI.WHITE);
    for (int i = 0; i < Start_logo.length; i++) {
      tg.putString(60,i + 15, Start_logo[i]);
    }
    screen.refresh();
    Thread.sleep(3000);


     */
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





  Thread.sleep(5000);

    // Kilépés előtt leállítjuk a képernyőt
    screen.stopScreen();
    System.out.println("Program leállt, vissza a normál konzolhoz.");





    //bossfight
        /*

        String Boss_File_Path = "boss_adatok.txt";
        ArrayList<String> boss_adatok = new ArrayList<>();
        Boss_Adatok_Lekerese(Boss_File_Path, boss_adatok);

        int Te_eletero = Integer.parseInt(character_hp(file_Path));
        int Te_sebzes = Integer.parseInt(weapon_Dmg(file_Path));

        String Boss_nev = boss_adatok.get(0);
        int Boss_eletero = Integer.parseInt(boss_adatok.get(1));
        int Boss_sebzes = Integer.parseInt(boss_adatok.get(2));

        int tamadas = 1;

        boolean bossfight = true;
        while (bossfight) {

            if (Te_eletero <= 0 || Boss_eletero <= 0) {
                bossfight = false;
                break;
            }

            String Tamadas = tamadas_fajtaja(tamadas);

            System.out.println(
                    "###############################################################\n" +
                            "###                                                         ###\n" +
                            "###                                                         ###\n" +
                            "###                                      /)  ____  (\\       ###\n" +
                            "###          _O_                        <=( ( @@ ) )=>      ###\n" +
                            "###         / | \\                        \\)  \\WW/  (/       ###\n" +
                            "###          / \\                                            ###\n" +
                            "###                                                         ###\n" +
                            "###                                                         ###\n" +
                            "###############################################################"
            );

            System.out.println("Akció típusa: "+Tamadas);
            System.out.println("Boss neve: "+Boss_nev);
            System.out.println("Boss életereje: "+Boss_eletero);
            System.out.println("Boss sebzése: "+Boss_sebzes);
            System.out.println("Neved: ");
            System.out.println("Életerőd: "+Te_eletero);
            System.out.println("Sebzésed: "+Te_sebzes);

            if (Tamadas.equals("támadás")) {
                Boss_eletero -= Te_sebzes;
            } else {
                Te_eletero -= Boss_sebzes;
            }
            tamadas++;

            villogoVarakozas("Kezd mega következő Lépésedet! (Nyomj ENTER-t a kezdéshez)");
        }
        if (Boss_eletero > Te_eletero) {
            System.out.println("Vesztettél");
        } else {
            System.out.println("Nyertél");
        }

         */
  }
}




        /*

    String equipped_Weapon = equipped_Weapon(file_Path);
    System.out.println(equipped_Weapon);
        System.out.println("===========================================================");
        System.out.println("  _______  ______  __  __  _____  ______   _____  _______ ");
        System.out.println(" |__   __||  ____||  \\/  ||  __ \\|  ____| / ____||__   __|");
        System.out.println("    | |   | |__   | \\  / || |__) | |__   | (___     | |   ");
        System.out.println("    | |   |  __|  | |\\/| ||  ___/|  __|   \\___ \\    | |   ");
        System.out.println("    | |   | |____ | |  | || |    | |____  ____) |   | |   ");
        System.out.println("    |_|   |______||_|  |_||_|    |______||_____/    |_|   ");
        System.out.println("==========================================================");
        System.out.println("                  V E R S I O N  1 . 0                    \n");

        while (System.in.available() == 0) {
            System.out.print("\r  » Ez a szöveg villog! (Nyomj ENTER-t a kezdéshez) ");
            Thread.sleep(600);

            // ha már megnyomta a tovabbi felesleges
            if (System.in.available() > 0) break;
            System.out.print("\r                                                        ");
            Thread.sleep(400);
        }

        scanner.nextLine();
        System.out.println();

        int darab = 0;
        String start = "Játék indítása: ............";

        while (darab < 2) {
            System.out.print("\r"+start);
            Thread.sleep(400);

            start = kiiratas(start);
            System.out.print("\r"+start);
            Thread.sleep(400);

            start = kiiratas(start);
            System.out.print("\r"+start);
            Thread.sleep(400);

            start = kiiratas(start);
            System.out.print("\r"+start);
            Thread.sleep(400);

            darab++;
        }
        System.out.println();
        Thread.sleep(1000);
        clearScreen();

        //bossfight

        String Boss_File_Path = "boss_adatok.txt";
        ArrayList<String> boss_adatok = new ArrayList<>();
        Boss_Adatok_Lekerese(Boss_File_Path, boss_adatok);

        int Te_eletero = Integer.parseInt(character_hp(file_Path));
        int Te_sebzes = Integer.parseInt(weapon_Dmg(file_Path));

        String Boss_nev = boss_adatok.get(0);
        int Boss_eletero = Integer.parseInt(boss_adatok.get(1));
        int Boss_sebzes = Integer.parseInt(boss_adatok.get(2));

        int tamadas = 1;

        boolean bossfight = true;
        while (bossfight) {

            if (Te_eletero <= 0 || Boss_eletero <= 0) {
                bossfight = false;
                break;
            }

            String Tamadas = tamadas_fajtaja(tamadas);

            System.out.println(
                    "###############################################################\n" +
                            "###                                                         ###\n" +
                            "###                                                         ###\n" +
                            "###                                      /)  ____  (\\       ###\n" +
                            "###          _O_                        <=( ( @@ ) )=>      ###\n" +
                            "###         / | \\                        \\)  \\WW/  (/       ###\n" +
                            "###          / \\                                            ###\n" +
                            "###                                                         ###\n" +
                            "###                                                         ###\n" +
                            "###############################################################"
            );

            System.out.println("Akció típusa: "+Tamadas);
            System.out.println("Boss neve: "+Boss_nev);
            System.out.println("Boss életereje: "+Boss_eletero);
            System.out.println("Boss sebzése: "+Boss_sebzes);
            System.out.println("Neved: ");
            System.out.println("Életerőd: "+Te_eletero);
            System.out.println("Sebzésed: "+Te_sebzes);

            if (Tamadas.equals("támadás")) {
                Boss_eletero -= Te_sebzes;
            } else {
                Te_eletero -= Boss_sebzes;
            }
            tamadas++;

            villogoVarakozas("Kezd mega következő Lépésedet! (Nyomj ENTER-t a kezdéshez)");
        }
        if (Boss_eletero > Te_eletero) {
            System.out.println("Vesztettél");
        } else {
            System.out.println("Nyertél");
        }

         */


    /*
    public static void villogoVarakozas(String uzenet) throws IOException, InterruptedException {
        // Kiszámoljuk, hány szóköz kell a törléshez (az üzenet hossza + a nyilak/extra karakterek)
        String szokesek = " ".repeat(uzenet.length() + 10);

        while (System.in.available() == 0) {
            // Megjelenítés
            System.out.print("\r  » " + uzenet + " ");
            Thread.sleep(600);

            if (System.in.available() > 0) break;

            // Törlés (szóközökkel felülírjuk)
            System.out.print("\r" + szokesek);
            Thread.sleep(400);
        }

    }
     */