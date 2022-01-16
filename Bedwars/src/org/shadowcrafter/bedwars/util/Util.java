package org.shadowcrafter.bedwars.util;

import java.util.Random;

import org.shadowcrafter.bedwars.data.Teams;

public class Util {

	public Teams getRandomTeam() {
		Random rand = new Random();
		
		int i = rand.nextInt(Teams.values().length);
		
		return Teams.values()[i];
	}
	
}
