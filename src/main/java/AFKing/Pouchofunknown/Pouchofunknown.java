package AFKing.Pouchofunknown;

import AFKing.Pouchofunknown.Utils.ItemUtils;
import net.darkhax.itemstages.Restriction; 
import net.darkhax.itemstages.RestrictionManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

@Mod("pouchofunknown")
public class Pouchofunknown {

    private static final Logger LOGGER = LogManager.getLogger();

    public Pouchofunknown() {
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerPickupItem);
        MinecraftForge.EVENT_BUS.register(this);
        ItemLoader.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onPlayerPickupItem(EntityItemPickupEvent event) {
        System.out.println("pickup");
        ItemStack item = event.getItem().getItem();
        Player player = event.getEntity();
        Restriction restriction = RestrictionManager.INSTANCE.getRestriction(player, item);
        LOGGER.info(restriction);
        if (restriction != null && restriction.shouldPreventPickup()) {
            int i = ItemUtils.getPouch(player.getInventory());
            if (i == -1) {
                player.sendSystemMessage(Component.translatable("message.pouchofunknown.none"));
            } else {
                ItemStack pouch = player.getInventory().getItem(i);
                CompoundTag childTag = pouch.getTagElement(String.valueOf(Item.getId(item.getItem())));
                if (childTag != null) {
                    int itemAmount = childTag.getInt("amount");
                    childTag.putInt("amount", itemAmount + item.getCount());
                } else {
                    childTag = new CompoundTag();
                    pouch.addTagElement(String.valueOf(Item.getId(item.getItem())), childTag);
                    CompoundTag tag = pouch.getOrCreateTag();
                    int amount = tag.getInt("amount");
                    tag.putInt("amount", amount + 1);
                    childTag.putInt("amount", item.getCount());
                    int[] items = tag.getIntArray("items");
                    ArrayList<Integer> itemsList = new ArrayList<>(Arrays.stream(items).boxed().toList());
                    itemsList.add(Item.getId(item.getItem()));
                    tag.putIntArray("items", itemsList);
                }
                player.sendSystemMessage(Component.translatable("message.pouchofunknown.pickup", item.getCount(), item.getDisplayName()));
                event.getItem().discard();
            }
        }
    }
}