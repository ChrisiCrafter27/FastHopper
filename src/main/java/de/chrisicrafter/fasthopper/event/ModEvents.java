package de.chrisicrafter.fasthopper.event;

import de.chrisicrafter.fasthopper.FastHopper;
import de.chrisicrafter.fasthopper.networking.ModMessages;
import de.chrisicrafter.fasthopper.networking.packet.DebugScreenDataS2CPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FastHopper.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onPlayerJoinedLevel(EntityJoinLevelEvent event) {
        if(!event.getLevel().isClientSide() && event.getLevel() instanceof ServerLevel level && event.getEntity() instanceof ServerPlayer serverPlayer) {
            ModMessages.sendToPlayer(new DebugScreenDataS2CPacket(FastHopper.getHopperData(level)), serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onHopperRightClick(PlayerInteractEvent.RightClickBlock event) {
        if(event.getLevel() instanceof ServerLevel level && event.getEntity().isCrouching() && level.getBlockState(event.getPos()).getBlock() == Blocks.HOPPER && event.getHand() == InteractionHand.MAIN_HAND && !event.getLevel().isClientSide()) {
            event.getEntity().setItemInHand(InteractionHand.MAIN_HAND, FastHopper.getHopperData(level).hopperShiftUse(level, event.getItemStack(), event.getPos()));
        }
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if(!event.level.isClientSide() && event.level instanceof ServerLevel level) {
            FastHopper.getHopperData(level).update(level);
        }
    }
}
