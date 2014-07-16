package io.github.twipsta.testplugin;

import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class playerMethods {

	public static void reloadAddPlayers(List<onlinePlayers> playerList){
		Date date = new Date();
		long tempTime = date.getTime();
		Player players[] = Bukkit.getServer().getOnlinePlayers();
		for(int i = 0; i < players.length; i++){
			onlinePlayers tempPlayer = new onlinePlayers(players[i].getName(), tempTime, 0, 0, 0);
			playerList.add(tempPlayer);
			
		}
		
	}
		
}
