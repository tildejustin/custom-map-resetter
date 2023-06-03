package xyz.tildejustin.custommapresetter;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import xyz.tildejustin.custommapresetter.mixin.SelectWorldScreenAccessor;

import java.util.Objects;


public class SetWorldScreen extends SelectWorldScreen {
    protected final Screen parent;

    public SetWorldScreen(Screen parent) {
        super(parent);
        this.parent = parent;
    }

    @Override
    public void init() {
        assert this.client != null;
        this.client.keyboard.setRepeatEvents(true);
        ((SelectWorldScreenAccessor)this).setDeleteButton(new ButtonWidget(0, 0, 0, 0, new LiteralText(""), (buttonWidget) -> {}));
        ((SelectWorldScreenAccessor)this).setEditButton(new ButtonWidget(0, 0, 0, 0, new LiteralText(""), (buttonWidget) -> {}));
        ((SelectWorldScreenAccessor)this).setRecreateButton(new ButtonWidget(0, 0, 0, 0, new LiteralText(""), (buttonWidget) -> {}));
        ((SelectWorldScreenAccessor)this).setSearchBox(new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 22, 200, 20, this.searchBox, new TranslatableText("selectWorld.search")));
        ((SelectWorldScreenAccessor)this).getSearchBox().setChangedListener((string) -> ((SelectWorldScreenAccessor)this).getLevelList().filter(() -> string, false));
        ((SelectWorldScreenAccessor)this).setLevelList(new WorldListWidget(this, this.client, this.width, this.height, 48, this.height - 64, 36, () -> ((SelectWorldScreenAccessor)this).getSearchBox().getText(), ((SelectWorldScreenAccessor)this).getLevelList()));
        this.children.add(((SelectWorldScreenAccessor)this).getSearchBox());
        this.children.add(((SelectWorldScreenAccessor)this).getLevelList());
        ButtonWidget selectButton = this.addDrawableChild(new ButtonWidget(this.width / 2 - (150 / 2) - 150 - 5, this.height - 28, 150, 20,  new TranslatableText("Select World"), (buttonWidget) -> {
                CustomMapResetter.resetTracker.setCurrentWorld(Objects.requireNonNull(((SelectWorldScreenAccessor) this).getLevelList().getSelectedOrNull()).level.getName());
            this.client.setScreen(parent);
        }));
        ((SelectWorldScreenAccessor)this).setSelectButton(selectButton);
//        if > 16 this.addDrawableChild(button)
        this.addDrawableChild(new ButtonWidget(this.width / 2 + (150 / 2) + 5, this.height - 28, 150, 20, new TranslatableText("gui.cancel"), (buttonWidget) -> this.client.setScreen(this.parent)));
//        if > 16 this.addDrawableChild(button)
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 75, this.height - 28, 150, 20, new TranslatableText("Delete Session Worlds"), (buttonWidget) -> {
            CustomMapResetter.resetTracker.deleteWorlds();
            ((SelectWorldScreenAccessor)this).getLevelList().filter(() -> "", false);
            ((SelectWorldScreenAccessor) this).getLevelList().filter(() -> ((SelectWorldScreenAccessor)this).getSearchBox().getText(), true);
        }));
        this.worldSelected(false);
        this.setInitialFocus(((SelectWorldScreenAccessor)this).getSearchBox());
    }
}
