/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.genericbiomes.worldgen;

import org.terasology.biomesAPI.Biome;
import org.terasology.biomesAPI.BiomeManager;
import org.terasology.biomesAPI.BiomeRegistry;
import org.terasology.biomesAPI.ConditionalBiome;
import org.terasology.core.world.CoreBiome;
import org.terasology.core.world.generator.facets.BiomeFacet;
import org.terasology.genericbiomes.biomes.GenericBiome;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.registry.CoreRegistry;
import org.terasology.registry.In;
import org.terasology.utilities.random.FastRandom;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;
import org.terasology.world.generation.Requires;
import org.terasology.world.generation.facets.SeaLevelFacet;
import org.terasology.world.generation.facets.SurfaceHeightFacet;
import org.terasology.world.generation.facets.SurfaceHumidityFacet;
import org.terasology.world.generation.facets.SurfaceTemperatureFacet;
import org.terasology.world.generation.facets.ZoneFacet;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Determines the biome based on temperature and humidity
 */
@Produces(BiomeFacet.class)
@Requires({
    @Facet(SeaLevelFacet.class),
    @Facet(SurfaceHeightFacet.class),
    @Facet(SurfaceTemperatureFacet.class),
    @Facet(SurfaceHumidityFacet.class),
    @Facet(ZoneFacet.class)})
public class GenericBiomeProvider implements FacetProvider {

    @In
    private BiomeRegistry biomeRegistry;

    private FastRandom random = new FastRandom();
    private long seed = 0;

    @Override
    public void setSeed(long aSeed) {
        seed = aSeed;
        random = new FastRandom(seed);
        biomeRegistry = CoreRegistry.get(BiomeRegistry.class);
    }

    @Override
    public void process(GeneratingRegion region) {
        SeaLevelFacet seaLevelFacet = region.getRegionFacet(SeaLevelFacet.class);
        SurfaceHeightFacet heightFacet = region.getRegionFacet(SurfaceHeightFacet.class);
        SurfaceTemperatureFacet temperatureFacet = region.getRegionFacet(SurfaceTemperatureFacet.class);
        SurfaceHumidityFacet humidityFacet = region.getRegionFacet(SurfaceHumidityFacet.class);
        ZoneFacet zoneFacet = region.getRegionFacet(ZoneFacet.class);

        Border3D border = region.getBorderForFacet(BiomeFacet.class);
        BiomeFacet biomeFacet = new BiomeFacet(region.getRegion(), border);

        int seaLevel = seaLevelFacet.getSeaLevel();

        for (BaseVector2i pos : biomeFacet.getRelativeRegion().contents()) {
            float temp = temperatureFacet.get(pos);
            float hum = temp * humidityFacet.get(pos);
            float height = heightFacet.get(pos);
            int zone = (int)(zoneFacet.get(pos) * 10);

            if (height <= seaLevel) {
                 biomeFacet.set(pos, CoreBiome.OCEAN);
            } else if (height <= seaLevel + 2) {
                biomeFacet.set(pos, CoreBiome.BEACH);
            } else {
                List<Biome> choices;
                if (biomeRegistry == null) {
                    Biome[] biomes = new Biome[]{CoreBiome.FOREST, CoreBiome.SNOW, CoreBiome.PLAINS, CoreBiome.MOUNTAINS, CoreBiome.DESERT, GenericBiome.PINEFOREST, GenericBiome.MARSH, GenericBiome.SAVANNAH, GenericBiome.RAINFOREST};
                    choices = Arrays.stream(biomes).map(ConditionalBiome.class::cast).filter(b -> b.isValid(region, pos)).collect(Collectors.toList());
                } else {
                    choices = ((BiomeManager)biomeRegistry).getValidBiomes(region, pos, true);
                }
                Biome choice = CoreBiome.FOREST;//
                if (!choices.isEmpty())
                {
                    choice = choices.get((choices.size() + zone) % choices.size());
                }
                //if (choice == null) choice = CoreBiome.FOREST;
                biomeFacet.set(pos, choice);
            }
        }
        region.setRegionFacet(BiomeFacet.class, biomeFacet);
    }
}
