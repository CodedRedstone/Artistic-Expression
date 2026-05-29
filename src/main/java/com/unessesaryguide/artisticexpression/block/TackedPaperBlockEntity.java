package com.unessesaryguide.artisticexpression.block;

import com.unessesaryguide.artisticexpression.ArtisticExpression;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;
import java.util.Set;

public class TackedPaperBlockEntity extends BlockEntity {

    private final Set<Direction> occupiedFaces = EnumSet.noneOf(Direction.class);

    public TackedPaperBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TACKED_PAPER_BE.get(), pos, state);
    }

    public Set<Direction> getOccupiedFaces() {
        return occupiedFaces;
    }

    public boolean hasFace(Direction face) {
        return occupiedFaces.contains(face);
    }

    public void addFace(Direction face) {
        occupiedFaces.add(face);
        setChanged();
    }

    public void removeFace(Direction face) {
        occupiedFaces.remove(face);
        setChanged();
    }

    public boolean isEmpty() {
        return occupiedFaces.isEmpty();
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public net.minecraft.network.protocol.Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getUpdatePacket() {
        return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ListTag faceList = new ListTag();
        for (Direction dir : occupiedFaces) {
            faceList.add(StringTag.valueOf(dir.getName()));
        }
        tag.put("faces", faceList);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        occupiedFaces.clear();
        ListTag faceList = tag.getList("faces", Tag.TAG_STRING);
        for (int i = 0; i < faceList.size(); i++) {
            Direction dir = Direction.byName(faceList.getString(i));
            if (dir != null) occupiedFaces.add(dir);
        }
    }
}
