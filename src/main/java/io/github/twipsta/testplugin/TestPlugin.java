package io.github.twipsta.testplugin;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin implements Listener {
	long[] login = new long[32]; 
	onlinePlayers[] onlinePlayers = new onlinePlayers[4];
	
	public void onEnable(){
		getLogger().info("Hello World!");
		getServer().getPluginManager().registerEvents(this, this);
		
		onlinePlayers[0].name = "twipsta";
		onlinePlayers[0].totalCredits = (double) 1337;
				
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player){
			if(cmd.getName().equalsIgnoreCase("hello")){
				sender.sendMessage("Hello, " + sender.getName() + ", you are a player");
			} 
			else if(cmd.getName().equalsIgnoreCase("ptime")){
				sender.sendMessage(ChatColor.YELLOW + "Session Time: " + ChatColor.RED + getTime(0));
			}
			else if(cmd.getName().equalsIgnoreCase("addplayer")) {
				int pos = 0;
				int totalCredits = 0;
				String name = null;
				try {
					name = args[1];
					pos = Integer.parseInt(args[0]);
					totalCredits = Integer.parseInt(args[2]);
				} catch (NumberFormatException e) {
					sender.sendMessage("Invalid ID or ammount specified");
				}
				onlinePlayers[pos].name = name;
				onlinePlayers[pos].totalCredits = (double) totalCredits;
				sender.sendMessage("Added " + onlinePlayers[pos].name + "to online players with " + onlinePlayers[pos].totalCredits + ", at index " + pos);
			}
		}
		else{
			if(cmd.getName().equalsIgnoreCase("hello")){
				sender.sendMessage("Hello, " + sender.getName() + ", you are the console");
			}
		}
		return false;
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent player){
		player.getPlayer().sendMessage("OHAI PLAIS ENJOI HOUR SERVR!!!");
		Date date = new Date();
		Bukkit.getPlayer("twipsta").sendMessage("Sending this to twipsta");
		Bukkit.broadcastMessage("Join Time: " + date.getTime());
		login[1] = date.getTime();
	}
	
	public String getTime(long time){
		Date date = new Date();
		long curTime = date.getTime();
		long playTime = curTime - login[1];
		
		long days = TimeUnit.MILLISECONDS.toDays(playTime);
		playTime -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(playTime);
		playTime -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(playTime);
		playTime -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(playTime);
		
		return("" + days + " days " + hours + " hours " + minutes + " minutes " + seconds + " seconds");
	}
}
