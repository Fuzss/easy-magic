package fuzs.easymagic.neoforge;

import fuzs.easymagic.common.EasyMagic;
import fuzs.easymagic.common.data.tags.ModBlockTagsProvider;
import fuzs.easymagic.common.data.tags.ModItemTagsProvider;
import fuzs.easymagic.common.init.ModRegistry;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v3.core.DataProviderBuilder;
import fuzs.puzzleslib.neoforge.api.init.v3.capability.NeoForgeCapabilityHelper;
import net.neoforged.fml.common.Mod;

@Mod(EasyMagic.MOD_ID)
public class EasyMagicNeoForge {

    public EasyMagicNeoForge() {
        ModConstructor.construct(EasyMagic.MOD_ID, EasyMagic::new);
        DataProviderBuilder.of(EasyMagic.MOD_ID).addProvider(ModItemTagsProvider::new, ModBlockTagsProvider::new);
        NeoForgeCapabilityHelper.registerWorldlyBlockEntityContainer(ModRegistry.ENCHANTING_TABLE_BLOCK_ENTITY_TYPE);
    }
}
