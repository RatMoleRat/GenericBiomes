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
import org.terasology.core.world.generator.facetProviders.BiomeProvider;
import org.terasology.core.world.generator.facetProviders.DefaultFloraProvider;
import org.terasology.core.world.generator.facetProviders.DefaultTreeProvider;
import org.terasology.core.world.generator.facetProviders.PerlinBaseSurfaceProvider;
import org.terasology.core.world.generator.facetProviders.PerlinHillsAndMountainsProvider;
import org.terasology.core.world.generator.facetProviders.PerlinHumidityProvider;
import org.terasology.core.world.generator.facetProviders.PerlinOceanProvider;
import org.terasology.core.world.generator.facetProviders.PerlinRiverProvider;
import org.terasology.core.world.generator.facetProviders.PerlinSurfaceTemperatureProvider;
import org.terasology.core.world.generator.facetProviders.PlateauProvider;
import org.terasology.core.world.generator.facetProviders.SeaLevelProvider;
import org.terasology.core.world.generator.facetProviders.SurfaceToDensityProvider;
import org.terasology.core.world.generator.rasterizers.FloraRasterizer;
import org.terasology.core.world.generator.rasterizers.FloraType;
import org.terasology.core.world.generator.rasterizers.SolidRasterizer;
import org.terasology.core.world.generator.rasterizers.TreeRasterizer;
import org.terasology.engine.SimpleUri;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.genericbiomes.worldgen.trees.GenericTreeProvider;
import org.terasology.genericbiomes.worldgen.trees.GenericTreeRasterizer;
import org.terasology.genericbiomes.biomes.GenericBiome;
import org.terasology.logic.spawner.FixedSpawner;
import org.terasology.math.geom.ImmutableVector2i;
import org.terasology.math.geom.Vector3f;
import org.terasology.registry.In;
import org.terasology.world.generation.BaseFacetedWorldGenerator;
import org.terasology.world.generation.WorldBuilder;
import org.terasology.world.generator.RegisterWorldGenerator;
import org.terasology.world.generator.plugin.WorldGeneratorPluginLibrary;

/**
 */
@RegisterWorldGenerator(id = "facetedperlingb", displayName = "Perlin Generic Biomes", description = "Faceted world generator using perlin")
public class GenericPerlinFacetedWorldGenerator extends BaseFacetedWorldGenerator {

    private final FixedSpawner spawner = new FixedSpawner(0, 0);

    @In
    private WorldGeneratorPluginLibrary worldGeneratorPluginLibrary;

    public GenericPerlinFacetedWorldGenerator(SimpleUri uri) {
        super(uri);
    }

    @Override
    public Vector3f getSpawnPosition(EntityRef entity) {
        return spawner.getSpawnPosition(getWorld(), entity);
    }

    @Override
    protected WorldBuilder createWorld() {
        int seaLevel = 32;
        ImmutableVector2i spawnPos = new ImmutableVector2i(0, 0); // as used by the spawner

        return new WorldBuilder(worldGeneratorPluginLibrary)
                .setSeaLevel(seaLevel)
                .addProvider(new SeaLevelProvider(seaLevel))
                .addProvider(new PerlinHumidityProvider())
                .addProvider(new PerlinSurfaceTemperatureProvider())
                .addProvider(new PerlinBaseSurfaceProvider())
                .addProvider(new PerlinRiverProvider())
                .addProvider(new PerlinOceanProvider())
                .addProvider(new PerlinHillsAndMountainsProvider())
                .addProvider(new PerlinZoneProvider())
                .addProvider(new GenericBiomeProvider())
                .addProvider(new SurfaceToDensityProvider())
                .addProvider(new DefaultFloraProvider()
                        .addNewFlora(GenericBiome.STONYDESERT, FloraType.GRASS, 0.01F)
                        .addNewFlora(GenericBiome.STONYDESERT, FloraType.FLOWER, 0.02F)
                        .addNewFlora(GenericBiome.SHRUBLANDS, FloraType.GRASS, 0.1F)
                        .addNewFlora(GenericBiome.SHRUBLANDS, FloraType.FLOWER, 0.3F)
                        .addNewFlora(GenericBiome.STEPPE, FloraType.GRASS, 0.3F)
                        .addNewFlora(GenericBiome.STEPPE, FloraType.FLOWER, 0.05F)
                        .addNewFlora(CoreBiome.DESERT, FloraType.GRASS, 0.1F)
                        .addNewFlora(CoreBiome.DESERT, FloraType.FLOWER, 0.1F)
                        .addNewFlora(GenericBiome.PINEFOREST, FloraType.GRASS, 0.05F)
                        .addNewFlora(GenericBiome.PINEFOREST, FloraType.FLOWER, 0.1F)
                        .addNewFlora(GenericBiome.PINEFOREST, FloraType.MUSHROOM, 0.2F)
                        .addNewFlora(GenericBiome.BAYOU, FloraType.FLOWER, 0.05F)
                        .addNewFlora(GenericBiome.BAYOU, FloraType.MUSHROOM, 0.2F)
                        .addNewFlora(GenericBiome.SAVANNAH, FloraType.GRASS, 0.3F)
                        .addNewFlora(GenericBiome.SAVANNAH, FloraType.FLOWER, 0.05F)
                )
                .addProvider(new GenericTreeProvider())
                .addProvider(new PlateauProvider(spawnPos, seaLevel + 4, 10, 30))
                        //.addRasterizer(new GroundRasterizer(blockManager))
                .addRasterizer(new GenericSolidRasterizer())
                .addPlugins()
                .addRasterizer(new DesertFloraRasterizer())
                .addRasterizer(new FloraRasterizer())
                .addRasterizer(new GenericTreeRasterizer());
    }
}
