package com.noxpvp.homes.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.commands.CommandRunner;
import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;
import com.noxpvp.core.locales.GlobalLocale;
import com.noxpvp.core.utils.MessageUtil;
import com.noxpvp.core.utils.PermissionHandler;
import com.noxpvp.homes.HomeManager;
import com.noxpvp.homes.NoxHomes;
import com.noxpvp.homes.tp.BaseHome;

public class DeleteHomeCommand implements CommandRunner {
	public static final String COMMAND_NAME = "delhome";
	public static final String PERM_NODE = "delhome";
	private HomeManager manager;
	private PermissionHandler permHandler;
	private NoxHomes plugin;
	
	public DeleteHomeCommand()
	{
		plugin = NoxHomes.getInstance();
		manager = plugin.getHomeManager();
		permHandler = NoxHomes.getInstance().getPermissionHandler();
	}
	
	
	public boolean execute(CommandContext context) {
		if (context.hasFlag("h") || context.hasFlag("help"))
		{
			displayHelp(context.getSender());
			if (!context.isPlayer())
				MessageUtil.sendMessage(context.getSender(), "Use (-p | --player) to specify a player to use this on.");
			return true;
		}
		
		Player player;
		
		if (context.hasFlag("p"))
			player = Bukkit.getPlayer(context.getFlag("p", String.class));
		else if (context.hasFlag("player"))
			player = Bukkit.getPlayer(context.getFlag("player", String.class));
		else
			player = (context.isPlayer())? context.getPlayer() : null;
			
		if (player == null)
		{
			MessageUtil.sendLocale(context.getSender(), GlobalLocale.CONSOLE_NEEDPLAYER, "To delete a home.");
			return true;
		}
		
		boolean own = player.equals(context.getSender());
		
		String homeName = null;
		if (context.hasArgument(0))
			homeName = context.getArgument(0);
		
		String perm = StringUtil.join(".", NoxHomes.HOMES_NODE, PERM_NODE, (own? "": "others.") + (homeName==null ? "default": "named"));
		
		if (!permHandler.hasPermission(context.getSender(), perm))
			throw new NoPermissionException(context.getSender(), perm, new StringBuilder().append("Delete ").append(((own)?"Own":"Others")).append(" homes.").toString());

		BaseHome home = manager.getHome(player.getName(), homeName);
		
		if (home != null)
			manager.removeHome(home);
		
		MessageUtil.sendLocale(plugin, context.getSender(), "homes.delhome"+((own)?".own":""), player.getName(), (homeName == null? "default": homeName));
		return true;
	}
	
	public void displayHelp(CommandSender sender)
	{
		MessageBuilder mb = new MessageBuilder();
		
		mb.setSeparator("\n");
		for (String line : GlobalLocale.HELP_HEADER.get("Homes", COMMAND_NAME).split("\n"))
			mb.append(line);
		for (String line : getHelp())
			mb.append(line);
		
		MessageUtil.sendMessage(sender, mb.lines());
	}
	
	public String[] getHelp()
	{
		MessageBuilder mb = new MessageBuilder();
		mb.gold("/").blue(COMMAND_NAME).aqua(" [").aqua("name]").newLine();
		return mb.lines();
	}

	public String getName() {
		return COMMAND_NAME;
	}

}
