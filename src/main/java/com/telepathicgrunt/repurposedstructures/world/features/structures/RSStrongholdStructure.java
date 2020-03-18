package com.telepathicgrunt.repurposedstructures.world.features.structures;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;
import com.telepathicgrunt.repurposedstructures.RSConfig;

import net.minecraft.util.Direction;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;


public class RSStrongholdStructure extends Structure<NoFeatureConfig>
{
	public RSStrongholdStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> p_i51427_1_)
	{
		super(p_i51427_1_);
	}


	@Override
	protected ChunkPos getStartPositionForPosition(ChunkGenerator<?> chunkGenerator, Random random, int x, int z, int spacingOffsetsX, int spacingOffsetsZ)
	{
		int maxDistance = RSConfig.strongholdSpawnrate;
		int minDistance = maxDistance - 10;
		if (maxDistance < 12)
		{
			minDistance = maxDistance - 1;
		}
		int k = x + maxDistance * spacingOffsetsX;
		int l = z + maxDistance * spacingOffsetsZ;
		int i1 = k < 0 ? k - maxDistance + 1 : k;
		int j1 = l < 0 ? l - maxDistance + 1 : l;
		int targetChunkX = i1 / maxDistance;
		int targetChunkZ = j1 / maxDistance;
		((SharedSeedRandom) random).setLargeFeatureSeedWithSalt(chunkGenerator.getSeed(), targetChunkX, targetChunkZ, 148523564);
		targetChunkX = targetChunkX * maxDistance;
		targetChunkZ = targetChunkZ * maxDistance;
		targetChunkX = targetChunkX + random.nextInt(maxDistance - minDistance);
		targetChunkZ = targetChunkZ + random.nextInt(maxDistance - minDistance);
		return new ChunkPos(targetChunkX, targetChunkZ);
	}


	@Override
	public boolean func_225558_a_(BiomeManager biomeManager, ChunkGenerator<?> chunkGenerator, Random random, int chunkPosX, int chunkPosZ, Biome biome)
	{

		ChunkPos chunkpos = this.getStartPositionForPosition(chunkGenerator, random, chunkPosX, chunkPosZ, 0, 0);
		if (chunkPosX == chunkpos.x && chunkPosZ == chunkpos.z)
		{
			if (chunkGenerator.hasStructure(biome, this))
			{
				return true;
			}
		}

		return false;
	}


	@Override
	public Structure.IStartFactory getStartFactory()
	{
		return RSStrongholdStructure.Start::new;
	}


	@Override
	public String getStructureName()
	{
		return "Stronghold";
	}


	@Override
	public int getSize()
	{
		return 8;
	}

	public static class Start extends StructureStart
	{
		public Start(Structure<?> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn)
		{
			super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
		}


		@Override
		public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
		{
			RSStrongholdPieces.prepareStructurePieces();
			RSStrongholdPieces.EntranceStairs strongholdpieces$entrancestairs = new RSStrongholdPieces.EntranceStairs(this.rand, (chunkX << 4) + 2, (chunkZ << 4) + 2);
			this.components.add(strongholdpieces$entrancestairs);
			strongholdpieces$entrancestairs.buildComponent(strongholdpieces$entrancestairs, this.components, this.rand);
			List<StructurePiece> list = strongholdpieces$entrancestairs.pendingChildren;

			if (!this.components.isEmpty() && strongholdpieces$entrancestairs.strongholdPortalRoom == null)
			{
				list.add(new RSStrongholdPieces.PortalRoom(0, list.get(list.size()-1).getBoundingBox(), Direction.NORTH));
			}
			
			while (!list.isEmpty())
			{
				int i = this.rand.nextInt(list.size());
				StructurePiece structurepiece = list.remove(i);
				structurepiece.buildComponent(strongholdpieces$entrancestairs, this.components, this.rand);
			}

			this.recalculateStructureSize();
			this.func_214628_a(generator.getSeaLevel(), this.rand, 10);

		}
	}
}