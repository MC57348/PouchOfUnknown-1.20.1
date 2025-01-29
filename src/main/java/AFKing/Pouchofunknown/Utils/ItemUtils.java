package AFKing.Pouchofunknown.Utils;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemUtils {
    public static int getPouch(Inventory playerInventory) {
        ResourceLocation pouchId = new ResourceLocation("pouchofunknown", "pouch");
        for (int i = 0; i < playerInventory.getContainerSize(); i++) {
            ItemStack itemStack = playerInventory.getItem(i);
            ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(itemStack.getItem());
            if (pouchId.equals(itemId)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean hasEmptySlot(Inventory playerInventory) {
        for (int i = 0; i < playerInventory.getContainerSize(); i++) {
            if (playerInventory.getItem(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
