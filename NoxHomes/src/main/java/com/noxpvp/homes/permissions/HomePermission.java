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

package com.noxpvp.homes.permissions;

import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.permissions.NoxPermission;
import com.noxpvp.homes.NoxHomes;
import com.noxpvp.homes.commands.*;
import com.noxpvp.homes.tp.DefaultHome;
import com.noxpvp.homes.tp.NamedHome;
import org.bukkit.permissions.PermissionDefault;

public class HomePermission extends NoxPermission {

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Constant Perms
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public static final HomePermission ROOT;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Admin Nodes
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public static final HomePermission ROOT_ADMIN;

	public static final HomePermission ROOT_ADMIN_WIPE;

	public static final HomePermission ADMIN_WIPE_HISTORY;

	public static final HomePermission ADMIN_WIPE_HOMES;

	public static final HomePermission ADMIN_HISTORY;

	public static final HomePermission ADMIN_LOAD;

	public static final HomePermission ADMIN_SAVE;

	public static final HomePermission ADMIN_IMPORT;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Root Nodes
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public static final HomePermission ROOT_SET;

	public static final HomePermission ROOT_SET_OTHERS;

	public static final HomePermission ROOT_WARP;

	public static final HomePermission ROOT_WARP_OTHER;

	public static final HomePermission ROOT_DEL;

	public static final HomePermission ROOT_DEL_OTHERS;

	public static final HomePermission ROOT_LIST;

//	public static final HomePermission ROOT_INVITE;

	public static final HomePermission ROOT_LOCATE;

	public static final HomePermission ROOT_LOCATE_OTHERS_DEFAULT;

	public static final HomePermission ROOT_LOCATE_OTHERS;

	public static final HomePermission ROOT_LOCATE_OTHERS_NAMED;

	public static final HomePermission ROOT_LOCATE_OWN_DEFAULT;

	public static final HomePermission ROOT_LOCATE_OWN_NAMED;


	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Towny Nodes
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public static final HomePermission SET_OTHER_TOWN;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Own Nodes
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public static final HomePermission SET_OWN_NAMED;

	public static final HomePermission SET_OWN_DEFAULT;

	public static final HomePermission DEL_OWN_DEFAULT;

	public static final HomePermission DEL_OWN_NAMED;

	public static final HomePermission LIST_OWN;

	public static final HomePermission WARP_OWN_DEFAULT;

	public static final HomePermission WARP_OWN_NAMED;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Other Nodes
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public static final HomePermission SET_OTHERS_DEFAULT;

	public static final HomePermission SET_OTHERS_NAMED;

	public static final HomePermission DEL_OTHERS_DEFAULT;

	public static final HomePermission DEL_OTHERS_NAMED;

	public static final HomePermission LIST_OTHERS;

	public static final HomePermission WARP_OTHER_DEFAULT;

	public static final HomePermission WARP_OTHER_NAMED;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Locate Nodes
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public static final HomePermission LOCATE_OTHERS_DEFAULT_COMPASS;

	public static final HomePermission LOCATE_OTHERS_DEFAULT_COORDS;

	public static final HomePermission LOCATE_OTHERS_NAMED_COMPASS;

	public static final HomePermission LOCATE_OTHERS_NAMED_COORDS;

	public static final HomePermission LOCATE_OWN_DEFAULT_COORDS;

	public static final HomePermission LOCATE_OWN_DEFAULT_COMPASS;

	public static final HomePermission LOCATE_OWN_NAMED_COORDS;

