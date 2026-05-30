package com.unessesaryguide.artisticexpression.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class TackedPaperEntity extends HangingEntity {

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
    }

    public TackedPaperEntity(EntityType<? extends TackedPaperEntity> type, Level level) {
        super(type, level);
    }

    public TackedPaperEntity(Level level, BlockPos pos, Direction direction) {
        super(ModEntities.TACKED_PAPER.get(), level, pos);
        this.setDirection(direction.getAxis().isHorizontal() ? direction : Direction.NORTH);
    }

    // -------------------------------------------------------------------------
    // Required abstract methods
    // -------------------------------------------------------------------------

    @Override
    protected AABB calculateBoundingBox(BlockPos pos, Direction direction) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        double thickness = 0.0625;
        double half = 0.375;

        double ax = Math.abs(direction.getStepX());
        double az = Math.abs(direction.getStepZ());

        return new AABB(
            x - half * az - thickness * Math.max(0, direction.getStepX()),
            y - half,
            z - half * ax - thickness * Math.max(0, direction.getStepZ()),
            x + half * az + thickness * Math.max(0, -direction.getStepX()),
            y + half,
            z + half * ax + thickness * Math.max(0, -direction.getStepZ())
        );
    }

    @Override
    public void playPlacementSound() {
        this.playSound(SoundEvents.ITEM_FRAME_PLACE, 1.0f, 1.0f);
    }

    @Override
    public void dropItem(@Nullable Entity entity) {
        this.playSound(SoundEvents.ITEM_FRAME_BREAK, 1.0f, 1.0f);
        this.spawnAtLocation(new ItemStack(Items.PAPER));
    }

    // -------------------------------------------------------------------------
    // NBT & networking
    // -------------------------------------------------------------------------

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putByte("Facing", (byte) this.getDirection().get3DDataValue());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        Direction dir = Direction.from3DDataValue(tag.getByte("Facing"));
        this.setDirection(dir.getAxis().isHorizontal() ? dir : Direction.NORTH);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket(this, serverEntity,
            this.getDirection().get3DDataValue());
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        Direction dir = Direction.from3DDataValue(packet.getData());
        this.setDirection(dir.getAxis().isHorizontal() ? dir : Direction.NORTH);
    }
}
