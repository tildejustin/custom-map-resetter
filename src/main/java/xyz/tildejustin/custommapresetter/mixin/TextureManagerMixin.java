package xyz.tildejustin.custommapresetter.mixin;

import net.minecraft.client.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TextureManager.class)
public interface TextureManagerMixin {
    @Invoker
    int callGetTextureFromPath(String path);

    @Invoker
    void callBindTexture(int idx);
}
