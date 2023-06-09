package xyz.tildejustin.custommapresetter;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
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
        ((SelectWorldScreenAccessor)this).setDeleteButton(new ButtonWidget(0, 0, 0, 0, Text.of(""), (buttonWidget) -> {}, ButtonWidget.DEFAULT_NARRATION_SUPPLIER));
        ((SelectWorldScreenAccessor)this).setEditButton(new ButtonWidget(0, 0, 0, 0, Text.of(""), (buttonWidget) -> {}, ButtonWidget.DEFAULT_NARRATION_SUPPLIER));
        ((SelectWorldScreenAccessor)this).setRecreateButton(new ButtonWidget(0, 0, 0, 0, Text.of(""), (buttonWidget) -> {}, ButtonWidget.DEFAULT_NARRATION_SUPPLIER));
        ((SelectWorldScreenAccessor)this).setSearchBox(new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 22, 200, 20, this.searchBox, Text.translatable("selectWorld.search")));
        ((SelectWorldScreenAccessor)this).setLevelList(new WorldListWidget(this, this.client, this.width, this.height, 48, this.height - 64, 36, ((SelectWorldScreenAccessor)this).getSearchBox().getText(), ((SelectWorldScreenAccessor)this).getLevelList()));
        this.addDrawableChild(((SelectWorldScreenAccessor)this).getSearchBox());
        this.addDrawableChild(((SelectWorldScreenAccessor)this).getLevelList());
        ButtonWidget selectButton = this.addDrawableChild(new ButtonWidget(this.width / 2 - (150 / 2) - 150 - 5, this.height - 28, 150, 20,  Text.translatable("Select World"), (buttonWidget) -> {
                CustomMapResetter.resetTracker.setCurrentWorld(((WorldListWidget.WorldEntry) Objects.requireNonNull(((SelectWorldScreenAccessor) this).getLevelList().getSelectedOrNull())).level.getName());
            this.client.setScreen(parent);
        }, ButtonWidget.DEFAULT_NARRATION_SUPPLIER));
        ((SelectWorldScreenAccessor)this).setSelectButton(selectButton);
//        if > 16 this.addDrawableChild(button)
        this.addDrawableChild(new ButtonWidget(this.width / 2 + (150 / 2) + 5, this.height - 28, 150, 20, Text.translatable("gui.cancel"), (buttonWidget) -> this.client.setScreen(this.parent), ButtonWidget.DEFAULT_NARRATION_SUPPLIER));
//        if > 16 this.addDrawableChild(button)
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 75, this.height - 28, 150, 20, Text.translatable("Delete Session Worlds"), (buttonWidget) -> {
            CustomMapResetter.resetTracker.deleteWorlds();
            ((SelectWorldScreenAccessor)this).getLevelList().setSearch("");
            ((SelectWorldScreenAccessor) this).getLevelList().load();
        }, ButtonWidget.DEFAULT_NARRATION_SUPPLIER));
        this.worldSelected(false, false);
        this.setInitialFocus(((SelectWorldScreenAccessor)this).getSearchBox());
    }
}
