package Pola;

public class Puste extends Pole {
    public Puste(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean maPożywienie() {
        return false;
    }

    @Override
    public void resetujPożywienie() {}

    @Override
    public void regenerujPożywienie() {}
}
