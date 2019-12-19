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

import com.google.common.collect.ImmutableMap;
import org.terasology.core.world.generator.rasterizers.RasterFilters;
import org.terasology.core.world.generator.trees.TreeGenerator;
import org.terasology.core.world.generator.trees.TreeGeneratorLSystem;
import org.terasology.math.LSystemRule;
import org.terasology.world.block.BlockUri;

public final class GenericTrees {
    private GenericTrees() {
        // no instances!
    }

    public static TreeGenerator mapleTree() {
        return new TreeGeneratorLSystem(
                "FFFFFA", ImmutableMap.<Character, LSystemRule>builder()
                .put('A', new LSystemRule("[&FFBFA]////[&BFFFA]////[&FBFFA]", 1.0f))
                .put('B', new LSystemRule("[&FFFA]////[&FFFA]////[&FFFA]", 0.8f)).build(),
                4, (float) Math.toRadians(30))
                .setLeafType(new BlockUri("PlantPack:MapleLeaf"))
                .setBarkType(new BlockUri("PlantPack:MapleTrunk"));
    }

    public static TreeGenerator palmTree() {
        return new PalmTreeGenerator().setLeafType("PlantPack:AcaiPalmLeaf").setTrunkType("PlantPack:AcaiPalmTrunk");
    }
}
