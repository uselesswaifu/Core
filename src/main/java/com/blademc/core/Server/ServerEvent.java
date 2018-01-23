package com.blademc.Server;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.QueryRegenerateEvent;
import cn.nukkit.event.weather.WeatherChangeEvent;

public class ServerEvent implements Listener {

	@EventHandler
	public void onQuery(QueryRegenerateEvent event) {
		event.setMaxPlayerCount(1000);
	}

	@EventHandler
	public void weather(WeatherChangeEvent event) {
		event.setCancelled(true);
	}

}
