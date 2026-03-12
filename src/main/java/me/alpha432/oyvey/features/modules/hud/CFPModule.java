package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.event.impl.render.Render2DEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.client.gui.GuiGraphics;

public class InfoHUD extends Module {
    public InfoHUD() {
        super("InfoHUD", "Displays Coords, FPS, and Ping", Category.CLIENT);
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        int height = mc.getWindow().getGuiScaledHeight();
        int color = 0xFFFFFF;

        // 1. Coordinates
        String coords = String.format("XYZ: %.1f, %.1f, %.1f", mc.player.getX(), mc.player.getY(), mc.player.getZ());
        mc.font.drawShadow(event.getMatrix(), coords, 2, height - 10, color);

        // 2. FPS & Ping
        String fps = "FPS: " + mc.getFps();
        String ping = "MS: " + (mc.getConnection() != null ? mc.getConnection().getPlayerInfo(mc.player.getUUID()).getLatency() : 0);
        
        mc.font.drawShadow(event.getMatrix(), fps, 2, height - 20, color);
        mc.font.drawShadow(event.getMatrix(), ping, 2, height - 30, color);
    }
}
