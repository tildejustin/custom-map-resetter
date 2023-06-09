package xyz.tildejustin.custommapresetter;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.ClientException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.IdentifibleBooleanConsumer;
import net.minecraft.client.gui.widget.ListWidget;
import net.minecraft.client.render.Tessellator;
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
    public WorldListWidget worldList;
    protected Screen parent;
    private int selectedWorld;
    private List<LevelSummary> worlds;
    private ButtonWidget selectButton;
    private String defaultWorldName;
    private String mustConvertText;
    private ButtonWidget autoreset;


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
        this.selectButton = new ButtonWidget(1, 3, this.height - 28, this.width / 4 - 6, 20, I18n.translate("selectWorld.title"));
        this.selectButton.active = false;
        this.buttons.add(selectButton);
        // remember to update when button is pressed
        this.autoreset = new ButtonWidget(7, this.width / 4 + 3, this.height - 28, this.width / 4 - 6, 20, "Autoreset: " + CustomMapResetter.autoreset);
        this.buttons.add(this.autoreset);
        this.buttons.add(new ButtonWidget(6, this.width / 2 + 3, this.height - 28, this.width / 4 - 6, 20, "Delete Session Worlds"));
        this.buttons.add(new ButtonWidget(0, this.width / 4 * 3 + 3, this.height - 28, this.width / 4 - 6, 20, I18n.translate("gui.cancel")));
    }

    @Override
    protected void buttonClicked(@NotNull ButtonWidget button) {
        if (!button.active) {
            return;
        }
        if (button.id == this.selectButton.id) {
            CustomMapResetter.resetTracker.setCurrentWorld(this.getWorldFileName(this.selectedWorld));
            CustomMapResetter.resetTracker.writeResetCountFile(CustomMapResetter.resetTracker.resetCount, CustomMapResetter.resetTracker.resetCountFile);
        } else if (button.id == 0) {
            this.client.setScreen(this.parent);
        } else if (button.id == 6) {
            CustomMapResetter.resetTracker.deleteWorlds();
            try {
                this.loadWorlds();
            } catch (ClientException e) {
                e.printStackTrace();
            }
        } else if (button.id == this.autoreset.id) {
            CustomMapResetter.autoreset = !CustomMapResetter.autoreset;
            CustomMapResetter.resetTracker.writeResetCountFile(CustomMapResetter.resetTracker.resetCount, CustomMapResetter.resetTracker.resetCountFile);
            this.autoreset.message = "Autoreset: " + CustomMapResetter.autoreset;
        } else {
            worldList.buttonClicked(button);
        }
    }


    @Override
    public void handleMouse() {
        super.handleMouse();
        // setting myself up for failure here :P
        if (Integer.parseInt(FabricLoader.getInstance().getModContainer("minecraft").get().getMetadata().getVersion().getFriendlyString().split("\\.")[1]) > 7) {
            worldList.handleMouse();
        }
    }

    protected String getWorldFileName(int index) {
        return this.worlds.get(index).getFileName();
    }

    private void loadWorlds() throws ClientException {
        LevelStorageAccess levelStorageAccess = this.client.getCurrentSave();
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
    class WorldListWidget extends ListWidget implements WorldListWidgetFaker {
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
                MinecraftClient.getInstance().setScreen(SetWorldScreen.this.parent);
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
        public void renderEntry(int index, int x, int y, int rowHeight, int mouseX, int mouseY) {
            LevelSummary levelSummary = SetWorldScreen.this.worlds.get(index);
            String string = levelSummary.getDisplayName();
            if (StringUtils.isEmpty(string)) {
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
                string3 += Formatting.DARK_RED + I18n.translate("gameMode.hardcore") + Formatting.RESET;
                comma = true;
            }
            if (levelSummary.cheatsEnabled()) {
                if (comma) {
                    string3 += ", ";
                }
                string3 += I18n.translate("selectWorld.cheats");
            }
            SetWorldScreen.this.drawWithShadow(SetWorldScreen.this.textRenderer, string, x + 2, y + 1, 0xFFFFFF);
            SetWorldScreen.this.drawWithShadow(SetWorldScreen.this.textRenderer, string2, x + 2, y + 12, 0x808080);
            SetWorldScreen.this.drawWithShadow(SetWorldScreen.this.textRenderer, string3, x + 2, y + 12 + 10, 0x808080);
        }

        @Override
        public void method_1055(int index, int x, int y, int rowHeight, int mouseX, int mouseY, float f) {
            renderEntry(index, x, y, rowHeight, mouseX, mouseY);
        }

        @Override
        public void method_1055(int i, int j, int k, int l, Tessellator t, int m, int n) {
            renderEntry(i, j, k, l, m, n);
        }

//        @Override
//        public void method_1055(int index, int x, int y, int rowHeight, int mouseX, int mouseY) {
//            renderEntry(index, x, y, rowHeight, mouseX, mouseY);
//        }
    }
}
