package classes;

public class Trainer extends Person {
    private int trainerId; // Wird von der Datenbank automatisch gesetzt
    private int verfuegbareKurse; // Anzahl der verfügbaren Kurse
    private String fachgebiet; // Fachgebiet des Trainers

    // Konstruktor
    public Trainer(String nachname, String vorname, String adresse, String nummer, String geburtstag, String email, int verfuegbareKurse, String fachgebiet) {
        super(nachname, vorname, adresse, nummer, geburtstag, email);
        this.verfuegbareKurse = verfuegbareKurse; // Setzen der verfügbaren Kurse
        this.fachgebiet = fachgebiet; // Setzen des Fachgebiets
    }

    @Override
    public void anzeigen() {
        // Aufruf der anzeigen()-Methode der Basisklasse
        super.anzeigen();
        System.out.println("TrainerID: " + getTrainerId());
        System.out.println("Verfügbare Kurse: " + verfuegbareKurse); // Ausgabe der verfügbaren Kurse
        System.out.println("Fachgebiet: " + fachgebiet); // Ausgabe des Fachgebiets
    }

    public void setTeilnehmerId(int teilnehmerId) {
        this.trainerId = teilnehmerId;
    }

    public int getTrainerId() {
        return trainerId;
    }

    public int getVerfuegbareKurse() {
        return verfuegbareKurse;
    }

    public void setVerfuegbareKurse(int verfuegbareKurse) {
        this.verfuegbareKurse = verfuegbareKurse;
    }

    public String getFachgebiet() {
        return fachgebiet;
    }

    public void setFachgebiet(String fachgebiet) {
        this.fachgebiet = fachgebiet;
    }
}
