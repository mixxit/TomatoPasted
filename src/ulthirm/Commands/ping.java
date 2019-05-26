package ulthirm.Commands;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import ulthirm.Celestine.Main;

public class ping extends ListenerAdapter {
	
	@Override
	@SuppressWarnings("unused")
	public void onMessageReceived(MessageReceivedEvent event) {
		User author = event.getAuthor();
		boolean bot = author.isBot();
		Message message = event.getMessage();
		String msg = message.getContentDisplay();

		String[] args = event.getMessage().getContentRaw().split("\\s+");
		if (args[0].equalsIgnoreCase(Main.prefix + "ping") && bot != true) {

			System.out.println("seen command");
			System.out.println(msg);

			event.getChannel().sendMessage("Pong!").queue();
		}
	}
}