package Programy;

public class Instrukcja {
    private String nazwa;

    public Instrukcja(char znak) {
        switch (znak) {
            case 'l':
                nazwa = "lewo";
                break;
            case 'p':
                nazwa = "prawo";
                break;
            case 'i':
                nazwa = "idź";
                break;
            case 'w':
                nazwa = "wąchaj";
                break;
            case 'j':
                nazwa = "jedz";
                break;
            // W tym programie się to nie wydarzy.
            default:
                System.err.println("Nie ma instrukcji " + znak + ".");
                System.exit(1);
                break;
        }
    }

    public String nazwa() {
        return nazwa;
    }

    public char pierwszaLit() {
        return nazwa.charAt(0);
    }
}
