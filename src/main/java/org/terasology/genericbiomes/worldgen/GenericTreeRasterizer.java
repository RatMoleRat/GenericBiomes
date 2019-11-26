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

import org.terasology.core.world.generator.rasterizers.TreeRasterizer;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.BlockManager;

public class GenericTreeRasterizer extends TreeRasterizer {

    private BlockManager blockManager;

    @Override
    public void initialize()
    {
        super.initialize();
        blockManager = CoreRegistry.get(BlockManager.class);
        //TODO: Remove these lines when lazy block registration is fixed
        //Currently they are required to ensure that the blocks are all registered before worldgen
        blockManager.getBlock("PlantPack:MapleLeaf");
        blockManager.getBlock("PlantPack:MapleTrunk");
        blockManager.getBlock("PlantPack:UmbrellaLeaf");
        blockManager.getBlock("PlantPack:UmbrellaTrunk");
        blockManager.getBlock("PlantPack:AcaiPalmLeaf");
        blockManager.getBlock("PlantPack:AcaiPalmTrunk");
    }
}
