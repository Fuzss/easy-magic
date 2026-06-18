package fuzs.easymagic.common.data.tags;

import fuzs.easymagic.common.init.ModRegistry;
import fuzs.puzzleslib.common.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.common.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.ItemIds;
import net.minecraft.world.item.Item;

public class ModItemTagsProvider extends AbstractTagProvider<Item> {

    public ModItemTagsProvider(DataProviderContext context) {
        super(Registries.ITEM, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(ModRegistry.ENCHANTING_CATALYSTS_ITEM_TAG).add(ItemIds.LAPIS_LAZULI);
        this.tag(ModRegistry.REROLL_CATALYSTS_ITEM_TAG).add(ItemIds.AMETHYST_SHARD);
    }
}
