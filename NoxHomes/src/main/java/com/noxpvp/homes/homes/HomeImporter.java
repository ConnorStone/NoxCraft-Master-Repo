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

package com.noxpvp.homes.homes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.SafeLocation;
import com.noxpvp.core.utils.Importer;
import com.noxpvp.homes.NoxHomes;
import com.noxpvp.homes.tp.BaseHome;
import com.noxpvp.homes.tp.DefaultHome;
import com.noxpvp.homes.tp.NamedHome;

public enum HomeImporter implements Importer {
	Essentials(new Importer() {

		public boolean importData(boolean erase) {
			NoxHomes plugin = NoxHomes.getInstance();
			if (plugin == null)
				return false;
			List<BaseHome> homes = new ArrayList<BaseHome>();
			File essentialsDir = new File(StringUtil.join(File.separator, "plugins", "Essentials", "userdata"));
			if (!essentialsDir.exists())
				return false;
			else {
				File[] userFiles = essentialsDir.listFiles();
				List<?> homeLocation;
				for (File userFile : userFiles) {
					try {
						String user = userFile.getName().replaceAll("\\.yml", "");
						YamlConfiguration userConfig = new YamlConfiguration();
						userConfig.load(userFile);

						homeLocation = userConfig.getList("home");
						if (!LogicUtil.nullOrEmpty(homeLocation)) {
							try {
								Map<String, Object> data = new HashMap<String, Object>();
								data.put("world", ((String) homeLocation.get(5)).toLowerCase());
								data.put("x", (Double) homeLocation.get(0));
								data.put("y", (Double) homeLocation.get(1));
								data.put("z", (Double) homeLocation.get(2));
								data.put("pitch", (Float) homeLocation.get(4));
								data.put("yaw", (Float) homeLocation.get(3));

								SafeLocation loc = SafeLocation.deserialize(data);
								if (loc == null)
									throw new Exception();

								homes.add(new DefaultHome(user, loc.toLocation()));

							} catch (Exception e) {
								//This entry failed. Ignore and continue
							}
						}

						ConfigurationSection homeWorlds = userConfig.getConfigurationSection("home.worlds");
						if (homeWorlds != null) {
							for (String homeWorld : homeWorlds.getKeys(false)) {
								ConfigurationSection homeData = userConfig.getConfigurationSection("home.worlds." + homeWorld);

								if (homeData != null) {
									try {
										Map<String, Object> data = new HashMap<String, Object>();
										data.put("world", homeWorld);

										Double x, y, z;
										Float pitch, yaw;

										x = homeData.getDouble("x", Double.NaN);
										y = homeData.getDouble("y", Double.NaN);
										z = homeData.getDouble("z", Double.NaN);
										pitch = (float) homeData.getDouble("pitch", Float.NaN);
										yaw = (float) homeData.getDouble("yaw", Float.NaN);

										if (x.isNaN() || y.isNaN() || z.isNaN() || pitch.isNaN() || yaw.isNaN())
											throw new Exception();

										data.put("x", x);
										data.put("y", y);
										data.put("z", z);
										data.put("pitch", pitch);
										data.put("yaw", yaw);

										SafeLocation loc = SafeLocation.deserialize(data);
										if (loc == null)
											throw new Exception();

										homes.add(new NamedHome(user, homeWorld, loc.toLocation()));
									} catch (Exception e) {
										//This entry failed. Ignore and continue;
									}
								}
							}
						}

						ConfigurationSection homeEntries = userConfig.getConfigurationSection("homes");
						for (String homeName : homeEntries.getKeys(false)) {
							ConfigurationSection homeData = userConfig.getConfigurationSection("homes." + homeName);
							if (homeData != null) {
								try {
									if (homeName.equals("home"))
										homes.add(new DefaultHome(user, SafeLocation.deserialize(homeData.getValues(false)).toLocation()));
									else
										homes.add(new NamedHome(user, homeName, SafeLocation.deserialize(homeData.getValues(false)).toLocation()));
								} catch (Exception e) {
									//This entry failed. Ignore and continue
								}
							}
						}
					} catch (Exception e) {
						return false;
					}
				}
			}
			if (erase)
				plugin.getHomeManager().clear();
			plugin.getHomeManager().addHomes(homes);
			return true;
		}

	}),
	MultiHome(new Importer() {

		public boolean importData(boolean erase) {
			NoxHomes plugin = NoxHomes.getInstance();
			if (plugin == null)
				return false;
			List<BaseHome> homes = new ArrayList<BaseHome>();
			File multiHomeFile = new File(StringUtil.join(File.separator, "plugins", "MultiHome", "homes.txt"));
			if (!multiHomeFile.exists())
				return false;
			else {
				BufferedReader reader = null;
				try {
					FileReader fstream = new FileReader(multiHomeFile);
					reader = new BufferedReader(fstream);

					String line = reader.readLine();
					if (line != null)
						line = line.trim();

					while (line != null) {
						if (!line.startsWith("#") && line.length() > 0) {
							String[] values = line.split(";");
							double x = 0, y = 0, z = 0;
							float pitch = 0, yaw = 0;
							String world = "";
							String name = "";
							String player = "";

							try {
								if (values.length == 7) {
									player = values[0];
									x = Double.parseDouble(values[1]);
									y = Double.parseDouble(values[2]);
									z = Double.parseDouble(values[3]);
									pitch = Float.parseFloat(values[4]);
									yaw = Float.parseFloat(values[5]);

									world = values[6];
									name = "";
								} else if (values.length == 8) {
									player = values[0];
									x = Double.parseDouble(values[1]);
									y = Double.parseDouble(values[2]);
									z = Double.parseDouble(values[3]);
									pitch = Float.parseFloat(values[4]);
									yaw = Float.parseFloat(values[5]);

									world = values[6];
									name = values[7];
								}
							} catch (Exception e) {
								// This entry failed. Ignore and continue.
							}
							SafeLocation l = new SafeLocation(world, x, y, z, pitch, yaw);
							if (name.equals(""))
								homes.add(new DefaultHome(player, l.toLocation()));
							else
								homes.add(new NamedHome(player, name, l.toLocation()));
						}
						line = reader.readLine();
					}
				} catch (Exception e) {
					NoxCore.getInstance().log(Level.SEVERE, "Could not read the homes file from multihome.");
					e.printStackTrace();
					return false;
				} finally {
					if (reader != null)
						try {
							reader.close();
						} catch (IOException e) {
							NoxCore.getInstance().log(Level.SEVERE, "Failed to close file handle for multihome...");
							e.printStackTrace();
						}
				}
				if (erase)
					plugin.getHomeManager().clear();
				plugin.getHomeManager().addHomes(homes);
				return true;
			}
		}
	});

	private Importer importer;

	HomeImporter(Importer importer) {
		this.importer = importer;
	}

	public boolean importData(boolean erase) {
		return this.importer.importData(erase);
	}
}
