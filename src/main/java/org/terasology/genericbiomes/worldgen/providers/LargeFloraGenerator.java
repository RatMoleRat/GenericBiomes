/*
 * Copyright 2013 MovingBlocks
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

import org.terasology.core.world.generator.trees.AbstractTreeGenerator;
import org.terasology.core.world.generator.trees.TreeGenerator;
import org.terasology.utilities.random.Random;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.BlockUri;
import org.terasology.world.chunks.CoreChunk;

/**
 * Cactus generator.
 *
 */
public class LargeFloraGenerator extends AbstractTreeGenerator {

    private BlockUri floraType;
    private int patchSize = 8;
    private int minHeight = 1;
    private int maxHeight = 1;

    @Override
    public void generate(BlockManager blockManager, CoreChunk view, Random rand, int posX, int posY, int posZ) {
        Block block = blockManager.getBlock(floraType);
        int height = rand.nextInt(minHeight, maxHeight);
        for (int y = posY; y < posY + height; y++) {
            safelySetBlock(view, posX, y, posZ, block);
        }
    }

    public LargeFloraGenerator setBlockType(BlockUri b) {
        floraType = b;
        return this;
    }

    public LargeFloraGenerator setPatchSize(int s) {
        patchSize = Math.max(1, Math.min(s, 10));
        return this;
    }

    public int getPatchSize() {
        return patchSize;
    }

    public LargeFloraGenerator setHeights(int min, int max) {
        minHeight = Math.max(min, 1);
        maxHeight = Math.min(max, 25);
        return this;
    }
}
