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

import org.terasology.biomesAPI.Biome;

public enum GenericBiome implements Biome {
    BAYOU("Bayou"),
    MARSH("Marsh"),
    PINEFOREST("Pine Forest"),
    RAINFOREST("Rainforest"),
    SAVANNAH("Savannah"),
    SHRUBLANDS("Shrublands"),
    STEPPE("Steppe"),
    STONYDESERT("Stony Desert");

    private final String id;
    private final String name;

    GenericBiome(String name) {
        this.id = "Generic:" + name().toLowerCase();
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
