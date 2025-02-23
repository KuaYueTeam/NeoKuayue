package willow.train.kuayue.initial.create;

import com.simibubi.create.content.trains.bogey.BogeyBlockEntityRenderer;
import kasuga.lib.registrations.common.BlockEntityReg;
import kasuga.lib.registrations.create.BogeyBlockReg;
import kasuga.lib.registrations.create.BogeyGroupReg;
import kasuga.lib.registrations.create.BogeySizeReg;
import kasuga.lib.registrations.registry.CreateRegistry;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import willow.train.kuayue.block.bogey.loco.LocoBogeyBlock;
import willow.train.kuayue.block.bogey.loco.LocoBogeyEntity;
import willow.train.kuayue.block.bogey.loco.MeterLocoBogeyBlock;
import willow.train.kuayue.block.bogey.loco.MeterLocoBogeyEntity;
import willow.train.kuayue.block.bogey.loco.renderer.*;
import willow.train.kuayue.initial.AllElements;

public class AllLocoBogeys {
    public static CreateRegistry testRegistry = AllElements.testRegistry;

    public static final BogeySizeReg df11g = new BogeySizeReg("df11g")
            .size(0.915F / 2F)
            .submit(testRegistry);

    public static final BogeySizeReg df11gBackward = new BogeySizeReg("df11g_backward")
            .size(0.915F / 2F)
            .submit(testRegistry);

    public static final BogeySizeReg qjMain = new BogeySizeReg("qj_main")
            .size(0.915F / 2F)
            .submit(testRegistry);

    public static final BogeySizeReg df21 = new BogeySizeReg("df21")
            .size(0.915F / 2F)
            .submit(testRegistry);

    public static final BogeySizeReg df21Backward = new BogeySizeReg("df21_backward")
            .size(0.915F / 2F)
            .submit(testRegistry);

    public static final BogeySizeReg ss3 = new BogeySizeReg("ss3")
            .size(0.915F / 2F)
            .submit(testRegistry);

    public static final BogeySizeReg ss3Backward = new BogeySizeReg("ss3_backward")
            .size(0.915F / 2F)
            .submit(testRegistry);

    public static final BogeySizeReg hxd3d = new BogeySizeReg("hxd3d")
            .size(0.915F / 2F)
            .submit(testRegistry);

    public static final BogeySizeReg hxd3dBackward = new BogeySizeReg("hxd3d_backward")
            .size(0.915F / 2F)
            .submit(testRegistry);

    public static final BogeySizeReg ss8 = new BogeySizeReg("ss8")
            .size(0.915F / 2F)
            .submit(testRegistry);

    public static final BogeySizeReg ss8Backward = new BogeySizeReg("ss8_backward")
            .size(0.915F / 2F)
            .submit(testRegistry);

    public static final BogeyGroupReg locoBogeyGroup = new BogeyGroupReg("loco", "kuayue_bogey")
            .bogey(df11g.getSize(), DF11GRenderer::new, testRegistry.asResource("df11g_bogey"))
            .bogey(df11gBackward.getSize(), DF11GRenderer.Backward::new, testRegistry.asResource("df11g_backward_bogey"))
            .bogey(qjMain.getSize(), QJMainRenderer::new, testRegistry.asResource("qj_bogey"))
            .bogey(ss3.getSize(), SS3Renderer::new, testRegistry.asResource("ss3_bogey"))
            .bogey(ss3Backward.getSize(), SS3Renderer.Backward::new, testRegistry.asResource("ss3_backward_bogey"))
            .bogey(hxd3d.getSize(), HXD3DRenderer::new, testRegistry.asResource("hxd3d_bogey"))
            .bogey(hxd3dBackward.getSize(), HXD3DRenderer.Backward::new, testRegistry.asResource("hxd3d_backward_bogey"))
            .bogey(ss8.getSize(), SS8Renderer::new, testRegistry.asResource("ss8_bogey"))
            .bogey(ss8Backward.getSize(), SS8Renderer.Backward::new, testRegistry.asResource("ss8_backward_bogey"))
            .translationKey("loco_group")
            .submit(testRegistry);

    public static final BogeyGroupReg meterLocoBogeyGroup = new BogeyGroupReg("meter_loco", "kuayue_bogey_2")
            .bogey(df21.getSize(), DF21Renderer::new, testRegistry.asResource("df21_bogey"))
            .bogey(df21Backward.getSize(), DF21Renderer.Backward::new, testRegistry.asResource("df21_backward_bogey"))
            .translationKey("meter_loco_group")
            .submit(testRegistry);

