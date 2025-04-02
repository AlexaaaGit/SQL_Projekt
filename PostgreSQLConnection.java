import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class PostgreSQLConnection {

    public static void main(String[] args) throws SQLException, IOException {

        Properties props = new Properties();
        String url;
        String user;
        String password;

        InputStream input = PostgreSQLConnection.class.getClassLoader().getResourceAsStream("db.properties");
        props.load(input);

        url = props.getProperty("db.url");
        user = props.getProperty("db.user");
        password = props.getProperty("db.password");

        Connection conn = DriverManager.getConnection(url, user, password);
        DatabaseMetaData metaData = conn.getMetaData();
        String[] types = {"TABLE"};
        ResultSet tablesResultSet = metaData.getTables(null, null, "%", types);

        while (tablesResultSet.next()) {
            String tableName = tablesResultSet.getString("TABLE_NAME");
            displayTable(conn, tableName);
        }
    }
    private static void displayTable(Connection conn, String tableName) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        List<Integer> columnWidths = new ArrayList<>();
        System.out.println("--- Zawartość tabeli: " + tableName + " ---");
        for (int i = 1; i <= columnCount; i++) {
            columnWidths.add(rsmd.getColumnName(i).length());
        }

        List<List<String>> dataRows = new ArrayList<>();
        while (rs.next()) {
            List<String> row = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                String value = rs.getString(i);
                String displayValue = (value == null ? "NULL" : value);
                row.add(displayValue);
                columnWidths.set(i - 1, Math.max(columnWidths.get(i - 1), displayValue.length()));
            }
            dataRows.add(row);
        }

        for (int i = 1; i <= columnCount; i++) {
            System.out.printf("%-" + columnWidths.get(i - 1) + "s  ", rsmd.getColumnName(i));
        }
        System.out.println();

        for (int width : columnWidths) {
            System.out.print(String.format("%-" + width + "s", "").replace(' ', '-') + "  ");
        }
        System.out.println();

        for (List<String> row : dataRows) {
            for (int i = 0; i < columnCount; i++) {
                System.out.printf("%-" + columnWidths.get(i) + "s  ", row.get(i));
            }
            System.out.println();
        }
        System.out.println();
    }
}