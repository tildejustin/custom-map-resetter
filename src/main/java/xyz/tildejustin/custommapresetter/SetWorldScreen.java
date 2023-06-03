package xyz.tildejustin.custommapresetter;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import xyz.tildejustin.custommapresetter.mixin.SelectWorldScreenAccessor;


public class SetWorldScreen extends SelectWorldScreen {
    protected final Screen parent;

    public SetWorldScreen(Screen parent) {
        super(parent);
        this.parent = parent;
    }

    @Override
    public void init() {
        ((SelectWorldScreenAccessor)this).setDeleteButton(new ButtonWidget(0, 0, 0, 0, "", (buttonWidget) -> {}));
        ((SelectWorldScreenAccessor)this).setEditButton(new ButtonWidget(0, 0, 0, 0, "", (buttonWidget) -> {}));
        ((SelectWorldScreenAccessor)this).setRecreateButton(new ButtonWidget(0, 0, 0, 0, "", (buttonWidget) -> {}));


        this.minecraft.keyboard.enableRepeatEvents(true);
        ((SelectWorldScreenAccessor)this).setSearchBox(new TextFieldWidget(this.font, this.width / 2 - 100, 22, 200, 20, this.searchBox, I18n.translate("selectWorld.search")));
        ((SelectWorldScreenAccessor)this).getSearchBox().setChangedListener((string) -> {
            ((SelectWorldScreenAccessor)this).getLevelList().filter(() -> string, false);
        });
        ((SelectWorldScreenAccessor)this).setLevelList(new WorldListWidget(this, this.minecraft, this.width, this.height, 48, this.height - 64, 36, () -> ((SelectWorldScreenAccessor)this).getSearchBox().getText(), ((SelectWorldScreenAccessor)this).getLevelList()));
        this.children.add(((SelectWorldScreenAccessor)this).getSearchBox());
        this.children.add(((SelectWorldScreenAccessor)this).getLevelList());
        ButtonWidget selectButton = this.addButton(new ButtonWidget(this.width / 2 - (150 / 2) - 150 - 5, this.height - 28, 150, 20, I18n.translate("selectWorld.select"), (buttonWidget) -> {
            CustomMapResetter.resetTracker.setCurrentWorld(((SelectWorldScreenAccessor)this).getLevelList().method_20159().get().level.getName());
            this.minecraft.openScreen(parent);
        }));
        ((SelectWorldScreenAccessor)this).setSelectButton(selectButton);
        this.addButton(new ButtonWidget(this.width / 2 + (150 / 2) + 5, this.height - 28, 150, 20, I18n.translate("gui.cancel"), (buttonWidget) -> {
            this.minecraft.openScreen(this.parent);
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 75, this.height - 28, 150, 20, "Delete Session Worlds", (buttonWidget) -> {
            CustomMapResetter.resetTracker.deleteWorlds();
            ((SelectWorldScreenAccessor)this).getLevelList().filter(() -> "", false);
            ((SelectWorldScreenAccessor) this).getLevelList().filter(() -> ((SelectWorldScreenAccessor)this).getSearchBox().getText(), true);
        }));
        this.worldSelected(false);
        this.setInitialFocus(((SelectWorldScreenAccessor)this).getSearchBox());
    }
}
