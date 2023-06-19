package xyz.tildejustin.custommapresetter;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import xyz.tildejustin.custommapresetter.mixin.SelectWorldScreenAccessor;

import java.util.Objects;


public class SetWorldScreen extends SelectWorldScreen {
    public final Screen parent;

    public SetWorldScreen(Screen parent) {
        super(parent);
        this.parent = parent;
    }

    @Override
    public void init() {
        assert this.client != null;
        this.client.keyboard.setRepeatEvents(true);
        ((SelectWorldScreenAccessor) this).setDeleteButton(new ButtonWidget(0, 0, 0, 0, new LiteralText(""), (buttonWidget) -> {
        }));
        ((SelectWorldScreenAccessor) this).setEditButton(new ButtonWidget(0, 0, 0, 0, new LiteralText(""), (buttonWidget) -> {
        }));
        ((SelectWorldScreenAccessor) this).setRecreateButton(new ButtonWidget(0, 0, 0, 0, new LiteralText(""), (buttonWidget) -> {
        }));
        ((SelectWorldScreenAccessor) this).setSearchBox(new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 22, 200, 20, this.searchBox, new TranslatableText("selectWorld.search")));
        ((SelectWorldScreenAccessor) this).getSearchBox().setChangedListener((string) -> ((SelectWorldScreenAccessor) this).getLevelList().filter(() -> string, false));
        ((SelectWorldScreenAccessor) this).setLevelList(new WorldListWidget(this, this.client, this.width, this.height, 48, this.height - 64, 36, () -> ((SelectWorldScreenAccessor) this).getSearchBox().getText(), ((SelectWorldScreenAccessor) this).getLevelList()));
        this.children.add(((SelectWorldScreenAccessor) this).getSearchBox());
        this.children.add(((SelectWorldScreenAccessor) this).getLevelList());
        ButtonWidget selectButton = this.addDrawableChild(new ButtonWidget(3, this.height - 28, this.width / 4 - 6, 20, new TranslatableText("selectWorld.title"), (buttonWidget) -> {
            CustomMapResetter.resetTracker.setCurrentWorld(Objects.requireNonNull(((SelectWorldScreenAccessor) this).getLevelList().getSelectedOrNull()).level.getName());
            CustomMapResetter.resetTracker.writeResetCountFile(CustomMapResetter.resetTracker.resetCount, CustomMapResetter.resetTracker.resetCountFile);
        }));
        ((SelectWorldScreenAccessor) this).setSelectButton(selectButton);
        this.addDrawableChild(new ButtonWidget(this.width / 4 + 3, this.height - 28, this.width / 4 - 6, 20, Text.of("Autoreset: " + CustomMapResetter.autoreset), (button -> {
            CustomMapResetter.autoreset = !CustomMapResetter.autoreset;
            button.setMessage(Text.of("Autoreset: " + CustomMapResetter.autoreset));
            CustomMapResetter.resetTracker.writeResetCountFile(CustomMapResetter.resetTracker.resetCount, CustomMapResetter.resetTracker.resetCountFile);
        })));
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 3, this.height - 28, this.width / 4 - 6, 20, Text.of("Delete Session Worlds"), (buttonWidget) -> {
            CustomMapResetter.resetTracker.deleteWorlds();
            ((SelectWorldScreenAccessor) this).getLevelList().filter(() -> ((SelectWorldScreenAccessor) this).getSearchBox().getText(), true);
        }));
        this.addDrawableChild(new ButtonWidget(this.width / 4 * 3 + 3, this.height - 28, this.width / 4 - 6, 20, new TranslatableText("gui.cancel"), (buttonWidget) -> this.client.setScreen(this.parent)));
        this.worldSelected(false);
        this.setInitialFocus(((SelectWorldScreenAccessor) this).getSearchBox());
    }
}
