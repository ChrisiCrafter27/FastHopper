package de.chrisicrafter.fasthopper.item;

import de.chrisicrafter.fasthopper.FastHopper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FastHopper.MOD_ID);

    public static final RegistryObject<Item> GREASE_BOTTLE = ITEMS.register("grease_bottle", () -> new Item(new Item.Properties()
            .craftRemainder(Items.GLASS_BOTTLE)
            .durability(16)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
