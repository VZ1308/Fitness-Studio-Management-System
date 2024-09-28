import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class MenuHandling {

    // Hauptmenü für die Verwaltung von Trainern, Kursen, Teilnehmern und Trainingsplänen
    public static void startMenu(Connection conn) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Hauptmenü anzeigen
            System.out.println("Fitnessstudio-Verwaltung - Hauptmenü");
            System.out.println("1. Trainer verwalten");
            System.out.println("2. Kurse verwalten");
            System.out.println("3. Teilnehmer verwalten");
            System.out.println("4. Trainingsplan verwalten");
            System.out.println("5. Programm beenden");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Konsolenpuffer leeren

            // Navigation basierend auf Benutzereingabe
            switch (choice) {
                case 1 -> trainerMenu(conn, scanner);  // Untermenü für Trainer
                case 2 -> kursMenu(conn, scanner);     // Untermenü für Kurse
                case 3 -> teilnehmerMenu(conn, scanner); // Untermenü für Teilnehmer
                case 4 -> trainingsplanMenu(conn, scanner); // Untermenü für Trainingsplan
                case 5 -> {
                    System.out.println("Programm wird beendet.");
                    return;
                }
                default -> System.out.println("Ungültige Auswahl.");
            }
        }
    }

    private static void trainingsplanMenu(Connection conn, Scanner scanner) {
        while (true) {
            System.out.println("Trainingsplan-Verwaltung");
            System.out.println("1. Trainingsplan hinzufügen");
            System.out.println("2. Trainingsplan anzeigen");
            System.out.println("3. Trainingsplan filtern");
            System.out.println("4. Zurück zum Hauptmenü");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Konsolenpuffer leeren

            switch (choice) {
                case 1 -> hinzufuegenTrainingsplan(conn, scanner);
                case 2 -> anzeigen(conn, "trainingsplan");
                case 3 -> filternTrainingsplan(conn, scanner);
                case 4 -> {
                    System.out.println("--- Zurück zum Hauptmenü ---");
                    return;
                }
                default -> System.out.println("Ungültige Auswahl.");
            }
        }
    }

    private static void hinzufuegenTrainingsplan(Connection conn, Scanner scanner) {
        System.out.printf("Trainingsplan hinzufügen:%n");

        // Dateneingabe vom Benutzer sammeln
        Map<String, String> daten = new HashMap<>();
        daten.put("beschreibung", eingabe(scanner, "Beschreibung"));
        daten.put("teilnehmerId", eingabe(scanner, "TeilnehmerId"));

        // SQL-Abfrage zum Einfügen eines neuen Trainingsplans
        String sql = "INSERT INTO Trainingsplan (beschreibung, teilnehmerID) VALUES (?, ?)";

        // Ausführen der SQL-Abfrage
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, daten.get("beschreibung"));
            stmt.setString(2, daten.get("teilnehmerId"));

            stmt.executeUpdate(); // SQL-Abfrage ausführen
            System.out.printf("Trainingsplan wurde erfolgreich hinzugefügt.%n");
        } catch (SQLException e) {
            System.out.println("Fehler: " + e.getMessage());
        }
    }

    private static void kursHinzufuegen(Connection conn, Scanner scanner) {
        System.out.println("Neuen Kurs hinzufügen:");

        // Benutzereingaben sammeln
        String kursName = eingabe(scanner, "Kursname");
        int maxTeilnehmer = Integer.parseInt(eingabe(scanner, "Maximale Teilnehmerzahl"));
        String kursBeginn = Validator.validiereDatum(scanner, true);  // Validiert das Datum
        String kursEnde = Validator.validiereDatum(scanner, false);    // Validiert das Datum

        // SQL-Query für das Einfügen eines neuen Kurses (ohne Beschreibung)
        String sql = "INSERT INTO kurs (kursName, maxTeilnehmer, kursBeginn, kursEnde) VALUES (?, ?, ?, ?)";

        // SQL-Abfrage ausführen
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kursName);
            stmt.setInt(2, maxTeilnehmer);
            stmt.setString(3, kursBeginn);
            stmt.setString(4, kursEnde);

            stmt.executeUpdate(); // SQL-Abfrage ausführen
            System.out.println("Kurs erfolgreich hinzugefügt.");
        } catch (SQLException e) {
            System.out.println("Fehler beim Hinzufügen des Kurses: " + e.getMessage());
        }
    }


    // Untermenü für die Verwaltung von Trainern
    public static void trainerMenu(Connection conn, Scanner scanner) {
        while (true) {
            // Menüoptionen für Trainer anzeigen
            System.out.println("Trainer-Verwaltung");
            System.out.println("1. Trainer hinzufügen");
            System.out.println("2. Trainer anzeigen");
            System.out.println("3. Trainer filtern");
            System.out.println("4. Zurück zum Hauptmenü");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Konsolenpuffer leeren

            // Navigation basierend auf Benutzereingabe
            switch (choice) {
                case 1 -> hinzufuegen(conn, scanner, "trainer");
                case 2 -> anzeigen(conn, "trainer");
                case 3 -> filternTrainer(conn, scanner);
                case 4 -> {
                    System.out.println("--- Zurück zum Hauptmenü ---");
                    return;
                }
                default -> System.out.println("Ungültige Auswahl.");
            }
        }
    }

    // Untermenü für die Verwaltung von Kursen
    public static void kursMenu(Connection conn, Scanner scanner) {
        while (true) {
            // Menüoptionen für Kurse anzeigen
            System.out.println("Kurs-Verwaltung");
            System.out.println("1. Kurs hinzufügen");
            System.out.println("2. Kurse anzeigen");
            System.out.println("3. Trainer einem Kurs zuweisen");
            System.out.println("4. Teilnehmer einem Kurs zuweisen");
            System.out.println("5. Zurück zum Hauptmenü");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Konsolenpuffer leeren

            // Navigation basierend auf Benutzereingabe
            switch (choice) {
                case 1 -> kursHinzufuegen(conn, scanner);
                case 2 -> anzeigen(conn, "kurs");
                case 3 -> zuweisen(conn, scanner, "trainer");
                case 4 -> zuweisen(conn, scanner, "teilnehmer");
                case 5 -> {
                    System.out.println("--- Zurück zum Hauptmenü ---");
                    return;
                }
                default -> System.out.println("Ungültige Auswahl.");
            }
        }
    }

    // Untermenü für die Verwaltung von Teilnehmern
    public static void teilnehmerMenu(Connection conn, Scanner scanner) {
        while (true) {
            // Menüoptionen für Teilnehmer anzeigen
            System.out.println("Teilnehmer-Verwaltung");
            System.out.println("1. Teilnehmer hinzufügen");
            System.out.println("2. Teilnehmer anzeigen");
            System.out.println("3. Teilnehmer filtern");
            System.out.println("4. Zurück zum Hauptmenü");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Konsolenpuffer leeren

            // Navigation basierend auf Benutzereingabe
            switch (choice) {
                case 1 -> hinzufuegen(conn, scanner, "teilnehmer");
                case 2 -> anzeigen(conn, "teilnehmer");
                case 3 -> filternTeilnehmer(conn, scanner);
                case 4 -> {
                    System.out.println("--- Zurück zum Hauptmenü ---");
                    return;
                }
                default -> System.out.println("Ungültige Auswahl.");
            }
        }
    }

    // Methode zum Hinzufügen von Trainern oder Teilnehmern
    private static void hinzufuegen(Connection conn, Scanner scanner, String typ) {
        System.out.printf("%s hinzufügen:%n", capitalize(typ));

        // Dateneingabe vom Benutzer sammeln
        Map<String, String> daten = new HashMap<>();
        daten.put("nachname", eingabe(scanner, "Nachname"));
        daten.put("vorname", eingabe(scanner, "Vorname"));
        daten.put("adresse", eingabe(scanner, "Adresse"));
        daten.put("nummer", Validator.validiereTelefon(scanner));
        daten.put("geburtstag", Validator.validiereGeburtsdatum(scanner)); // Aufruf der Validator-Methode
        daten.put("email", Validator.validiereEmail(scanner));

        if ("trainer".equalsIgnoreCase(typ)) { // Überprüfung, ob der Typ "trainer" ist
            daten.put("verfuegbareKurse", eingabe(scanner, "Verfuegbare Kurse"));
            daten.put("fachgebiet", eingabe(scanner, "Fachgebiet"));
        } else if ("teilnehmer".equalsIgnoreCase(typ)) { // Überprüfung, ob der Typ "teilnehmer" ist
            daten.put("beitrittsdatum", eingabe(scanner, "Beitrittsdatum"));
        } else {
            throw new IllegalArgumentException("Unbekannter Typ: " + typ);
        }

        // SQL-Abfrage je nach Typ
        String sql = switch (typ.toLowerCase()) {
            case "trainer" ->
                    "INSERT INTO trainer (nachname, vorname, adresse, nummer, geburtstag, email, verfuegbareKurse, fachgebiet) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            case "teilnehmer" ->
                    "INSERT INTO teilnehmer (nachname, vorname, adresse, nummer, geburtstag, email, beitrittsdatum) VALUES (?, ?, ?, ?, ?, ?, ?)";
            default -> throw new IllegalArgumentException("Unbekannter Typ: " + typ);
        };

        // Ausführen der SQL-Abfrage
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, daten.get("nachname"));
            stmt.setString(2, daten.get("vorname"));
            stmt.setString(3, daten.get("adresse"));
            stmt.setString(4, daten.get("nummer"));
            stmt.setString(5, daten.get("geburtstag"));
            stmt.setString(6, daten.get("email"));

            // Zusätzliche Parameter für Trainer oder Teilnehmer setzen
            if ("trainer".equalsIgnoreCase(typ)) {
                stmt.setString(7, daten.get("verfuegbareKurse"));
                stmt.setString(8, daten.get("fachgebiet"));
            } else if ("teilnehmer".equalsIgnoreCase(typ)) {
                stmt.setString(7, daten.get("beitrittsdatum"));
            }

            stmt.executeUpdate(); // SQL-Abfrage ausführen
            System.out.printf("%s wurde erfolgreich hinzugefügt.%n", capitalize(typ));
        } catch (SQLException e) {
            System.out.println("Fehler: " + e.getMessage());
        }
    }

    // Methode zur Anzeige von Trainern, Teilnehmern oder Kursen
    private static void anzeigen(Connection conn, String typ) {
        // SQL-Abfrage basierend auf dem Typ
        String sql = switch (typ.toLowerCase()) { // Vergleich case-insensitive
            case "trainer" -> "SELECT * FROM trainer";
            case "teilnehmer" -> "SELECT * FROM teilnehmer";
            case "kurs" -> "SELECT * FROM kurs";
            case "trainingsplan" -> "SELECT * FROM trainingsplan";
            default -> throw new IllegalArgumentException("Unbekannter Typ: " + typ);
        };

        // SQL-Abfrage ausführen und Ergebnisse anzeigen
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.printf("%s-Liste:%n", capitalize(typ));

            // Durch alle Ergebnisse iterieren
            while (rs.next()) {
                Map<String, Object> resultMap = new LinkedHashMap<>();

                // Dynamische Datenverarbeitung je nach Typ
                switch (typ.toLowerCase()) {
                    case "trainer", "teilnehmer" -> {
                        resultMap.put("ID", rs.getInt("id"));
                        resultMap.put("Nachname", rs.getString("nachname"));
                        resultMap.put("Vorname", rs.getString("vorname"));
                        resultMap.put("Adresse", rs.getString("adresse"));
                        resultMap.put("Nummer", rs.getString("nummer"));
                        resultMap.put("Geburtstag", rs.getDate("geburtstag"));
                        resultMap.put("Email", rs.getString("email"));

                        // Trainer hat zusätzliche Felder
                        if ("trainer".equalsIgnoreCase(typ)) {
                            resultMap.put("Verfügbare Kurse", rs.getString("verfuegbareKurse"));
                            resultMap.put("Fachgebiet", rs.getString("fachgebiet"));
                        } else if ("teilnehmer".equalsIgnoreCase(typ)) {
                            resultMap.put("Beitrittsdatum", rs.getDate("beitrittsdatum"));
                        }
                    }
                    case "kurs" -> {
                        resultMap.put("Kurs-ID", rs.getInt("kursId"));
                        resultMap.put("Kurs-Name", rs.getString("kursName"));
                        resultMap.put("Maximale Teilnehmer", rs.getString("maxTeilnehmer"));
                        resultMap.put("Kursbeginn", rs.getDate("Kursbeginn"));
                        resultMap.put("Kursende", rs.getDate("Kursende"));
                    }
                    case "trainingsplan" -> {
                        resultMap.put("Trainingsplan-ID", rs.getInt("trainingsplanID"));
                        resultMap.put("Beschreibung", rs.getString("beschreibung"));
                        resultMap.put("Teilnehmer-ID", rs.getInt("teilnehmerID"));

                    }
                }
                // Ergebnisse ausgeben
                resultMap.forEach((key, value) -> System.out.printf("%s: %s%n", key, value)); //LAMBDA
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Fehler: " + e.getMessage());
        }
    }

    // Methode für die Kurszuweisung
    private static void zuweisen(Connection conn, Scanner scanner, String typ) {
        String sql;
        int id;      // Die ID für den Trainer oder Teilnehmer
        int kursID;  // Die Kurs-ID
        boolean idExists;
        boolean kursExists;

        // Kurs-ID vom Benutzer abfragen
        System.out.println("Bitte geben Sie die Kurs-ID ein:");
        kursID = scanner.nextInt();
        scanner.nextLine(); // Um den Zeilenumbruch nach nextInt() zu verarbeiten

        // Überprüfen, ob die Kurs-ID existiert
        String kursCheckSql = "SELECT COUNT(*) FROM kurs WHERE kursID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(kursCheckSql)) {
            stmt.setInt(1, kursID);  // Kurs-ID setzen
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    kursExists = true;  // Kurs existiert
                } else {
                    System.out.println("Fehler: Der Kurs mit der ID " + kursID + " existiert nicht.");
                    return; // Abbruch, wenn Kurs-ID nicht gefunden wird
                }
            }
        } catch (SQLException e) {
            System.out.println("Fehler bei der Überprüfung der Kurs-ID: " + e.getMessage());
            return;
        }

        // Trainer- oder Teilnehmer-ID abfragen, je nach Typ
        if ("trainer".equals(typ)) {
            System.out.println("Bitte geben Sie die Trainer-ID ein:");
            id = scanner.nextInt();
            scanner.nextLine(); // Um den Zeilenumbruch zu verarbeiten

            String trainerCheckSql = "SELECT COUNT(*) FROM trainer WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(trainerCheckSql)) {
                stmt.setInt(1, id);  // Trainer-ID setzen
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        idExists = true;  // Trainer existiert
                    } else {
                        System.out.println("Fehler: Der Trainer mit der ID " + id + " existiert nicht.");
                        return; // Abbruch, wenn Trainer-ID nicht gefunden wird
                    }
                }
            } catch (SQLException e) {
                System.out.println("Fehler bei der Überprüfung der Trainer-ID: " + e.getMessage());
                return;
            }

            // Einfügen in die trainer_kurs-Tabelle
            sql = "INSERT INTO trainer_kurs (trainerID, kursID) VALUES (?, ?)";
        } else {
            System.out.println("Bitte geben Sie die Teilnehmer-ID ein:");
            id = scanner.nextInt();
            scanner.nextLine(); // Um den Zeilenumbruch zu verarbeiten

            String teilnehmerCheckSql = "SELECT COUNT(*) FROM teilnehmer WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(teilnehmerCheckSql)) {
                stmt.setInt(1, id);  // Teilnehmer-ID setzen
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        idExists = true;  // Teilnehmer existiert
                    } else {
                        System.out.println("Fehler: Der Teilnehmer mit der ID " + id + " existiert nicht.");
                        return; // Abbruch, wenn Teilnehmer-ID nicht gefunden wird
                    }
                }
            } catch (SQLException e) {
                System.out.println("Fehler bei der Überprüfung der Teilnehmer-ID: " + e.getMessage());
                return;
            }

            // Einfügen in die teilnehmer_kurs-Tabelle
            sql = "INSERT INTO teilnehmer_kurs (teilnehmerID, kursID) VALUES (?, ?)";
        }

        // Wenn die IDs gültig sind, den Eintrag in die entsprechende Zwischentabelle einfügen
        if (idExists && kursExists) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);  // Trainer- oder Teilnehmer-ID setzen
                stmt.setInt(2, kursID);  // Kurs-ID setzen
                stmt.executeUpdate();
                System.out.println("Erfolgreich in die Tabelle " + (typ.equals("trainer") ? "trainer_kurs" : "teilnehmer_kurs") + " eingefügt.");
            } catch (SQLException e) {
                System.out.println("Fehler beim Einfügen in die Zwischentabelle: " + e.getMessage());
            }
        }
    }


    // Validierungs- und Eingabehilfsmethoden
    private static String eingabe(Scanner scanner, String feldname) {
        System.out.printf("Bitte %s eingeben: ", feldname);
        return scanner.nextLine();
    }

    // Hilfsmethode zum Capitalize eines Strings
    private static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    // Filtern von Trainingsplänen nach Teilnehmer-ID
    private static void filternTrainingsplan(Connection conn, Scanner scanner) {
        System.out.println("Bitte geben Sie die Teilnehmer-ID ein, um die zugehörigen Trainingspläne zu filtern:");
        String teilnehmerID = scanner.nextLine();

        // Überprüfen, ob die Teilnehmer-ID existiert
        String checkSql = "SELECT COUNT(*) FROM Teilnehmer WHERE id = ?"; // warum count
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, teilnehmerID);

            try (ResultSet checkRs = checkStmt.executeQuery()) {
                if (checkRs.next() && checkRs.getInt(1) > 0) {
                    // Wenn die Teilnehmer-ID existiert, dann die Trainingspläne abfragen
                    String sql = "SELECT * FROM Trainingsplan WHERE trainingsplanId = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, teilnehmerID);

                        try (ResultSet rs = stmt.executeQuery()) {
                            while (rs.next()) {
                                System.out.printf("Trainingsplan-ID: %d%n", rs.getInt("trainingsplanID"));
                                System.out.printf("Beschreibung: %s%n", rs.getString("beschreibung"));
                                System.out.printf("Teilnehmer-ID: %d%n", rs.getInt("teilnehmerID"));
                                System.out.println();
                            }
                        }
                    }
                } else {
                    System.out.println("Fehler: Teilnehmer-ID " + teilnehmerID + " existiert nicht.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Fehler: " + e.getMessage());
        }
    }

    private static void filternTrainer(Connection conn, Scanner scanner) {
        System.out.println("Bitte geben Sie den Namen ein, um Trainer zu filtern:");
        String name = scanner.nextLine();

        String checkSql = "SELECT COUNT(*) FROM Trainer WHERE vorname LIKE ? OR nachname LIKE ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, "%" + name + "%");
            checkStmt.setString(2, "%" + name + "%");

            // Führt eine SQL-Abfrage aus und speichert das Ergebnis in einem ResultSet-Objekt
            try (ResultSet checkRs = checkStmt.executeQuery()) {
                // Prüft, ob das ResultSet eine Zeile enthält (d.h. es gibt Ergebnisse)
                // und ob der Wert in der ersten Spalte (z.B. Anzahl gefundener Trainer) größer als 0 ist
                if (checkRs.next() && checkRs.getInt(1) > 0) {
                    // Gibt die Anzahl der gefundenen Trainer aus
                    System.out.println(checkRs.getInt(1) + " Trainer gefunden:");


            // Wenn Trainer existieren, die eigentliche Abfrage ausführen
                    String sql = "SELECT * FROM Trainer WHERE vorname LIKE ? OR nachname LIKE ?";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, "%" + name + "%");
                        stmt.setString(2, "%" + name + "%");

                        try (ResultSet rs = stmt.executeQuery()) {
                            while (rs.next()) {
                                System.out.printf("Trainer-ID: %d%n", rs.getInt("id"));
                                System.out.printf("Nachname: %s%n", rs.getString("nachname"));
                                System.out.printf("Vorname: %s%n", rs.getString("vorname"));
                                System.out.printf("Adresse: %s%n", rs.getString("adresse"));
                                System.out.printf("Telefon: %s%n", rs.getString("nummer"));
                                System.out.printf("Geburtstag: %s%n", rs.getDate("geburtstag"));
                                System.out.printf("Email: %s%n", rs.getString("email"));
                                System.out.printf("Verfügbare Kurse: %s%n", rs.getInt("verfuegbareKurse"));
                                System.out.printf("Fachgebiet: %s%n", rs.getString("fachgebiet"));
                                System.out.println();
                            }
                        }
                    }
                } else {
                    System.out.println("Kein Trainer mit dem Namen '" + name + "' gefunden.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Fehler: " + e.getMessage());
        }
    }

    private static void filternTeilnehmer(Connection conn, Scanner scanner) {
        System.out.println("Bitte geben Sie den Namen ein, um Teilnehmer zu filtern:");
        String name = scanner.nextLine();

        String checkSql = "SELECT COUNT(*) FROM Teilnehmer WHERE vorname LIKE ? OR nachname LIKE ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, "%" + name + "%");
            checkStmt.setString(2, "%" + name + "%");

            try (ResultSet checkRs = checkStmt.executeQuery()) {
                if (checkRs.next() && checkRs.getInt(1) > 0) {
                    System.out.println(checkRs.getInt(1) + " Teilnehmer gefunden:");

                    // Wenn Teilnehmer existieren, die eigentliche Abfrage ausführen
                    String sql = "SELECT * FROM Teilnehmer WHERE vorname LIKE ? OR nachname LIKE ?";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, "%" + name + "%");
                        stmt.setString(2, "%" + name + "%");

                        try (ResultSet rs = stmt.executeQuery()) {
                            while (rs.next()) {
                                System.out.printf("Teilnehmer-ID: %d%n", rs.getInt("id"));
                                System.out.printf("Nachname: %s%n", rs.getString("nachname"));
                                System.out.printf("Vorname: %s%n", rs.getString("vorname"));
                                System.out.printf("Adresse: %s%n", rs.getString("adresse"));
                                System.out.printf("Telefon: %s%n", rs.getString("nummer"));
                                System.out.printf("Geburtstag: %s%n", rs.getDate("geburtstag"));
                                System.out.printf("Email: %s%n", rs.getString("email"));
                                System.out.printf("Beitrittsdatum: %s%n", rs.getDate("beitrittsdatum"));
                                System.out.println();
                            }
                        }
                    }
                } else {
                    System.out.println("Kein Teilnehmer mit dem Namen '" + name + "' gefunden.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Fehler: " + e.getMessage());
        }
    }
}