package ulthirm.Commands;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.core.entities.Guild;
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
		User author = event.getAuthor();
		boolean bot = author.isBot();
		Message message = event.getMessage();
		String msg = message.getContentDisplay();

		String[] args = event.getMessage().getContentRaw().split("\\s+");
		if (args[0].equalsIgnoreCase(Main.prefix + "setup") && bot != true) {
			Guild guild = event.getGuild();
			Long gldID = guild.getIdLong();

			

			StringBuilder builder = new StringBuilder();

			/*
			 * guild.getMembers().forEach(m ->
			 * builder.append("\r\n").append(m.getUser().getId()).append(" ").append(m.
			 * getUser().getName()).append("#").append(m.getUser().getDiscriminator()));
			 *
			guild.getMembers().forEach(m -> builder.append(String.format("INSERT INTO \"%s\" VALUES", Main.jobj.getString("SQLTable"))).append(" ").append(String.format("(%s,",m.getUser().getIdLong())).append(" ")
					.append(String.format("%s,",m.getUser().getName())).append(" ").append(String.format("\"{%s}\" )",m.getUser().getName())));
*/
			guild.getMembers().forEach(m -> insertUserIfNotExists(sqlConnection,m));
			
			event.getChannel().sendMessage(String.format("%s queued", guild.getMembers().size())).queue();

			con = ConnectionManager.getConnection(); 
			try {
				stmt = con.createStatement();
				stmt.executeUpdate(builder.toString());
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			

		}

	}

}
