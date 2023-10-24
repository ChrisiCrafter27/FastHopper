package de.chrisicrafter.fasthopper.event;

import de.chrisicrafter.fasthopper.FastHopper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FastHopper.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onHopperRightClick(PlayerInteractEvent.RightClickBlock event) {
        if(event.getLevel() instanceof ServerLevel world && world.getBlockState(event.getPos()).getBlock() == Blocks.HOPPER && event.getHand() == InteractionHand.MAIN_HAND && !event.getLevel().isClientSide()) {
            event.getEntity().setItemInHand(InteractionHand.MAIN_HAND, FastHopper.getHopperData(world).hopperShiftUse(world, event.getItemStack(), event.getPos()));
        }
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if(!event.level.isClientSide() && event.level instanceof ServerLevel world) {
            FastHopper.getHopperData(world).update(world);
        }
    }
}
