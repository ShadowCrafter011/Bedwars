package org.shadowcrafter.bedwars.data;

import org.apache.commons.lang.time.StopWatch;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.shadowcrafter.bedwars.Bedwars;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;

@Data
public class BwPlayer {
	
	private StopWatch stopWatch;
	@Setter(value=AccessLevel.NONE)
	private Player p;
	
	@Getter(value=AccessLevel.NONE)
	@Setter(value=AccessLevel.NONE)
	private int taskID;
	
	private Location checkpoint;
	private boolean canCheckpoint;
	
	public BwPlayer(Player p) {
		this.p = p;
		this.checkpoint = new Location(Bedwars.getPlugin().getLobby(), -0.5d, 52d, -50.5d);
		this.canCheckpoint = true;
	}
	
	public void startTimer(StopWatch w) {
		this.stopWatch = w;
		this.stopWatch.start();
		
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Bedwars.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				if (stopWatch == null) return;
				int time = (int) stopWatch.getTime();
				int millis = time % 1000 / 100; time -= time % 1000;
				int secs = time / 1000;
				int mins = (int) Math.floor(secs/60); secs = secs % 60;
				int hours = (int) Math.floor(mins/60); mins = mins % 60;
				
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder("§aTime " + (hours > 0 ? hours + ":" : "") + (mins >= 10 ? (mins > 0 || hours > 0 ? mins + ":" : "") : (hours > 0 ? "0" : "") + (mins > 0 || hours > 0 ? mins + ":" : "")) + (secs >= 10 ? secs : (mins > 0 ? "0" : "") + secs) + "." + millis).create());
			}
		}, 0, 1);
	}
	
	public void endTimer(boolean msg) {
		if (stopWatch == null) return;
		int time = (int) stopWatch.getTime();
		int millis = time % 1000 / 100; time -= time % 1000;
		int secs = time / 1000;
		int mins = (int) Math.floor(secs/60); secs = secs % 60;
		int hours = (int) Math.floor(mins/60); mins = mins % 60;
		Bukkit.getScheduler().cancelTask(taskID);
		checkpoint = p.getLocation();
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder("").create());
		if(msg) p.sendMessage("§6Your final time was " + (hours > 0 ? hours + ":" : "") + (mins >= 10 ? (mins > 0 || hours > 0 ? mins + ":" : "") : (hours > 0 ? "0" : "") + (mins > 0 || hours > 0 ? mins + ":" : "")) + (secs >= 10 ? secs : (mins > 0 ? "0" : "") + secs) + "." + millis);
		try{
			this.stopWatch.stop();
		}catch (IllegalStateException | NullPointerException e) {
			return;
		}
	}
	
	public void addCheckPointTimer() {
		canCheckpoint = false;
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				canCheckpoint = true;
			}
		}.runTaskLater(Bedwars.getPlugin(), 40);
	}
	
	public void startNewTimer() {
		startTimer(new StopWatch());
	}
	
}
