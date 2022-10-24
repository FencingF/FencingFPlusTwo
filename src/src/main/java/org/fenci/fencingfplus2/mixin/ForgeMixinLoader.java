package org.fenci.fencingfplus2.mixin;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.Name("ForgeMixinLoader") //made by perry
@IFMLLoadingPlugin.MCVersion("1.12.2")
public class ForgeMixinLoader implements IFMLLoadingPlugin {
    public ForgeMixinLoader() {
        MixinBootstrap.init();
        MixinEnvironment.getCurrentEnvironment().setObfuscationContext("searge");
        MixinEnvironment.getCurrentEnvironment().setSide(MixinEnvironment.Side.CLIENT);
        Mixins.addConfiguration("mixins.fencingf.json");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}