package de.chrisicrafter.fasthopper;

import com.mojang.logging.LogUtils;
import de.chrisicrafter.fasthopper.data.HopperData;
import de.chrisicrafter.fasthopper.item.ModItems;
import de.chrisicrafter.fasthopper.networking.ModMessages;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.HashMap;

@Mod(FastHopper.MOD_ID)
public class FastHopper {
    public static final String MOD_ID = "fasthopper";
    private static final Logger LOGGER = LogUtils.getLogger();
    private static HashMap<ResourceKey<Level>, HopperData> HOPPER_DATA = new HashMap<>();

    public FastHopper() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("COMMON SETUP");
        event.enqueueWork(() -> {
            ModMessages.register();
            BrewingRecipeRegistry.addRecipe(new BrewingRecipe(Ingredient.of(Items.HONEY_BOTTLE), Ingredient.of(Items.SLIME_BALL), new ItemStack(ModItems.GREASE_BOTTLE.get())));
        });
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) event.accept(ModItems.GREASE_BOTTLE);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        HOPPER_DATA = new HashMap<>();
    }

    public static HopperData getHopperData(ServerLevel level) {
        if(!HOPPER_DATA.containsKey(level.dimension())) HOPPER_DATA.put(level.dimension(), level.getDataStorage().computeIfAbsent(HopperData.factory(), "hopper_data"));
        return HOPPER_DATA.get(level.dimension());
    }
}
