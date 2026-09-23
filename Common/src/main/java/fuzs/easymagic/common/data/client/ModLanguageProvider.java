package fuzs.easymagic.common.data.client;

import fuzs.easymagic.common.client.util.EnchantmentTooltipHelper;
import fuzs.easymagic.common.init.ModRegistry;
import fuzs.puzzleslib.common.api.client.data.v3.language.AbstractLanguageProvider;
import fuzs.puzzleslib.common.api.data.v3.core.DataProviderContext;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations() {
        this.add(EnchantmentTooltipHelper.KEY_REROLL, "Reroll enchantments");
        this.add(EnchantmentTooltipHelper.KEY_ONE_REROLL_CATALYST, "1 Amethyst Shard");
        this.add(EnchantmentTooltipHelper.KEY_MANY_REROLL_CATALYSTS, "%s Amethyst Shards");
        this.add(EnchantmentTooltipHelper.KEY_ONE_EXPERIENCE_POINT, "1 Experience Point");
        this.add(EnchantmentTooltipHelper.KEY_MANY_EXPERIENCE_POINTS, "%s Experience Points");
        this.add(ModRegistry.ENCHANTING_CATALYSTS_ITEM_TAG, "Enchanting Catalysts");
        this.add(ModRegistry.REROLL_CATALYSTS_ITEM_TAG, "Reroll Catalysts");
        this.add(ModRegistry.UNALTERED_ENCHANTING_TABLES_BLOCK_TAG, "Unaltered Enchanting Tables");
    }

    @Override
    protected boolean mustHaveTranslationKey(Holder.Reference<?> holder, String translationKey) {
        return !(holder.value() instanceof Block) && super.mustHaveTranslationKey(holder, translationKey);
    }
}
