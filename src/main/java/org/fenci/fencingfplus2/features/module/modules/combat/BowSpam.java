package org.fenci.fencingfplus2.features.module.modules.combat;

import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.math.BlockPos;
import org.fenci.fencingfplus2.features.module.Category;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;
import org.fenci.fencingfplus2.util.Globals;

public class BowSpam extends Module {
    public static final Setting<Integer> ticks = new Setting<>("Ticks", 3, 0, 20);

    public BowSpam() {
        super("BowSpam", "Automatically releases your bow for you", Category.Combat);
    }

    @Override
    public void onUpdate() {
        if (Globals.mc.player.getHeldItemMainhand().getItem() == Items.BOW && Globals.mc.player.isHandActive() && Globals.mc.player.getItemInUseMaxCount() > ticks.getValue()) {
            Globals.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Globals.mc.player.getHorizontalFacing()));
            Globals.mc.player.stopActiveHand();
        }
    }
}