    public static final BogeyBlockReg<LocoBogeyBlock> df11gBogey =
            new BogeyBlockReg<LocoBogeyBlock>("df11g_bogey")
                    .block(LocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("df11g_bogey")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(df11g)
                    .submit(testRegistry);

    public static final BogeyBlockReg<LocoBogeyBlock> df11gBackwardBogey =
            new BogeyBlockReg<LocoBogeyBlock>("df11g_backward_bogey")
                    .block(LocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("df11g_bogey")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(df11gBackward)
                    .submit(testRegistry);

    public static final BogeyBlockReg<LocoBogeyBlock> qjMainBogey =
            new BogeyBlockReg<LocoBogeyBlock>("qj_bogey")
                    .block(LocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("qj_bogey")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(qjMain)
                    .submit(testRegistry);

    public static final BogeyBlockReg<LocoBogeyBlock> ss3Bogey =
            new BogeyBlockReg<LocoBogeyBlock>("ss3_bogey")
                    .block(LocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("ss3_bogey")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(ss3)
                    .submit(testRegistry);

    public static final BogeyBlockReg<LocoBogeyBlock> ss3BackwardBogey =
            new BogeyBlockReg<LocoBogeyBlock>("ss3_backward_bogey")
                    .block(LocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("ss3_backward_bogey")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(ss3Backward)
                    .submit(testRegistry);

    public static final BogeyBlockReg<LocoBogeyBlock> hxd3dBogey =
            new BogeyBlockReg<LocoBogeyBlock>("hxd3d_bogey")
                    .block(LocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("hxd3d_bogey")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(hxd3d)
                    .submit(testRegistry);

    public static final BogeyBlockReg<LocoBogeyBlock> hxd3dBackwardBogey =
            new BogeyBlockReg<LocoBogeyBlock>("hxd3d_backward_bogey")
                    .block(LocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("hxd3d_backward_bogey")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(hxd3dBackward)
                    .submit(testRegistry);
    public static final BogeyBlockReg<LocoBogeyBlock> ss8Bogey =
            new BogeyBlockReg<LocoBogeyBlock>("ss8_bogey")
                    .block(LocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("ss8_bogey")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(ss8)
                    .submit(testRegistry);

    public static final BogeyBlockReg<LocoBogeyBlock> ss8BackwardBogey =
            new BogeyBlockReg<LocoBogeyBlock>("ss8_backward_bogey")
                    .block(LocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("ss8_backward_bogey")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(ss8Backward)
                    .submit(testRegistry);
    public static final BlockEntityReg<LocoBogeyEntity> locoBogeyEntity =
            new BlockEntityReg<LocoBogeyEntity>("loco_bogey_entity")
                    .blockEntityType(LocoBogeyEntity::new)
                    .addBlock(() -> df11gBogey.getEntry().get())
                    .addBlock(() -> df11gBackwardBogey.getEntry().get())
                    .addBlock(() -> qjMainBogey.getEntry().get())
                    .addBlock(() -> ss3Bogey.getEntry().get())
                    .addBlock(() -> ss3BackwardBogey.getEntry().get())
                    .addBlock(() -> hxd3dBogey.getEntry().get())
                    .addBlock(() -> hxd3dBackwardBogey.getEntry().get())
                    .addBlock(() -> ss8Bogey.getEntry().get())
                    .addBlock(() -> ss8BackwardBogey.getEntry().get())
                    .withRenderer(() -> BogeyBlockEntityRenderer::new)
                    .submit(testRegistry);

    public static final BogeyBlockReg<MeterLocoBogeyBlock> df21Bogey =
            new BogeyBlockReg<MeterLocoBogeyBlock>("df21_bogey")
                    .block(MeterLocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("df21_bogey")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(df21)
                    .submit(testRegistry);

    public static final BogeyBlockReg<MeterLocoBogeyBlock> df21BackwardBogey =
            new BogeyBlockReg<MeterLocoBogeyBlock>("df21_backward_bogey")
                    .block(MeterLocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("df21_backward_bogey")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(df21Backward)
                    .submit(testRegistry);

    public static final BlockEntityReg<MeterLocoBogeyEntity> meterLocoBogeyEntity =
            new BlockEntityReg<MeterLocoBogeyEntity>("meter_loco_bogey_entity")
                    .blockEntityType(MeterLocoBogeyEntity::new)
                    .addBlock(() -> df21Bogey.getEntry().get())
                    .addBlock(() -> df21BackwardBogey.getEntry().get())
                    .withRenderer(() -> BogeyBlockEntityRenderer::new)
                    .submit(testRegistry);

    public static void invoke() {}
}
