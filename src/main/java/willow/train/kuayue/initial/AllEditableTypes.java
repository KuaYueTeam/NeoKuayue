package willow.train.kuayue.initial;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.common.extensions.IForgeMenuType;
import willow.train.kuayue.systems.editable_panel.*;
import willow.train.kuayue.block.panels.base.TrainPanelProperties;
import willow.train.kuayue.initial.item.EditablePanelItem;
import willow.train.kuayue.systems.editable_panel.screens.LaqueredScreen;
import willow.train.kuayue.systems.editable_panel.screens.SpeedScreen;
import willow.train.kuayue.systems.editable_panel.screens.TypeScreen;

public class AllEditableTypes {

    public static final PanelColorType C25B_YELLOW2  = EditableTypeConstants
            .signColorRegister("yellow2", AllTags.C25B, EditableTypeConstants.YELLOW2);

    public static final PanelColorType C25G_RED  = EditableTypeConstants
            .signColorRegister("red", AllTags.C25G, EditableTypeConstants.RED);

    public static final PanelColorType C25K_BLUE  = EditableTypeConstants
            .signColorRegister("blue", AllTags.C25K, EditableTypeConstants.BLUE);

    public static final PanelColorType C25Z_BLUE2  = EditableTypeConstants
            .signColorRegister("blue2", AllTags.C25Z, EditableTypeConstants.BLUE2);

    public static final PanelColorType C25T_BLUE3  = EditableTypeConstants
            .signColorRegister("blue3", AllTags.C25T, EditableTypeConstants.BLUE3);

    public static final PanelColorType CM1_BLUE4 = EditableTypeConstants
            .signColorRegister("blue4", AllTags.CM1, EditableTypeConstants.BLUE4);

    public static final PanelColorType FREIGHT_WHITE = EditableTypeConstants
            .signColorRegister("white", AllTags.C_FREIGHT, EditableTypeConstants.WHITE);

    public static final SignType CARRIAGE_TYPE_SIGN = EditableTypeConstants
            .signLambdaRegister("carriage_type_sign",
                    TrainPanelProperties.EditType.TYPE,
                    () -> () -> EditableTypeConstants.CARRIAGE_TYPE_RENDER,
                    () -> EditableTypeConstants.CARRIAGE_TYPE_SIGN_MESSAGES,
                    () -> TypeScreen::new,
                    EditableTypeConstants.CARRIAGE_TYPE_NBT_VALIDATOR);

    public static final SignType CARRIAGE_SPEED_SIGN = EditableTypeConstants
            .signLambdaRegister("carriage_speed_sign",
                    TrainPanelProperties.EditType.SPEED,
                    () -> () -> EditableTypeConstants.TRAIN_SPEED_SIGN,
                    () -> EditableTypeConstants.TRAIN_SPEED_SIGN_MESSAGES,
                    () -> SpeedScreen::new,
                    EditableTypeConstants.TRAIN_SPEED_NBT_VALIDATOR);

    public static final SignType CARRIAGE_NO_SIGN = EditableTypeConstants
            .signLambdaRegister("carriage_no_sign",
                    TrainPanelProperties.EditType.NUM,
                    () -> () -> EditableTypeConstants.CARRIAGE_NO_SIGN,
                    () -> EditableTypeConstants.CARRIAGE_NO_SIGN_MESSAGES,
                    () -> SpeedScreen::new,
                    EditableTypeConstants.CARRIAGE_NO_SIGN_NBT_VALIDATOR);

    /*
    public static final SignType CARRIAGE_NO_SIGN = EditableTypeConstants
            .signLambdaRegister("carriage_no_sign",
                    TrainPanelProperties.EditType.NUM,
                    () -> () -> EditableTypeConstants.CARRIAGE_NO_SIGN,
                    () -> EditableTypeConstants.CARRIAGE_NO_SIGN_MESSAGES,
                    () -> EditableTypeConstants.CARRIAGE_NO_SIGN_METHODS);
                    */

    public static final SignType LAQUERED_BOARD_SIGN = EditableTypeConstants
            .signLambdaRegister("laquered_board",
                    TrainPanelProperties.EditType.LAQUERED,
                    () -> () -> EditableTypeConstants.LAQUERED_BOARD_SIGN,
                    () -> EditableTypeConstants.LAQUERED_BOARD_MESSAGES,
                    () -> LaqueredScreen::new,
                    EditableTypeConstants.LAQUERED_BOARD_NBT_VALIDATOR);
/*
    public static final SignType TRAIN_SPEED_SIGN = EditableTypeConstants
            .signLambdaRegister("train_speed_sign",
                    TrainPanelProperties.EditType.SPEED,
                    () -> () -> EditableTypeConstants.TRAIN_SPEED_SIGN,
                    () -> EditableTypeConstants.TRAIN_SPEED_SIGN_MESSAGES,
                    () -> EditableTypeConstants.TRAIN_SPEED_SIGN_METHODS);


     */
    public static void invoke() {
        EditablePanelItem.invoke();
    }
}
