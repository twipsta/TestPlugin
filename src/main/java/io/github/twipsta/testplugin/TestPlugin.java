package io.github.twipsta.testplugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin implements Listener {
	File playerConfigFile = new File(getDataFolder(),"players.yml");
	YamlConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerConfigFile);
	
	Long login; 
	List<onlinePlayers> playerList = new ArrayList<onlinePlayers>();
	
	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
		
		Player[] alreadyOnline = getServer().getOnlinePlayers(); 
		if(alreadyOnline.length > 0){
			getServer().getConsoleSender().sendMessage("[DLNC Whitelist] " + ChatColor.YELLOW + "People are already online!");
			playerMethods.reloadAddPlayers(playerList);
		}		
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player){
			if(cmd.getName().equalsIgnoreCase("hello")){
				sender.sendMessage("Hello, " + sender.getName() + ", you are a player");
			} 
			else if(cmd.getName().equalsIgnoreCase("ptime")){
				Date date = new Date();
				long tempTime = date.getTime();
				long loginTime = 0;
				String playerName = sender.getName();

				for(int i = 0; i < playerList.size(); i++) {
					onlinePlayers temp = playerList.get(i);			
					if(temp.name == playerName) {
						loginTime = temp.logonTime;
						break;
					}			
				}

				sender.sendMessage(ChatColor.YELLOW + "Session Time: " + ChatColor.RED + getTime(loginTime, tempTime));				
			}
			else if(cmd.getName().equalsIgnoreCase("getplayer")) {
//				int pos = 0;
//				try {
//					pos = Integer.parseInt(args[0]);
//				} catch (NumberFormatException e) {
//					sender.sendMessage("Invalid ID or ammount specified");
//				}
//				sender.sendMessage("Player at ID " + pos + ": " + playerList[pos].name + "  with " + playerList[pos].totalCredits);
				
				if(args.length > 0){					
					if(Bukkit.getPlayer(args[0]) instanceof Player){
						loadPlayer(Bukkit.getPlayer(args[0]));
					}
					else {
						sender.sendMessage(ChatColor.RED + args[0] + " is not a valid player!");
					}
				}
				else {
					sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.RED + " /getplayer <player>");
				}
			
			}
			else if(cmd.getName().equalsIgnoreCase("getplayers")) {

				Date date = new Date();
				long tempTime = date.getTime();
								
				sender.sendMessage(ChatColor.YELLOW + "Online Players:");
				sender.sendMessage(ChatColor.YELLOW + "---------------");
				sender.sendMessage(ChatColor.YELLOW + "Name / Current Session Time / Credits Remaining");
				
				for(int i = 0; i < playerList.size(); i++) {
					onlinePlayers temp = playerList.get(i);			
					sender.sendMessage(ChatColor.YELLOW + Integer.toString(i) + ": " + 
										ChatColor.BLUE + temp.name + 
										ChatColor.YELLOW + " | " + 
										ChatColor.BLUE + getTime(temp.logonTime,tempTime) + 
										ChatColor.YELLOW + " | " + 
										ChatColor.BLUE + temp.totalCredits);
					//getLogger().info("Data: " + temp.name + " | " + temp.logonTime);
				}

			}
		}
		else{
			if(cmd.getName().equalsIgnoreCase("hello")){
				sender.sendMessage("Hello, " + sender.getName() + ", you are the console");
			}
			else if(cmd.getName().equalsIgnoreCase("getplayers")) {

				for(int i = 0; i < playerList.size(); i++) {
					onlinePlayers temp = playerList.get(i);			
					getLogger().info("Data: " + temp.name + " | " + temp.logonTime);
				}

			}

		}
		return false;
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent player){
		Date date = new Date();
		String name;
		name = player.getPlayer().getName();
		login = date.getTime();
		Bukkit.broadcastMessage("Join Time: " + date.getTime());
		onlinePlayers temp = new onlinePlayers();
		temp.name = name;
		temp.logonTime = login;
		playerList.add(temp);
		for(int i = 0; i < playerList.size(); i++) {
			temp = playerList.get(i);			
			getLogger().info("Data: " + temp.name + " | " + temp.logonTime);
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent player){
		Date date = new Date();
		String playerName = player.getPlayer().getName();
		long login = 0;
		long logout = date.getTime();		
		String playTime;
		
		playerName = player.getPlayer().getName();
		
		for(int i = 0; i < playerList.size(); i++) {
			onlinePlayers temp = playerList.get(i);			
			if(temp.name == playerName) {
				login = temp.logonTime;
				playerList.remove(i);
				getLogger().info(playerName + " has been removed from list");
				break;
			}			
		}
		
		playTime = this.getTime(login, logout); 
		
		getLogger().info(player.getPlayer().getName() + " has quit. Reason: " + player.getQuitMessage());
		Bukkit.broadcastMessage("Quit Time: " + date.getTime() + ". Played Time: " + playTime);
	}
	
	public String getTime(long login, long logout){
//		Date date = new Date();
//		long logOut = logout;
//		long logIn = login;
		long playTime = logout - login;
		
		long days = TimeUnit.MILLISECONDS.toDays(playTime);
		playTime -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(playTime);
		playTime -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(playTime);
		playTime -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(playTime);
		
		String tempReturn = new String();
		
		if(days > 0) tempReturn += Long.toString(days) + " days ";
		if(hours > 0) tempReturn += Long.toString(hours) + " hours ";
		if(minutes > 0) tempReturn += Long.toString(minutes) + " minutes ";
		tempReturn += Long.toString(seconds) + " seconds";
		
		return(tempReturn);
		
	}

	public void loadPlayer(Player player){
		if(player instanceof Player){
			String tempPlayer = player.getName();
			if(playerConfig.getString(tempPlayer, "") == ""){
				Date date = new Date();
				long time = date.getTime();
				playerConfig.set(tempPlayer + ".logonTime", time);
				try{
					playerConfig.save(playerConfigFile);
				} catch(IOException e) {
					getLogger().severe("Cannot save player.yml");
				}
			}
		}
		
	}

}
