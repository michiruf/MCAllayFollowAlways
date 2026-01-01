package de.michiruf.allayfollowalways.versioned;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.util.math.ChunkPos;

//? if <1.21.5 {
/*import java.util.Comparator;
*///?}

public class ChunkTicketHelper {

    public static void keepChunksLoaded(Entity entity) {
        keepChunksLoaded((ServerChunkManager) EntityHelper.getWorld(entity).getChunkManager(), entity.getChunkPos());
    }

    public static void keepChunksLoaded(ServerChunkManager cm, ChunkPos pos) {
        // Keep the chunk loaded for 2 ticks
        // NOTE This variant here might not be the most performant thing ever, since we queue another chunk ticket
        //      on every tick, but for now, this should at least work pretty well

        //? if <1.21.5 {
        /*cm.addTicket(ticket(), pos, 2, pos);
        *///?} else {
        cm.addTicket(ticket(), pos, 2);
        //?}
    }

    //? if <1.21.5 {
    /*public static ChunkTicketType ticket() {
        return ChunkTicketType.create("allayfollowalways", Comparator.comparingLong(ChunkPos::toLong), 1);
    }
    *///?} else {
    public static ChunkTicketType<ChunkPos> ticket() {
        return ChunkTicketType.UNKNOWN;
    }
    //?}
}
