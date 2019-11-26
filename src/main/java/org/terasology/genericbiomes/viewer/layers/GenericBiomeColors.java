/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.terasology.genericbiomes.viewer.layers;

import com.google.common.collect.Maps;
import org.terasology.biomesAPI.Biome;
import org.terasology.core.world.CoreBiome;
import org.terasology.genericbiomes.biomes.GenericBiome;
import org.terasology.rendering.nui.Color;

import java.util.Map;
import java.util.function.Function;

/**
 * Maps the core biomes to colors
 */
public class GenericBiomeColors implements Function<Biome, Color> {

    private final Map<Biome, Color> biomeColors = Maps.newHashMap();

    public GenericBiomeColors() {
        biomeColors.put(GenericBiome.BAYOU, new Color(0x323e2eff));
        biomeColors.put(GenericBiome.MARSH, new Color(0x349169ff));
        biomeColors.put(GenericBiome.SAVANNAH, new Color(0x819134ff));
        biomeColors.put(GenericBiome.PINEFOREST, new Color(0x1c3f15ff));
        biomeColors.put(GenericBiome.RAINFOREST, new Color(0x1b9e00ff));
        biomeColors.put(GenericBiome.SHRUBLANDS, new Color(0x60ae50ff));
        biomeColors.put(GenericBiome.STEPPE, new Color(0x899d59ff));
        biomeColors.put(GenericBiome.STONYDESERT, new Color(0x595931ff));
    }

    @Override
    public Color apply(Biome biome) {
        Color color = biomeColors.get(biome);
        return color;
    }

    /**
     * @param biome the biome
     * @param color the new color
     */
    public void setBiomeColor(Biome biome, Color color) {
        this.biomeColors.put(biome, color);
    }
}
