package com.blademc.core.utils.TNT;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import com.blademc.core.BladeMC;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by avigh on 8/25/2016.
 */
public class PastedBlock {

	private final double x, y, z;
	private final int id, data;

	public PastedBlock(double x, double y, double z, int id, int data) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.id = id;
		this.data = data;
	}

	public static class BlockQueue {

		private final Deque<PastedBlock> queue = new ConcurrentLinkedDeque<>();
		private static Map<Level, BlockQueue> queueMap = new ConcurrentHashMap<>();

		public void add(PastedBlock block) {
			if (block.id != Block.TNT)
				queue.add(block);
		}

		public BlockQueue(final Level world) {

			Server.getInstance().getScheduler().scheduleRepeatingTask(BladeMC.plugin, () -> {
				PastedBlock block = null;
				boolean hasTime = true;
				final long start = System.currentTimeMillis();

				while (hasTime)
					if ((block = queue.poll()) != null) {
						hasTime = System.currentTimeMillis() - start < 10;
						world.setBlock(new Vector3(block.x, block.y, block.z), Block.get(block.id, block.data));
					}
			}, 1);
		}

		public static BlockQueue getQueue(Level w) {
			if (!queueMap.containsKey(w)) {
				final BlockQueue blockQueue = new BlockQueue(w);
				queueMap.put(w, blockQueue);

				return blockQueue;
			}
			return queueMap.get(w);
		}
	}

	/**
	 * public void setBlockFast(World world, int x, int y, int z, int blockId, byte
	 * data) { net.minecraft.server.v1_10_R1.World w = ((CraftWorld)
	 * world).getHandle(); Chunk chunk = w.getChunkAt(x >> 4, z >> 4); a(chunk, new
	 * BlockPosition(x, y, z),
	 * net.minecraft.server.v1_10_R1.Block.getById(blockId).fromLegacyData(data)); }
	 *
	 * private IBlockData a(Chunk that, BlockPosition blockposition, IBlockData
	 * iblockdata) { return that.a(blockposition, iblockdata); }
	 **/

}