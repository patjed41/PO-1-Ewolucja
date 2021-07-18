package Plansza;

import Pola.*;

import java.util.Random;

public class Plansza {
    private Pole[][] pola; // pola[y][x] oznacza pole o współrzędnych (x, y)
    private int rozmiar_planszy_x;
    private int rozmiar_planszy_y;
    private static Random random = new Random();

    // Oczekiwana wartość pola[x][y], to 'x' jeśli pole jest żywieniowe, a ' ', jeśli pole jest puste.
    // Jeśli pola[x][y] to inny znak, uznajemy to pole za puste. Nie ma to jednak znaczenia, bo w tym
    // programie metoda wczytajPlanszę klasy Symulacja zapewnia poprawność tablicy pola względem treści.
    public Plansza(char[][] pola) {
        rozmiar_planszy_y = pola.length;
        rozmiar_planszy_x = pola[0].length;
        this.pola = new Pole[rozmiar_planszy_y][rozmiar_planszy_x];

        for (int y = 0; y < rozmiar_planszy_y; y++) {
            for (int x = 0; x < rozmiar_planszy_x; x++) {
                if (pola[y][x] == 'x')
                    this.pola[y][x] = new Żywieniowe(x, y);
                else
                    this.pola[y][x] = new Puste(x, y);
            }
        }
    }

    public void regenerujPożywienieNaPolach() {
        for (int y = 0; y < rozmiar_planszy_y; y++)
            for (int x = 0; x < rozmiar_planszy_x; x++)
                pola[y][x].regenerujPożywienie();
    }

    public int ilePólMaPożywienie() {
        int ile = 0;
        for (int y = 0; y < rozmiar_planszy_y; y++)
            for (int x = 0; x < rozmiar_planszy_x; x++)
                if (pola[y][x].maPożywienie())
                    ile++;
        return ile;
    }

    public Pole górnySąsiad(Pole pole) {
        return pola[(pole.pozycja_y() + 1) % rozmiar_planszy_y][pole.pozycja_x()];
    }

    public Pole prawySąsiad(Pole pole) {
        return pola[pole.pozycja_y()][(pole.pozycja_x() + 1) % rozmiar_planszy_x];
    }

    public Pole dolnySąsiad(Pole pole) {
        return pola[(pole.pozycja_y() + rozmiar_planszy_y - 1) % rozmiar_planszy_y][pole.pozycja_x()];
    }

    public Pole lewySąsiad(Pole pole) {
        return pola[pole.pozycja_y()][(pole.pozycja_x() + rozmiar_planszy_x - 1) % rozmiar_planszy_x];
    }

    public Pole losowePole() {
        return pola[random.nextInt(rozmiar_planszy_y)][random.nextInt(rozmiar_planszy_x)];
    }
}
