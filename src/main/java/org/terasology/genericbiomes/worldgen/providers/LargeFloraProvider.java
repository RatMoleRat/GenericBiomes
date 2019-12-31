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
package org.terasology.genericbiomes.worldgen.providers;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import org.terasology.biomesAPI.Biome;
import org.terasology.core.world.CoreBiome;
import org.terasology.core.world.generator.facetProviders.PositionFilters;
import org.terasology.core.world.generator.facetProviders.SurfaceObjectProvider;
import org.terasology.core.world.generator.facets.BiomeFacet;
import org.terasology.core.world.generator.facets.TreeFacet;
import org.terasology.core.world.generator.trees.TreeGenerator;
import org.terasology.core.world.generator.trees.Trees;
import org.terasology.entitySystem.Component;
import org.terasology.genericbiomes.biomes.GenericBiome;
import org.terasology.genericbiomes.worldgen.facets.LargeFloraFacet;
import org.terasology.math.geom.Vector3i;
import org.terasology.rendering.nui.properties.Range;
import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.procedural.WhiteNoise;
import org.terasology.world.block.BlockUri;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.ConfigurableFacetProvider;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.FacetBorder;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;
import org.terasology.world.generation.Requires;
import org.terasology.world.generation.facets.SeaLevelFacet;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

import java.util.List;

/**
 * Determines where grass fields can be placed.
 */
@Produces(LargeFloraFacet.class)
@Requires({
        @Facet(value = SeaLevelFacet.class, border = @FacetBorder(sides = 20)),
        @Facet(value = SurfaceHeightFacet.class, border = @FacetBorder(sides = 20 + 1)),
        @Facet(value = BiomeFacet.class, border = @FacetBorder(sides = 20))
})
public class LargeFloraProvider extends SurfaceObjectProvider<Biome, LargeFloraGenerator> implements ConfigurableFacetProvider {

    private Noise densityNoiseGen;
    private Configuration configuration = new Configuration();

    public LargeFloraProvider() {
        register(GenericBiome.SAVANNAH, new LargeFloraGenerator().setBlockType(new BlockUri("PlantPack:NapierBase")), 0.04f);
    }

    /**
     * @param configuration the default configuration to use
     */
    public LargeFloraProvider(Configuration configuration) {
        this();
        this.configuration = configuration;
    }

    @Override
    public void setSeed(long seed) {
        super.setSeed(seed);

        densityNoiseGen = new WhiteNoise(seed);
    }

    @Override
    public void process(GeneratingRegion region) {
        SurfaceHeightFacet surface = region.getRegionFacet(SurfaceHeightFacet.class);
        BiomeFacet biome = region.getRegionFacet(BiomeFacet.class);

        List<Predicate<Vector3i>> filters = getFilters(region);

        // These values must be identical in the class annotations.
        int maxRad = 20;
        int maxHeight = 6;
        Border3D borderForTreeFacet = region.getBorderForFacet(TreeFacet.class);
        LargeFloraFacet facet = new LargeFloraFacet(region.getRegion(), borderForTreeFacet.extendBy(0, maxHeight, maxRad));

        populateFacet(facet, surface, biome, filters);

        region.setRegionFacet(LargeFloraFacet.class, facet);
    }

    protected List<Predicate<Vector3i>> getFilters(GeneratingRegion region) {
        List<Predicate<Vector3i>> filters = Lists.newArrayList();

        SeaLevelFacet seaLevel = region.getRegionFacet(SeaLevelFacet.class);
        filters.add(PositionFilters.minHeight(seaLevel.getSeaLevel()));

        filters.add(PositionFilters.probability(densityNoiseGen, configuration.density * 0.05f));

        SurfaceHeightFacet surface = region.getRegionFacet(SurfaceHeightFacet.class);
        filters.add(PositionFilters.flatness(surface, 1, 0));

        return filters;
    }

    @Override
    public String getConfigurationName() {
        return "Tall Grass Patches";
    }

    @Override
    public Component getConfiguration() {
        return configuration;
    }

    @Override
    public void setConfiguration(Component configuration) {
        this.configuration = (Configuration) configuration;
    }

    public static class Configuration implements Component {
        @Range(min = 0, max = 1.0f, increment = 0.05f, precision = 2, description = "Define the overall grass density")
        public float density = 0.2f;
    }
}
