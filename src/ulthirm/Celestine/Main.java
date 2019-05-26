package ulthirm.Celestine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.security.auth.login.LoginException;

import org.json.JSONObject;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import ulthirm.Commands.Allinfo;
import ulthirm.Commands.begin;
import ulthirm.Commands.ping;
import ulthirm.events.beginDatabase;

public class Main {

	public static JDA jda;
	public static String prefix = "~";
	
	// collects JSON data from specified target config.json
	public static String configData = readFile("config.json");
	public static JSONObject jobj = new JSONObject(configData);

	// Main Method
	public static void main(String[] args) throws LoginException {

		// System.out.println("Token: " + jobj.getString("Token"));

		// Turn on the bot using the above method to solve JSON data collection
		jda = new JDABuilder(AccountType.BOT).setToken(jobj.getString("Token")).build();

		// Adds two easy tests to confirm my bot is correctly working
		jda.getPresence().setStatus(OnlineStatus.IDLE);
		jda.getPresence().setGame(Game.watching(jobj.getString("Game")));
		System.out.println("Prefix: " + prefix);

		jda.addEventListener(new Allinfo());
		jda.addEventListener(new ping());
		jda.addEventListener(new beginDatabase());
		jda.addEventListener(new begin());
	}

	// Just reads the file called in the method
	// stores it and then allows access return
	public static String readFile(String filename) {
		String result = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			result = sb.toString();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
}
