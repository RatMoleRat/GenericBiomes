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
package org.terasology.genericbiomes.biomes;

import org.terasology.biomesAPI.BiomeRegistry;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.registry.In;
import org.terasology.world.generation.facets.SurfaceHumidityFacet;
import org.terasology.world.generation.facets.SurfaceTemperatureFacet;

import java.util.stream.Stream;

/**
 * Registers all core biomes with the engine.
 */
@RegisterSystem
public class GenericBiomes extends BaseComponentSystem {
    @In
    private BiomeRegistry biomeRegistry;

    /**
     * Registration of systems must be done in preBegin to be early enough.
     */
    @Override
    public void preBegin() {
        GenericBiome.MARSH.setLimits(SurfaceHumidityFacet.class, 0.5F, 0.8F);
        GenericBiome.MARSH.setLowerLimit(SurfaceTemperatureFacet.class, 0.3F);
        GenericBiome.BAYOU.setLowerLimit(SurfaceHumidityFacet.class, 0.6F);
        GenericBiome.BAYOU.setLimits(SurfaceTemperatureFacet.class, 0.3F, 0.8F);
        GenericBiome.PINEFOREST.setUpperLimit(SurfaceTemperatureFacet.class, 0.6F);
        GenericBiome.SAVANNAH.setLimits(SurfaceHumidityFacet.class, 0.1F, 0.4F);
        GenericBiome.SAVANNAH.setLowerLimit(SurfaceTemperatureFacet.class, 0.5F);
        GenericBiome.SHRUBLANDS.setLimits(SurfaceHumidityFacet.class, 0.1F, 0.7F);
        GenericBiome.SHRUBLANDS.setLimits(SurfaceTemperatureFacet.class, 0.3F, 0.7F);
        GenericBiome.STEPPE.setLimits(SurfaceTemperatureFacet.class, 0.1F, 0.4F);
        GenericBiome.STEPPE.setUpperLimit(SurfaceHumidityFacet.class, 0.6F);
        GenericBiome.STONYDESERT.setLowerLimit(SurfaceTemperatureFacet.class, 0.6F);
        GenericBiome.STONYDESERT.setUpperLimit(SurfaceHumidityFacet.class, 0.3F);
        Stream.of(GenericBiome.values()).forEach(biomeRegistry::registerBiome);
    }
}
