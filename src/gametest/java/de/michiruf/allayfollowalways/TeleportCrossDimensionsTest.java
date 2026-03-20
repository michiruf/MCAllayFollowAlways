package de.michiruf.allayfollowalways;

//? if >= 1.19.4 {
import de.michiruf.allayfollowalways.testhelper.Assert;
import de.michiruf.allayfollowalways.testhelper.TestExecutor;
import de.michiruf.allayfollowalways.testhelper.TestObjectHolder;
import de.michiruf.allayfollowalways.testhelper.VersionedPlayerTeleport;
import de.michiruf.allayfollowalways.versioned.VersionedFabricTeleport;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AllayEntity;
import de.michiruf.allayfollowalways.versioned.EntityHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
//? if <=1.21.4 {
/*import net.minecraft.test.GameTest;
 *///? } else {
import net.fabricmc.fabric.api.gametest.v1.GameTest;
//? }
import net.minecraft.test.TestContext;
import net.minecraft.util.math.Vec3d;
//? }

@SuppressWarnings("unused")
public class TeleportCrossDimensionsTest {

    //? if >= 1.19.4 {
    /*? if <1.20.5 { */
    /*@GameTest(templateName = "fabric-gametest-api-v1:empty")
     *//*? } elif <1.21.5 { */
    /*@GameTest(templateName = "fabric-gametest-api-v1:empty", skyAccess = true)
     *//*?} else { */
    @GameTest(skyAccess = true)
     /*?} */
    public void manualTeleportCrossDimension(TestContext context) {
        var check = new Assert(context);
        var holder = new TestObjectHolder(context);
        var netherWorld = context.getWorld().getServer().getWorld(World.NETHER);

        new TestExecutor(context)
                .immediate(() -> {
                    AllayFollowAlwaysMod.CONFIG.teleportEnabled(false);
                    holder.createUniquePlayer();
                    holder.createAllay(); // not linked in this test
                })
                // Player
                .then(() -> VersionedPlayerTeleport.teleport(holder.player, new Vec3d(0, 0, 0), netherWorld))
                .then(() -> check.assertTrue(
                        EntityHelper.getWorld(holder.player).getRegistryKey() == World.NETHER,
                        "Player is not in the Nether. Player world: " + EntityHelper.getWorld(holder.player).getRegistryKey()))
                // Allay
                .then(() -> {
                    VersionedFabricTeleport.teleport(holder.allay, new Vec3d(0, 0, 0), netherWorld);
                    holder.relinkAllayForWorld(netherWorld);
                })
                .then(() -> check.assertTrue(
                        EntityHelper.getWorld(holder.allay).getRegistryKey() == World.NETHER,
                        "Allay is not in the Nether. Allay world: " + EntityHelper.getWorld(holder.allay).getRegistryKey()))
                .immediate(check::complete)
                .run();
    }

    //? }
}
