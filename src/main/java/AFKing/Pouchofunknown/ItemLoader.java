package AFKing.Pouchofunknown;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ItemLoader {
    public static final String MODID = "pouchofunknown";
    
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    
    public static final RegistryObject<Item> PouchOfUnknown = ITEMS.register("pouch", ItemPouchofunknown::new);
}

