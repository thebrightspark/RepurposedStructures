package com.telepathicgrunt.repurposedstructures.world.features;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;


public class BoulderGiant extends Feature<NoFeatureConfig>
{
	public BoulderGiant(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory)
	{
		super(configFactory);
	}

	private final static BlockState MOSSY_COBBLESTONE = Blocks.MOSSY_COBBLESTONE.getDefaultState();
	private final static BlockState COBBLESTONE = Blocks.COBBLESTONE.getDefaultState();
	private final static BlockState ANDESITE = Blocks.ANDESITE.getDefaultState();
	private final static BlockState COAL_ORE = Blocks.COAL_ORE.getDefaultState();
	private final static BlockState IRON_ORE = Blocks.IRON_ORE.getDefaultState();
	private final static BlockState DIAMOND_ORE = Blocks.DIAMOND_ORE.getDefaultState();
	private final static int START_RADIUS = 4;


	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> chunkGenerator, Random random, BlockPos position, NoFeatureConfig config)
	{

		BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable(position);
		BlockState blockState = world.getBlockState(blockpos$Mutable);

		//Will keeps moving down position until it finds valid ground to generate on while ignoring other boulders
		while (blockpos$Mutable.getY() >= 6)
		{
			if (blockState.getMaterial() == Material.AIR || (blockState.getBlock() != Blocks.GRASS_BLOCK && !isDirt(blockState.getBlock())))
			{
				//block was air or a non-dirt/grass block. Thus move down one.
				blockpos$Mutable.move(Direction.DOWN);
				blockState = world.getBlockState(blockpos$Mutable);
			}
			else
			{
				break; //We hit a valid spot to generate a boulder, time to exit loop
			}
		}

		//if the height is too low or high, just quit.
		if (blockpos$Mutable.getY() <= 10 || blockpos$Mutable.getY() >= 250)
		{
			return false;
		}

		//we are at a valid spot to generate a boulder now. Begin generation.
		for (int currentCount = 0; START_RADIUS >= 0 && currentCount < 3; ++currentCount)
		{
			int x = START_RADIUS + random.nextInt(2);
			int y = START_RADIUS + random.nextInt(2);
			int z = START_RADIUS + random.nextInt(2);
			float calculatedDistance = (x + y + z) * 0.333F + 0.5F;

			for (BlockPos blockpos : BlockPos.getAllInBoxMutable(blockpos$Mutable.add(-x, -y, -z), blockpos$Mutable.add(x, y, z)))
			{
				if (blockpos.distanceSq(blockpos$Mutable) <= calculatedDistance * calculatedDistance)
				{
					//adds the blocks for generation in this boulder
					//note, if user turns off an ore, that ore's chance is dumped into the below ore for generation
					int randomChance = random.nextInt(3000);

					// 1/3000th chance for diamond ore
					if (randomChance == 0)
					{
						world.setBlockState(blockpos, DIAMOND_ORE, 4);
					}

					// 75/3000th chance for iron ore
					else if (randomChance <= 75)
					{
						world.setBlockState(blockpos, IRON_ORE, 4);
					}

					// 180/3000th chance for coal ore
					else if (randomChance <= 255)
					{
						world.setBlockState(blockpos, COAL_ORE, 4);
					}

					// 770/3000th chance for andesite
					else if (randomChance <= 1025)
					{
						world.setBlockState(blockpos, ANDESITE, 4);
					}

					// 700/3000th chance for cobblestone
					else if (randomChance <= 1725)
					{
						world.setBlockState(blockpos, COBBLESTONE, 4);
					}

					// 1275/3000th chance for mossyCobblestone
					else
					{
						world.setBlockState(blockpos, MOSSY_COBBLESTONE, 4);
					}
				}
			}
			blockpos$Mutable.move(-(START_RADIUS + 1) + random.nextInt(2 + START_RADIUS * 2), 0 - random.nextInt(2), -(START_RADIUS + 1) + random.nextInt(2 + START_RADIUS * 2));

		}
		//finished generating the boulder
		return true;
	}
}
