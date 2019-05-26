package ulthirm.Celestine;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONObject;

public class ConnectionManager {
	private static String driverName = "org.postgresql.Driver";
	private static String username = "postgres";
	private static String password = "1111";
	private static Connection c;


	public static Connection getConnection() {
		try {
			Class.forName(driverName);
			try {
				c = DriverManager.getConnection("jdbc:postgresql:" + Main.jobj.getString("SQLServer"), username, password);
				System.out.println("Opened database successfully");
				try (Statement stmt = c.createStatement()) {
					final DatabaseMetaData metaData = c.getMetaData();
					final ResultSet tables = metaData.getTables(null, null, Main.jobj.getString("SQLTable"), null);
					if (tables.next()) {
						System.out.println("The table named " + Main.jobj.getString("SQLTable") + " already exists");
					} else {
						System.out.println("Creating table: " + Main.jobj.getString("SQLTable"));
						createTable(c, stmt, Main.jobj);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} catch (SQLException ex) {
				// log an exception. for example:
				System.out.println("Failed to create the database connection.");
			}
		} catch (ClassNotFoundException ex) {
			// log an exception. for example:
			System.out.println("Driver not found.");
		}
		return c;
	}

	public static void createTable(Connection c, Statement stmt, JSONObject jobj) {
		try {
			stmt = c.createStatement();
			String sql = String.format("CREATE TABLE %s", jobj.getString("SQLTable")) 
					+ "(UID            BIGINT     NOT NULL, "
					+ " CURRENTNAME    TEXT    NOT NULL, "
					+ " NAMEHISTORY    TEXT[]  NOT NULL)";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

}
