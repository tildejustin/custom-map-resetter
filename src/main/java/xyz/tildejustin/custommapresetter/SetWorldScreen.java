package xyz.tildejustin.custommapresetter;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.ClientException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.IdentifibleBooleanConsumer;
import net.minecraft.client.gui.widget.ListWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Formatting;
import net.minecraft.world.level.storage.LevelStorageAccess;
import net.minecraft.world.level.storage.LevelSummary;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SetWorldScreen extends Screen implements IdentifibleBooleanConsumer {
    private final DateFormat dateFormat = new SimpleDateFormat();
    protected Screen parent;
    private int selectedWorld;
    private List<LevelSummary> worlds;
    public WorldListWidget worldList;
    private String[] gameModeTexts = new String[4];
    private ButtonWidget selectButton;
    private String defaultWorldName;
    private String mustConvertText;


    public SetWorldScreen(Screen parent) {
        this.parent = parent;
    }
    
    @Override
    public void init() {
        try {
            this.loadWorlds();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        this.mustConvertText = I18n.translate("selectWorld.conversion");
        this.defaultWorldName = I18n.translate("selectWorld.world");
        worldList = new WorldListWidget(this.client);
        worldList.setButtonIds(4, 5);
        this.selectButton = new ButtonWidget(1, this.width / 2 - 154, this.height - 28, 150, 20, "Select World");
        this.selectButton.active = false;
        this.buttons.add(new ButtonWidget(0, this.width / 2 + 5, this.height - 28, 150, 20, I18n.translate("gui.cancel")));
        this.buttons.add(selectButton);
    }

    @Override
    protected void buttonClicked(@NotNull ButtonWidget button) {
        if (!button.active) {
            return;
        }
        if (button.id == 1) {
            CustomMapResetter.resetTracker.setCurrentWorld(this.getWorldFileName(this.selectedWorld));
            this.client.setScreen(this.parent);
        } else if (button.id == 0) {
            CustomMapResetter.resetTracker.setCurrentWorld(null);;
            this.client.setScreen(this.parent);
        } else {
            worldList.buttonClicked(button);
        }
    }


    @Override
    public void handleMouse() {
        super.handleMouse();
        worldList.handleMouse();
    }

    protected String getWorldName(int index) {
        String string = this.worlds.get(index).getDisplayName();
        if (StringUtils.isEmpty(string)) {
            string = I18n.translate("selectWorld.world") + " " + (index + 1);
        }
        return string;
    }

    protected String getWorldFileName(int index) {
        return this.worlds.get(index).getFileName();
    }

    private void loadWorlds() throws ClientException {
        LevelStorageAccess levelStorageAccess = this.client.getCurrentSave();
        this.worlds = levelStorageAccess.getLevelList();
        Collections.sort(this.worlds);
        this.selectedWorld = -1;
    }

    @Override
    public void render(int mouseX, int mouseY, float tickDelta) {
        worldList.render(mouseX, mouseY, tickDelta);
//        this.drawCenteredString(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(mouseX, mouseY, tickDelta);
    }

    @Environment(value= EnvType.CLIENT)
    class WorldListWidget extends ListWidget {
        public WorldListWidget(MinecraftClient client) {
            super(client, SetWorldScreen.this.width, SetWorldScreen.this.height, 32, SetWorldScreen.this.height - 32, 36);
        }

        @Override
        protected int getEntryCount() {
            return SetWorldScreen.this.worlds.size();
        }

        @Override
        protected void selectEntry(int index, boolean doubleClick, int lastMouseX, int lastMouseY) {
            boolean bl;
            SetWorldScreen.this.selectedWorld = index;
            SetWorldScreen.this.selectButton.active = bl = SetWorldScreen.this.selectedWorld >= 0 && SetWorldScreen.this.selectedWorld < this.getEntryCount();
            if (doubleClick && bl) {
                CustomMapResetter.resetTracker.setCurrentWorld(SetWorldScreen.this.getWorldFileName(SetWorldScreen.this.selectedWorld));
                this.client.setScreen(SetWorldScreen.this.parent);
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
        protected void renderEntry(int index, int x, int y, int rowHeight, int mouseX, int mouseY) {
            LevelSummary levelSummary = (LevelSummary)SetWorldScreen.this.worlds.get(index);
            String string = levelSummary.getDisplayName();
            if (StringUtils.isEmpty(string)) {
                string = SetWorldScreen.this.defaultWorldName + " " + (index + 1);
            }
            String string2 = levelSummary.getFileName();
            string2 = string2 + " (" + SetWorldScreen.this.dateFormat.format(new Date(levelSummary.getLastPlayed()));
            string2 = string2 + ")";
            String string3 = "";
            if (levelSummary.requiresConversion()) {
                string3 = SetWorldScreen.this.mustConvertText + " " + string3;
            } else {
                string3 = SetWorldScreen.this.gameModeTexts[levelSummary.getGameMode().getId()];
                if (levelSummary.isHardcore()) {
                    string3 = (Object)((Object) Formatting.DARK_RED) + I18n.translate("gameMode.hardcore", new Object[0]) + (Object)((Object)Formatting.RESET);
                }
                if (levelSummary.cheatsEnabled()) {
                    string3 = string3 + ", " + I18n.translate("selectWorld.cheats");
                }
            }
            SetWorldScreen.this.drawWithShadow(SetWorldScreen.this.textRenderer, string, x + 2, y + 1, 0xFFFFFF);
            SetWorldScreen.this.drawWithShadow(SetWorldScreen.this.textRenderer, string2, x + 2, y + 12, 0x808080);
            SetWorldScreen.this.drawWithShadow(SetWorldScreen.this.textRenderer, string3, x + 2, y + 12 + 10, 0x808080);
        }
    }
}
