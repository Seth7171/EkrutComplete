package Application.server;


import OCSF.AbstractServer;
import OCSF.ConnectionToClient;
import Presentation.serverGUI.ServerUIController;
import common.UserConnection;
import javafx.application.Platform;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * @author Lior Jigalo
 * This class controlls the server, and allows us to send and recieve messages from the client.
 */
public class ServerController extends AbstractServer {
    //Class fields *************************************************
    public ServerUIController serverUI;
    private MysqlController sqlController;
    private TableView<UserConnection> connectionList;
    private TableColumn<UserConnection, String> ipColumn;
    private TableColumn<UserConnection, String> hostNameColumn;
    private TableColumn<UserConnection, String> connectionStatusColumn;
    private java.util.concurrent.Executors Executors;
    private final Executor executor = Executors.newFixedThreadPool(2);


    /**
    * @param port port number to listen for connections
    * @param primaryStage primary stage to load on program startup
    * This is the constructor for the server controller.
    * It initializes the MysqlController and serverUIController.
    */
    //Constructors ****************************************************
    public ServerController(int port, Stage primaryStage) {
      super(port);
      sqlController = MysqlController.getSQLInstance();
      serverUI = new ServerUIController();
      try{
          this.serverUI.start(primaryStage);
      }catch (Exception exception){
          exception.printStackTrace();
      }
    }


    /**
     *This method gets hooked when the server starts listening for connections.
     */
    protected void serverStarted() {
      sqlController.turnOffSafeUpdate();
      sqlController.runGenerator();
    }

    /**
     *This message gets hooked when the server is stopped.
    */
    protected void serverStopped() {
      try{
          close();
          System.out.println("Server has stopped listening for connections.");
      }catch (IOException exception){
          exception.printStackTrace();
      }
    }


  public ServerUIController getServerUI(){
      return this.serverUI;
  }

    /**
     * @param client the connection connected to the client.
     * This method gets hooked when a client is connected.
     */
    // listen for client connections, this method is called when a client is connected
    @Override
    protected void clientConnected(ConnectionToClient client) {
        super.clientConnected(client);
        this.serverUI.addClientConnection(client);
    }

    /**
     * @param client
     * This method calls the removeClientConnection in ServerUIController because the clientDisconnected method of the OCSF framework is broken.
     */
    // this method
    private void disconnectClient(ConnectionToClient client){
        this.serverUI.removeClientConnection(client);
    }

    /**
     * This method handles a message received from a client.
     * The message is passed to an executor for execution.
     * If the message is a String and is "disconnect", the client is removed from the server UI and a "disconnected" message is sent to the client.
     * Otherwise, the message is passed to the handleMessage method of the MessageHandler class for further handling.
     * @param msg The message received from the client. Can be of any type.
     * @param client The connection to the client that sent the message.
     */
    public void handleMessageFromClient(Object msg, ConnectionToClient client) {
        executor.execute(() -> {
            if (msg instanceof String){
                String message = (String)msg;
                if(message.equals("disconnect")){
                    Platform.runLater(() -> this.serverUI.removeClientConnection(client));
                    sendMessageToClient(client, "disconnected");
                    return;
                }
            }
            MessageHandler.handleMessage(msg, client);
        });
    }

    /**
     * @param client    the client to send the message to.
     * @param message   the message to send to the client.
     * This method sends a message to the client passed as a parameter.
     */
    private void sendMessageToClient(ConnectionToClient client , String message){
        try{
            client.sendToClient(message);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}


// backup:
//public void handleMessageFromClient(Object msg, ConnectionToClient client) {
//    if (msg instanceof String){
//        String message = (String)msg;
//        if(message.equals("disconnect")){
//            this.serverUI.removeClientConnection(client);
//            sendMessageToClient(client, "disconnected");
//            return;
//        }
//    }
//    MessageHandler.handleMessage(msg, client);
//}




// SPARE
//    public void handleMessageFromClient(Object msg, ConnectionToClient client) {
//        String message = (String)msg;
//        String[] queryArgs = message.split(" ");
//
//        switch (queryArgs[0]){
//            case "newUser":
//                if(sqlController.checkUserExists(queryArgs[3])){
//                    sendMessageToClient(client, "exists");
//                    return;
//                }
//                if(sqlController.addUser(queryArgs[1], queryArgs[2], queryArgs[3],queryArgs[4],queryArgs[5],queryArgs[6])){
//                    sendMessageToClient(client, "true");
//                    return;
//                }
//                sendMessageToClient(client, "bad");
//                return;
//
//            case "deleteUser":
//                if (sqlController.deleteUser(queryArgs[1], queryArgs[2], queryArgs[3])){
//                    sendMessageToClient(client, "true");
//                    return;
//                }
//                sendMessageToClient(client, "Error deleting user.");
//                return;
//
//            // Non-functional for now
//            case "login":
//                sendMessageToClient(client, sqlController.getAllDB());
//                return;
//
//            case "updateUser":
//                if(sqlController.checkUserExists(queryArgs[1])){
//                    sqlController.updateUser(queryArgs[1], queryArgs[2], queryArgs[3]);
//                    sendMessageToClient(client,"true");
//                    return;
//                }
//                sendMessageToClient(client,"none");
//                return;
//
//            case "disconnect":
//                sendMessageToClient(client, "disconnected");
//                disconnectClient(client);
//                return;
//
//            case "?":
//                sendMessageToClient(client, "newUser name lastname ID phonenumber email creditcardnumber\n" +
//                        "update ID creditcardnumber subscribernumber" +
//                        "login ID\n" +
//                        "deleteUser ID");
//                return;
//
//            default:
//                sendMessageToClient(client, "Unknown command.");
//        }
//        //this.sendToAllClients(msg);
//    }