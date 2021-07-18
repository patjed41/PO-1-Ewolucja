package Roby;

import Plansza.Plansza;
import Pola.Pole;
import Programy.Program;

import java.util.Random;

public class Rob {
    private int indeks; // Każdy rob ma inny indeks, co pozwala je łatwo odróżniać w raportach symulacji.
    private static int ostatni_indeks = 0; // indeks ostatnio powstałego roba
    private int wiek;
    private int kierunek; // 0 - góra, 1 - prawo, 2 - dół, 3 - lewo
    private Pole pole; // pole, na którym znajduje się rob
    private static Plansza plansza; // plansza, po której poruszają się roby
    private static int koszt_tury;
    private static int ile_daje_jedzenie;
    private int energia;
    private Program program;
    private static float ułamek_energii_rodzica;
    private static int limit_powielania;
    private static float pr_powielania;
    private static Random random = new Random();

    public static void koszt_tury(int koszt) {
        koszt_tury = koszt;
    }

    public static void ile_daje_jedzenie(int ile) {
        ile_daje_jedzenie = ile;
    }

    public static void ułamek_energii_rodzica(float ułamek) {
        ułamek_energii_rodzica = ułamek;
    }

    public static void limit_powielania(int limit) {
        limit_powielania = limit;
    }

    public static void pr_powielania(float pr) {
        pr_powielania = pr;
    }

    public static void plansza(Plansza p) {
        plansza = p;
    }

    public Rob(int kierunek, Pole pole, int energia, Program program) {
        ostatni_indeks++;
        this.indeks = ostatni_indeks;
        this.wiek = 0;
        this.kierunek = kierunek;
        this.pole = pole;
        this.energia = energia;
        this.program = program;
    }

    // Wszystko, co dzieje się z robem podczas tury, z wyjątkiem powielania.
    public void przeżyjTurę() {
        energia -= koszt_tury;
        wiek++;
        program.wykonajSię(this);
    }

    public void lewo() {
        kierunek = (kierunek + 3) % 4;
        energia--;
    }

    public void prawo() {
        kierunek = (kierunek + 1) % 4;
        energia--;
    }

    public void idź() {
        switch (kierunek) {
            case 0:
                pole = plansza.górnySąsiad(pole);
                break;
            case 1:
                pole = plansza.prawySąsiad(pole);
                break;
            case 2:
                pole = plansza.dolnySąsiad(pole);
                break;
            case 3:
                pole = plansza.lewySąsiad(pole);
                break;
        }
        if (pole.maPożywienie()) {
            pole.resetujPożywienie();
            energia += ile_daje_jedzenie;
        }
        energia--;
    }

    public void wąchaj() {
        if (plansza.górnySąsiad(pole).maPożywienie())
            kierunek = 0;
        else if (plansza.prawySąsiad(pole).maPożywienie())
            kierunek = 1;
        else if (plansza.dolnySąsiad(pole).maPożywienie())
            kierunek = 2;
        else if (plansza.lewySąsiad(pole).maPożywienie())
            kierunek = 3;
        energia--;
    }

    public void jedz() {
        // zmienne opisujące początkową pozycję roba
        int x = pole.pozycja_x();
        int y = pole.pozycja_y();

        if (plansza.górnySąsiad(pole).maPożywienie())
            pole = plansza.górnySąsiad(pole);
        else if (plansza.prawySąsiad(pole).maPożywienie())
            pole = plansza.prawySąsiad(pole);
        else if (plansza.dolnySąsiad(pole).maPożywienie())
            pole = plansza.dolnySąsiad(pole);
        else if (plansza.lewySąsiad(pole).maPożywienie())
            pole = plansza.lewySąsiad(pole);
        else if (plansza.prawySąsiad(plansza.górnySąsiad(pole)).maPożywienie())
            pole = plansza.prawySąsiad(plansza.górnySąsiad(pole));
        else if (plansza.dolnySąsiad(plansza.prawySąsiad(pole)).maPożywienie())
            pole = plansza.dolnySąsiad(plansza.prawySąsiad(pole));
        else if (plansza.lewySąsiad(plansza.dolnySąsiad(pole)).maPożywienie())
            pole = plansza.lewySąsiad(plansza.dolnySąsiad(pole));
        else if (plansza.górnySąsiad(plansza.lewySąsiad(pole)).maPożywienie())
            pole = plansza.górnySąsiad(plansza.lewySąsiad(pole));

        // Jeśli rob zmienił pozycję, to na pewno jest tam pożywienie.
        if (pole.pozycja_x() != x || pole.pozycja_y() != y) {
            pole.resetujPożywienie();
            energia += ile_daje_jedzenie;
        }
        energia--;
    }

    // Zwraca nowego roba. Jeśli powielania się nie odbyło, zwraca null.
    public Rob powielaj() {
        Rob rob = null;
        if (energia >= limit_powielania && random.nextFloat() <= pr_powielania) {
            rob = new Rob((kierunek + 2) % 4, pole, (int)(energia * ułamek_energii_rodzica), program.mutuj());
            energia -= (int)(energia * ułamek_energii_rodzica);
        }
        return rob;
    }

    public boolean czyŻywy() {
        return energia >= 0; // Zakładamy, że jeśli energia == 0, to rob nadal żyje.
    }

    public int wiek() {
        return wiek;
    }

    public int energia() {
        return energia;
    }

    public int długośćProgramu() {
        return program.długość();
    }

    @Override
    public String toString() {
        StringBuilder stan = new StringBuilder();
        stan.append("indeks: ").append(indeks);
        stan.append(", wiek: ").append(wiek);
        stan.append(", kierunek: ");
        switch (kierunek) {
            case 0:
                stan.append("góra");
                break;
            case 1:
                stan.append("prawo");
                break;
            case 2:
                stan.append("dół");
                break;
            case 3:
                stan.append("lewo");
                break;
        }
        stan.append(", pozycja: (");
        stan.append(pole.pozycja_x()).append(", ").append(pole.pozycja_y()).append(")");
        stan.append(", energ: ").append(energia);
        stan.append(", prg: ").append(program.toString());
        return stan.toString();
    }
}
