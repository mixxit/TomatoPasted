package ulthirm.Commands;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import ulthirm.Celestine.ConnectionManager;
import ulthirm.Celestine.Main;

public class begin extends ListenerAdapter {
	private Connection con = null;
	private Statement stmt = null;
	private ResultSet rs = null;

	@SuppressWarnings("unchecked")
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");

		// Guard checks
		if (args.length < 1)
			return;

		String command = args[0].toLowerCase();
		if (event.getAuthor().isBot() == true)
			return;

		if (!command.startsWith(Main.prefix.toLowerCase()))
			return;

		command = command.replaceFirst(Main.prefix.toLowerCase(), "");

		// Now command is just the word they used (lowercased already)
		// Forward to a handler
		switch (command) {
		case "setup":
			CommandSetup(event);
			return;
		default:
			CommandOther(event);
			return;
		}

	}

	private void CommandSetup(MessageReceivedEvent event) {
		StoreMembers(event.getGuild().getMembers());

	}

	private void StoreMembers(List<Member> members) {

		Connection conn = null;
		
		try {
			Properties props = new Properties();
			props.setProperty("user",Main.jobj.getString("SQLUsername"));
			props.setProperty("password",Main.jobj.getString("SQLPassword"));
			props.setProperty("ssl","true");
			
			conn = DriverManager.getConnection(Main.jobj.getString("SQLHostname"),props);
			for (Member member : members)
			{
				insertUserIfNotExists(conn, member);
				recordUserHistory(conn, member);
			}
			
		} catch (SQLException ex) {
        	System.out.println(ex.getMessage() + " " + ex.getStackTrace());
		} finally {
		    if (conn != null) {
		        try {
		            conn.close();
		        } catch (SQLException e) { 
		        	// do nothing
		        }
		    }
		}
	}

	private Member insertUserIfNotExists(Connection conn, Member m) throws SQLException {
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt = conn.prepareStatement("INSERT INTO Users (Id,CurrentName) VALUES (?,?) ON CONFLICT (Id) DO UPDATE SET CurrentName = excluded.CurrentName");
			pstmt.setLong(1, m.getUser().getIdLong());
			pstmt.setString(2, m.getUser().getName());
		}
		catch (SQLException ex) {
			throw ex;
		} finally {
		    if (pstmt != null) {
		        try {
		        	pstmt.close();
		        } catch (SQLException e) { 
		        	// do nothing
		        	}
		    }
		}
		
		System.out.println("Recorded user ID for name: " + m.getUser().getName());
		return m;
	}
	
	private Member recordUserHistory(Connection conn, Member m) throws SQLException {
		PreparedStatement pstmt = null;
		
		try
		{
			pstmt = conn.prepareStatement("INSERT INTO UserHistory (Id,Name,Date) VALUES (?,?,CURRENT_TIME) ON CONFLICT (Id,Name) DO NOTHING RETURNING Id");
			pstmt.setLong(1, m.getUser().getIdLong());
			pstmt.setString(2, m.getUser().getName());
			pstmt.executeUpdate();
		}
		catch (SQLException ex) {
			throw ex;
		} finally {
		    if (pstmt != null) {
		        try {
		        	pstmt.close();
		        } catch (SQLException e) { 
		        	// do nothing
		        	}
		    }
		}
		
		System.out.println("Recorded user history for name: " + m.getUser().getName());
		return m;
	}
		
	private void CommandOther(MessageReceivedEvent event) {
		// TODO Auto-generated method stub

	}
}
