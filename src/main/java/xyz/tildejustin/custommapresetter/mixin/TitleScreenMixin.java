package xyz.tildejustin.custommapresetter.mixin;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
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
        this.buttons.add(new ButtonWidget(13, this.width / 2 - 124, this.height / 4 + 48, 20, 20, "") {
            @Override
            public void method_18374(double d, double e) {
                if (Screen.hasShiftDown()) {
                    client.setScreen(new SetWorldScreen(client.currentScreen));
                } else {
                    CustomMapResetter.tryLoadNewWorld();
                }
            }
        });
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void custommapresetter$goldBootsOverlay(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        this.client.getTextureManager().bindTexture(BUTTON_IMAGE);
        DrawableHelper.drawTexture(this.width / 2 - 124 + 2, this.height / 4 + 48 + 2, 0.0F, 0.0F, 16, 16, 16, 16);
    }
}
