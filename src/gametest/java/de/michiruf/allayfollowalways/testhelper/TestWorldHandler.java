package de.michiruf.allayfollowalways.testhelper;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public class TestWorldHandler {

    private final ServerLevel world;
    private final List<ChunkPos> forcedChunks = new ArrayList<>();

    /**
     * @see Level#OVERWORLD
     * @see Level#NETHER
     * @see Level#END
     */
    public TestWorldHandler(GameTestHelper context, ResourceKey<Level> worldKey) {
        var server = context.getLevel().getServer();

        if (server == null)
            throw new RuntimeException("Server is null");

        world = server.getLevel(worldKey);

        new Assert(context).assertTrue(world != null, "World " + worldKey + " not found");
    }

    public ServerLevel getWorld() {
        // This cannot happen, but helps the IDE
        if(world == null)
            throw new RuntimeException("World is null");

        return world;
    }

    /**
     * Force-loads a chunk so that non-player entities teleported into it
     * get tracked in the EntityIndex and become visible to lookups.
     */
    public void forceLoadChunk(int chunkX, int chunkZ) {
        world.getChunk(chunkX, chunkZ);
        world.setChunkForced(chunkX, chunkZ, true);
        forcedChunks.add(new ChunkPos(chunkX, chunkZ));
    }

    /**
     * Checks that all forced chunks have reached entity-ticking level,
     * meaning non-player entities in them will be tracked in the EntityIndex.
     */
    public boolean areAllForcedChunksLoaded() {
        for (var pos : forcedChunks) {
            if (!world.getChunkSource().hasChunk(pos.x, pos.z)) {
                return false;
            }

            //? if <1.21.5 {
            /*if (!world.isNaturalSpawningAllowed(new BlockPos(pos.getMinBlockX(), 0, pos.getMinBlockZ()))) {
            *///? } else {
            if (!world.canSpawnEntitiesInChunk(pos)) {
            //? }
                return false;
            }

            // Additionally checks simulation distance tracking. While the above check should be
            // sufficient in theory, without this second check entity lookups rarely fail on CI due
            // to a timing gap between the chunk becoming ticking and entity registration completing.
            //? if <1.21.5 {
            /*if (!world.isPositionEntityTicking(new BlockPos(pos.getMinBlockX(), 0, pos.getMinBlockZ()))) {
            *///? } else {
            if (!world.isPositionEntityTicking(new BlockPos(pos.getMinBlockX(), 0, pos.getMinBlockZ()))) {
            //? }
                return false;
            }
        }

        return true;
    }

    public void cleanup() {
        for (var pos : forcedChunks) {
            world.setChunkForced(pos.x, pos.z, false);
        }
        forcedChunks.clear();
    }
}
