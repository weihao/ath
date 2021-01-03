package org.akadia.ath.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = "ath", name = "Ath", version = "1.0.0")
public class Main {

    @Inject
    private ProxyServer server;

    @Inject
    private Logger logger;

    @Inject
    @DataDirectory
    public Path configDir;


    @Subscribe
    public void onServerPostConnect(PlayerChooseInitialServerEvent event) {

    }

}