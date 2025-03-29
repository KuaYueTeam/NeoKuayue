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
import willow.train.kuayue.block.bogey.loco.AndesiteLocoBogeyEntity;
import willow.train.kuayue.block.bogey.loco.AndesiteLocoBogeyBlock;
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

    public static final BogeySizeReg ss8Andesite = new BogeySizeReg("ss8_a")
            .size(0.915F / 2F)
            .submit(testRegistry);

    public static final BogeySizeReg ss8BackwardAndesite = new BogeySizeReg("ss8_backward_a")
            .size(0.915F / 2F)
            .submit(testRegistry);
    public static final BogeySizeReg ss3Andesite = new BogeySizeReg("ss3_a")
            .size(0.915F / 2F)
            .submit(testRegistry);

    public static final BogeySizeReg ss3BackwardAndesite = new BogeySizeReg("ss3_backward_a")
            .size(0.915F / 2F)
            .submit(testRegistry);


    public static final BogeySizeReg hxd3dAndesite = new BogeySizeReg("hxd3d_a")
            .size(0.915F / 2F)
            .submit(testRegistry);

    public static final BogeySizeReg hxd3dBackwardAndesite = new BogeySizeReg("hxd3d_backward_a")
            .size(0.915F / 2F)
            .submit(testRegistry);


    public static final BogeySizeReg df11gAndesite = new BogeySizeReg("df11g_a")
            .size(0.915F / 2F)
            .submit(testRegistry);

    public static final BogeySizeReg df11gBackwardAndesite = new BogeySizeReg("df11g_backward_a")
            .size(0.915F / 2F)
            .submit(testRegistry);

    public static final BogeySizeReg qjMainAndesite = new BogeySizeReg("qj_main_a")
            .size(0.915F / 2F)
            .submit(testRegistry);

    public static final BogeySizeReg dfh21 = new BogeySizeReg("dfh21")
            .size(0.915F / 2F)
            .submit(testRegistry);
    public static final BogeySizeReg dfh21Backward = new BogeySizeReg("dfh21_backward")
            .size(0.915F / 2F)
            .submit(testRegistry);

    public static final BogeySizeReg dfh21Standard = new BogeySizeReg("dfh21_s")
            .size(0.915F / 2F)
            .submit(testRegistry);

    public static final BogeySizeReg dfh21BackwardStandard = new BogeySizeReg("dfh21_backward_s")
            .size(0.915F / 2F)
            .submit(testRegistry);

    public static final BogeySizeReg dfh21Andesite = new BogeySizeReg("dfh21_a")
            .size(0.915F / 2F)
            .submit(testRegistry);

    public static final BogeySizeReg dfh21BackwardAndesite = new BogeySizeReg("dfh21_backward_a")
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
            .bogey(dfh21Standard.getSize(), DFH21Renderer.Standard::new, testRegistry.asResource("dfh21_bogey_s"))
            .bogey(dfh21BackwardStandard.getSize(), DFH21Renderer.Standard.Backward::new, testRegistry.asResource("dfh21_backward_bogey_s"))
            .translationKey("loco_group")
            .submit(testRegistry);

    public static final BogeyGroupReg meterLocoBogeyGroup = new BogeyGroupReg("meter_loco", "kuayue_bogey_2")
            .bogey(df21.getSize(), DF21Renderer::new, testRegistry.asResource("df21_bogey"))
            .bogey(df21Backward.getSize(), DF21Renderer.Backward::new, testRegistry.asResource("df21_backward_bogey"))
            .bogey(dfh21.getSize(), DFH21Renderer::new, testRegistry.asResource("dfh21_bogey"))
            .bogey(dfh21Backward.getSize(), DFH21Renderer.Backward::new, testRegistry.asResource("dfh21_backward_bogey"))
            .translationKey("meter_loco_group")
            .submit(testRegistry);

    public static final BogeyGroupReg andesiteLocoBogeyGroup = new BogeyGroupReg("andesite_loco", "standard")
            .bogey(ss8Andesite.getSize(), SS8Renderer.Andesite::new, testRegistry.asResource("ss8_bogey_a"))
            .bogey(ss8BackwardAndesite.getSize(), SS8Renderer.Andesite.Backward::new, testRegistry.asResource("ss8_backward_bogey_a"))
            .bogey(ss3Andesite.getSize(), SS3Renderer.Andesite::new, testRegistry.asResource("ss3_bogey_a"))
            .bogey(ss3BackwardAndesite.getSize(), SS3Renderer.Andesite.Backward::new, testRegistry.asResource("ss3_backward_bogey_a"))
            .bogey(df11gAndesite.getSize(), DF11GRenderer.Andesite::new, testRegistry.asResource("df11g_bogey_a"))
            .bogey(df11gBackwardAndesite.getSize(), DF11GRenderer.Andesite.Backward::new, testRegistry.asResource("df11g_backward_bogey_a"))
            .bogey(qjMainAndesite.getSize(), QJMainRenderer.Andesite::new, testRegistry.asResource("qj_bogey_a"))
            .bogey(hxd3dAndesite.getSize(), HXD3DRenderer.Andesite::new, testRegistry.asResource("hxd3d_bogey_a"))
            .bogey(hxd3dBackwardAndesite.getSize(), HXD3DRenderer.Andesite.Backward::new, testRegistry.asResource("hxd3d_backward_bogey_a"))
            .bogey(dfh21Andesite.getSize(), DFH21Renderer.Andesite::new, testRegistry.asResource("dfh21_bogey_a"))
            .bogey(dfh21BackwardAndesite.getSize(), DFH21Renderer.Andesite.Backward::new, testRegistry.asResource("dfh21_backward_bogey_a"))
            .translationKey("andesite_loco_group")
            .submit(AllElements.createRegistry);
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

    public static final BogeyBlockReg<LocoBogeyBlock> dfh21BogeyStandard =
            new BogeyBlockReg<LocoBogeyBlock>("dfh21_bogey_s")
                    .block(LocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("dfh21_bogey_s")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(dfh21Standard)
                    .submit(testRegistry);

    public static final BogeyBlockReg<LocoBogeyBlock> dfh21BackwardBogeyStandard =
            new BogeyBlockReg<LocoBogeyBlock>("dfh21_backward_bogey_s")
                    .block(LocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("dfh21_backward_bogey_s")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(dfh21BackwardStandard)
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
                    .addBlock(() -> dfh21BogeyStandard.getEntry().get())
                    .addBlock(() -> dfh21BackwardBogeyStandard.getEntry().get())
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

    public static final BogeyBlockReg<MeterLocoBogeyBlock> dfh21Bogey =
            new BogeyBlockReg<MeterLocoBogeyBlock>("dfh21_bogey")
                    .block(MeterLocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("dfh21_bogey")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(dfh21)
                    .submit(testRegistry);

    public static final BogeyBlockReg<MeterLocoBogeyBlock> dfh21BackwardBogey =
            new BogeyBlockReg<MeterLocoBogeyBlock>("dfh21_backward_bogey")
                    .block(MeterLocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("dfh21_backward_bogey")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(dfh21Backward)
                    .submit(testRegistry);

    public static final BlockEntityReg<MeterLocoBogeyEntity> meterLocoBogeyEntity =
            new BlockEntityReg<MeterLocoBogeyEntity>("meter_loco_bogey_entity")
                    .blockEntityType(MeterLocoBogeyEntity::new)
                    .addBlock(() -> df21Bogey.getEntry().get())
                    .addBlock(() -> df21BackwardBogey.getEntry().get())
                    .addBlock(() -> dfh21Bogey.getEntry().get())
                    .addBlock(() -> dfh21BackwardBogey.getEntry().get())
                    .withRenderer(() -> BogeyBlockEntityRenderer::new)
                    .submit(testRegistry);

    public static final BogeyBlockReg<AndesiteLocoBogeyBlock> ss8BogeyAndesite =
            new BogeyBlockReg<AndesiteLocoBogeyBlock>("ss8_bogey_a")
                    .block(AndesiteLocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("ss8_bogey_a")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(ss8Andesite)
                    .submit(testRegistry);

    public static final BogeyBlockReg<AndesiteLocoBogeyBlock> ss8BackwardBogeyAndesite =
            new BogeyBlockReg<AndesiteLocoBogeyBlock>("ss8_backward_bogey_a")
                    .block(AndesiteLocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("ss8_backward_bogey_a")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(ss8BackwardAndesite)
                    .submit(testRegistry);

    public static final BogeyBlockReg<AndesiteLocoBogeyBlock> ss3BogeyAndesite =
            new BogeyBlockReg<AndesiteLocoBogeyBlock>("ss3_bogey_a")
                    .block(AndesiteLocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("ss3_bogey_a")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(ss3Andesite)
                    .submit(testRegistry);

    public static final BogeyBlockReg<AndesiteLocoBogeyBlock> ss3BackwardBogeyAndesite =
            new BogeyBlockReg<AndesiteLocoBogeyBlock>("ss3_backward_bogey_a")
                    .block(AndesiteLocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("ss3_backward_bogey_a")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(ss3BackwardAndesite)
                    .submit(testRegistry);

    public static final BogeyBlockReg<AndesiteLocoBogeyBlock> hxd3dBogeyAndesite =
            new BogeyBlockReg<AndesiteLocoBogeyBlock>("hxd3d_bogey_a")
                    .block(AndesiteLocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("hxd3d_bogey_a")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(hxd3dAndesite)
                    .submit(testRegistry);

    public static final BogeyBlockReg<AndesiteLocoBogeyBlock> hxd3dBackwardBogeyAndesite =
            new BogeyBlockReg<AndesiteLocoBogeyBlock>("hxd3d_backward_bogey_a")
                    .block(AndesiteLocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("hxd3d_backward_bogey_a")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(hxd3dBackwardAndesite)
                    .submit(testRegistry);

    public static final BogeyBlockReg<AndesiteLocoBogeyBlock> df11gBogeyAndesite =
            new BogeyBlockReg<AndesiteLocoBogeyBlock>("df11g_bogey_a")
                    .block(AndesiteLocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("df11g_bogey_a")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(df11gAndesite)
                    .submit(testRegistry);

    public static final BogeyBlockReg<AndesiteLocoBogeyBlock> df11gBackwardBogeyAndesite =
            new BogeyBlockReg<AndesiteLocoBogeyBlock>("df11g_backward_bogey_a")
                    .block(AndesiteLocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("df11g_bogey_a")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(df11gBackwardAndesite)
                    .submit(testRegistry);

    public static final BogeyBlockReg<AndesiteLocoBogeyBlock> qjMainBogeyAndesite =
            new BogeyBlockReg<AndesiteLocoBogeyBlock>("qj_bogey_a")
                    .block(AndesiteLocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("qj_bogey_a")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(qjMainAndesite)
                    .submit(testRegistry);

    public static final BogeyBlockReg<AndesiteLocoBogeyBlock> dfh21BogeyAndesite =
            new BogeyBlockReg<AndesiteLocoBogeyBlock>("dfh21_bogey_a")
                    .block(AndesiteLocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("dfh21_bogey_a")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(dfh21Andesite)
                    .submit(testRegistry);

    public static final BogeyBlockReg<AndesiteLocoBogeyBlock> dfh21BackwardBogeyAndesite =
            new BogeyBlockReg<AndesiteLocoBogeyBlock>("dfh21_backward_bogey_a")
                    .block(AndesiteLocoBogeyBlock::new)
                    .material(Material.METAL)
                    .materialColor(MaterialColor.PODZOL)
                    .translationKey("dfh21_backward_bogey_a")
                    .property(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .property(properties -> properties.strength(2.0f, 3.0f))
                    .size(dfh21BackwardAndesite)
                    .submit(testRegistry);
    public static final BlockEntityReg<AndesiteLocoBogeyEntity> andesiteLocoBogeyEntity =
            new BlockEntityReg<AndesiteLocoBogeyEntity>("andesite_loco_bogey_entity")
                    .blockEntityType(AndesiteLocoBogeyEntity::new)
                    .addBlock(() -> ss8BogeyAndesite.getEntry().get())
                    .addBlock(() -> ss8BackwardBogeyAndesite.getEntry().get())
                    .addBlock(() -> ss3BogeyAndesite.getEntry().get())
                    .addBlock(() -> ss3BackwardBogeyAndesite.getEntry().get())
                    .addBlock(() -> hxd3dBogeyAndesite.getEntry().get())
                    .addBlock(() -> hxd3dBackwardBogeyAndesite.getEntry().get())
                    .addBlock(() -> df11gBogeyAndesite.getEntry().get())
                    .addBlock(() -> df11gBackwardBogeyAndesite.getEntry().get())
                    .addBlock(() -> qjMainBogeyAndesite.getEntry().get())
                    .addBlock(() -> dfh21BogeyAndesite.getEntry().get())
                    .addBlock(() -> dfh21BackwardBogeyAndesite.getEntry().get())
                    .withRenderer(() -> BogeyBlockEntityRenderer::new)
                    .submit(testRegistry);

    public static void invoke() {}
}
