package com.tntmodders.takumi.client.particle;

import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.world.World;

public class ParticleTakumiRedstone extends ParticleRedstone {

    public ParticleTakumiRedstone(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float xSpeedIn,
                                  float ySpeedIn, float zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        float f = this.rand.nextFloat() * 0.6F + 0.4F;
        this.particleRed = f * 0.1f;
        this.particleGreen = f;
        this.particleBlue = f * 0.1f;
    }
}
