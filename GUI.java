import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class GUI {
    static Connection conn = DatabaseConnector.connect(); // Sicherstellen, dass DatabaseConnector existiert und die Verbindung hergestellt wird.

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final String PHONE_REGEX = "^\\+?[0-9]{1,4}?[-.\\s]?\\(?\\d{1,4}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}$";

    public static void main(String[] args) {
        if (conn == null) {
            JOptionPane.showMessageDialog(null, "Datenbankverbindung konnte nicht hergestellt werden. Programm wird beendet!");
            System.exit(1);
        }

        // Hauptfenster erstellen
        JFrame frame = new JFrame("Fitnessstudio Verwaltung");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(); // wird verwendet, um Layout-Bedingungen für Komponenten festzulegen
        gbc.fill = GridBagConstraints.HORIZONTAL; // Die Komponente wird sich in horizontaler Richtung ausdehnen
        gbc.insets = new Insets(10, 10, 10, 10);  // Abstand von 10 Pixeln um die Komponente herum

        // Buttons für verschiedene Kategorien
        JButton trainerButton = new JButton("Trainer verwalten");
        JButton teilnehmerButton = new JButton("Teilnehmer verwalten");
        JButton kursButton = new JButton("Kurs verwalten");
        JButton trainingsplanButton = new JButton("Trainingsplan verwalten");

        // Button Größe festlegen
        Dimension buttonSize = new Dimension(200, 50);
        trainerButton.setPreferredSize(buttonSize);
        teilnehmerButton.setPreferredSize(buttonSize);
        kursButton.setPreferredSize(buttonSize);
        trainingsplanButton.setPreferredSize(buttonSize);

        // Position der Buttons festlegen
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(trainerButton, gbc);

        gbc.gridy = 1;
        frame.add(teilnehmerButton, gbc);

        gbc.gridy = 2;
        frame.add(kursButton, gbc);

        gbc.gridy = 3;
        frame.add(trainingsplanButton, gbc);

        // ActionListener
        trainerButton.addActionListener(e -> showTrainerDialog(frame));
        teilnehmerButton.addActionListener(e -> showTeilnehmerDialog(frame));
        kursButton.addActionListener(e -> showKursDialog(frame));
        trainingsplanButton.addActionListener(e -> showTrainingsplanDialog(frame));

        frame.setVisible(true);
    }

    // Trainingsplan Dialog anzeigen
    private static void showTrainingsplanDialog(JFrame frame) {
        JDialog trainingsplanDialog = new JDialog(frame, "Trainingsplan", true);
        trainingsplanDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        trainingsplanDialog.setSize(300, 200);
        trainingsplanDialog.setLayout(new GridLayout(4, 1, 10, 10));

        JButton addTrainingsplanButton = new JButton("Trainingsplan hinzufügen");
        JButton viewTrainingsplanButton = new JButton("Trainingsplan anzeigen");

        addTrainingsplanButton.addActionListener(e -> addTrainingsplanDialog());
        viewTrainingsplanButton.addActionListener(e -> showViewDialog("trainingsplan"));

        trainingsplanDialog.add(addTrainingsplanButton);
        trainingsplanDialog.add(viewTrainingsplanButton);
        trainingsplanDialog.setVisible(true);
    }

    private static void addTrainingsplanDialog() {
        // Textfelder für die Benutzereingabe erstellen
        JTextField beschreibungField = new JTextField();
        JTextField teilnehmerIdField = new JTextField();

        // Das Dialogfeld mit den Eingabefeldern definieren
        Object[] fields = {
                "Beschreibung:", beschreibungField,
                "TeilnehmerId:", teilnehmerIdField
        };

        // Dialogfeld zur Eingabe des Trainingsplans anzeigen
        int option = JOptionPane.showConfirmDialog(null, fields, "Trainingsplan hinzufügen", JOptionPane.OK_CANCEL_OPTION);

        // Wenn der Benutzer auf "OK" geklickt hat
        if (option == JOptionPane.OK_OPTION) {
            String beschreibung = beschreibungField.getText();
            String teilnehmerIdStr = teilnehmerIdField.getText();

            // Überprüfen, ob die Felder ausgefüllt sind
            if (beschreibung.isEmpty() || teilnehmerIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Bitte füllen Sie alle Felder aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validierung der Teilnehmer-ID, sie sollte eine Zahl sein
            int teilnehmerId;
            try {
                teilnehmerId = Integer.parseInt(teilnehmerIdStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "TeilnehmerId muss eine Zahl sein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Trainingsplan in die Datenbank einfügen
            String sql = "INSERT INTO trainingsplan (beschreibung, teilnehmerId) VALUES (?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                // SQL-Parameter setzen
                stmt.setString(1, beschreibung);
                stmt.setInt(2, teilnehmerId);

                // SQL-Statement ausführen
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Trainingsplan erfolgreich hinzugefügt!");
            } catch (SQLException e) {
                // Fehlerbehandlung für SQL-Fehler
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Fehler beim Hinzufügen des Trainingsplans.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    // Kurs Dialog
    private static void showKursDialog(JFrame parent) {
        JDialog kursDialog = new JDialog(parent, "Kurs", true);
        kursDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        kursDialog.setSize(300, 200);
        kursDialog.setLayout(new GridLayout(4, 1, 10, 10));

        JButton addCourseButton = new JButton("Kurs hinzufügen");
        JButton viewCourseButton = new JButton("Kurs anzeigen");
        JButton assignTrainerButton = new JButton("Trainer einem Kurs zuweisen");
        JButton assignTeilnehmerButton = new JButton("Teilnehmer einem Kurs zuweisen");

        addCourseButton.addActionListener(e -> showAddCourseDialog());
        viewCourseButton.addActionListener(e -> showViewDialog("kurs"));
        assignTrainerButton.addActionListener(e -> showAssignDialog("Trainer"));
        assignTeilnehmerButton.addActionListener(e -> showAssignDialog("Teilnehmer"));

        kursDialog.add(addCourseButton);
        kursDialog.add(viewCourseButton);
        kursDialog.add(assignTrainerButton);
        kursDialog.add(assignTeilnehmerButton);

        kursDialog.setVisible(true);
    }

    private static void showAssignDialog(String trainer) {
    }

    // Kurs hinzufügen Dialog
    private static void showAddCourseDialog() {
        JTextField nameField = new JTextField();
        JTextField maxTeilnehmerField = new JTextField();
        JTextField kursBeginnField = new JTextField();
        JTextField kursEndeField = new JTextField();

        Object[] fields = {
                "Kursname:", nameField,
                "Maximale Teilnehmer:", maxTeilnehmerField,
                "Kursbeginn (YYYY-MM-DD):", kursBeginnField,
                "Kursende (YYYY-MM-DD):", kursEndeField
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Kurs hinzufügen", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String maxTeilnehmerStr = maxTeilnehmerField.getText();
            String kursBeginnStr = kursBeginnField.getText();
            String kursEndeStr = kursEndeField.getText();

            // Validierung der Eingaben
            if (name.isEmpty() || maxTeilnehmerStr.isEmpty() || kursBeginnStr.isEmpty() || kursEndeStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Bitte füllen Sie alle Felder aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int maxTeilnehmer;
            try {
                maxTeilnehmer = Integer.parseInt(maxTeilnehmerStr);
                if (maxTeilnehmer <= 0) {
                    JOptionPane.showMessageDialog(null, "Maximale Teilnehmerzahl muss größer als 0 sein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Bitte geben Sie eine gültige Zahl für die maximale Teilnehmerzahl ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validierung der Kursbeginn- und Kursende-Daten
            if (!isValidDate(kursBeginnStr) || !isValidDate(kursEndeStr)) {
                JOptionPane.showMessageDialog(null, "Bitte geben Sie ein gültiges Datum für Kursbeginn und Kursende ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Überprüfen, ob das Kursende nach dem Kursbeginn liegt
            if (LocalDate.parse(kursEndeStr).isBefore(LocalDate.parse(kursBeginnStr))) {
                JOptionPane.showMessageDialog(null, "Kursende muss nach Kursbeginn liegen.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            addCourse(name, maxTeilnehmer, kursBeginnStr, kursEndeStr);
        }
    }

    private static boolean isValidDate(String dateStr) {
        try {
            // Parse das Datum im Format YYYY-MM-DD
            LocalDate parsedDate = LocalDate.parse(dateStr);
            return true;
        } catch (DateTimeParseException e) {
            // Wenn das Datum nicht im gültigen Format ist
            JOptionPane.showMessageDialog(null, "Ungültiges Datum: " + dateStr + ". Bitte das Format YYYY-MM-DD verwenden.", "Ungültiges Datum", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private static void addCourse(String kursname, int maxTeilnehmer, String kursbeginn, String kursende) {
        String sql = "INSERT INTO kurs (kursname, maxTeilnehmer, kursbeginn, kursende) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Setze die Parameter für das SQL-Statement
            stmt.setString(1, kursname);
            stmt.setInt(2, maxTeilnehmer);
            stmt.setDate(3, Date.valueOf(kursbeginn));  // Konvertiere den String in ein SQL Date
            stmt.setDate(4, Date.valueOf(kursende));

            // Führe das SQL-Statement aus
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Kurs erfolgreich hinzugefügt!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Fehler beim Hinzufügen des Kurses.");
        }
    }


    // Teilnehmer Dialog
    private static void showTeilnehmerDialog(JFrame parent) {
        JDialog teilnehmerDialog = new JDialog(parent, "Teilnehmer", true);
        teilnehmerDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        teilnehmerDialog.setSize(300, 200);
        teilnehmerDialog.setLayout(new GridLayout(3, 1, 10, 10));

        JButton addTeilnehmerButton = new JButton("Teilnehmer hinzufügen");
        JButton viewTeilnehmerButton = new JButton("Teilnehmer anzeigen");

        addTeilnehmerButton.addActionListener(e -> showAddPersonDialog("teilnehmer"));
        viewTeilnehmerButton.addActionListener(e -> showViewDialog("teilnehmer"));

        teilnehmerDialog.add(addTeilnehmerButton);
        teilnehmerDialog.add(viewTeilnehmerButton);

        teilnehmerDialog.setVisible(true);
    }

    // Trainer Dialog
    private static void showTrainerDialog(JFrame parent) {
        JDialog trainerDialog = new JDialog(parent, "Trainer", true);
        trainerDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        trainerDialog.setSize(300, 200);
        trainerDialog.setLayout(new GridLayout(3, 1, 10, 10));

        JButton addTrainerButton = new JButton("Trainer hinzufügen");
        JButton viewTrainerButton = new JButton("Trainer anzeigen");

        addTrainerButton.addActionListener(e -> showAddPersonDialog("trainer"));
        viewTrainerButton.addActionListener(e -> showViewDialog("trainer"));

        trainerDialog.add(addTrainerButton);
        trainerDialog.add(viewTrainerButton);

        trainerDialog.setVisible(true);
    }

    // Personen hinzufügen Dialog
    private static void showAddPersonDialog(String typ) {
        JTextField nachnameField = new JTextField();
        JTextField vornameField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField birthdayField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField coursesField = typ.equalsIgnoreCase("trainer") ? new JTextField() : null; // Groß-/Kleinschreibung wird ignoriert
        JTextField subjectField = typ.equalsIgnoreCase("trainer") ? new JTextField() : null; // Für Trainer
        JTextField joinDateField = typ.equalsIgnoreCase("teilnehmer") ? new JTextField() : null; // Für Teilnehmer

        Object[] fields = {
                "Nachname:", nachnameField,
                "Vorname;", vornameField,
                "Adresse:", addressField,
                "Geburtsdatum (YYYY-MM-DD):", birthdayField,
                "Email:", emailField,
                "Telefon:", phoneField,
                typ.equalsIgnoreCase("trainer") ? "Verfügbare Kurse:" : null, coursesField,
                typ.equalsIgnoreCase("trainer") ? "Fachgebiet:" : null, subjectField,
                typ.equalsIgnoreCase("teilnehmer") ? "Beitrittsdatum (YYYY-MM-DD):" : null, joinDateField
        };

        int option = JOptionPane.showConfirmDialog(null, fields, typ + " hinzufügen", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            switch (typ.toLowerCase()) {
                case "trainer" -> {
                    // Check for null fields and validate the input
                    if (coursesField != null && subjectField != null) {
                        addTrainer(
                                nachnameField.getText(),
                                vornameField.getText(),
                                addressField.getText(),
                                birthdayField.getText(),
                                emailField.getText(),
                                phoneField.getText(),
                                coursesField.getText(),
                                subjectField.getText()
                        );
                    }
                }
                case "teilnehmer" -> {
                    // Validate the joinDateField is provided
                    if (joinDateField != null) {
                        addParticipant(
                                nachnameField.getText(),
                                vornameField.getText(),
                                addressField.getText(),
                                birthdayField.getText(),
                                emailField.getText(),
                                phoneField.getText(),
                                joinDateField.getText()
                        );
                    }
                }
                default -> {
                    JOptionPane.showMessageDialog(null, "Ungültiger Typ: " + typ, "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }


    // Teilnehmer hinzufügen in SQL Datenbank
    private static void addParticipant(String nachname, String vorname, String address, String birthday, String email, String phone, String joinDate) {
        if (!isValidBirthDate(birthday) || !isValidEmail(email) || !isValidPhone(phone) || !isValidDate(joinDate)) {
            JOptionPane.showMessageDialog(null, "Ungültige Eingaben. Bitte prüfen.");
            return;
        }

        String sql = "INSERT INTO teilnehmer (nachname, vorname, adresse, nummer, geburtstag, email, beitrittsdatum) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nachname);
            stmt.setString(2, vorname);
            stmt.setString(3, address);
            stmt.setString(4, phone);
            stmt.setDate(5, Date.valueOf(birthday));
            stmt.setString(6, email);
            stmt.setDate(7, Date.valueOf(joinDate));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Teilnehmer erfolgreich hinzugefügt!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Fehler beim Hinzufügen des Teilnehmers.");
        }
    }

    // Trainer hinzufügen
    private static void addTrainer(String nachname, String vorname, String address, String birthday, String email, String phone, String courses, String subject) {
        if (!isValidBirthDate(birthday) || !isValidEmail(email) || !isValidPhone(phone)) {
            JOptionPane.showMessageDialog(null, "Ungültige Eingaben. Bitte prüfen.");
            return;
        }

        String sql = "INSERT INTO trainer (nachname, vorname, adresse, geburtstag, nummer, email, verfuegbareKurse, fachgebiet) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nachname);
            stmt.setString(2, vorname);
            stmt.setString(3, address);
            stmt.setDate(4, Date.valueOf(birthday));
            stmt.setString(6, phone);
            stmt.setString(5, email);
            stmt.setString(7, courses);
            stmt.setString(8, subject);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Trainer erfolgreich hinzugefügt!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Fehler beim Hinzufügen des Trainers.");
        }
    }

    // Tabelle anzeigen
    private static void showViewDialog(String type) {
        String sql = switch (type) {
            case "trainer" -> "SELECT * FROM trainer";
            case "teilnehmer" -> "SELECT * FROM teilnehmer";
            case "kurs" -> "SELECT * FROM kurs";
            case "trainingsplan" -> "SELECT * FROM trainingsplan";
            default -> throw new IllegalArgumentException("Unbekannter Typ: " + type);
        };

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            ResultSetMetaData metaData = rs.getMetaData(); // alle Informationen von Tabelle in RS speichern
            int columnCount = metaData.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) { // durchläuft alle Spalten einer Datenbankabfrage und extrahiert deren Spaltennamen
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) { // durchläuft alle Spalten der aktuellen Zeile im ResultSet
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

            JTable table = new JTable(model);
            TableColumnModel columnModel = table.getColumnModel(); // um das Spaltenmodell der Tabelle zu erhalten
            for (int i = 0; i < columnCount; i++) {
                columnModel.getColumn(i).setPreferredWidth(150); // Größe für jede Spalte ändern
            }

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(600, 300));
            JOptionPane.showMessageDialog(null, scrollPane, type + " anzeigen", JOptionPane.PLAIN_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Validierung einer E-Mail-Adresse
    private static boolean isValidEmail(String email) {
        return Pattern.compile(EMAIL_REGEX).matcher(email).matches();
    }

    // Validierung einer Telefonnummer
    private static boolean isValidPhone(String phone) {
        return Pattern.compile(PHONE_REGEX).matcher(phone).matches();
    }

    // Validierung eines Datums
    private static boolean isValidBirthDate(String date) {
        try {
            LocalDate parsedDate = LocalDate.parse(date);
            if (parsedDate.isAfter(LocalDate.now())) {
                JOptionPane.showMessageDialog(null, "Das Datum darf nicht in der Zukunft liegen.", "Ungültiges Datum", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Ungültiges Datum: " + date, "Ungültiges Datum", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
