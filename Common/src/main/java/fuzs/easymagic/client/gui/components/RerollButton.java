package fuzs.easymagic.client.gui.components;

import fuzs.easymagic.EasyMagic;
import fuzs.easymagic.config.ServerConfig;
import fuzs.easymagic.world.inventory.ModEnchantmentMenu;
import fuzs.puzzleslib.common.api.client.gui.v2.GuiGraphicsHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TickableTexture;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ARGB;

public class RerollButton extends ImageButton implements TickableTexture {
    public static final WidgetSprites REROLL_SLOT_SPRITES = new WidgetSprites(EasyMagic.id(
            "container/enchanting_table/reroll_slot"),
            EasyMagic.id("container/enchanting_table/reroll_slot_disabled"),
            EasyMagic.id("container/enchanting_table/reroll_slot_highlighted"));
    public static final WidgetSprites REROLL_SPRITES = new WidgetSprites(EasyMagic.id(
            "container/enchanting_table/reroll"),
            EasyMagic.id("container/enchanting_table/reroll_disabled"),
            EasyMagic.id("container/enchanting_table/reroll_highlighted"));
    public static final WidgetSprites LEVEL_SMALL_SPRITES = new WidgetSprites(EasyMagic.id(
            "container/enchanting_table/level_small"),
            EasyMagic.id("container/enchanting_table/level_small_disabled"),
            EasyMagic.id("container/enchanting_table/level_small"));
    public static final WidgetSprites LEVEL_MEDIUM_SPRITES = new WidgetSprites(EasyMagic.id(
            "container/enchanting_table/level_medium"),
            EasyMagic.id("container/enchanting_table/level_medium_disabled"),
            EasyMagic.id("container/enchanting_table/level_medium"));
    public static final WidgetSprites LEVEL_LARGE_SPRITES = new WidgetSprites(EasyMagic.id(
            "container/enchanting_table/level_large"),
            EasyMagic.id("container/enchanting_table/level_large_disabled"),
            EasyMagic.id("container/enchanting_table/level_large"));
    public static final WidgetSprites LAPIS_SMALL_SPRITES = new WidgetSprites(EasyMagic.id(
            "container/enchanting_table/lapis_small"),
            EasyMagic.id("container/enchanting_table/lapis_small_disabled"),
            EasyMagic.id("container/enchanting_table/lapis_small"));
    public static final WidgetSprites LAPIS_MEDIUM_SPRITES = new WidgetSprites(EasyMagic.id(
            "container/enchanting_table/lapis_medium"),
            EasyMagic.id("container/enchanting_table/lapis_medium_disabled"),
            EasyMagic.id("container/enchanting_table/lapis_medium"));
    public static final WidgetSprites LAPIS_LARGE_SPRITES = new WidgetSprites(EasyMagic.id(
            "container/enchanting_table/lapis_large"),
            EasyMagic.id("container/enchanting_table/lapis_large_disabled"),
            EasyMagic.id("container/enchanting_table/lapis_large"));

    private final ModEnchantmentMenu menu;

    public RerollButton(int x, int y, OnPress onPress, ModEnchantmentMenu menu) {
        super(x, y, 38, 27, REROLL_SLOT_SPRITES, onPress);
        this.menu = menu;
    }

    @Override
    public void tick() {
        this.visible = this.menu.canEnchantItem();
        this.active = this.menu.canUseReroll(this.menu.player);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        // only play this locally as it can easily be spammed
        soundManager.play(SimpleSoundInstance.forUI(SoundEvents.ENCHANTMENT_TABLE_USE, 1.0F));
    }

