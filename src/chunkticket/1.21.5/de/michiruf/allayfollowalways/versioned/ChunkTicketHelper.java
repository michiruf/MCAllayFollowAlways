package de.michiruf.allayfollowalways.versioned;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.util.math.ChunkPos;

import java.util.Comparator;

public class ChunkTicketHelper {

    public static void keepChunksLoaded(Entity entity) {
        keepChunksLoaded((ServerChunkManager) EntityHelper.getWorld(entity).getChunkManager(), entity.getChunkPos());
    }

    public static void keepChunksLoaded(ServerChunkManager cm, ChunkPos pos) {
        cm.addTicket(ticket(), pos, 2);

        // NOTE This variant here might not be the most performant thing ever, since we queue another chunk ticket
        //      on every tick, but for now, this should at least work pretty well
    }

    public static ChunkTicketType ticket() {
        return ChunkTicketType.UNKNOWN;
    }
}
