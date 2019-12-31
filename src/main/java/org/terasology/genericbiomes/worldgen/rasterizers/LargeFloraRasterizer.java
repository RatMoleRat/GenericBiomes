/*
 * Copyright 2015 MovingBlocks
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
package org.terasology.genericbiomes.worldgen.rasterizers;

import org.terasology.core.world.generator.facets.TreeFacet;
import org.terasology.core.world.generator.trees.TreeGenerator;
import org.terasology.genericbiomes.worldgen.facets.LargeFloraFacet;
import org.terasology.genericbiomes.worldgen.providers.LargeFloraGenerator;
import org.terasology.math.Region3i;
import org.terasology.math.TeraMath;
import org.terasology.math.geom.BaseVector3i;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.utilities.random.FastRandom;
import org.terasology.utilities.random.Random;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizer;
import org.terasology.world.generation.facets.SurfaceHeightFacet;
import org.terasology.world.generation.facets.base.SparseFacet3D;

import java.util.Map;

/**
 * Creates trees based on the {@link LargeFloraGenerator} that is
 * defined by the {@link LargeFloraFacet}.
 *
 */
public class LargeFloraRasterizer implements WorldRasterizer {

    private BlockManager blockManager;

    @Override
    public void initialize() {
        blockManager = CoreRegistry.get(BlockManager.class);
        //TODO: Remove these lines when lazy block registration is fixed
        //Currently they are required to ensure that the blocks are all registered before worldgen
        blockManager.getBlock("PlantPack:NapierBase");
        blockManager.getBlock("PlantPack:NapierMiddle");
        blockManager.getBlock("PlantPack:NapierTop");
        blockManager.getBlock("PlantPack:Reeds");
        blockManager.getBlock("PlantPack:ReedsBase");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        LargeFloraFacet facet = chunkRegion.getFacet(LargeFloraFacet.class);
        SurfaceHeightFacet facet2 = chunkRegion.getFacet(SurfaceHeightFacet.class);

        for (Map.Entry<BaseVector3i, LargeFloraGenerator> entry : facet.getRelativeEntries().entrySet()) {
            BaseVector3i pos = entry.getKey();
            LargeFloraGenerator floraGen = entry.getValue();
            int baseHeight = TeraMath.floorToInt(facet2.getWorld(pos.x(), pos.z())) + 1;

            for (int x1 = -floraGen.getPatchSize(); x1 < floraGen.getPatchSize(); x1++) {
                int x = pos.x() + x1;
                for (int z1 = -floraGen.getPatchSize(); z1 < floraGen.getPatchSize(); z1++) {
                    int z = pos.z() + z1;
                    double distance = Math.abs(Math.abs(x) - Math.abs(pos.x())) + Math.abs(Math.abs(z) - Math.abs(pos.z()));
                    double chance = floraGen.getPatchSize() * 0.7;
                    int seed = relativeToWorld(facet, pos).hashCode();
                    Random random = new FastRandom(seed);
                    if (random.nextDouble(0, distance) < chance) {
                        int spotHeight = TeraMath.floorToInt(facet2.getWorld(x, z)) + 1;
                        int relativeHeight = pos.y() + (spotHeight - baseHeight);
                        floraGen.generate(blockManager, chunk, random, x, relativeHeight, z);
                    }
                }
            }
        }
    }

    // TODO: JAVA8 - move the two conversion methods from SparseFacet3D to default methods in WorldFacet3D
    protected final Vector3i relativeToWorld(SparseFacet3D facet, BaseVector3i pos) {

        Region3i worldRegion = facet.getWorldRegion();
        Region3i relativeRegion = facet.getRelativeRegion();

        return new Vector3i(
                pos.x() - relativeRegion.minX() + worldRegion.minX(),
                pos.y() - relativeRegion.minY() + worldRegion.minY(),
                pos.z() - relativeRegion.minZ() + worldRegion.minZ());
    }
}
