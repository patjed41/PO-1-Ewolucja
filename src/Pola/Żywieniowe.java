package Pola;

public class Żywieniowe extends Pole {
    private static int ile_rośnie_jedzenie;
    private int ile_do_regeneracji; // Jeśli ile_do_regeneracji == 0, to jedzenie się odnowiło.

    public static void ile_rośnie_jedzenie(int ile) {
        ile_rośnie_jedzenie = ile;
    }

    public Żywieniowe(int x, int y) {
        super(x, y);
        ile_do_regeneracji = 0;
    }

    @Override
    public boolean maPożywienie() {
        return ile_do_regeneracji == 0;
    }

    @Override
    public void resetujPożywienie() {
        ile_do_regeneracji = ile_rośnie_jedzenie;
    }

    @Override
    public void regenerujPożywienie() {
        ile_do_regeneracji = Math.max(ile_do_regeneracji - 1, 0);
    }
}
