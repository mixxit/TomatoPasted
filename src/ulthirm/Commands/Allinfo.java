package ulthirm.Commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import ulthirm.Celestine.Main;

public class Allinfo extends ListenerAdapter {
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");

		// These are provided with every event in JDA
		JDA jda = event.getJDA(); // JDA, the core of the api.
		long responseNumber = event.getResponseNumber();// The amount of discord events that JDA has received since the
														// last reconnect.

		// Event specific information
		User author = event.getAuthor(); // The user that sent the message
		Message message = event.getMessage(); // The message that was received.
		MessageChannel channel = event.getChannel(); // This is the MessageChannel that the message was sent to.
														// This could be a TextChannel, PrivateChannel, or Group!
		Guild guild = event.getGuild();

		String msg = message.getContentDisplay(); // This returns a human readable version of the Message. Similar to
													// what you would see in the client.

		String gld = guild.getName();
		String gldID = guild.getId();
		String Chnl = channel.getName();
		String msgID = message.getId();
		String auth = author.getName();
		String authID = author.getId();

		boolean bot = author.isBot(); // This boolean is useful to determine if the User that
										// sent the Message is a BOT or not!

		if (args[0].equalsIgnoreCase(Main.prefix + "AllInfo") && bot != true) {

			System.out.println("seen command");
			System.out.println(msg);

			EmbedBuilder info = new EmbedBuilder();
			info.setTitle("All Information");
			info.addField("Programmer", "Ulthirm", false);
			info.addField("Message Author", auth, false);
			info.addField("Message Author ID", authID, false);
			info.addField("Message ID", msgID, false);
			info.addField("Message Channel", Chnl, false);
			info.addField("Message Content", msg, false);
			info.addField("Guild", gld, false);
			info.addField("Guild ID", gldID, false);

			event.getChannel().sendMessage(info.build()).queue();
			info.clear();

		}

	}
}
