package harvestmoon.handlers;

public class CommonHandler {
    private static ClientHandler theClient = new ClientHandler();
    private static ServerHandler theServer;

    //Returns the handler for the client
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
}
