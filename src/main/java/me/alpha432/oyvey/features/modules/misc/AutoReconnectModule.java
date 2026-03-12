package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

public class AutoReconnectModule extends Module {
    public Setting<Integer> delay = num("Delay", 5, 1, 30);
    private static ServerAddress lastServerAddress;

    public AutoReconnectModule() {
        super("AutoReconnect", "Automatically reconnects to the last server", Category.MISC);
    }

    // This would be called from a Mixin on DisconnectedScreen
    public void onDisconnect(DisconnectedScreen screen) {
        if (!isEnabled()) return;
        
        // Timer logic to reconnect after 'delay' seconds
        // ConnectScreen.startConnect(parent, mc, lastServerAddress, lastServerInfo, false);
    }
}
