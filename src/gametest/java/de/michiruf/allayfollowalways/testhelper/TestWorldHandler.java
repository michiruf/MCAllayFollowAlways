package de.michiruf.allayfollowalways.testhelper;

//? if >= 1.19.4 {
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
//? }

public class TestWorldHandler {

    //? if >= 1.19.4 {

    private final ServerWorld world;
    private final List<ChunkPos> forcedChunks = new ArrayList<>();

    /**
     * @see World#OVERWORLD
     * @see World#NETHER
     * @see World#END
     */
    public TestWorldHandler(TestContext context, RegistryKey<World> worldKey) {
        var server = context.getWorld().getServer();
        assert server != null;
        world = server.getWorld(worldKey);
        new Assert(context).assertTrue(world != null, "World " + worldKey + " not found");
    }

    public ServerWorld getWorld() {
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

    public void cleanup() {
        for (var pos : forcedChunks) {
            world.setChunkForced(pos.x, pos.z, false);
        }
        forcedChunks.clear();
    }

    //? }
}
