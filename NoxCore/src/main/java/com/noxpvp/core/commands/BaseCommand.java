/*
 * Copyright (c) 2014. NoxPVP.com
 *
 * All rights are reserved.
 *
 * You are not permitted to
 * 	Modify
 * 	Redistribute nor distribute
 * 	Sublicense
 *
 * You are required to keep this license header intact
 *
 * You are allowed to use this for non commercial purpose only. This does not allow any ad.fly type links.
 *
 * When using this you are required to
 * 	Display a visible link to noxpvp.com
 * 	For crediting purpose.
 *
 * For more information please refer to the license.md file in the root directory of repo.
 *
 * To use this software with any different license terms you must get prior explicit written permission from the copyright holders.
 */

package com.noxpvp.core.commands;

import com.bergerkiller.bukkit.common.collections.StringMap;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.internal.PermissionHandler;
import com.noxpvp.core.localization.GlobalLocale;
import com.noxpvp.core.utils.NoxMessageBuilder;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public abstract class BaseCommand implements Command {
	private final boolean isPlayerOnly;
	private final String name;
	protected PermissionHandler handler;
	private boolean isRoot;
	private BaseCommand parent;
	private StringMap<BaseCommand> subCommands = new StringMap<BaseCommand>();
	private StringMap<String> subCommandAliases = new StringMap<String>();
	private List<String> aliases = new ArrayList<String>();

	protected static final String[] blankStringArray = new String[0];

	public BaseCommand(@Nullable BaseCommand parent, String name, boolean isPlayerOnly) {
		isRoot = parent == null;

		this.parent = parent;
		this.name = name;
		this.isPlayerOnly = isPlayerOnly;

		handler = getPlugin().getPermissionHandler();
	}

	/**
	 * @param name
	 * @param isPlayerOnly
	 */
	public BaseCommand(String name, boolean isPlayerOnly) {
		this(null, name, isPlayerOnly);
	}

	public final String[] getAliases() {
		return aliases.toArray(new String[aliases.size()]);
	}

	public final void addAlias(String alias) {
		aliases.add(alias);
	}

	public final boolean containsSubCommand(String name) {
		return subCommands.containsKeyLower(name);
	}

	public final boolean containsSubCommand(BaseCommand command) {
		return containsSubCommand(command.getName());
	}

	public void displayHelp(CommandSender sender) {
		StringMap<BaseCommand> cmds = getSubCommandMap();

		NoxMessageBuilder mb = new NoxMessageBuilder(getPlugin(), true);

		for (BaseCommand cmd : cmds.values())
			mb.withCommand(cmd, true);

		mb = onDisplayHelp(mb);

		mb.headerClose(false).send(sender);

	}

	/**
	 * Tells the specified text is a match to an alias of this command.
	 * @param alias
	 * @return true if alias exists. Else false
	 */
	public final boolean isAlias(String alias) {
		return aliases.contains(alias);
	}

	/**
	 * Tells if the specified text is a match to a sub commands alias.
	 * @param alias
	 * @return true if alias exists. Else false
	 */
	public boolean isSubCommandAlias(String alias) {
		return subCommandAliases.containsKeyLower(alias);
	}

	public NoxMessageBuilder onDisplayHelp(NoxMessageBuilder message) {
		return message;
	}

	public abstract CommandResult execute(CommandContext context) throws NoPermissionException;

	public final CommandResult executeCommand(CommandContext context) throws NoPermissionException {
		if (!hasSubCommands() || context.getArgumentCount() == 0)
			return execute(context);

		String[] args = context.getArguments();

		String nextArg = context.getArgument(0);
		CommandContext newContext = null;

		if (context.getArgumentCount() > 1)
			newContext = new CommandContext(context.getSender(), context.getFlags(), Arrays.copyOfRange(args, 1, args.length));
		else
			newContext = new CommandContext(context.getSender(), context.getFlags());

		BaseCommand subCMD = getSubCommand(nextArg);

		if (subCMD != null && newContext != null) {
			if (subCMD.isPlayerOnly() && !context.isPlayer()) {
				GlobalLocale.CONSOLE_ONLYPLAYER.message(context.getSender());
				return new CommandResult(this, true);
			}
			return subCMD.executeCommand(newContext);
		}

		return execute(context);
	}

	public final String getFullName() {
		StringBuilder sb = new StringBuilder();
		BaseCommand last, current = this;
		while (current != null) {
			last = current;
			if (!last.equals(this))
				sb.insert(0, " ");

			current = current.getParent();
			sb.insert(0, last.getName());
		}

		return sb.toString();
	}

	public final String getName() {
		return name;
	}

	public final BaseCommand getParent() {
		return parent;
	}

	private void setParent(BaseCommand command) {
		this.parent = command;
		this.isRoot = false;
	}

	public abstract NoxPlugin getPlugin();

	public final BaseCommand getRoot() {
		if (getParent() == null)
			return this;
		else
			return getParent().getRoot();
	}

	public final BaseCommand getSubCommand(String name) {
		if (containsSubCommand(name))
			return subCommands.getLower(name);
		else if (isSubCommandAlias(name))
			return subCommands.getLower(subCommandAliases.getLower(name));
		return null;
	}

	protected StringMap<BaseCommand> getSubCommandMap() {
		return subCommands;
	}

	public boolean hasArgumentLimit() {
		return getMaxArguments() < 0;
	}

	public final boolean hasSubCommands() {
		return subCommands.size() > 0;
	}

	public final boolean isPlayerOnly() {
		return isPlayerOnly;
	}

	public final boolean isRoot() {
		return isRoot;
	}

	public final void registerSubCommand(BaseCommand command) {
		if (containsSubCommand(command)) {
			getPlugin().log(Level.WARNING, "Sub Command: " + command.getName() + " is already registered to command " + getName());
			return;
		}

		command.setParent(this);
		subCommands.putLower(command.getName(), command);
		if (!LogicUtil.nullOrEmpty(command.getAliases()))
			for (String alias: command.getAliases())
				subCommandAliases.putLower(alias, command.getName());
	}

	public final void registerSubCommands(BaseCommand... commands) {
		for (BaseCommand c : commands)
			registerSubCommand(c);
	}
}

