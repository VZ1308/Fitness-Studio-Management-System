package classes;
public class Person {
    private String nachname;
    private String vorname;
    private String adresse;
    private String nummer;
    private String geburtstag;
    private String email;


    public Person(String nachname, String vorname, String adresse, String nummer, String geburtstag, String email) {
        setNachname(nachname);
        setVorname(vorname);
        setAdresse(adresse);
        setNummer(nummer);
        setGeburtsdatum(geburtstag);
        setEmail(email);

    }


    public  void anzeigen() {
        System.out.println("Nachname: " + getNachname());
        System.out.println("Vorname: " + getVorname());
        System.out.println("Adresse: " + getAdresse());
        System.out.println("Geburtstag: " + getGeburtsdatum());
        System.out.println("Email: " + getEmail());
        System.out.println("Telefon: " + getTelefon());
    }

    // Getter und Setter mit Validierung
    public String getNachname() {
        return nachname;
    }

    public void setNachname(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name darf nicht leer oder null sein.");
        }
        this.nachname = name;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        if (adresse == null || adresse.isEmpty()) {
            throw new IllegalArgumentException("Adresse darf nicht leer oder null sein.");
        }
        this.adresse = adresse;
    }

    public String getGeburtsdatum() {
        return geburtstag;
    }

    public void setGeburtsdatum(String geburtstag) {
        if (geburtstag == null || geburtstag.isEmpty()) {
            throw new IllegalArgumentException("Geburtsdatum darf nicht leer sein.");
        }
        this.geburtstag = geburtstag;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Ungültige E-Mail.");
        }
        this.email = email;
    }

    public String getTelefon() {
        return nummer;
    }

    public void setNummer(String telefon) {
        if (telefon == null || !telefon.matches("\\d+")) {
            throw new IllegalArgumentException("Telefonnummer darf nur Zahlen enthalten.");
        }
        this.nummer = telefon;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }


    public String getVorname() {
        return vorname;
    }
}

