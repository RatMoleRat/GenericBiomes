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

import com.google.common.collect.Maps;
import org.terasology.biomesAPI.Biome;
import org.terasology.core.world.generator.facets.BiomeFacet;
import org.terasology.core.world.generator.facets.FloraFacet;
import org.terasology.core.world.generator.rasterizers.FloraType;
import org.terasology.genericbiomes.biomes.GenericBiome;
import org.terasology.math.geom.BaseVector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.utilities.procedural.WhiteNoise;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.ChunkConstants;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 */
public class DesertFloraRasterizer implements WorldRasterizer {

    private final Map<FloraType, List<Block>> flora = Maps.newEnumMap(FloraType.class);
    private final List<Biome> biomes;
    private Block air;
    private Block litre;

    public DesertFloraRasterizer()
    {
        this(GenericBiome.STONYDESERT, GenericBiome.SHRUBLANDS);
    }

    public DesertFloraRasterizer(Biome... biome)
    {
        biomes = new ArrayList<>();
        biomes.addAll(Arrays.asList(biome));
    }

    @Override
    public void initialize() {
        BlockManager blockManager = CoreRegistry.get(BlockManager.class);
        air = blockManager.getBlock(BlockManager.AIR_ID);
        litre = blockManager.getBlock("PlantPack:Litre");

        flora.put(FloraType.GRASS, Arrays.asList(
                blockManager.getBlock("CoreBlocks:TallGrass1"),
                blockManager.getBlock("CoreBlocks:TallGrass2"),
                blockManager.getBlock("PlantPack:Yucca")));

        flora.put(FloraType.FLOWER, Arrays.asList(
                blockManager.getBlock("CoreBlocks:Dandelion"),
                blockManager.getBlock("PlantPack:CactusEasterLily"),
                blockManager.getBlock("PlantPack:SpikeBush"),
                litre,
                blockManager.getBlock("PlantPack:DeadBush"),
                blockManager.getBlock("PlantPack:Yucca")));

        flora.put(FloraType.MUSHROOM, Arrays.asList(
                blockManager.getBlock("CoreBlocks:BigBrownShroom"),
                blockManager.getBlock("CoreBlocks:BrownShroom"),
                blockManager.getBlock("CoreBlocks:RedShroom")));
    }

    public void addFloraBlock(FloraType floraType, Block block)
    {
        flora.get(floraType).add(block);
    }

    public void removeFloraBlock(FloraType floraType, Block block)
    {
        flora.get(floraType).remove(block);
    }

    public void addValidBiomes(Biome... biome) { biomes.addAll(Arrays.asList(biome)); }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {

        FloraFacet facet = chunkRegion.getFacet(FloraFacet.class);
        BiomeFacet bf = chunkRegion.getFacet(BiomeFacet.class);

        WhiteNoise noise = new WhiteNoise(chunk.getPosition().hashCode());

        Map<BaseVector3i, FloraType> entries = facet.getRelativeEntries();
        // check to make sure it's open and in a correct biome
        entries.keySet().stream().filter(pos ->
                chunk.getBlock(pos).equals(air) && biomes.contains(bf.get(pos.x(), pos.z())))
                .forEach(pos -> {

            FloraType type = entries.get(pos);
            List<Block> list = flora.get(type);
            int blockIdx = Math.abs(noise.intNoise(pos.x(), pos.y(), pos.z())) % list.size();
            Block block = list.get(blockIdx);
            chunk.setBlock(pos, block);
            if (block == litre) {
                int limit = Math.abs(noise.intNoise(pos.x(), pos.z())) % 4;
                for (int q = 0; q < limit; q++) {
                    //double-check our boundaries
                    if (pos.y() + q > 63) return;
                    chunk.setBlock(pos.x(), pos.y() + q, pos.z(), block);
                }
            }
        });
    }
}
