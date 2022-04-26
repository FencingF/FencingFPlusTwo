package org.fenci.fencingfplus2.util.client;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class MathUtil {

    public static float[] calcAngle(Vec3d from, Vec3d to) {
        double difX = to.x - from.x;
        double difY = (to.y - from.y) * -1.0;
        double difZ = to.z - from.z;
        double dist = MathHelper.sqrt(difX * difX + difZ * difZ);
        return new float[]{(float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0), (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist)))};
    }

    public static double square(double numberToSquare) {
        return numberToSquare * numberToSquare;
    }

    public static float clamp(float val, final float min, final float max) {
        if (val <= min) {
            val = min;
        }
        if (val >= max) {
            val = max;
        }
        return val;
    }

    //credit Drachir000 on Grepper
    public static boolean isInt(String str) {
        try {
            @SuppressWarnings("unused")
            int x = Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }
}
