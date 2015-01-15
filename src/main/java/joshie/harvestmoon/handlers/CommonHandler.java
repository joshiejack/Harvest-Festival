package joshie.harvestmoon.handlers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CommonHandler {
    @SideOnly(Side.CLIENT)
    private static ClientHandler theClient;
    private static ServerHandler theServer;

    //Returns the handler for the client
    @SideOnly(Side.CLIENT)
    public ClientHandler getClient() {
        return theClient;
    }

    //Returns the handler for the server
    public ServerHandler getServer() {
        return theServer;
    }

    //Called whenever a world loads, to update the server handler
    public void setServer(ServerHandler server) {
        theServer = server;
    }
    
    //Creates a new instance of client handler
    @SideOnly(Side.CLIENT)
    public void resetClient() {
        theClient = new ClientHandler();
    }
}
