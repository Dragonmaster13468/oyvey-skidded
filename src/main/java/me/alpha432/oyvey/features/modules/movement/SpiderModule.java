@Subscribe
public void onSpider(TickEvent event) {
    if (mc.player == null || !mc.player.horizontalCollision) return;
    
    // Set upward velocity when touching a wall
    mc.player.setDeltaMovement(mc.player.getDeltaMovement().x, 0.2, mc.player.getDeltaMovement().z);
}
