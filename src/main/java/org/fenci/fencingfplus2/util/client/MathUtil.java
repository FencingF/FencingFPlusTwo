package org.fenci.fencingfplus2.util.client;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class MathUtil {

    public static float[] calcAngle(Vec3d from, Vec3d to) {
        double difX = to.x - from.x;
        double difY = (to.y - from.y) * -1.0;
        double difZ = to.z - from.z;
        double dist = MathHelper.sqrt(difX * difX + difZ * difZ);
        return new float[]{(float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0), (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist)))};
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

    public static long getRealLifeTimeInSeconds() {
      int hour =  LocalTime.now().getHour();
      int minute = LocalTime.now().getMinute();
      int second = LocalTime.now().getSecond();
      return (hour*60*60) + (minute*60) + second;
    }

    public static long getAdjustedRealTimeForMinecraft(long time) {
        return time - 21600;
    }

//    public static long toMinecraftDays(long seconds) {
//        return seconds * 72 / 60 / 60 / 24;
//    }
//
//    public static long toMinecraftHours(long seconds) {
//        return seconds * 72 / 60 / 60;
//    }
//
//    public static long toMinecraftMinutes(long seconds) {
//        return seconds * 72 / 60;
//    }
//
//    public static long toMinecraftSeconds(long seconds) {
//        return seconds * 72;
//    }
}
