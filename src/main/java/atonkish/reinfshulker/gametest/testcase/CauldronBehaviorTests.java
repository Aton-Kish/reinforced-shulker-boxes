package atonkish.reinfshulker.gametest.testcase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import atonkish.reinfcore.gametest.TestFunction;
import atonkish.reinfcore.util.ReinforcingMaterials;

import atonkish.reinfshulker.ReinforcedShulkerBoxesMod;
import atonkish.reinfshulker.block.ModBlocks;
import atonkish.reinfshulker.block.ReinforcedShulkerBoxBlock;
import atonkish.reinfshulker.gametest.util.MockServerPlayerHelper;
import atonkish.reinfshulker.gametest.util.TestIdentifier;
import atonkish.reinfshulker.item.ModItems;
import atonkish.reinfshulker.stat.ModStats;

public class CauldronBehaviorTests {
    private static final String TEST_ENVIRONMENT_DEFAULT = String.format("%s:cauldron_behavior/default",
            ReinforcedShulkerBoxesMod.MOD_ID);
    private static final String TEST_STRUCTURE_EMPTY = "fabric-gametest-api-v1:empty";

    public static final Collection<TestFunction> TEST_FUNCTIONS = new ArrayList<>() {
        {
            // Copper Shulker Box
            for (DyeColor color : DyeColor.values()) {
                ReinforcedShulkerBoxBlock shulkerBoxBlock = (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("copper"))
                        .get(color);

                add(CauldronBehaviorTests.createTest(
                        String.format("Clean %s", shulkerBoxBlock.getName().getString()),
                        shulkerBoxBlock));
            }

            // Iron Shulker Box
            for (DyeColor color : DyeColor.values()) {
                ReinforcedShulkerBoxBlock shulkerBoxBlock = (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("iron"))
                        .get(color);

                add(CauldronBehaviorTests.createTest(
                        String.format("Clean %s", shulkerBoxBlock.getName().getString()),
                        shulkerBoxBlock));
            }

            // Gold Shulker Box
            for (DyeColor color : DyeColor.values()) {
                ReinforcedShulkerBoxBlock shulkerBoxBlock = (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("gold"))
                        .get(color);

                add(CauldronBehaviorTests.createTest(
                        String.format("Clean %s", shulkerBoxBlock.getName().getString()),
                        shulkerBoxBlock));
            }

            // Diamond Shulker Box
            for (DyeColor color : DyeColor.values()) {
                ReinforcedShulkerBoxBlock shulkerBoxBlock = (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("diamond"))
                        .get(color);

                add(CauldronBehaviorTests.createTest(
                        String.format("Clean %s", shulkerBoxBlock.getName().getString()),
                        shulkerBoxBlock));
            }

            // Netherite Shulker Box
            for (DyeColor color : DyeColor.values()) {
                ReinforcedShulkerBoxBlock shulkerBoxBlock = (ReinforcedShulkerBoxBlock) ModBlocks.REINFORCED_SHULKER_BOX_MAP
                        .get(ReinforcingMaterials.MAP.get("netherite"))
                        .get(color);

                add(CauldronBehaviorTests.createTest(
                        String.format("Clean %s", shulkerBoxBlock.getName().getString()),
                        shulkerBoxBlock));
            }
        }
    };

    private static TestFunction createTest(String name, ReinforcedShulkerBoxBlock shulkerBoxBlock) {
        Identifier testIdentifier = TestIdentifier.of(ReinforcedShulkerBoxesMod.MOD_ID,
                CauldronBehaviorTests.class,
                name);

        return new TestFunction(
                testIdentifier,
                CauldronBehaviorTests.TEST_ENVIRONMENT_DEFAULT,
                CauldronBehaviorTests.TEST_STRUCTURE_EMPTY,
                20,
                0,
                true,
                BlockRotation.NONE,
                false,
                1,
                1,
                false,
                (context) -> {
                    // Arrange
                    BlockPos blockPos = BlockPos.ORIGIN;
                    context.setBlockState(blockPos,
                            Blocks.WATER_CAULDRON.getDefaultState().with(Properties.LEVEL_3, 3));

                    ServerPlayerEntity player = MockServerPlayerHelper.spawn(context,
                            GameMode.SURVIVAL,
                            Vec3d.of(blockPos.south(4)));
                    player.setStackInHand(Hand.MAIN_HAND, new ItemStack(shulkerBoxBlock.asItem()));

                    Stat<Identifier> stat = Stats.CUSTOM
                            .getOrCreateStat(ModStats.CLEAN_REINFORCED_SHULKER_BOX_MAP
                                    .get(shulkerBoxBlock.getMaterial()));

                    // Act
                    CompletableFuture<Void> futurePartialAct1 = new CompletableFuture<>();
                    CompletableFuture<Void> futurePartialAct2 = new CompletableFuture<>();

                    Map<String, Integer> statMap = new HashMap<String, Integer>();
                    String statMapKeyBeforeCleaning = "beforeCleaning";
                    String statMapKeyAfterCleaning = "afterCleaning";

                    long tickOrigin = 0;
                    context.runAtTick(tickOrigin, () -> {
                        statMap.put(statMapKeyBeforeCleaning, player.getStatHandler().getStat(stat));

                        context.useBlock(blockPos, player);

                        futurePartialAct1.complete(null);
                    });

                    long tickShulkerBoxCleaning = 1;
                    context.runAtTick(tickShulkerBoxCleaning, () -> {
                        statMap.put(statMapKeyAfterCleaning, player.getStatHandler().getStat(stat));

                        futurePartialAct2.complete(null);
                    });

                    // Assert
                    CompletableFuture.allOf(futurePartialAct1, futurePartialAct2).thenRun(() -> {
                        try {
                            context.assertEquals(player.getMainHandStack().getItem(),
                                    ModItems.REINFORCED_SHULKER_BOX_MAP
                                            .get(shulkerBoxBlock.getMaterial())
                                            .get((DyeColor) null),
                                    Text.of("main hand item"));
                            context.assertEquals(context.getBlockState(blockPos).get(Properties.LEVEL_3),
                                    2,
                                    Text.of("fluid level"));
                            context.assertEquals(
                                    statMap.get(statMapKeyAfterCleaning) - statMap.get(statMapKeyBeforeCleaning),
                                    1,
                                    Text.of(String.format("diff %s value", stat.getName())));
                        } catch (Exception e) {
                            ReinforcedShulkerBoxesMod.LOGGER.error("[{}] {}", testIdentifier, e.getMessage());
                            throw e;
                        } finally {
                            MockServerPlayerHelper.destroy(context, player);
                        }

                        context.complete();
                    });
                });
    }
}
