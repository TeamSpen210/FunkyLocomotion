package com.rwtema.frames.blocks;

import com.rwtema.frames.groups.MoveManager;
import com.rwtema.frames.groups.MovingTileRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

import java.lang.ref.WeakReference;

public class TileMoving extends TileMovingBase {

    @Override
    public Packet getDescriptionPacket() {
        if (desc == null)
            return null;

        desc.setInteger("Time", time);
        desc.setInteger("MaxTime", maxTime);
        desc.setByte("Dir", (byte) dir.ordinal());
        desc.setByte("Light", (byte) lightLevel);
        desc.setShort("Opacity", (short) lightOpacity);

        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, desc);
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        MovingTileRegistry.deregister(this);
    }


    @Override
    public void invalidate() {
        super.invalidate();
        MovingTileRegistry.deregister(this);
    }

    @Override
    public void validate() {
        super.validate();
        MovingTileRegistry.register(this);
    }

    @Override
    public void updateEntity() {
        if (time < maxTime)
            time++;
        else
            MoveManager.finishMoving();
    }

    public WeakReference<EntityPlayer> activatingPlayer = null;
    public int activatingSide = -1;
    public float activatingHitX, activatingHitY, activatingHitZ;

    public void cacheActivate(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (this.activatingPlayer == null || this.activatingPlayer.get() == null) {
            activatingPlayer = new WeakReference<EntityPlayer>(player);
            activatingSide = side;
            activatingHitX = hitX;
            activatingHitY = hitY;
            activatingHitZ = hitZ;
        }
    }
}
