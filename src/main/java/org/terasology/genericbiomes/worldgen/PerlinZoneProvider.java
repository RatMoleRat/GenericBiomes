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

import org.terasology.math.TeraMath;
import org.terasology.math.geom.Vector2f;
import org.terasology.utilities.procedural.BrownianNoise;
import org.terasology.utilities.procedural.PerlinNoise;
import org.terasology.utilities.procedural.SubSampledNoise;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;
import org.terasology.world.generation.facets.SurfaceTemperatureFacet;
import org.terasology.world.generation.facets.ZoneFacet;

@Produces(ZoneFacet.class)
public class PerlinZoneProvider implements FacetProvider {
    private static final int SAMPLE_RATE = 4;

    private SubSampledNoise noise;

    @Override
    public void setSeed(long seed) {
        noise = new SubSampledNoise(new BrownianNoise(new PerlinNoise(seed + 42), 8), new Vector2f(0.0005f, 0.0005f), SAMPLE_RATE);
    }

    @Override
    public void process(GeneratingRegion region) {
        ZoneFacet facet = new ZoneFacet(region.getRegion(), region.getBorderForFacet(ZoneFacet.class));
        float[] noise = this.noise.noise(facet.getWorldRegion());

        for (int i = 0; i < noise.length; ++i) {
            noise[i] = TeraMath.clamp((noise[i] * 2.11f + 1f) * 0.5f);
        }

        facet.set(noise);
        region.setRegionFacet(ZoneFacet.class, facet);
    }
}