    @Override
    public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractContents(guiGraphics, mouseX, mouseY, partialTick);
        this.extractContentDecorations(guiGraphics);
    }

    private void extractContentDecorations(GuiGraphicsExtractor guiGraphics) {
        int rerollExperiencePointsCost = EasyMagic.CONFIG.get(ServerConfig.class).rerollExperiencePointsCost;
        int rerollCatalystCost = EasyMagic.CONFIG.get(ServerConfig.class).rerollCatalystCost;
        if (rerollExperiencePointsCost == 0 && rerollCatalystCost == 0) {
            Identifier sprite = REROLL_SPRITES.get(this.isActive(), this.isHoveredOrFocused());
            // arrow circle
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, this.getX() + 12, this.getY() + 6, 15, 15);
        } else {
            // arrow circle
            Identifier sprite = REROLL_SPRITES.get(this.isActive(), this.isHoveredOrFocused());
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, this.getX() + 3, this.getY() + 6, 15, 15);
            if (rerollExperiencePointsCost > 0 && rerollCatalystCost > 0) {
                // level orb
                this.extractCostOrb(guiGraphics,
                        this.getX() + (rerollExperiencePointsCost > 9 ? 17 : 20),
                        this.getY() + 13,
                        this.selectOrbSprite(rerollExperiencePointsCost,
                                LEVEL_SMALL_SPRITES,
                                LEVEL_MEDIUM_SPRITES,
                                LEVEL_LARGE_SPRITES),
                        rerollExperiencePointsCost,
                        this.isActive() ? ChatFormatting.GREEN : ChatFormatting.RED);
                // lapis orb
                this.extractCostOrb(guiGraphics,
                        this.getX() + (rerollCatalystCost > 9 ? 17 : 20),
                        this.getY() + 1,
                        this.selectOrbSprite(rerollCatalystCost,
                                LAPIS_SMALL_SPRITES,
                                LAPIS_MEDIUM_SPRITES,
                                LAPIS_LARGE_SPRITES),
                        rerollCatalystCost,
                        this.isActive() ? ChatFormatting.BLUE : ChatFormatting.RED);
            } else if (rerollExperiencePointsCost > 0) {
                // level orb
                this.extractCostOrb(guiGraphics,
                        this.getX() + (rerollExperiencePointsCost > 9 ? 17 : 20),
                        this.getY() + 7,
                        this.selectOrbSprite(rerollExperiencePointsCost,
                                LEVEL_SMALL_SPRITES,
                                LEVEL_MEDIUM_SPRITES,
                                LEVEL_LARGE_SPRITES),
                        rerollExperiencePointsCost,
                        this.isActive() ? ChatFormatting.GREEN : ChatFormatting.RED);
            } else if (rerollCatalystCost > 0) {
                // lapis orb
                this.extractCostOrb(guiGraphics,
                        this.getX() + (rerollCatalystCost > 9 ? 17 : 20),
                        this.getY() + 7,
                        this.selectOrbSprite(rerollCatalystCost,
                                LAPIS_SMALL_SPRITES,
                                LAPIS_MEDIUM_SPRITES,
                                LAPIS_LARGE_SPRITES),
                        rerollCatalystCost,
                        this.isActive() ? ChatFormatting.BLUE : ChatFormatting.RED);
            }
        }
    }

    private WidgetSprites selectOrbSprite(int cost, WidgetSprites small, WidgetSprites medium, WidgetSprites large) {
        return cost < 5 ? small : cost < 10 ? medium : large;
    }

    private void extractCostOrb(GuiGraphicsExtractor guiGraphics, int posX, int posY, WidgetSprites widgetSprites, int cost, ChatFormatting color) {
        Identifier sprite = widgetSprites.get(this.isActive(), this.isHoveredOrFocused());
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, posX, posY, 13, 13);
        // render shadow on every side to avoid readability issues with colorful background
        Font font = Minecraft.getInstance().font;
        GuiGraphicsHelper.drawInBatch8xOutline(guiGraphics,
                font,
                Component.literal(String.valueOf(cost)),
                posX + 8,
                posY + 3,
                ARGB.opaque(color.getColor()),
                ARGB.opaque(0));
    }
}
