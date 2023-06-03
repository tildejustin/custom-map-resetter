package xyz.tildejustin.custommapresetter.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.tildejustin.custommapresetter.CustomMapResetter;
import xyz.tildejustin.custommapresetter.SetWorldScreen;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
//    TODO: make it a wool block
    private static final Identifier BUTTON_IMAGE = new Identifier("textures/items/diamond_boots.png");

    @Inject(method = "init", at = @At(value = "HEAD"))
    private void custommapresetter$loadnext(CallbackInfo ci) {
        if (CustomMapResetter.running && !CustomMapResetter.loading) {
            CustomMapResetter.tryLoadNewWorld();
        }
    }

    @Inject(method = "init", at = @At(value = "TAIL"))
    private void custommapresetter$addTitleScreenButton(CallbackInfo ci) {
        this.buttons.add(new ButtonWidget(13, this.width / 2 - 124, this.height / 4 + 48, 20, 20, ""));
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;render(IIF)V"))
    private void custommapresetter$goldBootsOverlay(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        this.client.getTextureManager().bindTexture(this.client.getTextureManager().method_5846(Item.DIAMOND_BOOTS.method_5463()));
        this.drawTexture(this.width / 2 - 124 + 2, this.height / 4 + 48 + 2, 0, 0, 16, 16);    }

    @Inject(method = "buttonClicked", at = @At("HEAD"), cancellable = true)
    public void buttonClicked(ButtonWidget button, CallbackInfo ci) {
        if (button.id == 13) {
            if (Screen.hasShiftDown()) {
                this.client.setScreen(new SetWorldScreen(this.client.currentScreen));
            } else {
                CustomMapResetter.tryLoadNewWorld();
            }
            ci.cancel();
        }

    }
}
