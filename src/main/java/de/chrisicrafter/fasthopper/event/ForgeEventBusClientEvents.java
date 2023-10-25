package de.chrisicrafter.fasthopper.event;

import de.chrisicrafter.fasthopper.FastHopper;
import de.chrisicrafter.fasthopper.client.ClientDebugScreenData;
import de.chrisicrafter.fasthopper.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Map;

@Mod.EventBusSubscriber(modid = FastHopper.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeEventBusClientEvents {

    @SubscribeEvent
    public static void onRenderDebugScreen(CustomizeGuiOverlayEvent.DebugText event) {
        if(event.getRight().isEmpty()) return;
        ArrayList<String> rightTexts = event.getRight();
        String insertAfter = null;
        for (String string : rightTexts) {
            if(string.contains("minecraft:hopper")) {
                insertAfter = string;
                break;
            }
        }
        assert Minecraft.getInstance().player != null;
        String debugString = getDebugString();
        if(!debugString.isEmpty() && insertAfter != null) {
            rightTexts.add(rightTexts.indexOf(insertAfter) + 2, debugString);
        }
    }

    public static String getDebugString() {
        Level level = Minecraft.getInstance().level;

        if(level == null) return "";

        BlockPos pos = lookingAt();

        if(pos == null) return "";

        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if(state.is(Blocks.HOPPER)) {
            return ClientDebugScreenData.getHopperData().isFast(pos) ? "fast: " + ChatFormatting.GREEN + "true" : "fast: " + ChatFormatting.RED + "false";
        } else return "";
    }

    public static BlockPos lookingAt(){
        //HitResult hr = Minecraft.getInstance().hitResult;
        //Player player = Minecraft.getInstance().player;
        //Level level = Minecraft.getInstance().level;

        Entity entity = Minecraft.getInstance().getCameraEntity();
        HitResult hr = entity.pick(20.0D, 0.0F, false);

        if (hr.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockHitResult) hr).getBlockPos();
            return blockpos;
        }

        return null;
    }

    @SubscribeEvent
    public static void onBeaconTooltip(ItemTooltipEvent event) {
        if(event.getItemStack().is(ModItems.GREASE_BOTTLE.get())) {
            event.getToolTip().add(Component.literal(ChatFormatting.GRAY + "Sneak-use on hopper to make it faster."));
        }
    }
}
