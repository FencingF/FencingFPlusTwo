package org.fenci.fencingfplus2.features.module.modules.client;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.fenci.fencingfplus2.FencingFPlus2;
import org.fenci.fencingfplus2.events.client.OptionChangeEvent;
import org.fenci.fencingfplus2.features.module.Module;
import org.fenci.fencingfplus2.setting.Setting;

public class CustomFont extends Module {

    public static CustomFont INSTANCE;

    public CustomFont() {
        super("CustomFont", "Allows you to change the font of the client.", Category.Client);
        INSTANCE = this;
    }

    public static final Setting<Fonts> font = new Setting<>("Font", Fonts.Tahoma);
    public static final Setting<Integer> size = new Setting<>("Size", 16, 0, 30);

    @Override
    public void onEnable() {
//        StringBuilder builder = new StringBuilder();
//        for (String font : FencingFPlus2.INSTANCE.fontManager.fonts) {
//            //get the name of the font and if there are any spaces in the name, replace them with underscores
//            String enumName = font.replace(" ", "_").replace("-", "_");
//            builder.append(enumName).append("(").append("\"").append(font).append("\"").append("), ");
//        }
//        StringSelection stringSelection = new StringSelection(builder.toString());
//        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//        clipboard.setContents(stringSelection, null);
    }

    @SubscribeEvent
    public void onOption(OptionChangeEvent event) {
        if (event.getOption().equals(font) || event.getOption().equals(size)) {
            FencingFPlus2.INSTANCE.fontManager.setFont(font.getValue().getName());
            FencingFPlus2.INSTANCE.fontManager.setFontSize(size.getValue());
        }
    }

    public enum Fonts {
        Arial("Arial"), Arial_Black("Arial Black"), Bahnschrift("Bahnschrift"), Calibri("Calibri"), Calibri_Light("Calibri Light"), Cambria("Cambria"), Cambria_Math("Cambria Math"), Candara("Candara"), Candara_Light("Candara Light"), Cascadia_Code("Cascadia Code"), Cascadia_Mono("Cascadia Mono"), Comic_Sans_MS("Comic Sans MS"), Consolas("Consolas"), Constantia("Constantia"), Corbel("Corbel"), Corbel_Light("Corbel Light"), Courier_New("Courier New"), Dialog("Dialog"), DialogInput("DialogInput"), Ebrima("Ebrima"), Franklin_Gothic_Medium("Franklin Gothic Medium"), Gabriola("Gabriola"), Gadugi("Gadugi"), Georgia("Georgia"), HoloLens_MDL2_Assets("HoloLens MDL2 Assets"), Impact("Impact"), Ink_Free("Ink Free"), Javanese_Text("Javanese Text"), Leelawadee_UI("Leelawadee UI"), Leelawadee_UI_Semilight("Leelawadee UI Semilight"), Lucida_Console("Lucida Console"), Lucida_Sans_Unicode("Lucida Sans Unicode"), Malgun_Gothic("Malgun Gothic"), Malgun_Gothic_Semilight("Malgun Gothic Semilight"), Marlett("Marlett"), Microsoft_Himalaya("Microsoft Himalaya"), Microsoft_JhengHei("Microsoft JhengHei"), Microsoft_JhengHei_Light("Microsoft JhengHei Light"), Microsoft_JhengHei_UI("Microsoft JhengHei UI"), Microsoft_JhengHei_UI_Light("Microsoft JhengHei UI Light"), Microsoft_New_Tai_Lue("Microsoft New Tai Lue"), Microsoft_PhagsPa("Microsoft PhagsPa"), Microsoft_Sans_Serif("Microsoft Sans Serif"), Microsoft_Tai_Le("Microsoft Tai Le"), Microsoft_YaHei("Microsoft YaHei"), Microsoft_YaHei_Light("Microsoft YaHei Light"), Microsoft_YaHei_UI("Microsoft YaHei UI"), Microsoft_YaHei_UI_Light("Microsoft YaHei UI Light"), Microsoft_Yi_Baiti("Microsoft Yi Baiti"), MingLiU_ExtB("MingLiU-ExtB"), MingLiU_HKSCS_ExtB("MingLiU_HKSCS-ExtB"), Mongolian_Baiti("Mongolian Baiti"), Monospaced("Monospaced"), MS_Gothic("MS Gothic"), MS_PGothic("MS PGothic"), MS_UI_Gothic("MS UI Gothic"), MV_Boli("MV Boli"), Myanmar_Text("Myanmar Text"), Nirmala_UI("Nirmala UI"), Nirmala_UI_Semilight("Nirmala UI Semilight"), NSimSun("NSimSun"), Palatino_Linotype("Palatino Linotype"), PMingLiU_ExtB("PMingLiU-ExtB"), SansSerif("SansSerif"), Segoe_MDL2_Assets("Segoe MDL2 Assets"), Segoe_Print("Segoe Print"), Segoe_Script("Segoe Script"), Segoe_UI("Segoe UI"), Segoe_UI_Black("Segoe UI Black"), Segoe_UI_Emoji("Segoe UI Emoji"), Segoe_UI_Historic("Segoe UI Historic"), Segoe_UI_Light("Segoe UI Light"), Segoe_UI_Semibold("Segoe UI Semibold"), Segoe_UI_Semilight("Segoe UI Semilight"), Segoe_UI_Symbol("Segoe UI Symbol"), Serif("Serif"), SimSun("SimSun"), SimSun_ExtB("SimSun-ExtB"), Sitka_Banner("Sitka Banner"), Sitka_Display("Sitka Display"), Sitka_Heading("Sitka Heading"), Sitka_Small("Sitka Small"), Sitka_Subheading("Sitka Subheading"), Sitka_Text("Sitka Text"), Sylfaen("Sylfaen"), Symbol("Symbol"), Tahoma("Tahoma"), Times_New_Roman("Times New Roman"), Trebuchet_MS("Trebuchet MS"), Verdana("Verdana"), Webdings("Webdings"), Wingdings("Wingdings"), Yu_Gothic("Yu Gothic"), Yu_Gothic_Light("Yu Gothic Light"), Yu_Gothic_Medium("Yu Gothic Medium"), Yu_Gothic_UI("Yu Gothic UI"), Yu_Gothic_UI_Light("Yu Gothic UI Light"), Yu_Gothic_UI_Semibold("Yu Gothic UI Semibold"), Yu_Gothic_UI_Semilight("Yu Gothic UI Semilight");

        private final String name;

        Fonts(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
