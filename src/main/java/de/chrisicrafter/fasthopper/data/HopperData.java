package de.chrisicrafter.fasthopper.data;

import de.chrisicrafter.fasthopper.Config;
import de.chrisicrafter.fasthopper.FastHopper;
import de.chrisicrafter.fasthopper.item.ModItems;
import de.chrisicrafter.fasthopper.networking.ModMessages;
import de.chrisicrafter.fasthopper.networking.packet.DebugScreenDataS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HopperData extends SavedData {
    private final ArrayList<BlockPos> fastHopperPositions;

    public static SavedData.Factory<HopperData> factory() {
        return new SavedData.Factory<>(HopperData::new, HopperData::load, DataFixTypes.LEVEL);
    }

    public HopperData() {
        fastHopperPositions = new ArrayList<>();
    }

    public HopperData(ArrayList<BlockPos> fastHopperPositions) {
        this.fastHopperPositions = fastHopperPositions;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag) {
        //save to tag
        int size;
        size = fastHopperPositions.size();
        tag.putInt("size", size);
        for(int i = 0; i < size; i++) {
            tag.put("pos_" + i, NbtUtils.writeBlockPos(fastHopperPositions.get(i)));
        }
        return tag;
    }

    public static HopperData load(CompoundTag tag) {
        //load from tag
        ArrayList<BlockPos> fastHopperPositions = new ArrayList<>();
        int size;
        size = tag.getInt("size");
        for(int i = 0; i < size; i++) {
            fastHopperPositions.add(NbtUtils.readBlockPos(tag.getCompound("pos_" + i)));
        }
        return new HopperData(fastHopperPositions);
    }

    public ItemStack hopperShiftUse(ServerLevel level, ItemStack itemStack, BlockPos blockPos) {
        if(itemStack.is(ModItems.GREASE_BOTTLE.get()) && !fastHopperPositions.contains(blockPos)) {
            level.playSound(null, blockPos, SoundEvents.SLIME_BLOCK_BREAK, SoundSource.BLOCKS);
            fastHopperPositions.add(blockPos);
            ModMessages.sendToPlayer(new DebugScreenDataS2CPacket(FastHopper.getHopperData(level)), level.dimension());
            setDirty();
            if(itemStack.getDamageValue() < itemStack.getMaxDamage() - 1) itemStack.setDamageValue(itemStack.getDamageValue() + 1);
            else return new ItemStack(Items.GLASS_BOTTLE);
        } else if(itemStack.is(Tags.Items.SHEARS) && fastHopperPositions.contains(blockPos)) {
            level.playSound(null, blockPos, SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS);
            fastHopperPositions.remove(blockPos);
            ModMessages.sendToPlayer(new DebugScreenDataS2CPacket(FastHopper.getHopperData(level)), level.dimension());
            setDirty();
            if(itemStack.isDamageableItem()) itemStack.setDamageValue(itemStack.getDamageValue() + 1);
        }
        return itemStack;
    }

    public void update(ServerLevel level) {
        fastHopperPositions.removeIf(pos -> !level.getBlockState(pos).is(Blocks.HOPPER));
        for(BlockPos blockPos : fastHopperPositions) {
            BlockState state = level.getBlockState(blockPos);
            HopperBlockEntity blockEntity = (HopperBlockEntity) level.getBlockEntity(blockPos);
            for(int i = 1; i < Config.fastHopperSpeed; i++) {
                assert blockEntity != null;
                HopperBlockEntity.pushItemsTick(level, blockPos, state, blockEntity);
            }
        }
        ModMessages.sendToPlayer(new DebugScreenDataS2CPacket(FastHopper.getHopperData(level)), level.dimension());
        setDirty();
    }

    public boolean isFast(BlockPos pos) {
        return fastHopperPositions.contains(pos);
    }
}
