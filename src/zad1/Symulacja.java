// autor - Patryk Jędrzejczak

package zad1;

import Plansza.Plansza;
import Pola.Żywieniowe;
import Programy.Program;
import Roby.Rob;
import Wyjątki.BłędnaPlansza;
import Wyjątki.BłędneParametry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

// Klasa Symulacja jest zaimplementowana jak typowa klasa, która wcale nie musi mieć funkcji main,
// aby działać. Konstruktor i metoda przeprowadźSymulację są publiczne, bo to klasa ma udostępniać
// (a przynajmniej miałaby, gdybyśmy usunęli funkcję main). Pozostałe metody są prywatne, bo ich
// zadaniem jest tylko polepszenie przejrzystości implementacji.
public class Symulacja {
    private Plansza plansza;
    private Rob[] roby; // żywe i martwe
    private int ile_tur;
    private int ile_robów; // na początku symulacji ile_robów == pocz_ile_robów
    private String pocz_progr;
    private int pocz_energia;
    private int co_ile_wypisz;
    private static Random random = new Random();

    public Symulacja(String nazwa_pliku_z_planszą, String nazwa_pliku_z_parametrami) {
        try {
            plansza = new Plansza(wczytajPlanszę(nazwa_pliku_z_planszą));
            Rob.plansza(plansza);
        }
        catch (IOException e) {
            System.err.println("Błędna nazwa pliku z planszą.");
            System.exit(1);
        }
        catch (BłędnaPlansza e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        try {
            wczytajParametry(nazwa_pliku_z_parametrami);
        }
        catch (IOException e) {
            System.err.println("Błędna nazwa pliku z parametrami.");
            System.exit(1);
        }
        catch (BłędneParametry e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        catch (NumberFormatException e) {
            System.err.println("Któryś z parametrów liczbowych jest w złym formacie.");
            System.exit(1);
        }
    }

    private char[][] wczytajPlanszę(String nazwa_pliku) throws IOException, BłędnaPlansza {
        File plik = new File(nazwa_pliku); // to może rzucić IOException
        Scanner skaner = new Scanner(plik);
        char[][] plansza = new char[0][0]; // inicjacja, żeby kompilator miał pewność, że coś zwrócimy
        int rozmiar_planszy_x = 0;
        int rozmiar_planszy_y = (int)Files.lines(Paths.get(nazwa_pliku)).count();
        if (rozmiar_planszy_y == 0)
            throw new BłędnaPlansza("Pusta plansza!");
        int y = 0;
        while (skaner.hasNextLine()) {
            String wiersz = skaner.nextLine();

            // Pierwszy wiersz ustala szerokość planszy.
            if (rozmiar_planszy_x == 0) {
                rozmiar_planszy_x = wiersz.length();
                if (rozmiar_planszy_x == 0)
                    throw new BłędnaPlansza("Pusty pierwszy wiersz!");
                plansza = new char[rozmiar_planszy_y][rozmiar_planszy_x];
            }

            if (wiersz.length() != rozmiar_planszy_x)
                throw new BłędnaPlansza("Wiersz " + (y + 1) + " jest złej długości.");
            for (int x = 0; x < rozmiar_planszy_x; x++) {
                if (wiersz.charAt(x) != ' ' && wiersz.charAt(x) != 'x')
                    throw new BłędnaPlansza("Błędny znak " + wiersz.charAt(x) + ".");
                plansza[y][x] = wiersz.charAt(x);
            }

            y++;
        }
        return plansza;
    }

    private void wczytajParametry(String nazwa_pliku) throws IOException, BłędneParametry, NumberFormatException {
        int liczba_parametrów = 15;
        int[] ile_wystąpień = new int[liczba_parametrów]; // ile razy wystąpił i-ty parametr
        for (int i = 0; i < liczba_parametrów; i++)
            ile_wystąpień[i] = 0;

        File plik = new File(nazwa_pliku); // to może rzucić IOException
        Scanner skaner = new Scanner(plik);
        while (skaner.hasNextLine()) {
            String wiersz = skaner.nextLine();
            String[] słowa = wiersz.split(" ");

            if (słowa.length != 2)
                throw new BłędneParametry("W każdym wierszu muszą być 2 słowa.");

            if (słowa[0].equals("ile_tur")) {
                ile_tur = Integer.parseInt(słowa[1]);
                if (ile_tur <= 0)
                    throw new BłędneParametry("Parametr ile_tur musi być dodatni.");
                ile_wystąpień[0]++;
            }
            else if (słowa[0].equals("pocz_ile_robów")) {
                ile_robów = Integer.parseInt(słowa[1]);
                if (ile_robów <= 0)
                    throw new BłędneParametry("Parametr pocz_ile_robów musi być dodatni.");
                ile_wystąpień[1]++;
            }
            else if (słowa[0].equals("pocz_progr")) {
                pocz_progr = słowa[1];
                for (int i = 0; i < pocz_progr.length(); i++)
                    if (pocz_progr.charAt(i) != 'l' && pocz_progr.charAt(i) != 'p' &&
                        pocz_progr.charAt(i) != 'i' && pocz_progr.charAt(i) != 'j' &&
                        pocz_progr.charAt(i) != 'w')
                        throw new BłędneParametry("Błędny znak '" + pocz_progr.charAt(i) + "' w pocz_progr.");
                ile_wystąpień[2]++;
            }
            else if (słowa[0].equals("pocz_energia")) {
                pocz_energia = Integer.parseInt(słowa[1]);
                if (pocz_energia <= 0)
                    throw new BłędneParametry("Parametr pocz_energia musi być dodatni.");
                ile_wystąpień[3]++;
            }
            else if (słowa[0].equals("ile_daje_jedzenie")) {
                Rob.ile_daje_jedzenie(Integer.parseInt(słowa[1]));
                if (Integer.parseInt(słowa[1]) <= 0)
                    throw new BłędneParametry("Parametr ile_daje_jedzenie musi być dodatni.");
                ile_wystąpień[4]++;
            }
            else if (słowa[0].equals("ile_rośnie_jedzenie")) {
                Żywieniowe.ile_rośnie_jedzenie(Integer.parseInt(słowa[1]));
                if (Integer.parseInt(słowa[1]) <= 0)
                    throw new BłędneParametry("Parametr ile_rośnie_jedzenie musi być dodatni.");
                ile_wystąpień[5]++;
            }
            else if (słowa[0].equals("koszt_tury")) {
                Rob.koszt_tury(Integer.parseInt(słowa[1]));
                // Zakładamy, że koszt_tury może być liczbą ujemną, bo to nic nie psuje.
                ile_wystąpień[6]++;
            }
            else if (słowa[0].equals("pr_powielania")) {
                float pr = Float.parseFloat(słowa[1]);
                Rob.pr_powielania(pr);
                if (pr < 0 || pr > 1)
                    throw new BłędneParametry("Parametr pr_powielania musi być z [0, 1].");
                ile_wystąpień[7]++;
            }
            else if (słowa[0].equals("ułamek_energii_rodzica")) {
                float ułamek = Float.parseFloat(słowa[1]);
                Rob.ułamek_energii_rodzica(ułamek);
                if (ułamek <= 0 || ułamek > 1)
                    throw new BłędneParametry("Parametr pr_powielania musi być z (0, 1].");
                ile_wystąpień[8]++;
            }
            else if (słowa[0].equals("limit_powielania")) {
                Rob.limit_powielania(Integer.parseInt(słowa[1]));
                if (Integer.parseInt(słowa[1]) <= 0)
                    throw new BłędneParametry("Parametr limit_powielania musi być dodatni.");
                ile_wystąpień[9]++;
            }
            else if (słowa[0].equals("pr_usunięcia_instr")) {
                float pr = Float.parseFloat(słowa[1]);
                Program.pr_usunięcia_instr(pr);
                if (pr < 0 || pr > 1)
                    throw new BłędneParametry("Parametr pr_usunięcia_instr musi być z [0, 1].");
                ile_wystąpień[10]++;
            }
            else if (słowa[0].equals("pr_dodania_instr")) {
                float pr = Float.parseFloat(słowa[1]);
                Program.pr_dodania_instr(pr);
                if (pr < 0 || pr > 1)
                    throw new BłędneParametry("Parametr pr_dodania_instr musi być z [0, 1].");
                ile_wystąpień[11]++;
            }
            else if (słowa[0].equals("pr_zmiany_instr")) {
                float pr = Float.parseFloat(słowa[1]);
                Program.pr_zmiany_instr(pr);
                if (pr < 0 || pr > 1)
                    throw new BłędneParametry("Parametr pr_zmiany_instr musi być z [0, 1].");
                ile_wystąpień[12]++;
            }
            else if (słowa[0].equals("spis_instr")) {
                int[] ile = new int[5]; // ile razy wystąpiła i-ta istrukcja
                for (int i = 0; i < słowa[1].length(); i++) {
                    switch (słowa[1].charAt(i)) {
                        case 'l':
                            ile[0]++;
                            break;
                        case 'p':
                            ile[1]++;
                            break;
                        case 'i':
                            ile[2]++;
                            break;
                        case 'w':
                            ile[3]++;
                            break;
                        case 'j':
                            ile[4]++;
                            break;
                        default:
                            System.err.println("Błędny znak " + słowa[1].charAt(i) + " w spis_insts");
                            System.exit(1);
                            break;
                    }
                }
                for (int i = 0; i < 5; i++)
                    if (ile[i] > 1)
                        throw new BłędneParametry("Instrukcje w spis_instr mogą wystąpić maksymalnie raz.");
                Program.spis_instr(słowa[1]);
                ile_wystąpień[13]++;
            }
            else if (słowa[0].equals("co_ile_wypisz")) {
                co_ile_wypisz = Integer.parseInt(słowa[1]);
                if (Integer.parseInt(słowa[1]) <= 0)
                    throw new BłędneParametry("Parametr co_ile_wypisz musi być dodatni.");
                ile_wystąpień[14]++;
            }
            else {
                throw new BłędneParametry("Błędny parametr " + słowa[0] + ".");
            }
        }

        for (int i = 0; i < liczba_parametrów; i++)
            if (ile_wystąpień[i] != 1)
                throw new BłędneParametry("Każdy parametr musi wystąpić dokładnie raz.");
    }

    private void małyRaport(int tura) {
        int żywe_roby = 0;
        for (int i = 0; i < ile_robów; i++)
            if (roby[i].czyŻywy())
                żywe_roby++;

        StringBuilder raport = new StringBuilder();
        raport.append(tura);

        if (żywe_roby == 0) {
            raport.append(", brak żywych robów");
        }
        else {
            raport.append(", rob: ").append(żywe_roby);
            raport.append(", żyw: ").append(plansza.ilePólMaPożywienie());
            int minimalna_dł_prog = Integer.MAX_VALUE;
            int maksymalna_dł_prog = 0;
            int suma_dł_prog = 0;
            int minimalna_energia = Integer.MAX_VALUE;
            int maksymalna_energia = 0;
            int suma_energii = 0;
            int minimalny_wiek = Integer.MAX_VALUE;
            int maksymalny_wiek = 0;
            int suma_wieków = 0;
            for (int i = 0; i < ile_robów; i++) {
                if (roby[i].czyŻywy()) {
                    minimalna_dł_prog = Math.min(minimalna_dł_prog, roby[i].długośćProgramu());
                    maksymalna_dł_prog = Math.max(maksymalna_dł_prog, roby[i].długośćProgramu());
                    suma_dł_prog += roby[i].długośćProgramu();
                    minimalna_energia = Math.min(minimalna_energia, roby[i].energia());
                    maksymalna_energia = Math.max(maksymalna_energia, roby[i].energia());
                    suma_energii += roby[i].energia();
                    minimalny_wiek = Math.min(minimalny_wiek, roby[i].wiek());
                    maksymalny_wiek = Math.max(maksymalny_wiek, roby[i].wiek());
                    suma_wieków += roby[i].wiek();
                }
            }
            raport.append(", prg: ").append(minimalna_dł_prog).append("/");
            raport.append(String.format("%.02f", (float)suma_dł_prog / (float)żywe_roby));
            raport.append("/").append(maksymalna_dł_prog);
            raport.append(", energ: ").append(minimalna_energia).append("/");
            raport.append(String.format("%.02f", (float)suma_energii / (float)żywe_roby));
            raport.append("/").append(maksymalna_energia);
            raport.append(", wiek: ").append(minimalny_wiek).append("/");
            raport.append(String.format("%.02f", (float)suma_wieków / (float)żywe_roby));
            raport.append("/").append(maksymalny_wiek);
        }

        System.out.println(raport.toString());
    }

    private void dużyRaport(int tura) {
        if (tura == 0)
            System.out.println("początek symulacji, dokładny raport:");
        else if (tura == ile_tur + 1)
            System.out.println("koniec symulacji, dokładny raport:");
        else
            System.out.println("tura " + tura + ", dokładny raport:");
        boolean czy_jest_jakiś_żywy = false;
        for (int i = 0; i < ile_robów; i++) {
            if (roby[i].czyŻywy()) {
                System.out.println(roby[i]);
                czy_jest_jakiś_żywy = true;
            }
        }
        if (!czy_jest_jakiś_żywy)
            System.out.println("brak żywych robów");
    }

    public void przeprowadźSymulację() {
        // ustawienie robów na planszy
        roby = new Rob[ile_robów];
        for (int i = 0; i < ile_robów; i++)
            roby[i] = new Rob(random.nextInt(4), plansza.losowePole(), pocz_energia, new Program(pocz_progr));

        dużyRaport(0);

        for (int tura = 1; tura <= ile_tur; tura++) {
            plansza.regenerujPożywienieNaPolach();

            for (int i = 0; i < ile_robów; i++) {
                if (roby[i].czyŻywy()) {
                    // Zakładamy, że najpierw rob wykonuje program, a potem się powiela.
                    roby[i].przeżyjTurę();
                    Rob nowy_rob = roby[i].powielaj();
                    if (nowy_rob != null) {
                        // Jeśli brakuje miejsca w tablicy roby, to zwiększamy ją dwukrotnie.
                        if (roby.length == ile_robów)
                            roby = Arrays.copyOf(roby, 2 * roby.length);
                        roby[ile_robów] = nowy_rob;
                        ile_robów++;
                    }
                }
            }

            małyRaport(tura);
            if (tura % co_ile_wypisz == 0 && tura < ile_tur)
                dużyRaport(tura);
        }

        dużyRaport(ile_tur + 1);
    }

    public static void main(String[] args) {
        Symulacja symulacja = new Symulacja(args[0], args[1]);
        symulacja.przeprowadźSymulację();
    }
}
