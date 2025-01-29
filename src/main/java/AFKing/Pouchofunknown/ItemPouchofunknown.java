package AFKing.Pouchofunknown;

import AFKing.Pouchofunknown.Utils.ItemUtils;
import net.darkhax.itemstages.Restriction;
import net.darkhax.itemstages.RestrictionManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemPouchofunknown extends Item {
    public ItemPouchofunknown() {
        super(new Item.Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getTag() == null) {
            player.sendSystemMessage(Component.translatable("message.pouchofunknown.nocanget"));
        } else {
            int[] ids = stack.getTag().getIntArray("items");
            boolean full = false;
            int num = 0;
            for (int id : ids) {
                CompoundTag childTag = stack.getTagElement(String.valueOf(id));
                if (childTag != null) {
                    int amount = childTag.getInt("amount");
                    Item item = Item.byId(id);
                    ItemStack stack1 = new ItemStack(item, amount);
                    Restriction restriction = RestrictionManager.INSTANCE.getRestriction(player, stack1);
                    if (!(restriction != null && restriction.shouldPreventPickup())) {
                        if (ItemUtils.hasEmptySlot(player.getInventory())) {
                            player.getInventory().add(stack1);
                        } else {
                            level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY(), player.getZ(), stack1));
                            full = true;
                        }
                        stack.removeTagKey(String.valueOf(id));
                        stack.getTag().remove(String.valueOf(id));
                        int amount1 = stack.getTag().getInt("amount");
                        stack.getTag().putInt("amount", amount1 - 1);
                        int[] items = stack.getTag().getIntArray("items");
                        ArrayList<Integer> ints = new ArrayList<>();
                        for (int i : items) {
                            ints.add(i);
                        }
                        ints.remove(Integer.valueOf(id));
                        stack.getTag().putIntArray("items", ints);
                        num += 1;
                    }
                }
            }
            if (num != 0) {
                player.sendSystemMessage(Component.translatable("message.pouchofunknown.get", num));
            } else {
                player.sendSystemMessage(Component.translatable("message.pouchofunknown.nocanget"));
            }
            if (full) {
                player.sendSystemMessage(Component.translatable("message.pouchofunknown.full"));
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        CompoundTag tag = stack.getTag();
        if (tag == null) {
            tooltip.add(Component.translatable("tooltip.pouchofunknown.pouch", 0));
        } else {
            tooltip.add(Component.translatable("tooltip.pouchofunknown.pouch", tag.getInt("amount")));
        }
    }
}