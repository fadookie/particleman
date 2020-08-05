package mchorse.blockbuster.client.particles;

import com.eliotlash.particlelib.particles.BedrockSchemeJsonAdapter;
import com.eliotlash.particlelib.particles.components.appearance.BedrockComponentAppearanceTinting;
import com.eliotlash.particlelib.particles.components.rate.BedrockComponentRateSteady;
import mchorse.blockbuster.client.particles.components.appearance.RenderableBedrockComponentAppearanceBillboard;
import mchorse.blockbuster.client.particles.components.appearance.RenderableBedrockComponentAppearanceTinting;

public class RenderableBedrockSchemeJsonAdapter extends BedrockSchemeJsonAdapter {
    public RenderableBedrockSchemeJsonAdapter() {
        super();

        /* Appearance */
		this.components.put("minecraft:particle_appearance_billboard", RenderableBedrockComponentAppearanceBillboard.class);
		this.components.put("minecraft:particle_appearance_tinting", RenderableBedrockComponentAppearanceTinting.class);
    }
}
