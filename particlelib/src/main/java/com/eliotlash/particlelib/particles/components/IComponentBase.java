package com.eliotlash.particlelib.particles.components;

public interface IComponentBase
{
	public default int getSortingIndex()
	{
		return 0;
	}
}
