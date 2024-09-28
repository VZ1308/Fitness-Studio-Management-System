package classes;

public class Teilnehmer extends Person {
    private int teilnehmerId; // Teilnehmer-ID
    private String beitrittsdatum; // Datum des Beitritts

    // Konstruktor
    public Teilnehmer(String nachname, String vorname, String adresse, String nummer, String geburtstag, String email, String beitrittsdatum) {
        super(nachname, vorname, adresse, nummer, geburtstag, email);
        this.beitrittsdatum = beitrittsdatum; // Setzen des Beitrittsdatums
    }

    @Override
    public void anzeigen() {
        super.anzeigen(); // Aufruf der anzeigen()-Methode der Basisklasse
        System.out.println("TeilnehmerID: " + getTeilnehmerId());
        System.out.println("Beitrittsdatum: " + beitrittsdatum); // Ausgabe des Beitrittsdatums
    }

    public int getTeilnehmerId() {
        return teilnehmerId; // Getter für die TeilnehmerID
    }

    public void setTeilnehmerId(int teilnehmerId) {
        this.teilnehmerId = teilnehmerId; // Setter für die TeilnehmerID
    }

    public String getBeitrittsdatum() {
        return beitrittsdatum; // Getter für das Beitrittsdatum
    }

    public void setBeitrittsdatum(String beitrittsdatum) {
        this.beitrittsdatum = beitrittsdatum; // Setter für das Beitrittsdatum
    }
}
