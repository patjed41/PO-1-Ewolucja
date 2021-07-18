package Pola;

public abstract class Pole {
    private int pozycja_x;
    private int pozycja_y;

    public Pole(int x, int y) {
        pozycja_x = x;
        pozycja_y = y;
    }

    public abstract boolean maPożywienie();

    public abstract void resetujPożywienie();

    // Jeśli na polu jest pożywienie, to się regeneruje o 1 jednostkę.
    public abstract void regenerujPożywienie();

    public int pozycja_x() {
        return pozycja_x;
    }

    public int pozycja_y() {
        return pozycja_y;
    }
}
