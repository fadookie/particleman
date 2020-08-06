package com.eliotlash.particleman.client.particles;

import com.eliotlash.particlelib.particles.BedrockSchemeJsonAdapter;
import com.eliotlash.particleman.client.particles.components.appearance.RenderableBedrockComponentAppearanceBillboard;
import com.eliotlash.particleman.client.particles.components.appearance.RenderableBedrockComponentAppearanceTinting;

public class RenderableBedrockSchemeJsonAdapter extends BedrockSchemeJsonAdapter {
    public RenderableBedrockSchemeJsonAdapter() {
        super();

        /* Appearance */
		this.components.put("minecraft:particle_appearance_billboard", RenderableBedrockComponentAppearanceBillboard.class);
		this.components.put("minecraft:particle_appearance_tinting", RenderableBedrockComponentAppearanceTinting.class);
    }
}
