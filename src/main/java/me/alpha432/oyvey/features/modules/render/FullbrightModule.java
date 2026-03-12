package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;

public class FullBrightModule extends Module {
    private double oldGamma;

    public FullBrightModule() {
        super("FullBright", "Makes everything bright as day", Category.RENDER);
    }

    @Override
    public void onEnable() {
        if (mc.options != null) {
            oldGamma = mc.options.gamma().get();
            // Set gamma to a massive value to override light levels
            mc.options.gamma().set(100.0);
        }
    }

    @Override
    public void onDisable() {
        if (mc.options != null) {
            // Restore original brightness
            mc.options.gamma().set(oldGamma);
        }
    }
}
