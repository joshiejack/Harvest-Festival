package joshie.harvest.quests.block;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.block.BlockHFEnum;
import joshie.harvest.quests.block.BlockQuestBoard.QuestBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public class BlockQuestBoard extends BlockHFEnum<BlockQuestBoard, QuestBlock> {
    public BlockQuestBoard() {
        super(Material.WOOD, QuestBlock.class, HFTab.TOWN);
    }

    public enum QuestBlock implements IStringSerializable {
        BOARD;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
