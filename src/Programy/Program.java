package Programy;

import Roby.Rob;

import java.util.Random;

public class Program {
    private static Instrukcja[] spis_instr;
    private Instrukcja[] instr; // instrukcje
    private int ilość_instr; // ilość instrukcji w programie
    private static float pr_usunięcia_instr;
    private static float pr_dodania_instr;
    private static float pr_zmiany_instr;
    private static Random random = new Random();

    public static void spis_instr(String spis) {
        spis_instr = new Instrukcja[spis.length()];
        for (int i = 0; i < spis.length(); i++)
            spis_instr[i] = new Instrukcja(spis.charAt(i));
    }

    public static void pr_usunięcia_instr(float pr) {
        pr_usunięcia_instr = pr;
    }

    public static void pr_dodania_instr(float pr) {
        pr_dodania_instr = pr;
    }

    public static void pr_zmiany_instr(float pr) {
        pr_zmiany_instr = pr;
    }

    public Program(String instr) {
        this.instr = new Instrukcja[instr.length() + 1]; // "+ 1" przyda się przy mutacji
        ilość_instr = instr.length();
        for (int i = 0; i < ilość_instr; i++)
            this.instr[i] = new Instrukcja(instr.charAt(i));
    }

    public Program mutuj() {
        Program zmutowany = new Program(toString());
        if (zmutowany.ilość_instr > 0 && random.nextFloat() <= pr_usunięcia_instr) {
            zmutowany.ilość_instr--;
        }
        if (random.nextFloat() <= pr_dodania_instr) {
            zmutowany.instr[ilość_instr] =
                new Instrukcja(spis_instr[random.nextInt(spis_instr.length)].pierwszaLit());
            zmutowany.ilość_instr++;
        }
        if (zmutowany.instr.length > 0 && random.nextFloat() <= pr_zmiany_instr) {
            zmutowany.instr[random.nextInt(zmutowany.instr.length)] =
                new Instrukcja(spis_instr[random.nextInt(spis_instr.length)].pierwszaLit());
        }
        return zmutowany;
    }

    public int długość() {
        return ilość_instr;
    }

    // Program się wykonuje i kieruje robem rob.
    public void wykonajSię(Rob rob) {
        for (Instrukcja instrukcja : instr) {
            if (instrukcja != null && rob.czyŻywy()) {
                if (instrukcja.nazwa().equals("lewo"))
                    rob.lewo();
                else if (instrukcja.nazwa().equals("prawo"))
                    rob.prawo();
                else if (instrukcja.nazwa().equals("idź"))
                    rob.idź();
                else if (instrukcja.nazwa().equals("wąchaj"))
                    rob.wąchaj();
                else if (instrukcja.nazwa().equals("jedz"))
                    rob.jedz();
            }
        }
    }

    public String toString() {
        StringBuilder wynik = new StringBuilder();
        for (int i = 0; i < ilość_instr; i++)
            wynik.append(instr[i].pierwszaLit());
        return wynik.toString();
    }
}