	public static final HomePermission LOCATE_OWN_NAMED_COMPASS;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Private Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private static final String HOMES_NODE = "nox.homes";

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	static {

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//Admin Nodes
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		ADMIN_WIPE_HOMES = new HomePermission(StringUtil.join(".", HOMES_NODE, "admin.wipe.homes"), "Permission to wipe homes data.", PermissionDefault.OP);
		ADMIN_WIPE_HISTORY = new HomePermission(StringUtil.join(".", HOMES_NODE, "admin.wipe.history"), "Permission to wipe history.", PermissionDefault.OP);

		ROOT_ADMIN_WIPE = new HomePermission(StringUtil.join(".", HOMES_NODE, "admin.wipe.*"), "Permission to wipe all data.", PermissionDefault.OP,
				ADMIN_WIPE_HOMES, ADMIN_WIPE_HISTORY);

		ADMIN_HISTORY = new HomePermission(StringUtil.join(".", HOMES_NODE, "admin.history"), "Ability to read Home History.", PermissionDefault.OP);

		ADMIN_LOAD = new HomePermission(StringUtil.join(".", HOMES_NODE, "admin.load"), "Permission to reload plugin.", PermissionDefault.OP);
		ADMIN_SAVE = new HomePermission(StringUtil.join(".", HOMES_NODE, "admin.save"), "Permission to force save.", PermissionDefault.OP);
		ADMIN_IMPORT = new HomePermission(StringUtil.join(".", HOMES_NODE, "admin", HomeAdminImportCommand.PERM_NODE), "Permission to migrate save data to and from plugin.", PermissionDefault.OP);

		ROOT_ADMIN = new HomePermission("homes.admin.*", "All NoxHome's Admin Permissions", PermissionDefault.FALSE,
				ADMIN_HISTORY,
				ROOT_ADMIN_WIPE,
				ADMIN_SAVE,
				ADMIN_LOAD,
				ADMIN_IMPORT);

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//Set Nodes
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		SET_OWN_DEFAULT = new HomePermission(StringUtil.join(".", HOMES_NODE, SetHomeCommand.PERM_NODE, DefaultHome.PERM_NODE), "Allowed to set default home.", PermissionDefault.OP);
		SET_OWN_NAMED = new HomePermission(StringUtil.join(".", HOMES_NODE, SetHomeCommand.PERM_NODE, NamedHome.PERM_NODE), "Allowed to set named homes.", PermissionDefault.OP);
		SET_OTHER_TOWN = new HomePermission(StringUtil.join(".", HOMES_NODE, SetHomeCommand.PERM_NODE, "other-towns"), "Allows setting homes in other towns you don't belong to.", PermissionDefault.FALSE);

		SET_OTHERS_DEFAULT = new HomePermission(StringUtil.join(".", HOMES_NODE, SetHomeCommand.PERM_NODE, "others", DefaultHome.PERM_NODE), "Allowed to set other peoples default homes.", PermissionDefault.OP);
		SET_OTHERS_NAMED = new HomePermission(StringUtil.join(".", HOMES_NODE, SetHomeCommand.PERM_NODE, "others", NamedHome.PERM_NODE), "Allowed to set other peoples named homes.", PermissionDefault.OP);

		ROOT_SET_OTHERS = new HomePermission(StringUtil.join(".", HOMES_NODE, SetHomeCommand.PERM_NODE, "others", "*"), "Allowed to set any type of other peoples homes.", PermissionDefault.FALSE,
				SET_OTHERS_DEFAULT,
				SET_OTHERS_NAMED);

		ROOT_SET = new HomePermission(StringUtil.join(".", HOMES_NODE, SetHomeCommand.PERM_NODE, "*"), "Allowed to set home of everything including others.", PermissionDefault.FALSE,
				SET_OWN_DEFAULT,
				SET_OWN_NAMED,
				ROOT_SET_OTHERS,
				SET_OTHER_TOWN);

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//Warp Nodes
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		WARP_OWN_DEFAULT = new HomePermission( StringUtil.join(".", HOMES_NODE, HomeCommand.PERM_NODE, "default"), "Allowed to warp to your default home.", PermissionDefault.OP);
		WARP_OWN_NAMED = new HomePermission( StringUtil.join(".", HOMES_NODE, HomeCommand.PERM_NODE, "named"), "Allowed to warp to your named home.", PermissionDefault.OP);

		WARP_OTHER_DEFAULT = new HomePermission( StringUtil.join(".", HOMES_NODE, HomeCommand.PERM_NODE, "others", "default"), "Allowed to warp to other peoples default home's.", PermissionDefault.OP);
		WARP_OTHER_NAMED = new HomePermission( StringUtil.join(".", HOMES_NODE, HomeCommand.PERM_NODE, "others", "named"), "Allowed to warp to other peoples named home's", PermissionDefault.OP);

		ROOT_WARP_OTHER = new HomePermission( StringUtil.join(".", HOMES_NODE, HomeCommand.PERM_NODE, "others", "*"), "Allowed to warp to others homes. Without invitiation.", PermissionDefault.FALSE,
				WARP_OTHER_DEFAULT,
				WARP_OTHER_NAMED);

		ROOT_WARP = new HomePermission( StringUtil.join(".", HOMES_NODE, HomeCommand.PERM_NODE, "*"), "Allowed to warp to any type of home. Including others.", PermissionDefault.FALSE,
				WARP_OWN_DEFAULT,
				WARP_OWN_NAMED,
				ROOT_WARP_OTHER);

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//Deletion Nodes
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		DEL_OWN_DEFAULT = new HomePermission( StringUtil.join(".", HOMES_NODE, DeleteHomeCommand.PERM_NODE, "default"), "Allowed to delete your default home.", PermissionDefault.OP);
		DEL_OWN_NAMED = new HomePermission( StringUtil.join(".", HOMES_NODE, DeleteHomeCommand.PERM_NODE, "named"), "Allowed to delete your named home.", PermissionDefault.OP);

		DEL_OTHERS_DEFAULT = new HomePermission( StringUtil.join(".", HOMES_NODE, DeleteHomeCommand.PERM_NODE, "others", "default"), "Allowed to delete other peoples default home's", PermissionDefault.OP);
		DEL_OTHERS_NAMED = new HomePermission( StringUtil.join(".", HOMES_NODE, DeleteHomeCommand.PERM_NODE, "others", "named"), "Allowed to delete other peoples named home's", PermissionDefault.OP);

		ROOT_DEL_OTHERS = new HomePermission( StringUtil.join(".", HOMES_NODE, DeleteHomeCommand.PERM_NODE, "others", "*"), "Allowed to delete any type of other people's homes.", PermissionDefault.FALSE,
				DEL_OTHERS_DEFAULT,
				DEL_OTHERS_NAMED);

		ROOT_DEL = new HomePermission( StringUtil.join(".", HOMES_NODE, DeleteHomeCommand.PERM_NODE, "*"), "Allowed to delete any and all homes.", PermissionDefault.FALSE,
				DEL_OWN_DEFAULT,
				DEL_OWN_NAMED,
				ROOT_DEL_OTHERS);

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//List Nodes
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


		LIST_OWN = new HomePermission( StringUtil.join(".", "homes", HomeListCommand.LIST_PERM_NODE, "own"), "List your own homes.", PermissionDefault.OP);
		LIST_OTHERS = new HomePermission( StringUtil.join(".", "homes", HomeListCommand.LIST_PERM_NODE, "others"), "List other people's homes.", PermissionDefault.OP);

		ROOT_LIST = new HomePermission( StringUtil.join(".", "homes", HomeListCommand.LIST_PERM_NODE, "*"), "Allows to list any home of any person.", PermissionDefault.FALSE,
				LIST_OWN,
				LIST_OTHERS);

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//Locate Nodes
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		LOCATE_OWN_DEFAULT_COMPASS = new HomePermission( "homes.locate.default.compass", "Allows you to use a compass to locate your home.", PermissionDefault.OP);
		LOCATE_OWN_DEFAULT_COORDS = new HomePermission( "homes.locate.default.coords", "Allows retrieval and display of the coords of your home.", PermissionDefault.OP);

		ROOT_LOCATE_OWN_DEFAULT = new HomePermission( "homes.locate.default.*", "Allows you to retrieve coords and set compass to your default home.", PermissionDefault.OP,
				LOCATE_OWN_DEFAULT_COMPASS,
				LOCATE_OWN_DEFAULT_COORDS);

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		LOCATE_OWN_NAMED_COMPASS = new HomePermission( "homes.locate.named.compass", "Allows you to use a compass to locate your home.", PermissionDefault.OP);
		LOCATE_OWN_NAMED_COORDS = new HomePermission( "homes.locate.named.coords", "Allows retrieval and display of the coords of your home.", PermissionDefault.OP);

		ROOT_LOCATE_OWN_NAMED = new HomePermission( "homes.locate.named.*", "Allows you to retrieve coords and set compass to your named home.", PermissionDefault.OP,
				LOCATE_OWN_NAMED_COMPASS,
				LOCATE_OWN_NAMED_COORDS);

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		LOCATE_OTHERS_DEFAULT_COMPASS = new HomePermission( "homes.locate.others.default.compass", "Allows you to locate others Default home with a compass.", PermissionDefault.OP);
		LOCATE_OTHERS_DEFAULT_COORDS = new HomePermission( "homes.locate.others.default.coords", "Allows you to locate others Default home with coords.", PermissionDefault.OP);

		ROOT_LOCATE_OTHERS_DEFAULT = new HomePermission( "homes.locate.others.default.*", "Allows you to locate other's Default homes.", PermissionDefault.OP,
				LOCATE_OTHERS_DEFAULT_COMPASS,
				LOCATE_OTHERS_DEFAULT_COORDS);

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		LOCATE_OTHERS_NAMED_COMPASS = new HomePermission( "homes.locate.others.named.compass", "Allows you to locate others Named home with a compass.", PermissionDefault.OP);
		LOCATE_OTHERS_NAMED_COORDS = new HomePermission( "homes.locate.others.named.coords", "Allows you to locate others Named home with coords.", PermissionDefault.OP);

		ROOT_LOCATE_OTHERS_NAMED = new HomePermission( "homes.locate.others.named.*", "Allows you to locate others Named homes.", PermissionDefault.OP,
				LOCATE_OTHERS_NAMED_COMPASS,
				LOCATE_OTHERS_NAMED_COORDS);

		ROOT_LOCATE_OTHERS = new HomePermission( "homes.locate.others.*", "Allows you to locate any type of other people's homes.", PermissionDefault.FALSE,
				ROOT_LOCATE_OTHERS_DEFAULT,
				ROOT_LOCATE_OTHERS_NAMED);

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		ROOT_LOCATE = new HomePermission( "homes.locate.*", "Allow you to locate any and all homes.", PermissionDefault.FALSE,
				ROOT_LOCATE_OTHERS,
				ROOT_LOCATE_OWN_DEFAULT,
				ROOT_LOCATE_OWN_NAMED);

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		//ROOT
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		ROOT = new HomePermission( "homes.*", "All of NoxHomes permissions Including admin.", PermissionDefault.FALSE,
				ROOT_ADMIN,
				ROOT_DEL,
				ROOT_LIST,
				ROOT_SET,
				ROOT_WARP,
				ROOT_LOCATE);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Unfinished Perm Nodes
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/*
	//      new HomePermission( "homes.invite.*", "Allowed to set invites to any home of anyone.", PermissionDefault.FALSE,
	//      		new HomePermission( "homes.invite.default", "Allowed to invite others into your default home.", PermissionDefault.OP),
	//      		new HomePermission( "homes.invite.named", "Allowed to invite others into your named home.", PermissionDefault.OP),
	//      		new HomePermission( "homes.invite.others.*", "Allowed to invite others into other people's homes.", PermissionDefault.FALSE,
	//      				new HomePermission( "homes.invite.others.default", "Allowed to invite others into other people's default home.", PermissionDefault.OP),
	//      				new HomePermission( "homes.invite.others.named", "Allowed to invite others into other people's named home's." , PermissionDefault.OP)
	//      		)
	//      )
	*/

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public HomePermission(String node, String description, PermissionDefault defaults, NoxPermission... children) {
		super(NoxHomes.getInstance(), node, description, defaults, children);
	}

	public HomePermission(String node, String description, PermissionDefault defaults) {
		super(NoxHomes.getInstance(), node, description, defaults);
	}
}
