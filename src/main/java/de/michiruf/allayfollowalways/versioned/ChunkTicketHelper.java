package de.michiruf.allayfollowalways.versioned;

//? if <1.21.5 {
/*import java.util.Comparator;
*///? }
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;

public class ChunkTicketHelper {

    public static void keepChunksLoaded(Entity entity) {
        keepChunksLoaded((ServerChunkCache) EntityHelper.getWorld(entity).getChunkSource(), entity.chunkPosition());
    }

    public static void keepChunksLoaded(ServerChunkCache cm, ChunkPos pos) {
        // Keep the chunk loaded for 2 ticks
        // NOTE This variant here might not be the most performant thing ever, since we queue another chunk ticket
        //      on every tick, but for now, this should at least work pretty well

        //? if <1.21.5 {
        /*cm.addRegionTicket(
                TicketType.create("allayfollowalways", Comparator.comparingLong(ChunkPos::toLong), 1),
                pos,
                2,
                pos);
        *///? } elif <1.21.9 {
        /*cm.addTicketWithRadius(new TicketType(2L, false, TicketType.TicketUse.LOADING_AND_SIMULATION), pos, 2);
        *///? } else {
        var flags = TicketType.FLAG_LOADING
                | TicketType.FLAG_SIMULATION
                | TicketType.FLAG_KEEP_DIMENSION_ACTIVE;
        cm.addTicketWithRadius(new TicketType(2L, flags), pos, 2);
        //? }
    }
}
