package net.cycasc.mc.cycascmaincommands_main;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MainCycascMainCommands extends JavaPlugin {
	public void onEnable() {
		Bukkit.getLogger().info("[" + this.getDescription().getName() + "] Plugin enabled");
		Bukkit.getLogger().info("[" + this.getDescription().getName() + "] Version " + this.getDescription().getVersion());
		Bukkit.getLogger().info("[" + this.getDescription().getName() + "] Plugin by " + this.getDescription().getAuthors().get(0));
	}
	
	public void onDisable() {
		Bukkit.getLogger().info("[" + this.getDescription().getName() + "] Plugin disabled");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("kill")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (args.length == 0) {
					p.setHealth(0);
					p.sendMessage(ChatColor.DARK_GREEN + "You have killed yourself.");
				}
				else if (args.length == 1) {
					if (p.hasPermission(this.getDescription().getName() + "." + cmd.getName())) {
						if (Bukkit.getPlayer(args[0]) != null) {
							Player target = Bukkit.getPlayer(args[0]);
							target.setHealth(0);
							target.sendMessage(ChatColor.GRAY + "You were killd by a command executed by " + ChatColor.DARK_RED + p.getName() + ChatColor.GRAY + ".");
							p.sendMessage(ChatColor.DARK_GREEN + target.getName() + " is dead.");
						}
						else {
							p.sendMessage(ChatColor.DARK_RED + "Player " + args[0] + " was not found / is not online.");
						}
					}
					else {
						p.sendMessage(ChatColor.DARK_RED + "You do not have the permissions to execute this command.");
					}
				}
				else {
					return false;
				}
			}
			else if (args.length == 1) {
				if (Bukkit.getPlayer(args[0]) != null) {
					Player target = Bukkit.getPlayer(args[0]);
					target.setHealth(0);
					target.sendMessage(ChatColor.DARK_RED + "You were killed by the server.");
					Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + target.getName() + " is dead.");
				}
				else {
					Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "Player " + args[0] + " was not found / is not online.");
				}
			}
			else {
				return false;
			}
		}
		else if (cmd.getName().equalsIgnoreCase("listonline")) {
			Player[] pall = Bukkit.getOnlinePlayers().toArray(new Player[0]);
			
			if (sender instanceof Player) {
				Player p = (Player) sender;
				
				if (args.length == 0) {
					if (pall.length > 1) {
						String pallstr1 = "-";
						String pallstr2 = "-";
						boolean thisp = false;
						
						for (int i = 0; i < pall.length; i++) {
							if (pall[i] != p) {
								if (thisp == false) {
									if (pallstr1 == "-") {
										pallstr1 = pall[i].getName();
									}
									else {
										pallstr1 = pallstr1 + ", " + pall[i].getName();
									}
								}
								else {
									if (pallstr2 == "-") {
										pallstr2 = pall[i].getName();
									}
									else {
										pallstr2 = pallstr2 + ", " + pall[i].getName();
									}
								}
							}
							else {
								thisp = true;
							}
						}
						
						p.sendMessage(ChatColor.DARK_GREEN + "There are " + pall.length + " players online...");
						
						if ((pallstr1 != "-") && (pallstr2 != "-")) {
							p.sendMessage(ChatColor.GRAY + pallstr1 + ", " + ChatColor.DARK_GREEN + p.getName() + ChatColor.GRAY + ", " + pallstr2);
						}
						else if (pallstr1 == "-") {
							p.sendMessage(ChatColor.DARK_GREEN + p.getName() + ChatColor.GRAY + ", " + pallstr2);
						}
						else {
							p.sendMessage(ChatColor.GRAY + pallstr1 + ", " + ChatColor.DARK_GREEN + p.getName());
						}
					}
					else {
						p.sendMessage(ChatColor.DARK_GREEN + "You are the only one online right now.");
					}
				}
				else {
					return false;
				}
			}
			else {
				if (args.length == 0) {
					if (pall.length > 1) {
						String pallstr = pall[0].getName();
						
						for (int i = 1; i < pall.length; i++) {
							pallstr = pallstr + ", " + pall[i].getName();
						}
						
						Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "There are " + pall.length + " players online...");
						Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + pallstr);
					}
					else if (pall.length == 1) {
						Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "There is only " + pall[0].getName() + " online.");
					}
					else {
						Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "There are no players online.");
					}
				}
				else {
					return false;
				}
			}
		}
		else if (cmd.getName().equalsIgnoreCase("tp")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (args.length == 1) {
					if (Bukkit.getPlayer(args[0]) != null) {
						Player target = Bukkit.getPlayer(args[0]);
						Location loc = target.getLocation();
						if (target.isFlying()) {
							if (p.getGameMode() == GameMode.CREATIVE) {
								p.setFlying(true);
							}
							else {
								World w = target.getWorld();
								int x = target.getLocation().getBlockX();
								int y = target.getLocation().getBlockY();
								int z = target.getLocation().getBlockZ();
								float yaw = target.getLocation().getYaw();
								float pitch = target.getLocation().getPitch();
								int i = 0;
								while (w.getBlockAt(x, y - i, z).getType() == Material.AIR) {
									i++;
								}
								y = y - i + 1;
								loc = new Location(w, x, y, z, yaw, pitch);
							}
						}
						p.teleport(loc);
						p.sendMessage(ChatColor.DARK_GREEN + "You have been teleported to " + target.getName() + ".");
						target.sendMessage(ChatColor.GRAY + p.getName() + " teleported to you.");
					}
					else {
						p.sendMessage(ChatColor.DARK_RED + "Player " + args[0] + " was not found / is not online.");
					}
				}
				else if (args.length == 2) {
					if (Bukkit.getPlayer(args[0]) != null) {
						Player target1 = Bukkit.getPlayer(args[0]);
						if (target1 == p) {
							if (Bukkit.getPlayer(args[1]) != null) {
								Player target2 = Bukkit.getPlayer(args[1]);
								Location loc = target2.getLocation();
								if (target2.isFlying()) {
									if (target1.getGameMode() == GameMode.CREATIVE) {
										target1.setFlying(true);
									}
									else {
										World w = target2.getWorld();
										int x = target2.getLocation().getBlockX();
										int y = target2.getLocation().getBlockY();
										int z = target2.getLocation().getBlockZ();
										float yaw = target2.getLocation().getYaw();
										float pitch = target2.getLocation().getPitch();
										int i = 0;
										while (w.getBlockAt(x, y - i, z).getType() == Material.AIR) {
											i++;
										}
										y = y - i + 1;
										loc = new Location(w, x, y, z, yaw, pitch);
									}
								}
								target1.teleport(loc);
								target1.sendMessage(ChatColor.DARK_GREEN + "You have been teleported to " + target2.getName() + ".");
								target1.sendMessage(ChatColor.GRAY + "Pro tip: Simply use \"/tp " + target2.getName() + "\"!");
								target2.sendMessage(ChatColor.GRAY + p.getName() + " teleported to you.");
							}
							else {
								p.sendMessage(ChatColor.DARK_RED + "Player " + args[1] + " was not found / is not online.");
							}
						}
						else if (p.hasPermission(this.getDescription().getName() + "." + cmd.getName())) {
							if (Bukkit.getPlayer(args[1]) != null) {
								Player target2 = Bukkit.getPlayer(args[1]);
								Location loc = target2.getLocation();
								if (target2.isFlying()) {
									if (target1.getGameMode() == GameMode.CREATIVE) {
										target1.setFlying(true);
									}
									else {
										World w = target2.getWorld();
										int x = target2.getLocation().getBlockX();
										int y = target2.getLocation().getBlockY();
										int z = target2.getLocation().getBlockZ();
										float yaw = target2.getLocation().getYaw();
										float pitch = target2.getLocation().getPitch();
										int i = 0;
										while (w.getBlockAt(x, y - i, z).getType() == Material.AIR) {
											i++;
										}
										y = y - i + 1;
										loc = new Location(w, x, y, z, yaw, pitch);
									}
								}
								target1.teleport(loc);
								target1.sendMessage(ChatColor.GRAY + "You have been teleported to " + p.getName() + " by " + target2.getName() + ".");
								target2.sendMessage(ChatColor.GRAY + target1.getName() + " was teleported to you by " + p.getName() + ".");
								p.sendMessage(ChatColor.DARK_GREEN + "You teleported " + target1.getName() + " to " + target2.getName() + ".");
							}
							else {
								p.sendMessage(ChatColor.DARK_RED + "Player " + args[1] + " was not found / is not online.");
							}
						}
						else {
							p.sendMessage(ChatColor.DARK_RED + "You do not have the permissions to execute this command.");
						}
					}
					else {
						p.sendMessage(ChatColor.DARK_RED + "Player " + args[0] + " was not found / is not online.");
					}
				}
				else if (args.length == 3) {
					try {
						int x = Integer.parseInt(args[0]);
						int y = Integer.parseInt(args[1]);
						int z = Integer.parseInt(args[2]);
						float yaw = p.getLocation().getYaw();
						float pitch = p.getLocation().getPitch();
						Location loc = new Location(p.getWorld(), x, y, z, yaw, pitch);
						
						p.teleport(loc);
						p.sendMessage(ChatColor.DARK_GREEN + "You have been teleported to [" + x + ", " + y + ", " + z + "].");
					}
					catch (Exception ex) {
						p.sendMessage(ChatColor.DARK_RED + "The specified values do not result in any valid coordinates.");
						p.sendMessage(ChatColor.GRAY + "Example (x, y, z): /tp 128 52 60");
						return false;
					}
				}
				else if (args.length == 4) {
					if (p.hasPermission(this.getDescription().getName() + "." + cmd.getName())) {
						if (Bukkit.getPlayer(args[0]) != null) {
							Player target = Bukkit.getPlayer(args[0]);
							
							try {
								World w = p.getWorld();
								int x = Integer.parseInt(args[1]);
								int y = Integer.parseInt(args[2]);
								int z = Integer.parseInt(args[3]);
								float yaw = target.getLocation().getYaw();
								float pitch = target.getLocation().getPitch();
								Location loc = new Location(w, x, y, z, yaw, pitch);
								
								target.teleport(loc);
								target.sendMessage(ChatColor.GRAY + "You have been teleported to [" + x + ", " + y + ", " + z + "] (" + w.getName() + ") by " + p.getName() + ".");
								p.sendMessage(ChatColor.DARK_GREEN + "You teleported " + target.getName() + " to [" + x + ", " + y + ", " + z + "] (" + w.getName() + ").");
							}
							catch (Exception ex) {
								p.sendMessage(ChatColor.DARK_RED + "The specified values do not result in any valid coordinates.");
								p.sendMessage(ChatColor.GRAY + "Example (x, y, z): /tp Dunkelklinge1 128 52 60");
								return false;
							}
						}
						else {
							p.sendMessage(ChatColor.DARK_RED + "Player " + args[0] + " was not found / is not online.");
						}
					}
					else {
						p.sendMessage(ChatColor.DARK_RED + "You do not have the permissions to execute this command.");
					}
				}
				else if (args.length == 5) {
					if (p.hasPermission(this.getDescription().getName() + "." + cmd.getName())) {
						if (Bukkit.getPlayer(args[0]) != null) {
							Player target = Bukkit.getPlayer(args[0]);
							
							if (Bukkit.getWorld(args[1]) != null) {
								World w = Bukkit.getWorld(args[1]);
								
								try {
									int x = Integer.parseInt(args[2]);
									int y = Integer.parseInt(args[3]);
									int z = Integer.parseInt(args[4]);
									float yaw = target.getLocation().getYaw();
									float pitch = target.getLocation().getPitch();
									Location loc = new Location(w, x, y, z, yaw, pitch);
									
									target.teleport(loc);
									target.sendMessage(ChatColor.GRAY + "You have been teleported to [" + x + ", " + y + ", " + z + "] (" + w.getName() + ") by " + p.getName() + ".");
									p.sendMessage(ChatColor.DARK_GREEN + "You teleported " + target.getName() + " to [" + x + ", " + y + ", " + z + "] (" + w.getName() + ").");
								}
								catch (Exception ex) {
									p.sendMessage(ChatColor.DARK_RED + "The specified values do not result in any valid coordinates.");
									p.sendMessage(ChatColor.GRAY + "Example (x, y, z): /tp Dunkelklinge1 NoobIsland 128 52 60");
									return false;
								}
							}
							else {
								p.sendMessage(ChatColor.DARK_RED + "World " + args[1] + " was not found.");
							}
						}
						else {
							p.sendMessage(ChatColor.DARK_RED + "Player " + args[0] + " was not found / is not online.");
						}
					}
					else {
						p.sendMessage(ChatColor.DARK_RED + "You do not have the permissions to execute this command.");
					}
				}
				else {
					return false;
				}
			}
			else if (args.length == 2) {
				if (Bukkit.getPlayer(args[0]) != null) {
					Player target1 = Bukkit.getPlayer(args[0]);
					if (Bukkit.getPlayer(args[1]) != null) {
						Player target2 = Bukkit.getPlayer(args[1]);
						Location loc = target2.getLocation();
						if (target2.isFlying()) {
							if (target1.getGameMode() == GameMode.CREATIVE) {
								target1.setFlying(true);
							}
							else {
								World w = target2.getWorld();
								int x = target2.getLocation().getBlockX();
								int y = target2.getLocation().getBlockY();
								int z = target2.getLocation().getBlockZ();
								float yaw = target2.getLocation().getYaw();
								float pitch = target2.getLocation().getPitch();
								int i = 0;
								while (w.getBlockAt(x, y - i, z).getType() == Material.AIR) {
									i++;
								}
								y = y - i + 1;
								loc = new Location(w, x, y, z, yaw, pitch);
							}
						}
						target1.teleport(loc);
						target1.sendMessage(ChatColor.GRAY + "You have been teleported to " + target2.getName() + " by the server.");
						target2.sendMessage(ChatColor.GRAY + target1.getName() + " was teleported to you by the server.");
						Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + target1.getName() + " was teleported to " + target2.getName() + ".");
					}
					else {
						Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "Player " + args[1] + " was not found / is not online.");
					}
				}
				else {
					Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "Player " + args[0] + " was not found / is not online.");
				}
			}
			else if (args.length == 5) {
				if (Bukkit.getPlayer(args[0]) != null) {
					Player target = Bukkit.getPlayer(args[0]);
					
					if (Bukkit.getWorld(args[1]) != null) {
						World w = Bukkit.getWorld(args[1]);
						
						try {
							int x = Integer.parseInt(args[2]);
							int y = Integer.parseInt(args[3]);
							int z = Integer.parseInt(args[4]);
							float yaw = target.getLocation().getYaw();
							float pitch = target.getLocation().getPitch();
							Location loc = new Location(w, x, y, z, yaw, pitch);
							
							target.teleport(loc);
							target.sendMessage(ChatColor.GRAY + "You have been teleported to [" + x + ", " + y + ", " + z + "] (" + w.getName() + ") by the server.");
							Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + target.getName() + " was teleported to " + x + ", " + y + ", " + z + " (" + w.getName() + ").");
						}
						catch (Exception ex) {
							Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "The specified values do not result in any valid coordinates.");
							Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "Example (x, y, z): /tp Dunkelklinge1 NoobIsland 128 52 60");
							return false;
						}
					}
					else {
						Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "World " + args[1] + " was not found.");
					}
				}
				else {
					Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "Player " + args[0] + " was not found / is not online.");
				}
			}
			else {
				return false;
			}
		}
		else if (cmd.getName().equalsIgnoreCase("serverinfo")) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.DARK_GREEN + "General server information...");
				sender.sendMessage(ChatColor.GRAY + "System time of server: " + ZonedDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss yyyy-MM-dd zzzz")));
				sender.sendMessage(ChatColor.GRAY + "Version: " + Bukkit.getVersion());
				sender.sendMessage(ChatColor.GRAY + "Plugin count: " + Bukkit.getPluginManager().getPlugins().length);
				sender.sendMessage(ChatColor.GRAY + "MOTD: " + Bukkit.getMotd());
				sender.sendMessage(ChatColor.GRAY + "Default gamemode: " + Bukkit.getDefaultGameMode().name());
				sender.sendMessage(ChatColor.GRAY + "Hardcore: " + Bukkit.isHardcore());
				if ((sender instanceof Player)) {
					sender.sendMessage(ChatColor.GRAY + "World difficulty: " + ((Player)sender).getWorld().getDifficulty().name());
				}
				sender.sendMessage(ChatColor.GRAY + "View distance: " + Bukkit.getViewDistance());
				if (Bukkit.hasWhitelist()) {
					sender.sendMessage(ChatColor.GRAY + "Whitelist: enabled, " + Bukkit.getWhitelistedPlayers().size() + " players whitelisted");
				}
				else {
					sender.sendMessage(ChatColor.GRAY + "Whitelist: disabled");
				}
				Runtime runt = Runtime.getRuntime();
				sender.sendMessage(ChatColor.GRAY + "JVM runtime memory: " + (runt.maxMemory() / (1024L * 1024L)) + " MB reserved, " + ((runt.maxMemory() - runt.freeMemory()) / (1024L * 1024L)) + " MB used, " + (runt.freeMemory() / (1024L * 1024L)) + " MB free");
			}
			else {
				return false;
			}
		}
		else if (cmd.getName().equalsIgnoreCase("playerinfo")) {
			if (args.length == 1) {
				if (Bukkit.getPlayer(args[0]) != null) {
					Player target = Bukkit.getPlayer(args[0]);
					
					sender.sendMessage(ChatColor.DARK_GREEN + "General information of player " + target.getName() + "...");
					if (sender.isOp()) {
						sender.sendMessage(ChatColor.GRAY + "IP-Address: " + target.getAddress().getHostString());
					}
					sender.sendMessage(ChatColor.GRAY + "Operator: " + target.isOp());
					sender.sendMessage(ChatColor.GRAY + "Gamemode: " + target.getGameMode().name());
					sender.sendMessage(ChatColor.GRAY + "Health: " + ((Damageable)target).getHealth() / 2);
					sender.sendMessage(ChatColor.GRAY + "Food level: " + target.getFoodLevel() / 2);
					sender.sendMessage(ChatColor.GRAY + "Level (XP): " + target.getLevel());
					if (target.isSneaking()) {
						sender.sendMessage(ChatColor.GRAY + "Location: HIDDEN (player is sneaking)");
					}
					else if (target.isDead()) {
						sender.sendMessage(ChatColor.GRAY + "Location: NO LOCATION (player is dead)");
					}
					else {
						sender.sendMessage(ChatColor.GRAY + "Location: " + target.getWorld().getName() + " (x: " + target.getLocation().getBlockX() + ", y: " + target.getLocation().getBlockY() + ", z: " + target.getLocation().getBlockZ() + ")");
					}
				}
				else {
					sender.sendMessage(ChatColor.DARK_RED + "Player " + args[0] + " was not found / is not online.");
				}
			}
			else {
				return false;
			}
		}
		
		return true;
	}
}
