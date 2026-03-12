@Subscribe
public void onUpdate(TickEvent event) {
    if (mc.screen instanceof ChestScreen chest) {
        for (int i = 0; i < chest.getMenu().getContainer().getContainerSize(); i++) {
            ItemStack stack = chest.getMenu().getContainer().getItem(i);
            if (!stack.isEmpty()) {
                // Shift-click the item into player inventory
                mc.gameMode.handleInventoryMouseClick(chest.getMenu().containerId, i, 0, ClickType.QUICK_MOVE, mc.player);
            }
        }
        mc.player.closeContainer();
    }
}
