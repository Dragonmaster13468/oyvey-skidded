package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.combat.KillAuraModule;
import net.minecraft.world.entity.player.Player;

public class TargetHUD extends Module {
    public TargetHUD() {
        super("TargetHUD", "Shows info about your target", Category.CLIENT);
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Assuming KillAuraModule has a public 'target' field
        Player target = KillAuraModule.target; 
        if (target == null) return;

        int x = mc.getWindow().getGuiScaledWidth() / 2 + 10;
        int y = mc.getWindow().getGuiScaledHeight() / 2 + 10;

        // Draw Background
        // RenderUtil.drawRect(x, y, 100, 40, 0x90000000);

        // Name and Health
        mc.font.drawShadow(event.getMatrix(), target.getGameProfile().getName(), x + 5, y + 5, -1);
        String health = String.format("%.1f HP", target.getHealth() + target.getAbsorptionAmount());
        mc.font.drawShadow(event.getMatrix(), health, x + 5, y + 15, 0xFF0000);
        
        // Bonus: Draw target's armor icons here using the ArmorHUD logic
    }
}
