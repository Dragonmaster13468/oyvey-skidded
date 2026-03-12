package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

public class ReachModule extends Module {
    public Setting<Float> distance = num("Distance", 3.5f, 3.0f, 6.0f);

    public ReachModule() {
        super("Reach", "Extends your hitting and placing distance", Category.PLAYER);
    }

    // Note: Your MixinGameRenderer or MixinEntity should call this:
    // if (ReachModule.INSTANCE.isEnabled()) return ReachModule.INSTANCE.distance.getValue();
}
