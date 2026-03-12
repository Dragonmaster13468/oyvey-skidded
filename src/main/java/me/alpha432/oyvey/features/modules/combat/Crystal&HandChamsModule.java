package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import java.awt.Color;

public class ChamsModule extends Module {
    public Setting<Boolean> crystals = b("Crystals", true);
    public Setting<Boolean> hands = b("Hands", true);
    public Setting<Boolean> throughWalls = b("ThroughWalls", true);
    public Setting<Color> color = color("Color", 255, 0, 255, 100); // Pink transparent

    public ChamsModule() {
        super("Chams", "See crystals and hands through walls", Category.RENDER);
    }
    
    // In your Mixin (RenderCrystal/ItemInHand), check:
    // if (ChamsModule.INSTANCE.isEnabled()) {
    //    RenderSystem.depthFunc(GL11.GL_ALWAYS); // This makes it see-through
    //    RenderUtil.setColor(color.getValue());
    // }
}
