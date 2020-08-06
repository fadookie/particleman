package com.eliotlash.particleman;

import com.eliotlash.particleman.client.RenderingHandler;
import com.eliotlash.particlelib.particles.BedrockScheme;
import com.eliotlash.particleman.client.particles.emitter.RenderableBedrockEmitter;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid= ParticleMan.MODID)
public class ModEventBus
{
    public static RenderableBedrockEmitter emitter;
    public static BedrockScheme scheme;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void doClientStuff(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START && event.player == Minecraft.getMinecraft().player)
        {
            if (scheme == null)
            {
                scheme = BedrockScheme.parse("{\"format_version\":\"1.10.0\",\"particle_effect\":{\"description\":{\"identifier\":\"snowstorm:rainbow\",\"basic_render_parameters\":{\"material\":\"particles_alpha\",\"texture\":\"examplemod:textures/default_particles.png\"}},\"curves\":{\"variable.rainbow\":{\"type\":\"catmull_rom\",\"input\":\"variable.particle_random_2\",\"horizontal_range\":1,\"nodes\":[1,0,1,1.18]},\"variable.psize\":{\"type\":\"catmull_rom\",\"input\":\"variable.particle_age\",\"horizontal_range\":\"variable.particle_lifetime\",\"nodes\":[0,0,1,0,0]}},\"components\":{\"minecraft:emitter_initialization\":{\"creation_expression\":\"variable.radius = 2.5;test = 1;\"},\"minecraft:emitter_rate_steady\":{\"spawn_rate\":250,\"max_particles\":500},\"minecraft:emitter_lifetime_looping\":{\"active_time\":4},\"minecraft:emitter_shape_point\":{\"offset\":[\"math.cos(variable.emitter_age * 90) * (2.5-variable.particle_random_2)\",\"math.sin(variable.emitter_age * 90) * (2.5-variable.particle_random_2)\",0]},\"minecraft:particle_lifetime_expression\":{\"max_lifetime\":1.5},\"minecraft:particle_initial_speed\":0,\"minecraft:particle_motion_dynamic\":{\"linear_acceleration\":[0,1,0]},\"minecraft:particle_appearance_billboard\":{\"size\":[\"0.12 * variable.psize\",\"0.12 * variable.psize\"],\"facing_camera_mode\":\"rotate_xyz\",\"uv\":{\"texture_width\":128,\"texture_height\":128,\"uv\":[32,88],\"uv_size\":[8,8]}},\"minecraft:particle_appearance_tinting\":{\"color\":{\"interpolant\":\"variable.rainbow\",\"gradient\":{\"0.0\":\"#d71c1c\",\"0.16\":\"#ffdf00\",\"0.33\":\"#08ff00\",\"0.5\":\"#00ffff\",\"0.67\":\"#0000ff\",\"0.83\":\"#ff00ff\",\"1.0\":\"#e21111\"}}}}}}");
            }

            if (emitter == null || emitter.isFinished())
            {
                emitter = new RenderableBedrockEmitter();
                emitter.setScheme(scheme);
            }

            emitter.update();
            /* Screw interpolation for now */
            emitter.lastGlobal.set(event.player.posX, event.player.posY, event.player.posZ);
            /* Sanity ticks is a stupid workaround to see if the emitter's controller
             * is still in the world, when the sanity ticks reaches 2, it stops itself */
            emitter.sanityTicks = 0;

            RenderingHandler.addEmitter(emitter, event.player);
        }
    }
}
