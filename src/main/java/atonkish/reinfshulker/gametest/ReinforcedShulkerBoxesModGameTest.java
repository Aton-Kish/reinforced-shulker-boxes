package atonkish.reinfshulker.gametest;

import java.util.ArrayList;
import java.util.Collection;

import atonkish.reinfcore.gametest.CustomTestProvider;
import atonkish.reinfcore.gametest.TestFunction;
import atonkish.reinfshulker.gametest.testcase.AdvancementTests;
import atonkish.reinfshulker.gametest.testcase.CauldronBehaviorTests;
import atonkish.reinfshulker.gametest.testcase.DispenserBehaviorTests;
import atonkish.reinfshulker.gametest.testcase.InventoryTests;
import atonkish.reinfshulker.gametest.testcase.LootTableTests;
import atonkish.reinfshulker.gametest.testcase.OpenTests;
import atonkish.reinfshulker.gametest.testcase.PiglinTests;
import atonkish.reinfshulker.gametest.testcase.PistonBehaviorTests;
import atonkish.reinfshulker.gametest.testcase.RecipeTests;

public class ReinforcedShulkerBoxesModGameTest {
  @CustomTestProvider
  public Collection<TestFunction> registerTests() {
    Collection<TestFunction> testFunctions = new ArrayList<>();

    if (System.getProperty(this.getClass().getPackageName()) == null) {
      return testFunctions;
    }

    testFunctions.addAll(AdvancementTests.TEST_FUNCTIONS);
    testFunctions.addAll(CauldronBehaviorTests.TEST_FUNCTIONS);
    testFunctions.addAll(DispenserBehaviorTests.TEST_FUNCTIONS);
    testFunctions.addAll(InventoryTests.TEST_FUNCTIONS);
    testFunctions.addAll(LootTableTests.TEST_FUNCTIONS);
    testFunctions.addAll(OpenTests.TEST_FUNCTIONS);
    testFunctions.addAll(PiglinTests.TEST_FUNCTIONS);
    testFunctions.addAll(PistonBehaviorTests.TEST_FUNCTIONS);
    testFunctions.addAll(RecipeTests.TEST_FUNCTIONS);

    return testFunctions;
  }
}
