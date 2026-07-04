package atonkish.reinfshulker.client.gui.screen;

import java.lang.reflect.Field;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

import atonkish.reinfcore.ReinforcedCoreMod;
import atonkish.reinfcore.screen.ReinforcedStorageScreenHandler;
import atonkish.reinfcore.util.ReinforcedStorageScreenType;

public class ReinforcedShulkerBoxScreen
    extends AbstractContainerScreen<ReinforcedStorageScreenHandler> {
  private static final int SLOT_SIZE = 18;
  private static final int CONTAINER_SLOT_BG_X = 7;
  private static final int CONTAINER_SLOT_BG_Y = 17;
  private static final int PLAYER_INVENTORY_GAP = 14;
  private static final int PLAYER_INVENTORY_HEIGHT = 76;
  private static final int HOTBAR_GAP = 4;

  private static final int VANILLA_PANEL_WIDTH = 176;
  private static final int LEFT_INSET = 7;
  private static final int RIGHT_INSET = 7;

  private static final int SCROLLBAR_GAP = 4;
  private static final int SCROLLBAR_WIDTH = 12;
  private static final int SCROLLER_HEIGHT = 15;

  private static final Identifier GENERIC_54_TEXTURE =
      Identifier.fromNamespaceAndPath("minecraft", "textures/gui/container/generic_54.png");

  private final int containerRows;
  private final int containerColumns;
  private int playerInventoryX;
  private int playerInventoryY;

  private float scrollPosition = 0.0F;
  private boolean scrolling = false;

  public ReinforcedShulkerBoxScreen(
      ReinforcedStorageScreenHandler menu, Inventory inventory, Component title) {
    super(menu, inventory, title, getImageWidth(menu), getImageHeight(menu.getRows()));

    this.containerRows = getVisibleRows(menu);
    this.containerColumns = menu.getColumns();
    this.playerInventoryX = getPlayerInventoryX(this.containerColumns);
    this.playerInventoryY = getPlayerInventoryY(this.containerRows);

    this.titleLabelX = LEFT_INSET + 1;
    this.titleLabelY = CONTAINER_SLOT_BG_Y - 11;

    this.inventoryLabelX = this.playerInventoryX + 1;
    this.inventoryLabelY = this.playerInventoryY - 11;

    scrollContainerSlots();
  }

  private static int getImageWidth(ReinforcedStorageScreenHandler menu) {
    int columns = getColumns(menu);
    return CONTAINER_SLOT_BG_X * 2
        + columns * SLOT_SIZE
        + (shouldUseScrollbar(menu) ? SCROLLBAR_WIDTH : 0);
  }

  private static int getImageHeight(int rows) {
    return CONTAINER_SLOT_BG_Y
        + rows * SLOT_SIZE
        + PLAYER_INVENTORY_GAP
        + PLAYER_INVENTORY_HEIGHT
        + RIGHT_INSET;
  }

  private static int getContainerSlotBgX(int columns) {
    return LEFT_INSET + (Math.max(9, columns) - columns) * SLOT_SIZE / 2;
  }

  private static int getPlayerInventoryX(int columns) {
    int chestWidth = columns * SLOT_SIZE;
    int playerWidth = 9 * SLOT_SIZE;
    return getContainerSlotBgX(columns) + (chestWidth - playerWidth) / 2;
  }

  private static int getPlayerInventoryY(int rows) {
    return CONTAINER_SLOT_BG_Y + rows * SLOT_SIZE + PLAYER_INVENTORY_GAP;
  }

  @Override
  protected void init() {
    super.init();
    updatePlayerInventoryLayout();
    scrollContainerSlots();
  }

  @Override
  public void extractBackground(
      GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
    super.extractBackground(graphics, mouseX, mouseY, partialTick);

    int x = this.leftPos;
    int y = this.topPos;

    drawPanelBackground(graphics, x, y);
    drawContainerSlots(graphics, x, y);
    drawPlayerSlots(graphics, x, y);

    if (this.hasScrollbar()) {
      drawScrollbar(graphics, x, y);
    }
  }

  private void drawPanelBackground(GuiGraphicsExtractor graphics, int x, int y) {
    graphics.fill(x, y, x + this.imageWidth, y + this.imageHeight, 0xFFC6C6C6);
    graphics.outline(x, y, this.imageWidth, this.imageHeight, 0xFF404040);
  }

  private void drawContainerSlots(GuiGraphicsExtractor graphics, int x, int y) {
    int containerSlots = this.menu.getInventory().getContainerSize();

    for (int index = 0; index < containerSlots && index < this.menu.slots.size(); index++) {
      Slot slot = this.menu.slots.get(index);

      if (slot.y <= -1000) {
        continue;
      }

      drawSlotBackground(graphics, this.leftPos + slot.x - 1, this.topPos + slot.y - 1);
    }
  }

  private void drawPlayerSlots(GuiGraphicsExtractor graphics, int x, int y) {
    int containerSlots = this.menu.getInventory().getContainerSize();

    for (int index = containerSlots; index < this.menu.slots.size(); index++) {
      Slot slot = this.menu.slots.get(index);

      if (slot.y <= -1000) {
        continue;
      }

      drawSlotBackground(graphics, this.leftPos + slot.x - 1, this.topPos + slot.y - 1);
    }
  }

  private void drawScrollbar(GuiGraphicsExtractor graphics, int x, int y) {
    int barX = getScrollbarX();
    int barY = y + CONTAINER_SLOT_BG_Y + 1;
    int trackHeight = this.containerRows * SLOT_SIZE - 2;

    graphics.fill(barX, barY, barX + SCROLLBAR_WIDTH, barY + trackHeight, 0xFF8B8B8B);
    graphics.outline(barX, barY, SCROLLBAR_WIDTH, trackHeight, 0xFF404040);

    int thumbTravel = trackHeight - SCROLLER_HEIGHT;
    int thumbY = barY + Math.round(thumbTravel * this.scrollPosition);

    graphics.fill(
        barX + 1, thumbY, barX + SCROLLBAR_WIDTH - 1, thumbY + SCROLLER_HEIGHT, 0xFFC6C6C6);
    graphics.outline(barX + 1, thumbY, SCROLLBAR_WIDTH - 2, SCROLLER_HEIGHT, 0xFF404040);
  }

  private int getScrollbarX() {
    return this.leftPos + LEFT_INSET + this.containerColumns * SLOT_SIZE + SCROLLBAR_GAP + 1;
  }

  private boolean hasScrollbar() {
    return shouldUseScrollbar(this.menu);
  }

  private boolean isClickInScrollbar(double mouseX, double mouseY) {
    int x = getScrollbarX();
    int y = this.topPos + CONTAINER_SLOT_BG_Y + 1;
    int height = this.containerRows * SLOT_SIZE - 2;

    return mouseX >= x && mouseX < x + SCROLLBAR_WIDTH && mouseY >= y && mouseY < y + height;
  }

  private int getHiddenRows() {
    int totalRows = (int) Math.ceil(this.menu.getInventory().getContainerSize() / 9.0D);
    return Math.max(1, totalRows - this.containerRows);
  }

  private void setScrollPosition(float position) {
    this.scrollPosition = Math.max(0.0F, Math.min(position, 1.0F));
    scrollContainerSlots();
  }

  private static float clamp(float value, float min, float max) {
    return Math.max(min, Math.min(max, value));
  }

  @Override
  public boolean mouseScrolled(
      double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    if (!this.hasScrollbar()) {
      return false;
    }

    float amount = (float) (verticalAmount / (double) getHiddenRows());
    setScrollPosition(this.scrollPosition - amount);
    return true;
  }

  @Override
  public boolean mouseClicked(MouseButtonEvent event, boolean doubled) {
    if (this.hasScrollbar() && event.button() == 0 && isClickInScrollbar(event.x(), event.y())) {
      this.scrolling = true;
      return true;
    }

    return super.mouseClicked(event, doubled);
  }

  @Override
  public boolean mouseReleased(MouseButtonEvent event) {
    if (event.button() == 0) {
      this.scrolling = false;
    }

    return super.mouseReleased(event);
  }

  @Override
  public boolean mouseDragged(MouseButtonEvent event, double offsetX, double offsetY) {
    if (this.hasScrollbar() && this.scrolling) {
      int y = this.topPos + CONTAINER_SLOT_BG_Y + 1;
      int height = this.containerRows * SLOT_SIZE - 2;
      float position =
          ((float) event.y() - y - SCROLLER_HEIGHT / 2.0F) / (height - SCROLLER_HEIGHT);

      setScrollPosition(position);
      return true;
    }

    return super.mouseDragged(event, offsetX, offsetY);
  }

  private static void drawSlotBackground(GuiGraphicsExtractor graphics, int x, int y) {
    graphics.blit(
        RenderPipelines.GUI_TEXTURED,
        GENERIC_54_TEXTURE,
        x,
        y,
        CONTAINER_SLOT_BG_X,
        CONTAINER_SLOT_BG_Y,
        SLOT_SIZE,
        SLOT_SIZE,
        256,
        256);
  }

  private static int getColumns(ReinforcedStorageScreenHandler menu) {
    return Math.max(9, menu.getColumns());
  }

  private static int getTotalRows(ReinforcedStorageScreenHandler menu) {
    return (int) Math.ceil(menu.getInventory().getContainerSize() / (double) getColumns(menu));
  }

  private static int getVisibleRows(ReinforcedStorageScreenHandler menu) {
    int totalRows = getTotalRows(menu);

    if (ReinforcedCoreMod.CONFIG.screenType == ReinforcedStorageScreenType.SINGLE) {
      return totalRows;
    }

    int configuredRows = ReinforcedCoreMod.CONFIG.scrollScreen.rows;
    configuredRows = Math.max(1, Math.min(configuredRows, totalRows));
    return configuredRows;
  }

  private static boolean shouldUseScrollbar(ReinforcedStorageScreenHandler menu) {
    return ReinforcedCoreMod.CONFIG.screenType == ReinforcedStorageScreenType.SCROLL
        && getTotalRows(menu) > getVisibleRows(menu);
  }

  private void scrollContainerSlots() {
    int totalSlots = this.menu.getInventory().getContainerSize();
    int columns = getColumns(this.menu);
    int totalRows = getTotalRows(this.menu);
    int hiddenRows = Math.max(0, totalRows - this.containerRows);
    int rowOffset =
        shouldUseScrollbar(this.menu) ? Math.round(this.scrollPosition * hiddenRows) : 0;

    for (int slotIndex = 0;
        slotIndex < totalSlots && slotIndex < this.menu.slots.size();
        slotIndex++) {
      Slot slot = this.menu.slots.get(slotIndex);

      int row = slotIndex / columns;
      int column = slotIndex % columns;
      int visibleRow = row - rowOffset;

      int x = CONTAINER_SLOT_BG_X + column * SLOT_SIZE;
      int y =
          visibleRow >= 0 && visibleRow < this.containerRows
              ? CONTAINER_SLOT_BG_Y + visibleRow * SLOT_SIZE
              : -2000;

      setSlotPosition(slot, x, y);
    }

    movePlayerInventorySlots(totalSlots);
  }

  private void updatePlayerInventoryLayout() {
    this.playerInventoryX = Math.max(CONTAINER_SLOT_BG_X, (this.imageWidth - 9 * SLOT_SIZE) / 2);

    this.playerInventoryY = this.imageHeight - (3 * SLOT_SIZE + HOTBAR_GAP + SLOT_SIZE) - 8;

    this.inventoryLabelX = this.playerInventoryX;
    this.inventoryLabelY = this.playerInventoryY - 10;
  }

  private void movePlayerInventorySlots(int firstPlayerSlotIndex) {
    int playerInventoryY = this.playerInventoryY;
    int playerInventoryX = this.playerInventoryX;

    for (int row = 0; row < 3; row++) {
      for (int column = 0; column < 9; column++) {
        int index = firstPlayerSlotIndex + row * 9 + column;

        if (index < this.menu.slots.size()) {
          setSlotPosition(
              this.menu.slots.get(index),
              playerInventoryX + column * SLOT_SIZE,
              playerInventoryY + row * SLOT_SIZE);
        }
      }
    }

    int hotbarY = playerInventoryY + 3 * SLOT_SIZE + HOTBAR_GAP;

    for (int column = 0; column < 9; column++) {
      int index = firstPlayerSlotIndex + 27 + column;

      if (index < this.menu.slots.size()) {
        setSlotPosition(this.menu.slots.get(index), playerInventoryX + column * SLOT_SIZE, hotbarY);
      }
    }
  }

  private static final Field SLOT_X_FIELD = getSlotField("x", "field_7873");
  private static final Field SLOT_Y_FIELD = getSlotField("y", "field_7872");

  private static Field getSlotField(String namedField, String intermediaryField) {
    try {
      Field field = Slot.class.getDeclaredField(namedField);
      field.setAccessible(true);
      return field;
    } catch (ReflectiveOperationException namedException) {
      try {
        Field field = Slot.class.getDeclaredField(intermediaryField);
        field.setAccessible(true);
        return field;
      } catch (ReflectiveOperationException intermediaryException) {
        throw new IllegalStateException(
            "Unable to access Slot position field", intermediaryException);
      }
    }
  }

  private static void setSlotPosition(Slot slot, int x, int y) {
    try {
      SLOT_X_FIELD.setInt(slot, x);
      SLOT_Y_FIELD.setInt(slot, y);
    } catch (IllegalAccessException exception) {
      throw new IllegalStateException("Unable to move shulker slot for scrolling", exception);
    }
  }
}
