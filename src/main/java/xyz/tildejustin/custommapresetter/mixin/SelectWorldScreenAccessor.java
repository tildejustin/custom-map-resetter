package xyz.tildejustin.custommapresetter.mixin;

import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SelectWorldScreen.class)
public interface SelectWorldScreenAccessor {
    @Accessor
    ButtonWidget getDeleteButton();

    @Accessor
    void setDeleteButton(ButtonWidget deleteButton);

    @Accessor
    ButtonWidget getSelectButton();

    @Accessor
    void setSelectButton(ButtonWidget selectButton);

    @Accessor
    ButtonWidget getEditButton();

    @Accessor
    void setEditButton(ButtonWidget editButton);

    @Accessor
    ButtonWidget getRecreateButton();

    @Accessor
    void setRecreateButton(ButtonWidget recreateButton);

    @Accessor
    TextFieldWidget getSearchBox();

    @Accessor
    void setSearchBox(TextFieldWidget searchBox);

    @Accessor
    WorldListWidget getLevelList();

    @Accessor
    void setLevelList(WorldListWidget levelList);
}
