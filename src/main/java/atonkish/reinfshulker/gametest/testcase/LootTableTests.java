package atonkish.reinfshulker.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.Vec3;

import atonkish.reinfcore.gametest.TestFunction;
import atonkish.reinfcore.util.ReinforcingMaterials;
import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;
import atonkish.reinfshulker.block.ModBlocks;
import atonkish.reinfshulker.gametest.util.MockServerPlayerHelper;
import atonkish.reinfshulker.gametest.util.TestIdentifier;

public class LootTableTests {
  private static final String TEST_ENVIRONMENT_DEFAULT =
      String.format("%s:loot_table/default", ReinforcedShulkerBoxesMod.MOD_ID);
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
                LootTableTests.createTest(
                    String.format("Break %s with Netherite Pickaxe", block.getName().getString()),
                    block,
                    Items.NETHERITE_PICKAXE,
                    true));
            add(
                LootTableTests.createTest(
                    String.format("Break %s with Netherite Axe", block.getName().getString()),
                    block,
                    Items.NETHERITE_AXE,
                    true));
            add(
                LootTableTests.createTest(
                    String.format("Break %s without tools", block.getName().getString()),
                    block,
                    Items.AIR,
                    true));
          }

          // Iron Shulker Box
          for (Block block :
              ModBlocks.REINFORCED_SHULKER_BOX_MAP
                  .get(ReinforcingMaterials.MAP.get("iron"))
                  .values()) {
            add(
                LootTableTests.createTest(
                    String.format("Break %s with Netherite Pickaxe", block.getName().getString()),
                    block,
                    Items.NETHERITE_PICKAXE,
                    true));
            add(
                LootTableTests.createTest(
                    String.format("Break %s with Netherite Axe", block.getName().getString()),
                    block,
                    Items.NETHERITE_AXE,
                    true));
            add(
                LootTableTests.createTest(
                    String.format("Break %s without tools", block.getName().getString()),
                    block,
                    Items.AIR,
                    true));
          }

          // Gold Shulker Box
          for (Block block :
              ModBlocks.REINFORCED_SHULKER_BOX_MAP
                  .get(ReinforcingMaterials.MAP.get("gold"))
                  .values()) {
            add(
                LootTableTests.createTest(
                    String.format("Break %s with Netherite Pickaxe", block.getName().getString()),
                    block,
                    Items.NETHERITE_PICKAXE,
                    true));
            add(
                LootTableTests.createTest(
                    String.format("Break %s with Netherite Axe", block.getName().getString()),
                    block,
                    Items.NETHERITE_AXE,
                    true));
            add(
                LootTableTests.createTest(
                    String.format("Break %s without tools", block.getName().getString()),
                    block,
                    Items.AIR,
                    true));
          }

          // Diamond Shulker Box
          for (Block block :
              ModBlocks.REINFORCED_SHULKER_BOX_MAP
                  .get(ReinforcingMaterials.MAP.get("diamond"))
                  .values()) {
            add(
                LootTableTests.createTest(
                    String.format("Break %s with Netherite Pickaxe", block.getName().getString()),
                    block,
                    Items.NETHERITE_PICKAXE,
                    true));
            add(
                LootTableTests.createTest(
                    String.format("Break %s with Netherite Axe", block.getName().getString()),
                    block,
                    Items.NETHERITE_AXE,
                    true));
            add(
                LootTableTests.createTest(
                    String.format("Break %s without tools", block.getName().getString()),
                    block,
                    Items.AIR,
                    true));
          }

          // Netherite Shulker Box
          for (Block block :
              ModBlocks.REINFORCED_SHULKER_BOX_MAP
                  .get(ReinforcingMaterials.MAP.get("netherite"))
                  .values()) {
            add(
                LootTableTests.createTest(
                    String.format("Break %s with Netherite Pickaxe", block.getName().getString()),
                    block,
                    Items.NETHERITE_PICKAXE,
                    true));
            add(
                LootTableTests.createTest(
                    String.format("Break %s with Netherite Axe", block.getName().getString()),
                    block,
                    Items.NETHERITE_AXE,
                    true));
            add(
                LootTableTests.createTest(
                    String.format("Break %s without tools", block.getName().getString()),
                    block,
                    Items.AIR,
                    true));
          }
        }
      };

  private static TestFunction createTest(
      String name, Block shulkerBoxBlock, Item tool, boolean shouldDrop) {
    Identifier testIdentifier =
        TestIdentifier.of(ReinforcedShulkerBoxesMod.MOD_ID, LootTableTests.class, name);

    return new TestFunction(
        testIdentifier,
        LootTableTests.TEST_ENVIRONMENT_DEFAULT,
        LootTableTests.TEST_STRUCTURE_EMPTY,
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
          player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(tool));

          // Act
          CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
          CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

          long tickOrigin = 0;
          context.runAtTickTime(
              tickOrigin,
              () -> {
                player.gameMode.handleBlockBreakAction(
                    context.absolutePos(blockPos),
                    ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK,
                    Direction.NORTH,
                    context.getLevel().getHeight(),
                    0);

                futurePartialAct1.complete(null);
              });

          long tickBlockBreaking =
              (long)
                  Math.ceil(
                      1.0D
                          / context
                              .getBlockState(blockPos)
                              .getDestroyProgress(player, context.getLevel(), blockPos));
          context.runAtTickTime(
              tickBlockBreaking,
              () -> {
                player.gameMode.handleBlockBreakAction(
                    context.absolutePos(blockPos),
                    ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK,
                    Direction.NORTH,
                    context.getLevel().getHeight(),
                    0);

                futurePartialAct2.complete(null);
              });

          ReinforcedShulkerBoxesMod.LOGGER.info(
              "[{}] {} can be mined in {} ticks by {}",
              testIdentifier,
              shulkerBoxBlock.getName().getString(),
              tickBlockBreaking,
              tool.getName().getString());

          // Assert
          CompletableFuture.allOf(futurePartialAct1, futurePartialAct2)
              .thenRun(
                  () -> {
                    try {
                      context.assertBlockPresent(Blocks.AIR, blockPos);
                      context.assertItemEntityCountIs(
                          shulkerBoxBlock.asItem(), blockPos, 1, shouldDrop ? 1 : 0);
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
