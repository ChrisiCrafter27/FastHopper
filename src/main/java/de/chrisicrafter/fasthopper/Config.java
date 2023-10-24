package de.chrisicrafter.fasthopper;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = FastHopper.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue FAST_HOPPER_SPEED = BUILDER
            .comment("Modified hopper speed in comparison to normal hopper speed")
            .defineInRange("fastHopperSpeed", 2, 2, 8);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int fastHopperSpeed;

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        fastHopperSpeed = FAST_HOPPER_SPEED.get();
    }
}
