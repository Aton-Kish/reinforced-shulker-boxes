package atonkish.reinfshulker.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.Vec3;

import atonkish.reinfcore.gametest.TestFunction;
import atonkish.reinfcore.util.ReinforcingMaterials;
import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;
import atonkish.reinfshulker.block.ModBlocks;
import atonkish.reinfshulker.gametest.util.MockServerPlayerHelper;
import atonkish.reinfshulker.gametest.util.TestIdentifier;

public class PiglinTests {
  private static final String TEST_ENVIRONMENT_DEFAULT =
      String.format("%s:piglin/default", ReinforcedShulkerBoxesMod.MOD_ID);
  private static final String TEST_STRUCTURE_EMPTY = "fabric-gametest-api-v1:empty";

  public static final Collection<TestFunction> TEST_FUNCTIONS =
      new ArrayList<>() {
        {
          // Copper Shulker Box
          for (Block block :
              ModBlocks.REINFORCED_SHULKER_BOX_MAP
                  .get(ReinforcingMaterials.MAP.get("copper"))
                  .values()) {
            add(
                PiglinTests.createTest(
                    String.format("Piglin get angry after opening %s", block.getName().getString()),
                    block));
          }

          // Iron Shulker Box
          for (Block block :
              ModBlocks.REINFORCED_SHULKER_BOX_MAP
                  .get(ReinforcingMaterials.MAP.get("iron"))
                  .values()) {
            add(
                PiglinTests.createTest(
                    String.format("Piglin get angry after opening %s", block.getName().getString()),
                    block));
          }

          // Gold Shulker Box
          for (Block block :
              ModBlocks.REINFORCED_SHULKER_BOX_MAP
                  .get(ReinforcingMaterials.MAP.get("gold"))
                  .values()) {
            add(
                PiglinTests.createTest(
                    String.format("Piglin get angry after opening %s", block.getName().getString()),
                    block));
          }

          // Diamond Shulker Box
          for (Block block :
              ModBlocks.REINFORCED_SHULKER_BOX_MAP
                  .get(ReinforcingMaterials.MAP.get("diamond"))
                  .values()) {
            add(
                PiglinTests.createTest(
                    String.format("Piglin get angry after opening %s", block.getName().getString()),
                    block));
          }

          // Netherite Shulker Box
          for (Block block :
              ModBlocks.REINFORCED_SHULKER_BOX_MAP
                  .get(ReinforcingMaterials.MAP.get("netherite"))
                  .values()) {
            add(
                PiglinTests.createTest(
                    String.format("Piglin get angry after opening %s", block.getName().getString()),
                    block));
          }
        }
      };

  private static TestFunction createTest(String name, Block shulkerBoxBlock) {
    Identifier testIdentifier =
        TestIdentifier.of(ReinforcedShulkerBoxesMod.MOD_ID, PiglinTests.class, name);

    return new TestFunction(
        testIdentifier,
        PiglinTests.TEST_ENVIRONMENT_DEFAULT,
        PiglinTests.TEST_STRUCTURE_EMPTY,
        100,
        0,
        true,
        Rotation.NONE,
        false,
        1,
        1,
        false,
        (context) -> {
          // Arrange
          BlockPos blockPos = BlockPos.ZERO;
          context.setBlock(blockPos, shulkerBoxBlock);

          ServerPlayer player =
              MockServerPlayerHelper.spawn(
                  context, GameType.SURVIVAL, Vec3.atLowerCornerOf(blockPos.south(4)));
          player.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));

          Piglin piglin = context.spawnWithNoFreeWill(EntityType.PIGLIN, blockPos.east(1));

          // Act
          CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
          CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

          Map<String, Boolean> angryAtMap = new HashMap<String, Boolean>();
          String angryAtMapKeyBeforeAngryAtPlayer = "beforeAngryAtPlayer";
          String angryAtMapKeyAfterAngryAtPlayer = "afterAngryAtPlayer";

          long tickChestOpen = 20;
          context.runAtTickTime(
              tickChestOpen,
              () -> {
                angryAtMap.put(
                    angryAtMapKeyBeforeAngryAtPlayer,
                    piglin.getBrain().isMemoryValue(MemoryModuleType.ANGRY_AT, player.getUUID()));

                context.useBlock(blockPos, player);

                futurePartialAct1.complete(null);
              });

          long tickAngryAtPlayer = 21;
          context.runAtTickTime(
              tickAngryAtPlayer,
              () -> {
                angryAtMap.put(
                    angryAtMapKeyAfterAngryAtPlayer,
                    piglin.getBrain().isMemoryValue(MemoryModuleType.ANGRY_AT, player.getUUID()));

                futurePartialAct2.complete(null);
              });

          // Assert
          CompletableFuture.allOf(futurePartialAct1, futurePartialAct2)
              .thenRun(
                  () -> {
                    try {
                      context.assertFalse(
                          angryAtMap.get(angryAtMapKeyBeforeAngryAtPlayer),
                          Component.nullToEmpty(
                              "Expected that the piglin is not angry at player, but it has been already angry."));
                      context.assertTrue(
                          angryAtMap.get(angryAtMapKeyAfterAngryAtPlayer),
                          Component.nullToEmpty(
                              "Expected that the piglin is angry at player, but it has not been angry yet."));
                    } catch (Exception e) {
                      ReinforcedShulkerBoxesMod.LOGGER.error(
                          "[{}] {}", testIdentifier, e.getMessage());
                      throw e;
                    } finally {
                      MockServerPlayerHelper.destroy(context, player);
                    }

                    context.succeed();
                  });
        });
  }
}
