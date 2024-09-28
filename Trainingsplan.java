package classes;

public class Trainingsplan {
    private int trainingsplanId;
    private String beschreibung;
    private int teilnehmerId;

    // Konstruktor
    public Trainingsplan( String beschreibung, int teilnehmerId) {
        this.beschreibung = beschreibung;
        this.teilnehmerId = teilnehmerId;
    }

    // Getter und Setter
    public int getTrainingsplanId() {
        return trainingsplanId;
    }

    public void setTrainingsplanId(int trainingsplanId) {
        this.trainingsplanId = trainingsplanId;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public int getTeilnehmerId() {
        return teilnehmerId;
    }

    public void setTeilnehmerId(int teilnehmerId) {
        this.teilnehmerId = teilnehmerId;
    }

    // Methode zur Anzeige der Trainingsplan-Daten
    public void anzeigen() {
        System.out.println("Trainingsplan-ID: " + trainingsplanId);
        System.out.println("Beschreibung: " + beschreibung);
        System.out.println("Teilnehmer-ID: " + teilnehmerId);
    }
}
