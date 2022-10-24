package org.fenci.fencingfplus2.mixin.mixins.render;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.fenci.fencingfplus2.features.module.modules.render.ExtraTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin({GuiPlayerTabOverlay.class})
public class MixinGuiPlayerTabOverlay extends Gui {
    @Redirect(method = {"renderPlayerlist"}, at = @At(value = "INVOKE", target = "Ljava/util/List;subList(II)Ljava/util/List;", remap = false))
    public List<NetworkPlayerInfo> subListHook(final List<NetworkPlayerInfo> list, final int fromIndex, final int toIndex) {
        return list.subList(fromIndex, ExtraTab.getInstance().isOn() ? Math.min(ExtraTab.players.getValue(), list.size()) : toIndex);
    }

    @Inject(method = {"getPlayerName"}, at = {@At("HEAD")}, cancellable = true)
    public void getPlayerNameHook(final NetworkPlayerInfo networkPlayerInfoIn, final CallbackInfoReturnable<String> info) {
        if (ExtraTab.getInstance().isOn()) {
            info.setReturnValue(ExtraTab.getPlayerName(networkPlayerInfoIn));
        }
    }
}