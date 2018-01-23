package com.blademc.core.AI;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.data.EntityData;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.utils.TextFormat;

import java.util.UUID;

public class Cop extends EntityHuman {

    private Player evilPerson = null;

    public Cop(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.getInventory().setItemInHand(Item.get(Item.DIAMOND_AXE));
    }

    @Override
    public String getName(){
      return TextFormat.RED + TextFormat.RED.toString() + "COP" + TextFormat.RESET  + TextFormat.YELLOW + " Nora Grey";
    }

    @Override
    public boolean onUpdate(int something){
        if(this.closed)
            return false;
        Player[] players = evilPerson != null ? new Player[]{this.evilPerson} : this.getViewers().values().toArray(new Player[this.getViewers().values().size()]);
        for(Player player : players){
            if(!(player instanceof Player && player.isAlive()))
                return false;
           // if(player == evilPerson){
                for(Block block : this.getBlocksAround()){
                    // gonna do this later
                }
                if(player == evilPerson) {
                    double diff = Math.abs(x) + Math.abs(z);
                    double x = player.x - this.x;
                    double y = player.y - this.y;
                    double z = player.z - this.z;
                    this.move(x / 10, y / 10, z / 10);
                    this.yaw = Math.toDegrees(-Math.atan2(x / diff, z / diff));
                    this.pitch = Math.toDegrees(-Math.atan2(y, Math.sqrt(x * x + z * z)));
                    this.setSprinting();
                    this.updateMovement();
                }
                if(player == evilPerson) {
                    if (this.distanceSquared(player) >= 15) {
                        double f = 10.0;
                        this.setMotion(this.getMotion().multiply(f));
                    }
                    if (this.distanceSquared(player) <= 10) {
                        player.sendMessage(this.getName() + TextFormat.GRAY + " : " + TextFormat.ITALIC + TextFormat.RED.toString() + "STOP IN THE NAME OF HOLY JESUS");
                    }
                    if (this.distanceSquared(player) <= 2) {
                        // something relavent towards kicking
                        player.sendMessage(TextFormat.YELLOW + "You have been damaged");
                        this.level.removeEntity(this);
                    }
                }
                if(player.isOp())
                    return false;
                double distance = this.distanceSquared(player);
                double airTicks = player.getInAirTicks();
                if(distance >= 3) {
                    if (airTicks == 50) {
                        evilPerson = null;
                        player.sendMessage(this.getName() + TextFormat.GRAY + " : " + TextFormat.ITALIC + TextFormat.RED.toString() + "Are you fly hacking?");
                    }
                    else if(airTicks == 100){
                        player.sendMessage(this.getName() + TextFormat.GRAY + " : " + TextFormat.ITALIC + TextFormat.RED.toString() + "Stop fly hacking or I will find and ban you!");
                    }
                    else if(airTicks == 300){
                        evilPerson = player;
                    }
                }

            }

        //}
        return false;
    }

    @Override
    protected void initEntity() {
        super.initEntity();
    }

    @Override
    public void spawnTo(Player player){
        AddPlayerPacket pk = new AddPlayerPacket();
        pk.entityRuntimeId = this.getId();
        pk.entityUniqueId = this.getId();
        pk.uuid = UUID.randomUUID();
        pk.username = this.getName();
        pk.x = (int) this.x;
        pk.y = (int) this.y;
        pk.z = (int) this.z;
        pk.speedX = 0;
        pk.speedY = 0;
        pk.speedZ = 0;
        pk.yaw = 0;
        pk.pitch = 0;
        pk.item = this.getInventory().getItemInHand();
        pk.metadata = new EntityMetadata(){
            {
                this.putLong(Entity.DATA_FLAGS,
                        1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG
                                ^ 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG
                );
                this.putBoolean(Entity.DATA_FLAG_NO_AI, true);
                this.putLong(Entity.DATA_LEAD_HOLDER_EID, -1);
            }
        };
        player.dataPacket(pk);
        this.saveNBT();
     super.spawnTo(player);

    }
}
