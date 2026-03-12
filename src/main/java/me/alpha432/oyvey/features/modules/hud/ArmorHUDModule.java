public void onRenderArmor(Render2DEvent event) {
    int screenWidth = mc.getWindow().getGuiScaledWidth();
    int screenHeight = mc.getWindow().getGuiScaledHeight();
    int x = screenWidth / 2 + 10;
    int y = screenHeight - 60;

    for (int i = 3; i >= 0; i--) {
        var stack = mc.player.getInventory().armor.get(i);
        if (stack.isEmpty()) continue;

        // Render Item
        mc.getItemRenderer().renderAndDecorateItem(stack, x, y);
        
        // Durability Text
        if (stack.isDamageableItem()) {
            int percent = (int) (((float) (stack.getMaxDamage() - stack.getDamageValue()) / stack.getMaxDamage()) * 100);
            int color = percent > 75 ? 0x00FF00 : (percent > 25 ? 0xFFFF00 : 0xFF0000);
            mc.font.drawShadow(event.getMatrix(), percent + "%", x + 2, y - 10, color);
        }
        x += 20;
    }
}
