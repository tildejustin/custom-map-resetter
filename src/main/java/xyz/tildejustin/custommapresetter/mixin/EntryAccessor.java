package xyz.tildejustin.custommapresetter.mixin;

import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldListWidget.Entry.class)
public interface EntryAccessor {
    @Accessor
    LevelSummary getLevel();
}
