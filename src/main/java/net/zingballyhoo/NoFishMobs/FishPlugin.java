package net.zingballyhoo.NoFishMobs;

import net.minecraft.server.v1_13_R2.BiomeBase;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.EnumCreatureType;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FishPlugin extends JavaPlugin {
    @Override
    @SuppressWarnings("unchecked")
    public void onEnable() {
        Set<BiomeBase> biomes;
        try {

            biomes = (Set<BiomeBase>) getPrivateStatic(BiomeBase.class, "aG");
        } catch (Exception exc) {
            getLogger().warning("Unable to find biome set!");
            return;
        }

        for (BiomeBase biomeBase : biomes) {
            if (biomeBase == null)
                break;

            try {
                Field list = BiomeBase.class.getDeclaredField("aZ");
                list.setAccessible(true);
                Map<EnumCreatureType, List<BiomeBase.BiomeMeta>> mobMap = (Map<EnumCreatureType, List<BiomeBase.BiomeMeta>>) list.get(biomeBase);

                for (Map.Entry<EnumCreatureType, List<BiomeBase.BiomeMeta>> entry : mobMap.entrySet()) {
                    List<BiomeBase.BiomeMeta> cloneMobList = new ArrayList<BiomeBase.BiomeMeta>(entry.getValue()); // clone the list so we can iterate and modify at the same time
                    List<BiomeBase.BiomeMeta> mobList = entry.getValue();
                    for (BiomeBase.BiomeMeta meta: cloneMobList) {
                        if (meta.b == EntityTypes.COD || meta.b == EntityTypes.SALMON || meta.b == EntityTypes.TROPICAL_FISH || meta.b == EntityTypes.PUFFERFISH) {
                            mobList.remove(meta);
                            getLogger().info("removed " + EntityTypes.getName(meta.b) + " from " + biomeBase.getClass().getSimpleName() + " spawn table");
                        }
                    }
                }
            } catch (Exception e) {
                getLogger().warning("Unable to search biome mobs");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDisable() {
        // todo: revert??
    }

    // from loads of places e.g https://gist.github.com/XvBaseballkidvX/11203452
    /**
     * A convenience method.
     *
     * @param clazz The class.
     * @param f     The string representation of the private static field.
     * @return The object found
     * @throws Exception if unable to get the object.
     */
    private static Object getPrivateStatic(Class clazz, String f) throws Exception {
        Field field = clazz.getDeclaredField(f);
        field.setAccessible(true);
        return field.get(null);
    }
}
