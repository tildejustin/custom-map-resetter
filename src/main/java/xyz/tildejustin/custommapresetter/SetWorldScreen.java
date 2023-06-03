package xyz.tildejustin.custommapresetter;

import net.minecraft.client.class_2847;
import net.minecraft.client.class_2848;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import xyz.tildejustin.custommapresetter.mixin.class2847Accessor;

public class SetWorldScreen extends SelectWorldScreen {
    protected Screen parent;
    protected TextFieldWidget field_20497;
    private class_2848 field_13358;


    public SetWorldScreen(Screen parent) {
        super(parent);
        this.parent = parent;
    }

    @Override
    public boolean mouseScrolled(double d) {
        return this.field_13358.mouseScrolled(d);
    }

    @Override
    public void tick() {
        this.field_20497.tick();
    }

    @Override
    public void init() {
        this.client.field_19946.method_18191(true);
        this.field_20497.method_18387((integer, string) -> this.field_13358.method_18898(() -> string, false));
        this.field_13358 = new class_2848(this, this.client, this.width, this.height, 48, this.height - 64, 36, () -> this.field_20497.getText(), this.field_13358);
        this.field_20497 = new TextFieldWidget(0, this.textRenderer, this.width / 2 - 100, 22, 200, 20, this.field_20497) {

            @Override
            public void setFocused(boolean focused) {
                super.setFocused(true);
            }
        };
        ButtonWidget selectButton = new ButtonWidget(1, this.width / 2 - 154, this.height - 28, 150, 20, "Select World") {
            @Override
            public void method_18374(double e, double f) {
                class2847Accessor accessor;
                try (class_2847 entry = field_13358.method_12216()) {
                    accessor = (class2847Accessor) entry;
                }
                CustomMapResetter.resetTracker.setCurrentWorld(accessor.getLevelSummary().getFileName());
                client.setScreen(parent);
            }
        };
        selectButton.active = false;
        this.buttons.add(new ButtonWidget(0, this.width / 2 + 5, this.height - 28, 150, 20, I18n.translate("gui.cancel")) {
            @Override
            public void method_18374(double e, double f) {
                CustomMapResetter.resetTracker.setCurrentWorld(null);
                client.setScreen(parent);
            }
        });
        this.buttons.add(selectButton);
        this.buttons.add(new ButtonWidget(6, this.width / 2 + 5 + 150 + 5, this.height - 28, 150, 20, "Delete Session Worlds") {
            @Override
            public void method_18374(double e, double f) {
                CustomMapResetter.resetTracker.deleteWorlds();
            }
        });
    }
}
