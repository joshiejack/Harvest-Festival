package uk.joshiejack.harvestcore.world.mine.dimension.floors;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import uk.joshiejack.harvestcore.world.mine.dimension.MineChunkGenerator;
import uk.joshiejack.harvestcore.world.mine.dimension.wrappers.AbstractDecoratorWrapper;
import uk.joshiejack.penguinlib.util.BlockStates;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Random;

public class RoomGeneratorTemplate extends RoomGenerator {
    private static final boolean ON_WINDOWS = Util.getOSType() == Util.EnumOS.WINDOWS;
    private static final CharMatcher BACKSLASH_MATCHER = CharMatcher.is('\\');
    private final List<Modified> extra = Lists.newArrayList();
    protected int[][] data;
    private final int width;
    private final int height;

    public RoomGeneratorTemplate(ResourceLocation resource) throws IOException {
        //BufferedImage image = ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(resource).getInputStream());
        InputStream stream = null;
        String s = "/assets/" + resource.getNamespace() + "/" + resource.getPath();
        try {
            URL url = RoomGeneratorTemplate.class.getResource(s);
            if (url != null && validatePath(new File(url.getFile()), s))
                stream = RoomGeneratorTemplate.class.getResourceAsStream(s);
        } catch (IOException var4) {
            stream = RoomGeneratorTemplate.class.getResourceAsStream(s);
        }

        BufferedImage image = ImageIO.read(stream);
        width = image.getWidth();
        height = image.getHeight();
        data = new int[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                data[row][col] = image.getRGB(col, row);
            }
        }

        init(resource);
    }

    private boolean validatePath(File p_191384_0_, String p_191384_1_) throws IOException {
        String s = p_191384_0_.getCanonicalPath();

        if (ON_WINDOWS) {
            s = BACKSLASH_MATCHER.replaceFrom(s, '/');
        }

        return s.endsWith(p_191384_1_);
    }

    public void init(ResourceLocation resource) throws IOException {
        //Add the rotations
        if (width == height) {
            extra.add(new Modified(resource, 0, true));
            for (int i = 1; i <= 3; i++) {
                extra.add(new Modified(resource, i, true));
                extra.add(new Modified(resource, i, false));
            }
        }
    }

    @Override
    public boolean canGenerate(BlockPos pos) {
        int buffer = (Math.max(width, height) + 1);
        int arrayMax = (MineChunkGenerator.CHUNKS_PER_SECTION * 16) - buffer;
        return pos.getX() > buffer && pos.getZ() > buffer && pos.getX() < arrayMax && pos.getZ() < arrayMax;
    }

    @Override
    public BlockPos generate(AbstractDecoratorWrapper world, BlockPos ladder) {
        Random rand = world.rand;
        List<BlockPos> offsets = Lists.newArrayList();
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] == -1) {
                    offsets.add(new BlockPos(i, 0, j));
                }
            }
        }

        BlockPos offset = offsets.get(rand.nextInt(offsets.size()));
        List<BlockPos> positions = Lists.newArrayList();
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] == -1) {
                    BlockPos target = ladder.add(-offset.getX() + i, 0, -offset.getZ() + j); //new BlockPos(x, 0, z);
                    world.setBlockState(target, world.tier.getFloor(world.floor));
                    for (int y = 1; y < 3 + rand.nextInt(3); y++) {
                        world.setBlockState(target.up(y), BlockStates.AIR);
                    }

                    positions.add(target);
                }
            }
        }

        return getLadderPosition(world, positions, 10);
    }

    public List<Modified> extra() {
        return extra;
    }

    public static class Modified extends RoomGeneratorTemplate {
        public Modified(ResourceLocation resource, int amount, boolean mirrored) throws IOException {
            super(resource);
            this.data = mirrored ? mirror(data) : data;
            for (int i = 0; i < amount; i++) {
                this.data = rotate(data);
            }
        }

        @Override
        public void init(ResourceLocation resource) {
        }

        private int[][] mirror(int[][] array) {
            int size = array.length;
            int ret[][] = new int[size][size];
            for (int i = size - 1; i >= 0; i--) {
                for (int j = size - 1; j >= 0; j--) {
                    ret[size - 1 - i][size - 1 - j] = array[i][j];
                }
            }

            return ret;
        }

        private int[][] rotate(int[][] array) {
            int size = array.length;
            int[][] ret = new int[size][size];
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                    ret[i][j] = array[size - j - 1][i];

            return ret;
        }
    }
}
