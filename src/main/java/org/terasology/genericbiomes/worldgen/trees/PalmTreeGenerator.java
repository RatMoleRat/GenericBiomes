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
package org.terasology.genericbiomes.worldgen.trees;

import org.terasology.core.world.generator.trees.AbstractTreeGenerator;
import org.terasology.utilities.random.Random;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;

public class PalmTreeGenerator extends AbstractTreeGenerator {

    public int smallestHeight;
    public int tallestHeight;
    public String trunkType;
    public String leafType;

    public PalmTreeGenerator()
    {
        this(8, 12);
    }

    public PalmTreeGenerator(int minHeight, int maxHeight)
    {
        if (minHeight > maxHeight) minHeight = maxHeight;
        smallestHeight = minHeight;
        tallestHeight = maxHeight;
    }

    public PalmTreeGenerator setTrunkType(String trunk)
    {
        trunkType = trunk;
        return this;
    }

    public PalmTreeGenerator setLeafType(String leaf)
    {
        leafType = leaf;
        return this;
    }

    @Override
    public void generate(BlockManager blockManager, CoreChunk view, Random rand, int posX, int posY, int posZ) {
        int height = rand.nextInt(smallestHeight, tallestHeight + 1);
        Block trunk = blockManager.getBlock(trunkType);
        Block leafNorth = blockManager.getBlock(leafType + ".FRONT");
        Block leafSouth = blockManager.getBlock(leafType + ".BACK");
        Block leafEast = blockManager.getBlock(leafType + ".LEFT");
        Block leafWest = blockManager.getBlock(leafType + ".RIGHT");
        if (leafNorth == null || leafSouth == null || leafEast == null || leafWest == null)
        {
            leafNorth = leafSouth = leafEast = leafWest = blockManager.getBlock(leafType);
        }
        for (int q = 0; q <=  height; q++)
        {
            safelySetBlock(view, posX, posY + q, posZ, trunk);
        }
        safelySetBlock(view, posX, posY + height, posZ - 1, leafNorth);
        safelySetBlock(view, posX, posY + height, posZ - 2, leafNorth);
        safelySetBlock(view, posX, posY + height, posZ + 1, leafSouth);
        safelySetBlock(view, posX, posY + height, posZ + 2, leafSouth);
        safelySetBlock(view, posX - 1, posY + height, posZ, leafEast);
        safelySetBlock(view, posX - 2, posY + height, posZ, leafEast);
        safelySetBlock(view, posX + 1, posY + height, posZ, leafWest);
        safelySetBlock(view, posX + 2, posY + height, posZ, leafWest);
        if (rand.nextBoolean())
        {
            safelySetBlock(view, posX, posY + height - 1, posZ - 1, leafNorth);
            safelySetBlock(view, posX, posY + height - 1, posZ + 1, leafSouth);
            safelySetBlock(view, posX - 1, posY + height - 1, posZ, leafEast);
            safelySetBlock(view, posX + 1, posY + height - 1, posZ, leafWest);
        }
    }
}
