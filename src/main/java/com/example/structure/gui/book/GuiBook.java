package com.example.structure.gui.book;

import com.example.structure.init.ModItems;
import com.example.structure.util.ModReference;
import com.example.structure.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class GuiBook extends GuiScreen {
    protected static final int X = 276;
    protected static final int Y = 180;
    private int currentPage;
    private int prevPage = -1;
    private final int totalPages = 27;
    private static final ResourceLocation GUI_BASE_BOOK_BACKGROUND = new ResourceLocation(ModReference.MOD_ID + ":textures/gui/book_default.png");
    private static final ResourceLocation GUI_INDEX_BUTTON = new ResourceLocation(ModReference.MOD_ID + ":textures/gui/button.png");
    private static final ResourceLocation GUI_INDEX_BUTTON_2 = new ResourceLocation(ModReference.MOD_ID + ":textures/gui/button_2.png");
    private static final ResourceLocation GUI_INDEX_BUTTON_3 = new ResourceLocation(ModReference.MOD_ID + ":textures/gui/button_3.png");
    private static final ResourceLocation GUI_INDEX_BUTTON_4 = new ResourceLocation(ModReference.MOD_ID + ":textures/gui/button_4.png");
    private static final ResourceLocation GUI_INDEX_BUTTON_5 = new ResourceLocation(ModReference.MOD_ID + ":textures/gui/button_5.png");
    private static final ResourceLocation MOB_PICTURES = new ResourceLocation( "ee:textures/gui/cards.png");
    private static final ResourceLocation BLOCK_PICTURES = new ResourceLocation("ee:textures/gui/block_cards.png");
    private static final ResourceLocation BLOCK_PICTURES_2 = new ResourceLocation("ee:textures/gui/block_cards_2.png");
    private static final ResourceLocation ENDFALL_PICTURES = new ResourceLocation("ee:textures/gui/block_cards_3.png");

    private static final ResourceLocation BLOCK_PICTURES_4 = new ResourceLocation("ee:textures/gui/block_cards_4.png");

    private static final ResourceLocation BLOCK_PICTURES_5 = new ResourceLocation("ee:textures/gui/block_cards_5.png");

    //Used for unlocking parts of the book
    public boolean hasLamentedIslands = false;
    public boolean hasEndResources = false;
    public boolean hasAshWastelands = false;
    public boolean hasEndGameGear = false;
    public boolean hasEndKingFortress = false;
    ItemStack book;
    GuiButton exitButton;
    private NextPageButton buttonNextPage;
    private NextPageButton buttonPrevPage;
    private IndexButtonTest indexButtons;
    private IndexButtonTest2 indexButtons2;
    private IndexButtonTest3 indexButtons3;
    private IndexButtonTest4 indexButtons4;
    private IndexButtonTest5 indexButtons5;


    GuiBook self;
    private float time = 0;
    EntityPlayer player;

    public GuiBook(ItemStack book, EntityPlayer player) {
        this.book = book;
        this.player = player;
        self = this;

    }

    public void initGui() {
        super.initGui();
        this.exitButton = this.addButton(new GuiButton(0, (this.width ) / 2 - 49, 196, 98, 20, I18n.format("gui.done")));
        int t = (this.width - X) / 2;
        this.buttonNextPage = this.addButton(new NextPageButton(1, t + 240, 154, true));
        this.buttonPrevPage = this.addButton(new NextPageButton(2, t + 12, 154, false));
        this.indexButtons = this.addButton(new IndexButtonTest(3, t + 151, 18, 1)); //End Resources and Creatures
        this.indexButtons2 = this.addButton(new IndexButtonTest2(4, t + 151, 38, 5)); // Lamented Islands
        this.indexButtons3 = this.addButton(new IndexButtonTest3(5, t + 151, 58, 8)); // Ash Wastelands
        this.indexButtons4 = this.addButton(new IndexButtonTest4(6, t + 151, 78, 19)); //End King's Fortress
        this.indexButtons5 = this.addButton(new IndexButtonTest5(7, t + 151, 98, 23)); //End Game Gear

        this.updateButtons();
    }


    protected void drawGuiContainerForegroundLayer(int x, int y) {

        for (GuiButton guibutton : this.buttonList) {
            if (guibutton.isMouseOver()) {
                guibutton.drawButtonForegroundLayer(x - this.width, y - this.height);
                break;
            }
        }
    }


    public void drawScreen(int mouse_x, int mouse_y, float p_ticks){
        this.drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(GUI_BASE_BOOK_BACKGROUND);
        int cornerX = (this.width - X) / 2;
        int cornerY = (this.height - Y) / 2;
        drawModalRectWithCustomSizedTexture((this.width - X) / 2, 2, 0, 0, 276, 180,  288, 224);
        this.drawPages(this.currentPage);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();

        if (this.prevPage != this.currentPage) {
            //this.cachedComponents = Lists.newArrayList(textcomponentstring);
            this.prevPage = this.currentPage;
        }
        super.drawScreen(mouse_x, mouse_y, p_ticks);
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate(cornerX, cornerY, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        short short1 = 240;
        short short2 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, short1, short2);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        this.drawGuiContainerForegroundLayer(mouse_x, mouse_y);
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();

    }

    private void updateButtons() {
        this.buttonNextPage.visible = this.currentPage < this.totalPages - 1;
        this.buttonPrevPage.visible = this.currentPage > 0;
        this.indexButtons.visible = this.currentPage == 0;
        this.indexButtons2.visible = this.currentPage == 0;
        this.indexButtons3.visible = this.currentPage == 0;
        this.indexButtons4.visible = this.currentPage == 0;
        this.indexButtons5.visible = this.currentPage == 0;
    }


    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            if (button.id == 0)
            {
                this.mc.displayGuiScreen(null);
            }
            else if (button.id == 1)
            {
                if (this.currentPage < this.totalPages - 1)
                {
                    ++this.currentPage;
                }
            }
            else if (button.id == 2)
            {
                if (this.currentPage > 0)
                {
                    --this.currentPage;
                }
            } else if(button.id == 3) {
                IndexButtonTest button1 = (IndexButtonTest) button;
                this.currentPage = button1.getPage();
            } else if(button.id == 4) {
                IndexButtonTest2 button2 = (IndexButtonTest2) button;
                this.currentPage = button2.getPage();
            } else if(button.id == 5) {
                IndexButtonTest3 button2 = (IndexButtonTest3) button;
                this.currentPage = button2.getPage();
            } else if(button.id == 6) {
                IndexButtonTest4 button2 = (IndexButtonTest4) button;
                this.currentPage = button2.getPage();
            } else if(button.id == 7) {
                IndexButtonTest5 button2 = (IndexButtonTest5) button;
                this.currentPage = button2.getPage();
            }
            this.updateButtons();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private void drawPages(int pages) {
        switch (pages) {
            case 0:
            default:
                //IntroDuction + Index
                GlStateManager.pushMatrix();
                int t = (this.width - X) / 2;
                this.writeLeftFromString(0, 11, "desc.intro_");
                this.drawItemStack(new ItemStack(ModItems.LAMENTED_EYE), (this.width - 75 * 2) / 2, 14);
                GlStateManager.popMatrix();
                break;
            case 1:
                //Pure Crystals
                GlStateManager.pushMatrix();
                this.mc.renderEngine.bindTexture(BLOCK_PICTURES);
                drawModalRectWithCustomSizedTexture((this.width -245) / 2, 13, 0, 406, 115, 58, 115, 464);
                this.mc.renderEngine.bindTexture(BLOCK_PICTURES_2);
                drawModalRectWithCustomSizedTexture((this.width + 17) / 2, 13, 0, 58, 115, 58, 115, 464);
                this.createLeftPictureTitleFromString("title.pure_cry_0");
                this.createRightPictureTitleFromString("title.pure_cry_1");
                this.writeLeftFromString(6, 12, "desc.pure_cry_");
                this.writeRightFromString(6, 9, "desc.cont_cry_");
                GlStateManager.popMatrix();
                break;

            case 2:
                //End Vaults + End Seeker
                GlStateManager.pushMatrix();
                if(ModUtils.getAdvancementCompletion(player, "kill_seeker")) {
                    this.mc.renderEngine.bindTexture(BLOCK_PICTURES_4);
                    drawModalRectWithCustomSizedTexture((this.width + 17) / 2, 13, 0, 0, 115, 58, 115, 464);
                    this.createRightPictureTitleFromString("entity.end_seeker.name");
                    this.writeRightFromString(6, 12, "desc.seeker_");
                } else {
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width + 55 * 2) / 2, 14);
                    this.createLockedEntryRight("title.locked_entry");
                    this.createRightPictureTitleFromString("entity.end_seeker.name");
                }

                this.createLeftTitleFromString("title.vaults_0");
                this.writeLeftFromString(0, 8, "desc.vaults_");
                GlStateManager.popMatrix();
                break;

            case 3:
                //End Compulsor + Ancient Guilder
                GlStateManager.pushMatrix();
                if(ModUtils.getAdvancementCompletion(player, "craft_compulsor")) {
                    this.mc.renderEngine.bindTexture(BLOCK_PICTURES_2);
                    drawModalRectWithCustomSizedTexture((this.width - 245) / 2, 13, 0, 232, 115, 58, 115, 464);
                    this.createLeftPictureTitleFromString("title.compulsor_0");
                    this.writeLeftFromString(6, 12, "block.cmp_");
                } else {
                    this.createLeftPictureTitleFromString("title.compulsor_0");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width - 75 * 2) / 2, 14);
                    this.createLockedEntryLeft("title.locked_entry");
                }
                if (ModUtils.getAdvancementCompletion(player, "kill_guilder")) {
                    this.mc.renderEngine.bindTexture(MOB_PICTURES);
                    drawModalRectWithCustomSizedTexture((this.width + 17) / 2, 13, 0, 406, 115, 58, 115, 464);
                    this.createRightPictureTitleFromString("entity.controller.name");
                    this.writeRightFromString(6, 12, "desc.guilder_");
                } else {
                    this.createRightPictureTitleFromString("entity.controller.name");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width + 55 * 2) / 2, 14);
                    this.createLockedEntryRight("title.locked_entry");
                }

                GlStateManager.popMatrix();
                break;

            case 4:
                //Conversion Charts
                GlStateManager.pushMatrix();
                if(ModUtils.getAdvancementCompletion(player, "kill_guilder")) {
                    this.mc.renderEngine.bindTexture(BLOCK_PICTURES_5);
                    drawModalRectWithCustomSizedTexture((this.width + 17) / 2, 13, 0, 232, 115, 58, 115, 464);
                    drawModalRectWithCustomSizedTexture((this.width - 245) / 2, 13, 0, 290, 115, 58, 115, 464);
                    drawModalRectWithCustomSizedTexture((this.width + 17) / 2, 91, 0, 348, 115, 58, 115, 464);
                    this.createLeftPictureTitleFromString("title.chart_1");
                    this.createRightPictureTitleFromString("title.chart_1_cont");
                    this.writeLeftFromString(6, 12, "desc.chart_");
                } else {
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width + 55 * 2) / 2, 14);
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width - 75 * 2) / 2, 14);
                    this.createLeftPictureTitleFromString("title.chart_1");
                    this.createRightPictureTitleFromString("title.chart_1_cont");
                    this.createLockedEntryRight("title.locked_entry");
                    this.createLockedEntryLeft("title.locked_entry");
                }

                GlStateManager.popMatrix();
                break;

            case 5:
                //Lamented Islands + Constructor
                GlStateManager.pushMatrix();
                if(ModUtils.getAdvancementCompletion(player, "kill_constructor")) {
                    this.mc.renderEngine.bindTexture(MOB_PICTURES);
                    drawModalRectWithCustomSizedTexture((this.width + 17) / 2, 13,0, 0, 115, 58, 115, 464);
                    this.createRightPictureTitleFromString("entity.buffker.name");
                    this.writeRightFromString(6, 13, "desc.cons_");
                } else {
                    this.createLockedEntryRight("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width + 55 * 2) / 2, 14);
                    this.createRightPictureTitleFromString("entity.buffker.name");
                }

                this.createLeftTitleFromString("title.lame_0");
                this.writeLeftFromString(0, 11, "desc.lame_");
          GlStateManager.popMatrix();
                break;

            case 6:
                //The Lamentor
                GlStateManager.pushMatrix();
                if(ModUtils.getAdvancementCompletion(player, "kill_seeker_prime") || ModUtils.getAdvancementCompletion(player, "kill_lamentor")) {
                    this.mc.renderEngine.bindTexture(MOB_PICTURES);
                    drawModalRectWithCustomSizedTexture((this.width - 245) / 2, 13,0, 58, 115, 58, 115, 464);
                    this.createLeftPictureTitleFromString("entity.crystal_boss.name");
                    this.writeLeftFromString(6, 13, "desc.cboss_");
                    this.createRightTitleFromString("title.cboss_0");
                    this.writeRightFromString(0, 13, "skill.cboss_");
                } else {
                    this.createLeftPictureTitleFromString("entity.crystal_boss.name");
                    this.createRightPictureTitleFromString("title.cboss_0");
                    this.writeLeftFromString(6, 13, "desc.cboss_");
                    this.createLockedEntryRight("title.locked_entry");
                    this.createLockedEntryLeft("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width + 55 * 2) / 2, 14);
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width - 75 * 2) / 2, 14);
                }

                GlStateManager.popMatrix();
                break;

            case 7:
                //Lamented Eye Trinket + End Compulsor
                GlStateManager.pushMatrix();
                if(ModUtils.getAdvancementCompletion(player, "kill_lamentor")) {
                    this.mc.renderEngine.bindTexture(BLOCK_PICTURES_2);
                    drawModalRectWithCustomSizedTexture((this.width - 245) / 2, 13, 0, 116, 115, 58 ,115 , 464);
                    this.writeLeftFromString(6, 11, "skill.mdl_");
                    this.createLeftPictureTitleFromString("title.lame_eye_0");
                } else {
                    this.createLeftPictureTitleFromString("title.lame_eye_0");
                    this.createLockedEntryLeft("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width + 55 * 2) / 2, 14);
                }

                if(ModUtils.getAdvancementCompletion(player, "kill_guilder")) {
                    this.mc.renderEngine.bindTexture(BLOCK_PICTURES_2);
                    drawModalRectWithCustomSizedTexture((this.width + 17) / 2, 13, 0, 174, 115, 58, 115, 464);
                    this.createRightPictureTitleFromString("title.shield_0");
                    this.writeRightFromString(6, 13, "skill.shield_");
                } else {
                    this.createRightPictureTitleFromString("title.shield_0");
                    this.createLockedEntryRight("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width - 75 * 2) / 2, 14);
                }
                GlStateManager.popMatrix();
                break;
            case 8:
                //Ash Wastelands Biome + End Ash + Ash Brick
                GlStateManager.pushMatrix();
                if(ModUtils.getAdvancementCompletion(player, "kill_lamentor")) {
                    this.mc.renderEngine.bindTexture(BLOCK_PICTURES);
                    drawModalRectWithCustomSizedTexture((this.width + 17) / 2, 13,0, 0, 115, 58, 115, 464);
                    this.createRightPictureTitleFromString("tile.ash_brick.name");
                    this.writeRightFromString(6, 12, "desc.ash_block_");
                } else {
                    this.createRightPictureTitleFromString("tile.ash_brick.name");
                    this.createLockedEntryRight("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width + 55 * 2) / 2, 14);
                }

                this.createLeftTitleFromString("title.biome_0");
                this.writeLeftFromString(0, 13, "desc.ash_");
                GlStateManager.popMatrix();
                break;
            case 9:
                //Unholy Crystals + Red Lamp
                GlStateManager.pushMatrix();
                if(ModUtils.getAdvancementCompletion(player, "kill_lamentor")) {
                    this.mc.renderEngine.bindTexture(BLOCK_PICTURES);
                    drawModalRectWithCustomSizedTexture((this.width - 245) / 2, 13,0, 348, 115, 58, 115, 464);
                    this.mc.renderEngine.bindTexture(BLOCK_PICTURES_2);
                    drawModalRectWithCustomSizedTexture((this.width + 17) / 2, 13, 0, 0, 115, 58, 115, 464);
                    this.createLeftPictureTitleFromString("item.red_crystal_item.name");
                    this.createRightPictureTitleFromString("title.cont_red");
                    this.writeLeftFromString(6, 12, "desc.red_");
                    this.writeRightFromString(6, 9, "desc.cont_red_");
                } else {
                    this.createLeftPictureTitleFromString("item.red_crystal_item.name");
                    this.createRightPictureTitleFromString("title.cont_red");
                    this.createLockedEntryRight("title.locked_entry");
                    this.createLockedEntryLeft("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width - 75 * 2) / 2, 14);
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width + 55 * 2) / 2, 14);
                }
                GlStateManager.popMatrix();
                break;
            case 10:
                //Door KeyHole + Door Creator
                GlStateManager.pushMatrix();
                if(ModUtils.getAdvancementCompletion(player, "kill_lamentor")) {
                    this.mc.renderEngine.bindTexture(BLOCK_PICTURES);
                    drawModalRectWithCustomSizedTexture((this.width -245) / 2, 13,0, 174, 115, 58, 115, 464);
                    drawModalRectWithCustomSizedTexture((this.width + 17) / 2, 13,0, 290, 115, 58, 115, 464);
                    this.createLeftPictureTitleFromString("tile.ash_door.name");
                    this.writeLeftFromString(6, 13, "desc.hole_");
                    this.createRightPictureTitleFromString("tile.end_door.name");
                    this.writeRightFromString(6, 10, "desc.crea_");
                } else {
                    this.createRightPictureTitleFromString("tile.end_door.name");
                    this.createLeftPictureTitleFromString("tile.ash_door.name");
                    this.createLockedEntryRight("title.locked_entry");
                    this.createLockedEntryLeft("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width - 75 * 2) / 2, 14);
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width + 55 * 2) / 2, 14);
                }

                GlStateManager.popMatrix();
                break;
            case 11:
                //Ash Trap + Start of Ash Items
                GlStateManager.pushMatrix();
                if(ModUtils.getAdvancementCompletion(player, "kill_lamentor")) {
                    this.mc.renderEngine.bindTexture(BLOCK_PICTURES);
                    drawModalRectWithCustomSizedTexture((this.width - 245) / 2, 13,0, 232, 115, 58, 115, 464);
                    drawModalRectWithCustomSizedTexture((this.width + 17) / 2, 13,0, 116, 115, 58, 115, 464);
                    this.createLeftPictureTitleFromString("tile.ash_trap_floor.name");
                    this.writeLeftFromString(6, 12, "desc.trap_");
                    this.createRightPictureTitleFromString("tile.red_lamp.name");
                    this.writeRightFromString(6, 7, "desc.lamp_");
                } else {
                    this.createLeftPictureTitleFromString("tile.ash_trap_floor.name");
                    this.createRightPictureTitleFromString("tile.red_lamp.name");
                    this.createLockedEntryRight("title.locked_entry");
                    this.createLockedEntryLeft("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width - 75 * 2) / 2, 14);
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width + 55 * 2) / 2, 14);
                }
                GlStateManager.popMatrix();
                break;
            case 12:
                //Brick Rods + Amber Ore
                GlStateManager.pushMatrix();
                if(ModUtils.getAdvancementCompletion(player, "kill_lamentor")) {
                    this.mc.renderEngine.bindTexture(BLOCK_PICTURES_5);
                    drawModalRectWithCustomSizedTexture((this.width - 245) / 2, 13,0, 116, 115, 58, 115, 464);
                    this.mc.renderEngine.bindTexture(BLOCK_PICTURES_4);
                    drawModalRectWithCustomSizedTexture((this.width + 17) / 2, 13,0, 116, 115, 58, 115, 464);
                    this.createLeftPictureTitleFromString("item.brick_rod.name");
                    this.createRightPictureTitleFromString("tile.amber_ore.name");
                    this.writeLeftFromString(6, 8, "desc.brick_rod_");
                    this.writeRightFromString(6, 11, "desc.amber_ore_");
                } else {
                    this.createLeftPictureTitleFromString("item.brick_rod.name");
                    this.createRightPictureTitleFromString("tile.amber_ore.name");
                    this.createLockedEntryRight("title.locked_entry");
                    this.createLockedEntryLeft("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width - 75 * 2) / 2, 14);
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width + 55 * 2) / 2, 14);
                }
                GlStateManager.popMatrix();
                break;
            case 13:
                //Amber Torches + Amber Pickaxe
                GlStateManager.pushMatrix();
                if(ModUtils.getAdvancementCompletion(player, "find_cordium")) {
                    this.mc.renderEngine.bindTexture(BLOCK_PICTURES_4);
                    drawModalRectWithCustomSizedTexture((this.width - 245) / 2, 13,0, 174, 115, 58, 115, 464);
                    this.mc.renderEngine.bindTexture(BLOCK_PICTURES_5);
                    drawModalRectWithCustomSizedTexture((this.width + 17) / 2, 13,0, 174, 115, 58, 115, 464);
                    this.createLeftPictureTitleFromString("tile.amber_torch.name");
                    this.createRightPictureTitleFromString("item.amber_pickaxe.name");
                    this.writeLeftFromString(6, 12, "desc.amber_torch_");
                    this.writeRightFromString(6, 10, "desc.amber_pickaxe_");
                } else {
                    this.createLeftPictureTitleFromString("tile.amber_torch.name");
                    this.createRightPictureTitleFromString("item.amber_pickaxe.name");
                    this.createLockedEntryRight("title.locked_entry");
                    this.createLockedEntryLeft("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width - 75 * 2) / 2, 14);
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width + 55 * 2) / 2, 14);
                }
                GlStateManager.popMatrix();
                break;
            case 14:
                //Amber Axe + Amber Dagger
                GlStateManager.pushMatrix();
                if(ModUtils.getAdvancementCompletion(player, "find_cordium")) {
                    this.mc.renderEngine.bindTexture(BLOCK_PICTURES_4);
                    drawModalRectWithCustomSizedTexture((this.width - 245) / 2, 13,0, 232, 115, 58, 115, 464);
                    drawModalRectWithCustomSizedTexture((this.width + 17) / 2, 13,0, 290, 115, 58, 115, 464);
                    this.createLeftPictureTitleFromString("item.amber_axe.name");
                    this.createRightPictureTitleFromString("item.amber_dagger.name");
                    this.writeLeftFromString(6, 12, "desc.amber_axe_");
                    this.writeRightFromString(6, 11, "desc.amber_dagger_");
                } else {
                    this.createLeftPictureTitleFromString("item.amber_axe.name");
                    this.createRightPictureTitleFromString("item.amber_dagger.name");
                    this.createLockedEntryRight("title.locked_entry");
                    this.createLockedEntryLeft("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width - 75 * 2) / 2, 14);
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width + 55 * 2) / 2, 14);
                }

                GlStateManager.popMatrix();
                break;

            case 15:
                //Infused Crystals + Infused Pickaxe
                GlStateManager.pushMatrix();
                if(ModUtils.getAdvancementCompletion(player, "find_core") && ModUtils.getAdvancementCompletion(player, "find_red") &&
                ModUtils.getAdvancementCompletion(player, "find_purple")) {
                    this.mc.renderEngine.bindTexture(BLOCK_PICTURES_2);
                    drawModalRectWithCustomSizedTexture((this.width - 245) / 2, 13, 0, 406, 115, 58, 115, 464);
                    this.mc.renderEngine.bindTexture(ENDFALL_PICTURES);
                    drawModalRectWithCustomSizedTexture((this.width + 17) / 2, 13, 0, 406, 115, 58, 115, 464);
                    this.createLeftPictureTitleFromString("item.infused_crystal.name");
                    this.createRightPictureTitleFromString("item.endfall_pickaxe.name");
                    this.writeLeftFromString(6, 13, "desc.if_cry_");
                    this.writeRightFromString(6, 10, "desc.pick_");
                } else {
                    this.createLeftPictureTitleFromString("item.infused_crystal.name");
                    this.createRightPictureTitleFromString("item.endfall_pickaxe.name");
                    this.createLockedEntryRight("title.locked_entry");
                    this.createLockedEntryLeft("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width - 75 * 2) / 2, 14);
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width + 55 * 2) / 2, 14);
                }

                GlStateManager.popMatrix();
                break;
            case 16:
                //Amber Armor Set + Helmet + Chestplate + leggings
                GlStateManager.pushMatrix();
                if(ModUtils.getAdvancementCompletion(player, "find_cordium") && ModUtils.getAdvancementCompletion(player, "kill_lamentor")) {
                    this.mc.renderEngine.bindTexture(BLOCK_PICTURES_4);
                    drawModalRectWithCustomSizedTexture((this.width - 245) / 2, 13, 0, 348, 115, 58, 115, 464);
                    drawModalRectWithCustomSizedTexture((this.width + 17) / 2, 13, 0, 406, 115, 58, 115, 464);
                    this.mc.renderEngine.bindTexture(BLOCK_PICTURES_5);
                    drawModalRectWithCustomSizedTexture((this.width + 17) / 2, 91, 0, 0, 115, 58, 115, 464);
                    this.createLeftPictureTitleFromString("title.amber_armor_0");
                    this.createRightPictureTitleFromString("title.amber_armor_0_cont");
                    this.writeLeftFromString(6, 12, "desc.amber_armor_");
                } else {
                    this.createLeftPictureTitleFromString("title.amber_armor_0");
                    this.createRightPictureTitleFromString("title.amber_armor_0_cont");
                    this.createLockedEntryRight("title.locked_entry");
                    this.createLockedEntryLeft("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width - 75 * 2) / 2, 14);
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width + 55 * 2) / 2, 14);
                }

                GlStateManager.popMatrix();
                break;
            case 17:
                //Amber Armor Set Boots + Depths Chomper
                GlStateManager.pushMatrix();
                if(ModUtils.getAdvancementCompletion(player, "find_cordium") && ModUtils.getAdvancementCompletion(player, "kill_lamentor")) {
                    this.mc.renderEngine.bindTexture(BLOCK_PICTURES_5);
                    drawModalRectWithCustomSizedTexture((this.width - 245) / 2, 13, 0, 58, 115, 58, 115, 464);
                    this.createLeftPictureTitleFromString("title.amber_armor_0_cont");
                } else {
                    this.createLeftPictureTitleFromString("title.amber_armor_0_cont");
                    this.createLockedEntryLeft("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width - 75 * 2) / 2, 14);
                }

                if(ModUtils.getAdvancementCompletion(player, "kill_chomper")) {
                    this.mc.renderEngine.bindTexture(BLOCK_PICTURES_4);
                    drawModalRectWithCustomSizedTexture((this.width + 17) / 2, 13, 0, 58, 115, 58, 115, 464);
                    this.createRightPictureTitleFromString("entity.depths_chomper.name");
                    this.writeRightFromString(6, 12, "desc.chomper_");
                } else {
                    this.createRightPictureTitleFromString("entity.depths_chomper.name");
                    this.createLockedEntryRight("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width + 55 * 2) / 2, 14);
                }

                GlStateManager.popMatrix();
                break;
            case 18:
                //Ashed Parasite + End Stalker
                GlStateManager.pushMatrix();
                if(ModUtils.getAdvancementCompletion(player, "kill_end_bug")) {
                    this.mc.renderEngine.bindTexture(MOB_PICTURES);
                    drawModalRectWithCustomSizedTexture((this.width - 245) / 2, 13,0, 290, 115, 58, 115, 464);
                    this.createLeftPictureTitleFromString("entity.end_bug.name");
                    this.writeLeftFromString(6, 13, "desc.bug_");
                } else {
                    this.createLeftPictureTitleFromString("entity.end_bug.name");
                    this.createLockedEntryLeft("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width - 75 * 2) / 2, 14);
                }

                if(ModUtils.getAdvancementCompletion(player, "kill_stalker")) {
                    this.mc.renderEngine.bindTexture(MOB_PICTURES);
                    drawModalRectWithCustomSizedTexture((this.width + 17) / 2, 13,0, 116, 115, 58, 115, 464);
                    this.createRightPictureTitleFromString("entity.snatcher.name");
                    this.writeRightFromString(6, 13, "desc.stal_");
                } else {
                    this.createRightPictureTitleFromString("entity.snatcher.name");
                    this.createLockedEntryRight("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width + 55 * 2) / 2, 14);
                }

                GlStateManager.popMatrix();
                break;

            case 19:
                //End King's Fortress plus other structures
                GlStateManager.pushMatrix();
                if(ModUtils.getAdvancementCompletion(player, "kill_lamentor")) {
                    this.createLeftTitleFromString("title.end_fort_0");
                    this.writeLeftFromString(0, 8, "desc.fort_");
                    this.createRightTitleFromString("title.ot_str_0");
                    this.writeRightFromString(0, 8, "desc.ot_");
                } else {
                    this.createLockedEntryRight("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width + 55 * 2) / 2, 14);
                    this.createLockedEntryLeft("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width - 75 * 2) / 2, 14);
                    this.createRightPictureTitleFromString("title.ot_str_0");
                    this.createLeftPictureTitleFromString("title.end_fort_0");
                }

                GlStateManager.popMatrix();
                break;

            case 20:
                //Ender Knights
                GlStateManager.pushMatrix();
                if(ModUtils.getAdvancementCompletion(player, "kill_knights")) {
                    this.mc.renderEngine.bindTexture(MOB_PICTURES);
                    drawModalRectWithCustomSizedTexture((this.width - 245) / 2, 13,0, 174, 115, 58, 115, 464);
                    this.createLeftPictureTitleFromString("title.knights_0");
                    this.writeLeftFromString(6, 13, "desc.knig_");
                    this.createRightTitleFromString("title.knights_1");
                    this.writeRightFromString(0, 12, "desc.mage_");
                } else {
                    this.createLeftPictureTitleFromString("title.knights_0");
                    this.createRightPictureTitleFromString("title.knights_1");
                    this.createLockedEntryRight("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width + 55 * 2) / 2, 14);
                    this.createLockedEntryLeft("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width - 75 * 2) / 2, 14);
                }
                GlStateManager.popMatrix();
                break;

            case 21:
                //Dark Armour Set
                GlStateManager.pushMatrix();
                if(ModUtils.getAdvancementCompletion(player, "kill_knights")) {
                    this.mc.renderEngine.bindTexture(BLOCK_PICTURES_2);
                    drawModalRectWithCustomSizedTexture((this.width - 245) / 2, 13, 0, 290, 115, 58, 115, 464);
                    drawModalRectWithCustomSizedTexture((this.width + 17) /2, 13, 0, 348, 115, 58, 115, 464);
                    this.createLeftPictureTitleFromString("item.dark_helmet.name");
                    this.createRightPictureTitleFromString("item.dark_chestplate.name");
                    this.writeLeftFromString(6, 11, "desc.dd_helm_");
                    this.writeRightFromString(6, 9, "desc.dd_cont_");
                } else {
                    this.createLeftPictureTitleFromString("item.dark_helmet.name");
                    this.createRightPictureTitleFromString("item.dark_chestplate.name");
                    this.createLockedEntryRight("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width + 55 * 2) / 2, 14);
                    this.createLockedEntryLeft("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width - 75 * 2) / 2, 14);
                }

                GlStateManager.popMatrix();
                break;
            case 22:
                //Ender King + End Chad
                GlStateManager.pushMatrix();

                if(ModUtils.getAdvancementCompletion(player, "kill_ultra_knight")) {
                    this.mc.renderEngine.bindTexture(MOB_PICTURES);
                    drawModalRectWithCustomSizedTexture((this.width - 245) / 2, 13,0, 232, 115, 58, 115, 464);
                    this.createLeftPictureTitleFromString("entity.end_lord.name");
                    this.writeLeftFromString(6, 13, "desc.chad_");
                } else {
                    this.createLeftPictureTitleFromString("entity.end_lord.name");
                    this.createLockedEntryLeft("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width - 75 * 2) / 2, 14);
                }

                if(ModUtils.getAdvancementCompletion(player, "kill_king") && ModUtils.getAdvancementCompletion(player, "kill_lamentor")) {
                    this.mc.renderEngine.bindTexture(MOB_PICTURES);
                    drawModalRectWithCustomSizedTexture((this.width + 17) / 2, 13, 0, 348, 115, 58, 115, 464);
                    this.createRightPictureTitleFromString("entity.end_king.name");
                    this.writeRightFromString(6, 13, "desc.king_");
                } else {
                    this.createRightPictureTitleFromString("entity.end_king.name");
                    this.createLockedEntryRight("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width + 55 * 2) / 2, 14);
                }

                GlStateManager.popMatrix();
                break;
            case 23:
                //Endfall Helmet, Chestplate
                GlStateManager.pushMatrix();
                if(ModUtils.getAdvancementCompletion(player, "kill_king") && ModUtils.getAdvancementCompletion(player, "find_soul")) {
                    this.mc.renderEngine.bindTexture(ENDFALL_PICTURES);
                    drawModalRectWithCustomSizedTexture((this.width - 245) / 2, 13, 0, 174, 115, 58, 115, 464);
                    drawModalRectWithCustomSizedTexture((this.width + 17) / 2, 13, 0, 232, 115, 58, 115, 464);
                    this.createLeftPictureTitleFromString("item.endfall_helmet.name");
                    this.createRightPictureTitleFromString("item.endfall_chestplate.name");
                    this.writeLeftFromString(6, 12, "desc.end_mod_");
                    this.writeRightFromString(6, 12, "desc.end_cont_");
                } else {
                    this.createLeftPictureTitleFromString("item.endfall_helmet.name");
                    this.createRightPictureTitleFromString("item.endfall_chestplate.name");
                    this.createLockedEntryLeft("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width - 75 * 2) / 2, 14);
                    this.createLockedEntryRight("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width + 55 * 2) / 2, 14);
                }

                GlStateManager.popMatrix();
                break;
            case 24:
                //Endfall Leggings, Boots, and the Mini-Nuke
                GlStateManager.pushMatrix();
                if(ModUtils.getAdvancementCompletion(player, "kill_king") && ModUtils.getAdvancementCompletion(player, "find_soul")) {
                    this.mc.renderEngine.bindTexture(ENDFALL_PICTURES);
                    drawModalRectWithCustomSizedTexture((this.width - 245) / 2, 13, 0, 290, 115, 58, 115, 464);
                    drawModalRectWithCustomSizedTexture((this.width - 245) / 2, 91, 0, 348, 115, 58, 115, 464);
                    drawModalRectWithCustomSizedTexture((this.width + 17) / 2, 13, 0, 0, 115, 58, 115, 464);
                    this.createLeftPictureTitleFromString("desc.ef_arm");
                    this.createRightPictureTitleFromString("tile.mini_nuke.name");
                    this.writeRightFromString(6, 9, "desc.mn_");
                } else {
                    this.createLeftPictureTitleFromString("desc.ef_arm");
                    this.createRightPictureTitleFromString("tile.mini_nuke.name");
                    this.createLockedEntryLeft("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width - 75 * 2) / 2, 14);
                    this.createLockedEntryRight("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width + 55 * 2) / 2, 14);
                }

                GlStateManager.popMatrix();
                break;
            case 25:
                //Endfall Sword & Bow
                GlStateManager.pushMatrix();
                if(ModUtils.getAdvancementCompletion(player, "kill_king") && ModUtils.getAdvancementCompletion(player, "find_soul")) {
                    this.mc.renderEngine.bindTexture(ENDFALL_PICTURES);
                    drawModalRectWithCustomSizedTexture((this.width - 245) / 2, 13, 0, 58, 115, 58, 115, 464);
                    drawModalRectWithCustomSizedTexture((this.width + 17) / 2, 13, 0, 116, 115, 58, 115, 464);
                    this.createLeftPictureTitleFromString("item.endfall_sword.name");
                    this.createRightPictureTitleFromString("item.bow.name");
                    this.writeLeftFromString(6, 12, "skill.sword_");
                    this.writeRightFromString(6, 11, "skill.ef_bow_");
                } else {
                    this.createLeftPictureTitleFromString("item.endfall_sword.name");
                    this.createRightPictureTitleFromString("item.bow.name");
                    this.createLockedEntryLeft("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width - 75 * 2) / 2, 14);
                    this.createLockedEntryRight("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width + 55 * 2) / 2, 14);
                }

                GlStateManager.popMatrix();
                break;
            case 26:
                //EndFall Staff
                GlStateManager.pushMatrix();
                if(ModUtils.getAdvancementCompletion(player, "kill_king") && ModUtils.getAdvancementCompletion(player, "find_soul")) {
                    this.mc.renderEngine.bindTexture(BLOCK_PICTURES);
                    drawModalRectWithCustomSizedTexture((this.width - 245) / 2, 13, 0, 58, 115, 58, 115, 464);
                    this.createLeftPictureTitleFromString("item.efstaff.name");
                    this.writeLeftFromString(6, 12, "skill.ef_st_");
                } else {
                    this.createLeftPictureTitleFromString("item.efstaff.name");
                    this.createLockedEntryLeft("title.locked_entry");
                    this.drawItemStack(new ItemStack(Blocks.BARRIER), (this.width - 75 * 2) / 2, 14);
                }
                //LAST PAGE
                GlStateManager.popMatrix();
                break;
        }
    }




    public static boolean doesPlayerhaveXAdvancement(AdvancementEvent event) {
            EntityPlayer player1 = event.getEntityPlayer();

        return false;
    }


    private void writeLeftFromString(int lineStart, int lineEnd, String text) {
        for(int x = lineStart; x <= lineEnd; x++) {
            String s = I18n.format(text + x);
            int k = this.fontRenderer.getStringWidth(s) / 2;
            int i = (this.width - X) / 2;
            GlStateManager.scale(0.6F, 0.6F, 0.0F);
            GlStateManager.translate(0.8F, 0.8F, 0F);
            this.fontRenderer.drawString(TextFormatting.BLACK + s, Math.round((i + 70 - (k * 0.6F))/ 0.6F),  Math.round(70 + (x * 8) / 0.6F), 0);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
        }
    }

    private void writeRightFromString(int lineStart, int lineEnd, String text) {
        for(int x = lineStart; x <= lineEnd; x++) {
            String s = I18n.format(text + x);
            int k = this.fontRenderer.getStringWidth(s) / 2;
            int i = (this.width - X) / 2;
            GlStateManager.scale(0.6F, 0.6F, 0.0F);
            GlStateManager.translate(0.8F, 0.8F, 0F);
            this.fontRenderer.drawString(TextFormatting.BLACK + s, Math.round((i + 200 - (k * 0.6F))/ 0.6F),  Math.round(70 + (x * 8) / 0.6F), 0);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
        }
    }

    private void createRightTitleFromString(String text) {
        String s = I18n.format(text);
        int k = this.fontRenderer.getStringWidth(s) / 2;
        int i = (this.width - X) / 2;
        GlStateManager.scale(0.8F, 0.8F, 0.0F);
        GlStateManager.translate(0.0F, 0.4F, 0F);
        this.fontRenderer.drawString(TextFormatting.GRAY + s, Math.round((i + 200 - (k * 0.75F))/ 0.8F),  Math.round(22  / 0.8F), 0);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
    }

    private void createLockedEntryRight(String text) {
        String s = I18n.format(text);
        int k = this.fontRenderer.getStringWidth(s) / 2;
        int i = (this.width - X) / 2;
        GlStateManager.scale(0.8F, 0.8F, 0.0F);
        GlStateManager.translate(0.0F, 0.4F, 0F);
        this.fontRenderer.drawString(TextFormatting.GRAY + s, Math.round((i + 200 - (k * 0.75F))/ 0.8F),  Math.round(33  / 0.8F), 0);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
    }


    private void createLockedEntryLeft(String text) {
        String s = I18n.format(text);
        int k = this.fontRenderer.getStringWidth(s) / 2;
        int i = (this.width - X) / 2;
        GlStateManager.scale(0.8F, 0.8F, 0.0F);
        GlStateManager.translate(0.0F, 0.4F, 0F);
        this.fontRenderer.drawString(TextFormatting.GRAY + s, Math.round((i + 70 - (k * 0.75F))/ 0.8F),  Math.round(33  / 0.8F), 0);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
    }

    private void createIndexTitleFromString(int x, int y, String text) {
        String s = I18n.format(text);
        int k = this.fontRenderer.getStringWidth(s) / 2;
        int i = (this.width - X) / 2;
        GlStateManager.scale(0.8F, 0.8F, 0.0F);
        GlStateManager.translate(0.0F, 0.4F, 0F);
        this.fontRenderer.drawString(TextFormatting.GRAY + s, Math.round((i + x - (k * 0.75F))/ 0.8F),  Math.round(y  / 0.8F), 0);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
    }


    private void createLeftTitleFromString(String text) {
        String s = I18n.format(text);
        int k = this.fontRenderer.getStringWidth(s) / 2;
        int i = (this.width - X) / 2;
        GlStateManager.scale(0.8F, 0.8F, 0.0F);
        GlStateManager.translate(0.0F, 0.4F, 0F);
        this.fontRenderer.drawString(TextFormatting.GRAY + s, Math.round((i + 70 - (k * 0.75F))/ 0.8F),  Math.round(22  / 0.8F), 0);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
    }

    private void createLeftPictureTitleFromString(String text) {
        String s = I18n.format(text);
        int k = this.fontRenderer.getStringWidth(s) / 2;
        int i = (this.width - X) / 2;
        GlStateManager.scale(0.8F, 0.8F, 0.0F);
        GlStateManager.translate(0.0F, 0.4F, 0F);
        this.fontRenderer.drawString(TextFormatting.GRAY + s, Math.round((i + 70 - (k * 0.75F))/ 0.8F),  Math.round(77  / 0.8F), 0);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
    }

    private void createRightPictureTitleFromString(String text) {
        String s = I18n.format(text);
        int k = this.fontRenderer.getStringWidth(s) / 2;
        int i = (this.width - X) / 2;
        GlStateManager.scale(0.8F, 0.8F, 0.0F);
        GlStateManager.translate(0.0F, 0.4F, 0F);
        this.fontRenderer.drawString(TextFormatting.GRAY + s, Math.round((i + 200 - (k * 0.75F))/ 0.8F),  Math.round(77  / 0.8F), 0);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
    }

    private void drawItemStack(ItemStack stack, int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 32.0F);
        this.zLevel = 200.0F;
        this.itemRender.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = null;
        if (!stack.isEmpty()) font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = fontRenderer;
        GlStateManager.scale((float) 1.0, (float) 1.0, (float) 1.0);
        this.itemRender.zLevel = 5;
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
        GlStateManager.disableLighting();
        this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y, null);
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    @SideOnly(Side.CLIENT)
    static class NextPageButton extends GuiButton
    {
        private final boolean isForward;


        public NextPageButton(int buttonId, int x, int y, boolean isForwardIn)
        {
            super(buttonId, x, y, 23, 13, "");
            this.isForward = isForwardIn;

        }

        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
        {
            if (this.visible)
            {
                boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(GUI_BASE_BOOK_BACKGROUND);
                int i = 110;
                int j = 190;

                if (flag)
                {
                    i += 23;
                }
                if (!this.isForward)
                {
                    j += 13;
                }
                drawModalRectWithCustomSizedTexture(this.x, this.y, i, j, 23, 13, 288, 224);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    static class IndexButtonTest extends GuiButton {

        private final int page;
        private String textTooDisplay;
        public IndexButtonTest(int buttonId, int x, int y, int pageIn) {
            super(buttonId, x, y, "");
            this.page = pageIn;

        }

        public int getPage() {
            return page;
        }

        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if(this.visible) {
               boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               mc.getTextureManager().bindTexture(GUI_INDEX_BUTTON);
               int i = 0;
               int j = 15;
               if(flag) {
                   j += 15;
               }
                drawModalRectWithCustomSizedTexture(this.x, this.y, i, j, 105, 15, 105,  30);


            }
        }

    }


    @SideOnly(Side.CLIENT)
    static class IndexButtonTest2 extends GuiButton {

        private final int page;
        private String textTooDisplay;
        public IndexButtonTest2(int buttonId, int x, int y, int pageIn) {
            super(buttonId, x, y, "");
            this.page = pageIn;

        }

        public int getPage() {
            return page;
        }

        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if(this.visible) {
                boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(GUI_INDEX_BUTTON_2);
                int i = 0;
                int j = 15;
                if(flag) {
                    j += 15;
                }
                drawModalRectWithCustomSizedTexture(this.x, this.y, i, j, 105, 15, 105,  30);


            }
        }

    }


    @SideOnly(Side.CLIENT)
    static class IndexButtonTest3 extends GuiButton {

        private final int page;
        private String textTooDisplay;
        public IndexButtonTest3(int buttonId, int x, int y, int pageIn) {
            super(buttonId, x, y, "");
            this.page = pageIn;

        }

        public int getPage() {
            return page;
        }

        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if(this.visible) {
                boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(GUI_INDEX_BUTTON_3);
                int i = 0;
                int j = 15;
                if(flag) {
                    j += 15;
                }
                drawModalRectWithCustomSizedTexture(this.x, this.y, i, j, 105, 15, 105,  30);


            }
        }

    }


    @SideOnly(Side.CLIENT)
    static class IndexButtonTest4 extends GuiButton {

        private final int page;
        private String textTooDisplay;
        public IndexButtonTest4(int buttonId, int x, int y, int pageIn) {
            super(buttonId, x, y, "");
            this.page = pageIn;

        }

        public int getPage() {
            return page;
        }

        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if(this.visible) {
                boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(GUI_INDEX_BUTTON_4);
                int i = 0;
                int j = 15;
                if(flag) {
                    j += 15;
                }
                drawModalRectWithCustomSizedTexture(this.x, this.y, i, j, 105, 15, 105,  30);


            }
        }

    }

    @SideOnly(Side.CLIENT)
    static class IndexButtonTest5 extends GuiButton {

        private final int page;
        private String textTooDisplay;
        public IndexButtonTest5(int buttonId, int x, int y, int pageIn) {
            super(buttonId, x, y, "");
            this.page = pageIn;

        }

        public int getPage() {
            return page;
        }

        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if(this.visible) {
                boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(GUI_INDEX_BUTTON_5);
                int i = 0;
                int j = 15;
                if(flag) {
                    j += 15;
                }
                drawModalRectWithCustomSizedTexture(this.x, this.y, i, j, 105, 15, 105,  30);


            }
        }

    }

}
