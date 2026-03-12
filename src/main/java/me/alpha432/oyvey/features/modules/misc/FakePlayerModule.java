package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.client.player.RemotePlayer;
import com.mojang.authlib.GameProfile;
import java.util.UUID;

public class FakePlayerModule extends Module {
    private RemotePlayer fakePlayer;

    public FakePlayerModule() {
        super("FakePlayer", "Spawns a fake entity for testing", Category.MISC);
    }

    @Override
    public void onEnable() {
        if (mc.player == null || mc.level == null) return;
        fakePlayer = new RemotePlayer(mc.level, new GameProfile(UUID.randomUUID(), "FakePlayer"));
        fakePlayer.copyPosition(mc.player);
        mc.level.addEntity(fakePlayer.getId(), fakePlayer);
    }

    @Override
    public void onDisable() {
        if (fakePlayer != null && mc.level != null) {
            mc.level.removeEntity(fakePlayer.getId(), Entity.RemovalReason.DISCARDED);
        }
    }
}
