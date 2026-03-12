package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.impl.render.Render3DEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.util.render.RenderUtil;
import net.minecraft.world.entity.player.Player;

public class NameTagsModule extends Module {
    public NameTagsModule() {
        super("NameTags", "Shows better nametags with health", Category.RENDER);
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        for (Player player : mc.level.players()) {
            if (player == mc.player || !player.isAlive()) continue;

            double x = player.getX();
            double y = player.getY() + player.getBbHeight() + 0.5;
            double z = player.getZ();

            float health = player.getHealth() + player.getAbsorptionAmount();
            String healthText = String.format(" [%.1f]", health);
            String name = player.getGameProfile().getName() + healthText;

            // Note: RenderUtil.drawTextInWorld would handle the MatrixStack and scaling
            RenderUtil.drawTextInWorld(event.getMatrix(), name, x, y, z, getHealthColor(health));
        }
    }

    private int getHealthColor(float health) {
        if (health > 15) return 0x00FF00; // Green
        if (health > 7) return 0xFFFF00;  // Yellow
        return 0xFF0000;                 // Red
    }
}
