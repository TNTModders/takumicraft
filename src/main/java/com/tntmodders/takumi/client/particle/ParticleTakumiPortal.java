package com.tntmodders.takumi.client.particle;

import net.minecraft.client.particle.ParticlePortal;
import net.minecraft.world.World;

public class ParticleTakumiPortal extends ParticlePortal {
    
    public ParticleTakumiPortal(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn * -10, ySpeedIn * -10, zSpeedIn * -10);
        float f = this.rand.nextFloat() * 0.6F + 0.4F;
        this.particleRed = f * 0.1f;
        this.particleGreen = f;
        this.particleBlue = f * 0.1f;
    }
}
