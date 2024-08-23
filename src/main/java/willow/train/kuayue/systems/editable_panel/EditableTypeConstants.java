package willow.train.kuayue.systems.editable_panel;

import kasuga.lib.registrations.common.BlockTagReg;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import willow.train.kuayue.block.panels.base.TrainPanelProperties;
import willow.train.kuayue.initial.AllTags;
import willow.train.kuayue.initial.item.EditablePanelItem;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class EditableTypeConstants {

    public static final Integer
            YELLOW = 16776961,
            YELLOW2 = 16776960,
            RED = 15216648,
            BLUE = 22220,
            BLUE2 = 0x60A0B0,
            BLUE3 = 468326,
            BLACK = 789516;

    public static final SignRenderLambda CARRIAGE_TYPE_RENDER = (blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay) -> {
        System.out.println("车厢类型渲染方法");
    };

    public static final SignRenderLambda CARRIAGE_NO_SIGN = (blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay) -> {
        System.out.println("车厢编号渲染方法");
    };

    public static final SignRenderLambda LAQUERED_BOARD_SIGN = (blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay) -> {
        System.out.println("水牌渲染方法");
    };

    public static final SignRenderLambda TRAIN_SPEED_SIGN = (blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay) -> {
        System.out.println("速度等级渲染方法");
    };

    public static final DefaultTextsLambda CARRIAGE_TYPE_SIGN_MESSAGES = new DefaultTextsLambda() {
        @Override
        public CompoundTag defaultTextComponent(BlockEntity blockEntity, BlockState blockState, CompoundTag nbt) {
            Component[] messages =
                new Component[] {
                        Component.literal("XXX"),
                        Component.literal("XXXXXX"),
                        Component.literal("XX"),
                        Component.literal("XXX"),
                        Component.literal("XXXXXX")
                };
            return new CompoundTag();
        }
    };

    public static final DefaultTextsLambda CARRIAGE_NO_SIGN_MESSAGES = new DefaultTextsLambda() {
        @Override
        public CompoundTag defaultTextComponent(BlockEntity blockEntity, BlockState blockState, CompoundTag nbt) {
            Component message = Component.literal("0");
            return new CompoundTag();
        }
    };

    public static final DefaultTextsLambda LAQUERED_BOARD_MESSAGES = new DefaultTextsLambda() {
        @Override
        public CompoundTag defaultTextComponent(BlockEntity blockEntity, BlockState blockState, CompoundTag nbt) {
            Component[] messages =
                new Component[] {
                        Component.empty(),
                        Component.empty(),
                        Component.empty(),
                        Component.empty(),
                        Component.empty(),
                        Component.empty(),
                        Component.empty(),
                        Component.empty(),
                        Component.empty(),
                        Component.empty(),
                        Component.empty(),
                        Component.empty(),
                        Component.empty(),
                        Component.empty(),
                        Component.empty(),
                        Component.empty()
                };
            return new CompoundTag();
        }
    };

    public static final DefaultTextsLambda TRAIN_SPEED_SIGN_MESSAGES = new DefaultTextsLambda() {
        @Override
        public CompoundTag defaultTextComponent(BlockEntity blockEntity, BlockState blockState, CompoundTag nbt) {
            Component content = Component.literal("xx km/h");
            return new CompoundTag();
        }
    };

    private static final Map<ResourceLocation, PanelColorType> signColorMap = new HashMap<>();
    private static final Map<ResourceLocation, SignType> signTypeMap = new HashMap<>();

    public static Map<ResourceLocation, PanelColorType> getSignColorMap() {
        return signColorMap;
    }

    public static Map<ResourceLocation, SignType> getSignTypeMap() {
        return signTypeMap;
    }

    public static int getColorByTag(BlockState state) {
        for (PanelColorType colorType : EditableTypeConstants.getSignColorMap().values()) {
            if (state.is(Objects.requireNonNull(colorType.blockTag.tag())))
                return colorType.signColor;
        }
        return YELLOW;
    }

    // 通过tag获取PanelColorType对象
    public static @Nullable PanelColorType getColorTypeByTag(BlockState state) {
        // 遍历所有PanelColorType类对象，每个对象中均包含车厢板类型（blockTag）- 标识颜色（signColor）的映射关系。
        for (PanelColorType colorType : EditableTypeConstants.getSignColorMap().values()) {
            // 当传入的blockstate中包含当前循环的PanelColorType类对象中的blockTag时，直接返回包含对应标识颜色的PanelColorType类对象。
            if (state.is(Objects.requireNonNull(colorType.blockTag.tag())))
                return colorType;
        }
        return null;
    }

    public static PanelColorType signColorRegister(String locationKey, BlockTagReg blockTag, Integer signColor) {

        PanelColorType panelColorType = new PanelColorType(new ResourceLocation(locationKey), blockTag, signColor);

        EditableTypeConstants.getSignColorMap()
                .put(new ResourceLocation("color_map_" + locationKey), panelColorType);

        return panelColorType;
    }

    public static SignType signLambdaRegister(String locationKey,
                                              TrainPanelProperties.EditType editType,
                                              Supplier<Supplier<SignRenderLambda>> supplier,
                                              Supplier<DefaultTextsLambda> defaultTextSupplier) {

        SignType signType = new SignType(locationKey, editType, supplier, defaultTextSupplier);

        EditableTypeConstants.getSignTypeMap().put(new ResourceLocation(locationKey), signType);

        return signType;
    }

    public static SignType getSignTypeByKey(ResourceLocation location) {
        return EditableTypeConstants.getSignTypeMap().get(location);
    }

    // 根据自定义类型返回对应包含自定义类型（EditType）- 默认字段lambda映射关系的SignType对象
    public static SignType getSignTypeByEditType(TrainPanelProperties.EditType type) {
        // 遍历所有SignType类对象，当传入的EditType等于当前循环中SignType类对象的EditType时，返回该SignType对象。
        for (SignType signType : EditableTypeConstants.getSignTypeMap().values()) {
            if (signType.shouldRender(type))
                return signType;
        }
        return null;
    }

    // 根据手持的物品与车厢板类型返回自定义类型
    public static TrainPanelProperties.EditType getPanelEditType (BlockState state, Player player, InteractionHand hand) {
        if (player.getItemInHand(hand).is(EditablePanelItem.COLORED_BRUSH.getItem())) {
            if (state.is(Objects.requireNonNull(AllTags.BOTTOM_PANEL.tag())))
                return TrainPanelProperties.EditType.TYPE;
            if (state.is(Objects.requireNonNull(AllTags.FLOOR.tag())))
                return TrainPanelProperties.EditType.SPEED;
        }
        if (player.getItemInHand(hand).is(EditablePanelItem.LAQUERED_BOARD.getItem()) && state.is(Objects.requireNonNull(AllTags.BOTTOM_PANEL.tag()))) {
            return TrainPanelProperties.EditType.LAQUERED;
        }
        if (player.getItemInHand(hand).is(EditablePanelItem.STICKER.getItem()) && state.is(Objects.requireNonNull(AllTags.UPPER_PANEL.tag()))) {
            return TrainPanelProperties.EditType.NUM;
        }
        return TrainPanelProperties.EditType.NONE;
    }
}
