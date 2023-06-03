package xyz.tildejustin.custommapresetter;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ListWidget;
import net.minecraft.client.render.Tessellator;

import net.minecraft.util.Language;
import net.minecraft.world.level.storage.LevelStorageAccess;
import net.minecraft.world.level.storage.LevelSummary;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SetWorldScreen extends Screen {
    private final DateFormat dateFormat = new SimpleDateFormat();
    public WorldListWidget worldList;
    protected Screen parent;
    private int selectedWorld;
    private List<LevelSummary> worlds;
    private ButtonWidget selectButton;
    private String defaultWorldName;
    private String mustConvertText;


    public SetWorldScreen(Screen parent) {
        this.parent = parent;
    }

    @Override
    public void init() {
        this.loadWorlds();
        this.mustConvertText = Language.getInstance().translate("selectWorld.conversion");
        this.defaultWorldName = Language.getInstance().translate("selectWorld.world");
        worldList = new WorldListWidget(this.field_1229);
//        worldList.setButtonIds(4, 5);
        this.selectButton = new ButtonWidget(1, this.width / 2 - 154, this.height - 28, 150, 20, "Select World");
        this.selectButton.active = false;
        this.buttons.add(new ButtonWidget(0, this.width / 2 + 5, this.height - 28, 150, 20, Language.getInstance().translate("gui.cancel")));
        this.buttons.add(selectButton);
        this.buttons.add(new ButtonWidget(6, this.width / 2 + 5 + 150 + 5, this.height - 28, 150, 20, "Delete Session Worlds"));
    }

    @Override
    protected void buttonClicked(@NotNull ButtonWidget button) {
        if (!button.active) {
            return;
        }
        if (button.id == 1) {
            CustomMapResetter.resetTracker.setCurrentWorld(this.getWorldFileName(this.selectedWorld));
            this.field_1229.openScreen(this.parent);
        } else if (button.id == 0) {
            CustomMapResetter.resetTracker.setCurrentWorld(null);
            this.field_1229.openScreen(this.parent);
        } else if (button.id == 6) {
            CustomMapResetter.resetTracker.deleteWorlds();
            this.loadWorlds();
        } else {
            worldList.buttonClicked(button);
        }
    }


    @Override
    public void handleMouse() {
        super.handleMouse();
    }

    protected String getWorldFileName(int index) {
        return this.worlds.get(index).getFileName();
    }

    private void loadWorlds() {
        LevelStorageAccess levelStorageAccess = this.field_1229.getCurrentSave();
        this.worlds = levelStorageAccess.getLevelList();
        Collections.sort(this.worlds);
        Collections.reverse(this.worlds);
        this.selectedWorld = -1;
    }

    @Override
    public void render(int mouseX, int mouseY, float tickDelta) {
        worldList.render(mouseX, mouseY, tickDelta);
        super.render(mouseX, mouseY, tickDelta);
    }

    @Environment(value = EnvType.CLIENT)
    class WorldListWidget extends ListWidget {
        public WorldListWidget(Minecraft client) {
            super(client, SetWorldScreen.this.width, SetWorldScreen.this.height, 32, SetWorldScreen.this.height - 32, 36);
        }

        @Override
        protected int getEntryCount() {
            return SetWorldScreen.this.worlds.size();
        }

        @Override
        public void method_1057(int index, boolean doubleClick) {
            boolean bl;
            SetWorldScreen.this.selectedWorld = index;
            SetWorldScreen.this.selectButton.active = bl = SetWorldScreen.this.selectedWorld >= 0 && SetWorldScreen.this.selectedWorld < this.getEntryCount();
            if (doubleClick && bl) {
                CustomMapResetter.resetTracker.setCurrentWorld(SetWorldScreen.this.getWorldFileName(SetWorldScreen.this.selectedWorld));
                Minecraft.getMinecraft().openScreen(SetWorldScreen.this.parent);
            }
        }

        @Override
        protected boolean isEntrySelected(int index) {
            return index == SetWorldScreen.this.selectedWorld;
        }

        @Override
        protected int getMaxPosition() {
            return SetWorldScreen.this.worlds.size() * 36;
        }

        @Override
        protected void renderBackground() {
            SetWorldScreen.this.renderBackground();
        }

        @Override
        public void method_1055(int index, int x, int y, int rowHeight, Tessellator t) {
//        public void renderEntry(int index, int x, int y, int rowHeight, int mouseX, int mouseY) {
            LevelSummary levelSummary = SetWorldScreen.this.worlds.get(index);
            String string = levelSummary.getDisplayName();
            if (string.isEmpty()) {
                string = SetWorldScreen.this.defaultWorldName + " " + (index + 1);
            }
            String string2 = levelSummary.getFileName();
            string2 += " (" + SetWorldScreen.this.dateFormat.format(new Date(levelSummary.getLastPlayed()));
            string2 += ")";
            String string3 = "";
            boolean comma = false;
            if (levelSummary.requiresConversion()) {
                string3 += SetWorldScreen.this.mustConvertText;
                comma = true;
            }
            if (levelSummary.isHardcore()) {
                if (comma) {
                    string3 += ", ";
                }
                string3 += Language.getInstance().translate("gameMode.hardcore");
                comma = true;
            }
            if (levelSummary.cheatsEnabled()) {
                if (comma) {
                    string3 += ", ";
                }
                string3 += Language.getInstance().translate("selectWorld.cheats");
            }
            SetWorldScreen.this.drawWithShadow(SetWorldScreen.this.textRenderer, string, x + 2, y + 1, 0xFFFFFF);
            SetWorldScreen.this.drawWithShadow(SetWorldScreen.this.textRenderer, string2, x + 2, y + 12, 0x808080);
            SetWorldScreen.this.drawWithShadow(SetWorldScreen.this.textRenderer, string3, x + 2, y + 12 + 10, 0x808080);
        }

//        @Override
//        public void method_1055(int index, int x, int y, int rowHeight, int mouseX, int mouseY, float f) {
//            renderEntry(index, x, y, rowHeight, mouseX, mouseY);
//        }
//
//        @Override
//        public void method_1055(int i, int j, int k, int l, Tessellator t, int m, int n) {
//            renderEntry(i, j, k, l, m, n);
//        }

//        @Override
//        public void method_1055(int index, int x, int y, int rowHeight, int mouseX, int mouseY) {
//            renderEntry(index, x, y, rowHeight, mouseX, mouseY);
//        }
    }
}
