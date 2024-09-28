import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        // Verbindung zur Datenbank herstellen
        Connection conn = DatabaseConnector.connect();

        if (conn != null) {
            try {
                // Menü starten
                MenuHandling.startMenu(conn);

            } catch (Exception e) {
                System.out.println("Ein Fehler ist aufgetreten: " + e.getMessage());
                e.printStackTrace(); // Detaillierte Fehlerausgabe
            } finally {
                try {
                    if (conn != null && !conn.isClosed()) {
                        conn.close(); // Verbindung schließen
                    }
                } catch (Exception ex) {
                    System.out.println("Fehler beim Schließen der Datenbankverbindung: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        } else {
            System.out.println("Verbindung zur Datenbank konnte nicht hergestellt werden.");
        }
    }
}
