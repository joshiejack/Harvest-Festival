package net.quetzi.morpheus.api;

public interface IMorpheusAPI
{
    /**
     * Register your INewDayHandler with MorpheusRegistry
     *
     * @param newdayhandler Method that updates time in the dimension to the next morning
     * @param dimension Dimension to be registered
     */
    void registerHandler(INewDayHandler newdayhandler, int dimension);
    void unregisterHandler(int dimension);
}