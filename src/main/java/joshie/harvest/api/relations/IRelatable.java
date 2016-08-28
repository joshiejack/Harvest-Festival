package joshie.harvest.api.relations;


import javax.annotation.Nonnull;

public interface IRelatable {
    @Nonnull
    IRelatableDataHandler getDataHandler();
}