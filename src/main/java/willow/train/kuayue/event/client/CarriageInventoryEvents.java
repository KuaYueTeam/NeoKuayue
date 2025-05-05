package willow.train.kuayue.event.client;

import kasuga.lib.core.client.render.texture.ImageMask;
import kasuga.lib.core.util.LazyRecomputable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import willow.train.kuayue.Kuayue;
import willow.train.kuayue.initial.*;
import willow.train.kuayue.initial.panel.*;
import willow.train.kuayue.systems.editable_panel.widget.ImageButton;
import willow.train.kuayue.systems.editable_panel.widget.ItemIconButton;
import willow.train.kuayue.utils.TagsUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class CarriageInventoryEvents {

    private static final int CARRIAGE_TYPE_COUNTS = 10;
    private static final int LOCO_TYPE_COUNTS = 7;
    private static int carriageType = 0;
    private static int locoType = 0;
    ItemIconButton[] imgBtn = new ItemIconButton[CARRIAGE_TYPE_COUNTS];
    ItemIconButton[] locoImgBtn = new ItemIconButton[LOCO_TYPE_COUNTS];
    ImageButton[] upAndDownBtn = new ImageButton[2];
    ImageButton[] locoUpAndDownBtn = new ImageButton[2];
    ArrayList<List<ItemStack>> itemList = new ArrayList<>();
    ArrayList<List<ItemStack>> locoItemList = new ArrayList<>();
    List<ItemStack> schematicItemList = new ArrayList<>();
    int btn_location = 0;
    int loco_btn_location = 0;
    int showBtnNumber = 5;
    int guiLeft = 0, guiTop = 0;
    boolean onChanged = true;
    boolean onLocoChanged = true;
    int bx = 0;
    int loco_bx = 0;

    // String playerName = "";

    // 懒加载向上箭头动态图标
    LazyRecomputable<ImageMask> upRegex = LazyRecomputable.of(() -> {
        try {
            return ClientInit.carriageEventRegexUp.getImage().get().getMask();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    });

    // 懒加载向下箭头动态图标
    LazyRecomputable<ImageMask> downRegex = LazyRecomputable.of(() -> {
        try {
            return ClientInit.carriageEventRegexDown.getImage().get().getMask();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    });

    @SubscribeEvent
    public void onPlayerLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        // 玩家登出时将列车类型置为0
        carriageType = 0;
        locoType = 0;
        this.bx = 0;
        this.loco_bx = 0;
    }

    @SubscribeEvent
    public void onPlayerLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        // playerName = event.getPlayer().getDisplayName().getString();
        // generateSchematicTab();
    }

    @SubscribeEvent
    @SuppressWarnings({"resource"})
    public void onScreenInit(ScreenEvent.Init.Post event) {
        // 若当前screen不是创造模式物品栏，则直接返回。
        if (!(event.getScreen() instanceof CreativeModeInventoryScreen)) return;
        // 获取当前gui的左侧起始位置坐标
        this.guiLeft = ((CreativeModeInventoryScreen) event.getScreen()).getGuiLeft();
        // 获取当前gui的顶部起始位置坐标
        this.guiTop = ((CreativeModeInventoryScreen) event.getScreen()).getGuiTop();
        // 获取当前物品栏菜单对象
        CreativeModeInventoryScreen.ItemPickerMenu menu =
                ((CreativeModeInventoryScreen) event.getScreen()).getMenu();
        // 获取当前物品栏标签对象
        int tab = ((CreativeModeInventoryScreen) event.getScreen()).getSelectedTab();

        if (tab == AllElements.neoKuayueMainTab.getTab().getId()) {
            addSchematicToTab(menu);
        }

        // 定义列车车厢板按钮图标的数据结构
        ItemStack[] icons = new ItemStack[CARRIAGE_TYPE_COUNTS];
        icons[0] = new ItemStack(AllBlocks.CR_LOGO.getBlock());
        icons[1] = new ItemStack(C25BPanel.PANEL_BOTTOM_25B.block.getBlock());
        icons[2] = new ItemStack(C25GPanel.PANEL_BOTTOM_25G.block.getBlock());
        icons[3] = new ItemStack(C25KPanel.PANEL_BOTTOM_25K.block.getBlock());
        icons[4] = new ItemStack(C25ZPanel.PANEL_BOTTOM_LINE_25Z.block.getBlock());
        icons[5] = new ItemStack(AllItems.LOGO_A25T.getItem());
        icons[6] = new ItemStack(C25BPanel.PANEL_SYMBOL_MARSHALLED_25B.block.getBlock());
        icons[7] = new ItemStack(CR200JPanel.PANEL_BOTTOM_MARSHALLED_CR200J.block.getBlock());
        icons[8] = new ItemStack(CM1Panel.BOTTOM_SLAB_M1.block.getBlock());
        icons[9] = new ItemStack(CFreightPanel.FREIGHT_C70_END_FACE.block.getBlock());

        ItemStack[] locoIcons = new ItemStack[LOCO_TYPE_COUNTS];
        locoIcons[0] = new ItemStack(AllBlocks.CR_LOGO.getBlock());
        locoIcons[1] = new ItemStack(I11GPanel.HEAD_DF11G_2.getBlock());
        locoIcons[2] = new ItemStack(I3DPanel.HEAD_HXD3D.getBlock());
        locoIcons[3] = new ItemStack(I21Panel.HEAD_DF21.getBlock());
        locoIcons[4] = new ItemStack(ISS8Panel.SS8_HEAD.getBlock());
        locoIcons[5] = new ItemStack(ISS3Panel.SS3_HEAD.getBlock());
        locoIcons[6] = new ItemStack(I11Panel.DF11_HEAD.getBlock());

        // 定义左侧向上箭头按钮
        upAndDownBtn[0] = new ImageButton(upRegex, this.guiLeft - 22, this.guiTop - 8, 20, 20, Component.empty(),
                b -> {
                    // 按下按钮后触发事件
                    if(btn_location > 0){
                        btn_location --;
                        btn_location = Math.max(btn_location, 0);
                        refreshBtn(tab);
                        onUp();
                    }
                });
        // 定义左侧向下箭头按钮
        upAndDownBtn[1] = new ImageButton(downRegex, this.guiLeft - 22, this.guiTop - 8 + (showBtnNumber + 1) * 22, 20, 20, Component.empty(),
                b -> {
                    if(btn_location < imgBtn.length - showBtnNumber){
                        btn_location++;
                        btn_location = Math.min(btn_location, imgBtn.length - showBtnNumber);
                        refreshBtn(tab);
                        onDown();
                    }
                });

        locoUpAndDownBtn[0] = new ImageButton(upRegex, this.guiLeft - 22, this.guiTop - 8, 20, 20, Component.empty(),
                b -> {
                    // 按下按钮后触发事件
                    if(loco_btn_location > 0){
                        loco_btn_location --;
                        loco_btn_location = Math.max(loco_btn_location, 0);
                        refreshLocoBtn(tab);
                        onLocoUp();
                    }
                });

        locoUpAndDownBtn[1] = new ImageButton(downRegex, this.guiLeft - 22, this.guiTop - 8 + (showBtnNumber + 1) * 22, 20, 20, Component.empty(),
                b -> {
                    if(loco_btn_location < locoImgBtn.length - showBtnNumber){
                        loco_btn_location++;
                        loco_btn_location = Math.min(loco_btn_location, locoImgBtn.length - showBtnNumber);
                        refreshLocoBtn(tab);
                        onLocoDown();
                    }
                });

        // 初始化各车厢板类型的映射
        initMapping();

        // 定义列车车厢板类型信息的数据结构
        Component[] components = new Component[CARRIAGE_TYPE_COUNTS];
        components[0] =
                Component.translatable("container." + Kuayue.MODID + ".inventory.button.all");
        components[1] =
                Component.translatable("container." + Kuayue.MODID + ".inventory.button.25b");
        components[2] =
                Component.translatable("container." + Kuayue.MODID + ".inventory.button.25g");
        components[3] =
                Component.translatable("container." + Kuayue.MODID + ".inventory.button.25k");
        components[4] =
                Component.translatable("container." + Kuayue.MODID + ".inventory.button.25z");
        components[5] =
                Component.translatable("container." + Kuayue.MODID + ".inventory.button.25t");
        components[6] =
                Component.translatable(
                        "container." + Kuayue.MODID + ".inventory.button.marshalled_25_series");
        components[7] =
                Component.translatable("container." + Kuayue.MODID + ".inventory.button.cr200j");
        components[8] =
                Component.translatable("container." + Kuayue.MODID + ".inventory.button.m1");
        components[9] =
                Component.translatable("container." + Kuayue.MODID + ".inventory.button.freight");

        Component[] locoComponents = new Component[LOCO_TYPE_COUNTS];
        locoComponents[0] =
                Component.translatable("container." + Kuayue.MODID + ".inventory.loco.button.all");
        locoComponents[1] =
                Component.translatable("container." + Kuayue.MODID + ".inventory.loco.button.df11g");
        locoComponents[2] =
                Component.translatable("container." + Kuayue.MODID + ".inventory.loco.button.hxd3d");
        locoComponents[3] =
                Component.translatable("container." + Kuayue.MODID + ".inventory.loco.button.df21");
        locoComponents[4] =
                Component.translatable("container." + Kuayue.MODID + ".inventory.loco.button.ss8");
        locoComponents[5] =
                Component.translatable("container." + Kuayue.MODID + ".inventory.loco.button.ss3");
        locoComponents[6] =
                Component.translatable("container." + Kuayue.MODID + ".inventory.loco.button.df11");


        // 定义所有列车车厢板类型按钮
        for (int i = 0; i < imgBtn.length; i++) {
            imgBtn[i] =
                    new ItemIconButton(
                            this.guiLeft - 22,
                            this.guiTop  + 14 + (i - btn_location) * 22,
                            components[i],
                            icons[i],
                            b -> {
                                for (int bx = 0; bx < imgBtn.length; bx++) {
                                    if (b.equals(imgBtn[bx])) {
                                        carriageType = bx;
                                        ((ItemIconButton) b).toggle();
                                        this.bx = bx;
                                        onChanged = true;
                                        menu.scrollTo(0.0f);
                                    } else {
                                        (imgBtn[bx]).reset();
                                    }
                                }
                            },
                            i == 0 ? -2 : 1,
                            i == 0 ? 0 : 2);
        }

        for (int i = 0; i < locoImgBtn.length; i++) {
            locoImgBtn[i] =
                    new ItemIconButton(
                            this.guiLeft - 22,
                            this.guiTop  + 14 + (i - loco_btn_location) * 22,
                            locoComponents[i],
                            locoIcons[i],
                            b -> {
                                for (int loco_bx = 0; loco_bx < locoImgBtn.length; loco_bx++) {
                                    if (b.equals(locoImgBtn[loco_bx])) {
                                        locoType = loco_bx;
                                        ((ItemIconButton) b).toggle();
                                        this.loco_bx = loco_bx;
                                        onLocoChanged = true;
                                        menu.scrollTo(0.0f);
                                    } else {
                                        (locoImgBtn[loco_bx]).reset();
                                    }
                                }
                            },
                            i == 0 ? -2 : 1,
                            i == 0 ? 0 : 2);
        }

        for (ItemIconButton b : imgBtn) {
            // 给列车车厢板类型按钮添加监听器
            event.addListener(b);
        }
        for (ItemIconButton b : locoImgBtn) {
            event.addListener(b);
        }
        // 刷新列车车厢板类型按钮显示状态
        refreshBtn(tab);
        refreshLocoBtn(tab);
        for(ImageButton b : upAndDownBtn) {
            // 给两个箭头按钮添加监听器
            event.addListener(b);
            // 当前标签为跨越列车物品栏时显示上下箭头按钮，反之隐藏。
            b.visible = tab == AllElements.neoKuayueCarriageTab.getTab().getId();
        }
        for (ImageButton b : locoUpAndDownBtn) {
            event.addListener(b);
            b.visible = tab == AllElements.neoKuayueLocoTab.getTab().getId();
        }

        // 默认显示所有的车厢板类型
        imgBtn[0].toggle();
        locoImgBtn[0].toggle();
        // imgBtn[bx].toggle();

        // 更新列车车厢板类型按钮状态与物品栏中物品
        updateButtons();
        // 菜单滚动到顶部
        menu.scrollTo(0);
    }

    // 刷新列车车厢板类型按钮的显示状态
    private void refreshBtn(int tab) {
        int right_edge = btn_location + showBtnNumber - 1;
        for(int i = 0; i < imgBtn.length; i++) {
            if(i < btn_location || i > right_edge) {
                imgBtn[i].visible = false;
            } else {
                imgBtn[i].visible = tab == AllElements.neoKuayueCarriageTab.getTab().getId();
            }
        }
    }

    private void refreshLocoBtn(int tab) {
        int right_edge = loco_btn_location + showBtnNumber - 1;
        for(int i = 0; i < locoImgBtn.length; i++) {
            if(i < loco_btn_location || i > right_edge) {
                locoImgBtn[i].visible = false;
            } else {
                locoImgBtn[i].visible = tab == AllElements.neoKuayueLocoTab.getTab().getId();
            }
        }
    }

    // 点击向上箭头按钮触发
    private void onUp() {
        // 列车车厢板类型按钮整体向上滚动
        for(ItemIconButton b : imgBtn) {
            b.y += 22;
        }
    }

    // 点击向下箭头按钮触发
    private void onDown() {
        // 列车车厢板类型按钮整体向下滚动
        for(ItemIconButton b : imgBtn) {
            b.y -= 22;
        }
    }

    private void onLocoUp() {
        for(ItemIconButton b : locoImgBtn) {
            b.y += 22;
        }
    }

    private void onLocoDown() {
        for(ItemIconButton b : locoImgBtn) {
            b.y -= 22;
        }
    }

    @SubscribeEvent
    public void onRender(ScreenEvent.Render.Pre event) {
        // 捕获screen渲染事件

        // 获取当前事件的screen
        Screen screen = event.getScreen();
        // 若当前screen为创造模式物品栏
        if (screen instanceof CreativeModeInventoryScreen) {
            // 更新列车车厢板类型按钮状态与物品栏中物品
            updateButtons();
            // 获取当前选择的物品栏标签
            int tab = ((CreativeModeInventoryScreen) screen).getSelectedTab();
            // 当选择跨越列车物品栏时显示左侧上下箭头按钮
            for(ImageButton b : upAndDownBtn) {
                b.visible = tab == AllElements.neoKuayueCarriageTab.getTab().getId();
            }
            for(ImageButton b : locoUpAndDownBtn) {
                b.visible = tab == AllElements.neoKuayueLocoTab.getTab().getId();
            }
            // 刷新列车车厢板类型按钮显示状态
            refreshBtn(tab);
            refreshLocoBtn(tab);
            // 若当前物品栏标签为跨越主标签
            if (tab == AllElements.neoKuayueMainTab.getTab().getId()) {
                // 向当前标签的菜单中添加物品列表schematicItemList
                addSchematicToTab(((CreativeModeInventoryScreen) screen).getMenu());
            }
        }
    }

    // 更新列车车厢板类型按钮状态与物品栏中物品
    public void updateButtons() {
        // 当前screen
        Screen screen = Minecraft.getInstance().screen;
        // 当前物品栏标签
        int tab = ((CreativeModeInventoryScreen) screen).getSelectedTab();
        // 物品选择菜单
        CreativeModeInventoryScreen.ItemPickerMenu menu =
                ((CreativeModeInventoryScreen) screen).getMenu();
        // 是否为创造模式物品栏的screen
        if (screen instanceof CreativeModeInventoryScreen) {
            // 是否为跨越列车物品栏
            boolean vis = tab == AllElements.neoKuayueCarriageTab.getTab().getId();
            boolean locoVis = tab == AllElements.neoKuayueLocoTab.getTab().getId();
            // 根据vis的值显示或隐藏列车车厢板类型按钮
            for (ItemIconButton b : imgBtn) {
                b.visible = vis;
            }
            for (ItemIconButton b : locoImgBtn) {
                b.visible = locoVis;
            }
            // 如果vis为True，更新物品栏中物品，并重置滚动条。
            if (vis) {
                if (onChanged) {
                    menu.scrollTo(0.0f);
                    // 更新物品列表
                    updateMenuItem(menu);
                    menu.scrollTo(0.0f);
                    onChanged = false;
                }
            } else if (locoVis) {
                if (onLocoChanged) {
                    menu.scrollTo(0.0f);
                    updateLocoMenuItem(menu);
                    menu.scrollTo(0.0f);
                    onLocoChanged = false;
                }
            } else {
                onChanged = true;
                onLocoChanged = true;
            }
        }
    }

    public void updateMenuItem(CreativeModeInventoryScreen.ItemPickerMenu menu) {
        switch (carriageType) {
            case 1: // B
                menu.items.clear();
                menu.items.addAll(itemList.get(0));
                menu.items.addAll(itemList.get(7));
                menu.items.addAll(itemList.get(8));
                break;
            case 2: // G
                menu.items.clear();
                menu.items.addAll(itemList.get(1));
                menu.items.addAll(itemList.get(7));
                menu.items.addAll(itemList.get(8));
                break;
            case 3: // K
                menu.items.clear();
                menu.items.addAll(itemList.get(2));
                menu.items.addAll(itemList.get(7));
                menu.items.addAll(itemList.get(8));
                break;
            case 4: // Z
                menu.items.clear();
                menu.items.addAll(itemList.get(3));
                menu.items.addAll(itemList.get(7));
                menu.items.addAll(itemList.get(8));
                break;
            case 5: // T
                menu.items.clear();
                menu.items.addAll(itemList.get(4));
                menu.items.addAll(itemList.get(7));
                break;
            case 6: // M
                menu.items.clear();
                menu.items.addAll(itemList.get(5));
                menu.items.addAll(itemList.get(7));
                break;
            case 7: // M
                menu.items.clear();
                menu.items.addAll(itemList.get(6));
                menu.items.addAll(itemList.get(7));
                break;
            case 8: // M1
                menu.items.clear();
                menu.items.addAll(itemList.get(9));
                break;
            case 9: // Freight
                menu.items.clear();
                menu.items.addAll(itemList.get(10));
                break;
            default: // carriageType为0时添加所有类型
                menu.items.clear();
                menu.items.addAll(itemList.get(0)); // B
                menu.items.addAll(itemList.get(1)); // G
                menu.items.addAll(itemList.get(2)); // K
                menu.items.addAll(itemList.get(3)); // Z
                menu.items.addAll(itemList.get(4)); // T
                menu.items.addAll(itemList.get(5)); // M
                menu.items.addAll(itemList.get(6)); // cr200j
                menu.items.addAll(itemList.get(7)); // general
                menu.items.addAll(itemList.get(8)); // bgkzt
                menu.items.addAll(itemList.get(9)); // M1
                menu.items.addAll(itemList.get(10)); // Freight
        }
    }

    public void updateLocoMenuItem(CreativeModeInventoryScreen.ItemPickerMenu menu) {
        switch (locoType) {
            case 1: // DF11G
                menu.items.clear();
                menu.items.addAll(locoItemList.get(0));
                break;
            case 2: // HXD3D
                menu.items.clear();
                menu.items.addAll(locoItemList.get(1));
                break;
            case 3: // DF21
                menu.items.clear();
                menu.items.addAll(locoItemList.get(2));
                break;
            case 4: // SS8
                menu.items.clear();
                menu.items.addAll(locoItemList.get(3));
                break;

            case 5: // SS3
                menu.items.clear();
                menu.items.addAll(locoItemList.get(4));
                break;
          
            case 6: // DF11
                menu.items.clear();
                menu.items.addAll(locoItemList.get(5));
                break;
            default: // locoType为0时添加所有类型
                menu.items.clear();
                menu.items.addAll(locoItemList.get(0)); // DF11G
                menu.items.addAll(locoItemList.get(1)); // HXD3D
                menu.items.addAll(locoItemList.get(2)); // DF21
                menu.items.addAll(locoItemList.get(3)); // SS8
                menu.items.addAll(locoItemList.get(4)); // SS3
                menu.items.addAll(locoItemList.get(5)); // DF11

        }
    }

    public void initMapping() {
        itemList =
                new ArrayList<>() {
                    {
                        add(getListByTag(AllTags.C25B.tag()));  // 25B 0
                        add(getListByTag(AllTags.C25G.tag()));  // 25G 1
                        add(getListByTag(AllTags.C25K.tag()));  // 25K 2
                        add(getListByTag(AllTags.C25Z.tag()));  // 25Z 3
                        add(getListByTag(AllTags.C25T.tag()));  // 25T 4
                        add(getListByTag(AllTags.MARSHALLED.tag()));  // 25 Marshalled Series 5
                        add(getListByTag(AllTags.C200J.tag())); // cr200j 6
                        add(List.of()); // 通用 7
                        add(getListByTag(AllTags.C25BGKZT.tag())); // BGKZT 8
                        add(getListByTag(AllTags.CM1.tag()));   // M1 9
                        add(getListByTag(AllTags.C_FREIGHT.tag())); // Freight 10
                    }
                };

        locoItemList =
                new ArrayList<>() {
                    {
                        add(getListByTag(AllTags.I11G.tag())); // DF11G 0
                        add(getListByTag(AllTags.I3D.tag())); // HXD3D 1
                        add(getListByTag(AllTags.I21.tag())); // DF21 2
                        add(getListByTag(AllTags.ISS8.tag())); // SS8 3
                        add(getListByTag(AllTags.ISS3.tag())); // SS3 4
                        add(getListByTag(AllTags.I11.tag())); // I11 5

                    }
                };
    }

    // 根据给定的tag获取带有该tag的方块的物品栈列表
    public List<ItemStack> getListByTag(TagKey<Block> tag) {
        Set<Block> blockSetByTag = TagsUtil.getBlocksByTag(tag);
        List<ItemStack> itemStackList = new ArrayList<>();
        for (Block block : blockSetByTag) {
            itemStackList.add(block.asItem().getDefaultInstance());
        }
        return itemStackList;
    }

    public void addSchematicToTab(CreativeModeInventoryScreen.ItemPickerMenu menu) {
        if (!schematicItemList.isEmpty()) {
            for (ItemStack stack : schematicItemList) {
                if (!menu.items.contains(stack)) {
                    menu.items.add(stack);
                }
            }
            //menu.scrollTo(1);
        }
    }

    public void generateSchematicTab() {
        try {
            // TODO: for next version (0.4)
            // MegumiKasuga 24/02/16
            /*
            Map<String, byte[]> streamMap = SchematicInjectionInit.schematicMap;

            for (String name : streamMap.keySet()) {
                if(name.contains("25k")) {
                    schematicItemList.add(
                            SchematicItemUtil.create(SchematicItemInit.SCHEMATIC_25K.asStack(), name, ""));
                    continue;
                }
                if(name.contains("25g")) {
                    schematicItemList.add(
                            SchematicItemUtil.create(SchematicItemInit.SCHEMATIC_25G.asStack(), name, ""));
                    continue;
                }
                if(name.contains("25b")) {
                    schematicItemList.add(
                            SchematicItemUtil.create(SchematicItemInit.SCHEMATIC_25B.asStack(), name, ""));
                    continue;
                }
                if(name.contains("25z")) {
                    schematicItemList.add(
                            SchematicItemUtil.create(SchematicItemInit.SCHEMATIC_25Z.asStack(), name, ""));
                    continue;
                }
                if(name.contains("25t")) {
                    schematicItemList.add(
                            SchematicItemUtil.create(SchematicItemInit.SCHEMATIC_25T.asStack(), name, ""));
                    continue;
                }
                schematicItemList.add(SchematicItem.create(name, ""));
            }

             */
        } catch (Exception e) {
            // Kuayue.LOGGER.error("FATAL ERROR IN LOADING KUAYUE SCHEMATICS ", e);
        }
    }
}
