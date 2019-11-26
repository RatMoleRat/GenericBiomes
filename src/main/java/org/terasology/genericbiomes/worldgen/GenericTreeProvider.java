/*
 * Copyright 2019 MovingBlocks
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

import org.terasology.core.world.CoreBiome;
import org.terasology.core.world.generator.facetProviders.DefaultTreeProvider;
import org.terasology.core.world.generator.facets.BiomeFacet;
import org.terasology.core.world.generator.facets.TreeFacet;
import org.terasology.core.world.generator.trees.Trees;
import org.terasology.genericbiomes.biomes.GenericBiome;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.FacetBorder;
import org.terasology.world.generation.Produces;
import org.terasology.world.generation.Requires;
import org.terasology.world.generation.facets.SeaLevelFacet;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

@Produces(TreeFacet.class)
@Requires({
        @Facet(value = SeaLevelFacet.class, border = @FacetBorder(sides = 13)),
        @Facet(value = SurfaceHeightFacet.class, border = @FacetBorder(sides = 13 + 1)),
        @Facet(value = BiomeFacet.class, border = @FacetBorder(sides = 13))
})
public class GenericTreeProvider extends DefaultTreeProvider {

    public GenericTreeProvider() {
        register(CoreBiome.MOUNTAINS, Trees.oakTree(), 0.02f);
        register(CoreBiome.MOUNTAINS, Trees.pineTree(), 0.02f);
        register(CoreBiome.MOUNTAINS, GenericTrees.mapleTree(), 0.02f);

        register(CoreBiome.FOREST, Trees.oakTree(), 0.25f);
        register(CoreBiome.FOREST, GenericTrees.mapleTree(), 0.10f);
        register(CoreBiome.FOREST, Trees.birchTree(), 0.10f);
        register(CoreBiome.FOREST, Trees.oakVariationTree(), 0.25f);

        register(CoreBiome.SNOW, Trees.birchTree(), 0.02f);
        register(CoreBiome.SNOW, GenericTrees.mapleTree(), 0.02f);
        register(CoreBiome.SNOW, Trees.pineTree(), 0.08f);

        register(CoreBiome.PLAINS, Trees.redTree(), 0.01f);
        register(CoreBiome.PLAINS, Trees.birchTree(), 0.01f);
        register(CoreBiome.PLAINS, Trees.oakTree(), 0.02f);

        register(CoreBiome.DESERT, Trees.cactus(), 0.04f);

        register(GenericBiome.MARSH, Trees.oakTree(), 0.02f);
        register(GenericBiome.MARSH, Trees.oakVariationTree(), 0.02f);

        register(GenericBiome.PINEFOREST, Trees.pineTree(), 0.75f);

    }
}
