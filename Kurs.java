package classes;

public class Kurs {
    private int kursId; // Kurs-ID
    private String kursname; // Name des Kurses
    private int maxTeilnehmer; // Maximale Anzahl der Teilnehmer
    private String kursbeginn; // Beginn des Kurses
    private String kursende; // Ende des Kurses

    // Konstruktor
    public Kurs(int kursId, String kursname, int maxTeilnehmer, String kursbeginn, String kursende) {
        this.kursId = kursId; // Kurs-ID
        this.kursname = kursname; // Name des Kurses
        this.maxTeilnehmer = maxTeilnehmer; // Maximale Teilnehmerzahl
        this.kursbeginn = kursbeginn; // Kursbeginn
        this.kursende = kursende; // Kursende
    }

    // Anzeige der Kursinformationen
    public void anzeigen() {
        System.out.println("Kurs-ID: " + kursId);
        System.out.println("Kursname: " + kursname);
        System.out.println("Max. Teilnehmer: " + maxTeilnehmer);
        System.out.println("Kursbeginn: " + kursbeginn);
        System.out.println("Kursende: " + kursende);
    }

    // Getter und Setter
    public int getKursId() {
        return kursId;
    }

    public void setKursId(int kursId) {
        this.kursId = kursId;
    }

    public String getKursname() {
        return kursname;
    }

    public void setKursname(String kursname) {
        this.kursname = kursname;
    }

    public int getMaxTeilnehmer() {
        return maxTeilnehmer;
    }

    public void setMaxTeilnehmer(int maxTeilnehmer) {
        this.maxTeilnehmer = maxTeilnehmer;
    }

    public String getKursbeginn() {
        return kursbeginn;
    }

    public void setKursbeginn(String kursbeginn) {
        this.kursbeginn = kursbeginn;
    }

    public String getKursende() {
        return kursende;
    }

    public void setKursende(String kursende) {
        this.kursende = kursende;
    }
}
