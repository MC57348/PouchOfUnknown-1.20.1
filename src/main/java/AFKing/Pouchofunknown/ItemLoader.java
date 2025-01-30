package AFKing.Pouchofunknown;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ItemLoader {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "pouchofunknown");

    public static final RegistryObject<Item> POUCH = ITEMS.register("pouch", () ->
        new Item(new Item.Properties().stacksTo(1))
    );
}
