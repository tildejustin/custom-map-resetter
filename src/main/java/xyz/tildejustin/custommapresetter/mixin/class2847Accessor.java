package xyz.tildejustin.custommapresetter.mixin;

import net.minecraft.client.class_2847;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(class_2847.class)
public interface class2847Accessor {
    @Accessor("field_13365")
    LevelSummary getLevelSummary();
}
