package fuzs.easymagic.client.gui.screens.inventory;

import fuzs.easymagic.EasyMagic;
import fuzs.easymagic.client.gui.components.EnchantmentSlotButton;
import fuzs.easymagic.client.gui.components.RerollButton;
import fuzs.easymagic.client.util.EnchantmentTooltipHelper;
import fuzs.easymagic.config.ClientConfig;
import fuzs.easymagic.config.ServerConfig;
import fuzs.easymagic.world.inventory.ModEnchantmentMenu;
import fuzs.puzzleslib.common.api.client.gui.v2.tooltip.TooltipBuilder;
import fuzs.puzzleslib.common.api.event.v1.core.EventResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.EnchantmentNames;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TickableTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModEnchantmentScreen extends EnchantmentScreen {
    private static final Identifier SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot");

    private boolean noTooltipRendering;

    public ModEnchantmentScreen(EnchantmentMenu enchantmentMenu, Inventory inventory, Component title) {
        super(enchantmentMenu, inventory, title);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.tickChildren();
    }

    public void tickChildren() {
        for (GuiEventListener guiEventListener : this.children()) {
            if (guiEventListener instanceof TickableTexture tickableTexture) {
                tickableTexture.tick();
            }
        }
    }

    @Override
    protected void init() {
        super.init();
        for (int i = 0; i < 3; i++) {
            int slotIndex = i;
            AbstractWidget enchantmentSlotButton = this.addRenderableWidget(new EnchantmentSlotButton(this.leftPos + 60,
                    this.topPos + 14 + 19 * i,
                    (Button button) -> {
                        if (this.menu.clickMenuButton(this.minecraft.player, slotIndex)) {
                            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, slotIndex);
                        }
                    },
                    DISABLED_LEVEL_SPRITES[i],
                    ENABLED_LEVEL_SPRITES[i],
                    i,
                    this.getMenu()));
            TooltipBuilder.create().setLines(() -> {
                List<EnchantmentInstance> slotData = this.getMenu().clues.get(slotIndex);
                if (!slotData.isEmpty()) {
                    List<Component> tooltipLines = new ArrayList<>();
                    EnchantmentTooltipHelper.gatherSlotEnchantmentsTooltip(slotData,
                            tooltipLines::add,
                            this.minecraft.getConnection().registryAccess());
                    EnchantmentTooltipHelper.gatherSlotCostsTooltip(slotIndex,
                            tooltipLines,
                            this.minecraft.player,
                            this.getMenu());
                    return tooltipLines;
                } else {
                    return Collections.emptyList();
                }
            }).build(enchantmentSlotButton);
        }
        AbstractWidget rerollButton = this.addRenderableWidget(new RerollButton(this.getRerollButtonX(),
                this.getRerollButtonY(),
                (Button button) -> {
                    if (EasyMagic.CONFIG.get(ServerConfig.class).rerollEnchantments) {
                        if (this.menu.clickMenuButton(this.minecraft.player, ModEnchantmentMenu.REROLL_DATA_SLOT)) {
                            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId,
                                    ModEnchantmentMenu.REROLL_DATA_SLOT);
                        }
                    }
                },
                this.getMenu()) {
            @Override
            public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
                if (!EasyMagic.CONFIG.get(ClientConfig.class).keepEnchantmentScreenBook()) {
                    super.extractContents(guiGraphics, mouseX, mouseY, partialTick);
                }
            }
        });
        TooltipBuilder.create().setLines(() -> {
            List<Component> tooltipLines = new ArrayList<>();
            EnchantmentTooltipHelper.gatherRerollTooltip(tooltipLines, this.minecraft.player, this.getMenu());
            return tooltipLines;
        }).build(rerollButton);
        this.tickChildren();
    }

    private int getRerollButtonX() {
        return this.leftPos + (EasyMagic.CONFIG.get(ServerConfig.class).dedicatedRerollCatalyst ? 12 : 14);
    }

    private int getRerollButtonY() {
        return this.topPos + 16;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                ENCHANTING_TABLE_LOCATION,
                this.leftPos,
                this.topPos,
                0.0F,
                0.0F,
                this.imageWidth,
                this.imageHeight,
                256,
                256);
        this.extractBook(guiGraphics, this.leftPos, this.topPos);
        EnchantmentNames.getInstance().initSeed(this.menu.getEnchantmentSeed());
        // don't render anything but the background just like vanilla for enchanting slots
        for (int i = 0; i < 3; ++i) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                    ENCHANTMENT_SLOT_DISABLED_SPRITE,
                    this.leftPos + 60,
                    this.topPos + 14 + 19 * i,
                    108,
                    19);
        }

        if (!EasyMagic.CONFIG.get(ClientConfig.class).keepEnchantmentScreenBook()) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                    RerollButton.REROLL_SLOT_SPRITES.disabled(),
                    this.getRerollButtonX(),
                    this.getRerollButtonY(),
                    38,
                    27);
        }

        if (EasyMagic.CONFIG.get(ServerConfig.class).dedicatedRerollCatalyst()) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                    ENCHANTING_TABLE_LOCATION,
                    this.leftPos + 4,
                    this.topPos + 46,
                    14,
                    46,
                    18,
                    18,
                    256,
                    256);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                    ENCHANTING_TABLE_LOCATION,
                    this.leftPos + 22,
                    this.topPos + 46,
                    34,
                    46,
                    18,
                    18,
                    256,
                    256);
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                    SLOT_SPRITE,
                    this.leftPos + 40,
                    this.topPos + 46,
                    18,
                    18);
        }
    }

    @Override
    public void extractBook(GuiGraphicsExtractor guiGraphics, int x, int y) {
        if (EasyMagic.CONFIG.get(ClientConfig.class).keepEnchantmentScreenBook()) {
            super.extractBook(guiGraphics, x, y);
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.noTooltipRendering = true;
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
        this.noTooltipRendering = false;
    }

    @Override
    public ModEnchantmentMenu getMenu() {
        return (ModEnchantmentMenu) super.getMenu();
    }

    public static EventResult onRenderTooltip(GuiGraphicsExtractor guiGraphics, Font font, int mouseX, int mouseY, List<ClientTooltipComponent> components, ClientTooltipPositioner positioner) {
        if (Minecraft.getInstance().screen instanceof ModEnchantmentScreen screen && screen.noTooltipRendering) {
            return EventResult.INTERRUPT;
        } else {
            return EventResult.PASS;
        }
    }
}
