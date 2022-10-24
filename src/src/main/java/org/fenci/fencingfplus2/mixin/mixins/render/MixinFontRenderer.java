package org.fenci.fencingfplus2.mixin.mixins.render;

import net.minecraft.client.gui.FontRenderer;
import org.fenci.fencingfplus2.features.commands.commands.NickCommand;
import org.fenci.fencingfplus2.features.module.modules.misc.NickHider;
import org.fenci.fencingfplus2.util.Globals;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = {FontRenderer.class})
public abstract class MixinFontRenderer implements Globals {

    @Shadow
    protected abstract void renderStringAtPos(String var1, boolean var2);

    @Redirect(method = {"renderString(Ljava/lang/String;FFIZ)I"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;renderStringAtPos(Ljava/lang/String;Z)V"))
    public void renderStringAtPosHook(FontRenderer renderer, String text, boolean shadow) {
        if (NickHider.getInstance().isOn()) {
            if (!text.equals("FencingF+2"))
                this.renderStringAtPos(text.replace(mc.getSession().getUsername(), (getName())), shadow);
        } else {
            this.renderStringAtPos(text, shadow);
        }
    }

    public String getName() {
        if (NickCommand.newName == null) {
            return "You";
        } else {
            return NickCommand.newName;
        }
    }
}