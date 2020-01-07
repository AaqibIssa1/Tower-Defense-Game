package edu.coms.sr2.backend.sockets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import edu.coms.sr2.backend.App;
import edu.coms.sr2.backend.Constants;
import edu.coms.sr2.backend.exceptions.ExceptionPrinter;
import edu.coms.sr2.backend.exceptions.GeneralException;
import edu.coms.sr2.backend.services.ProfileService;
import edu.coms.sr2.backend.sockets.controllers.CommandController;
import edu.coms.sr2.backend.sockets.controllers.CommandController.SocketCommand;
import edu.coms.sr2.backend.utils.BetterInputStream;
import edu.coms.sr2.backend.utils.BetterOutputStream;
import edu.coms.sr2.backend.utils.Command;

/**
 * Class to handle the server socket connections. Server Socket is handled in the static scope, 
 * and all individual client connection sockets are handled in their own instance of this class.
 * @author Nathan
 *
 */
public class SocketController {

	private static ServerSocket serverSocket;	
	private static Map<Integer, SocketController> controllersPerClientId = new HashMap<>();
	private static Map<Thread, SocketController> controllersPerThread = new HashMap<>();
	
	private Socket clientSocket;
	private int clientId;
	private boolean hasRegistered = false;
	private BetterInputStream inputStream;
	private BetterOutputStream outputStream;
	private boolean isDisconnecting = false;
	
	private ArrayList<ConnectionLifecycleListener> connectionListeners = new ArrayList<>();

	
	/**
	 * Constructor to create a new controller for a client connection
	 * @param clientSocket
	 * @param clientId
	 * @throws IOException
	 */
	private SocketController(Socket clientSocket) throws IOException
	{
		this.clientSocket = clientSocket;
		inputStream = new BetterInputStream(clientSocket.getInputStream());
		outputStream = new BetterOutputStream(clientSocket.getOutputStream());
	}

	/**
	 * Static initialization method, starts up the main server socket, inits the command controller, starts listening and pinging server time
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public static void init() throws IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException 
	{
		serverSocket = new ServerSocket(Constants.socketPort);
		CommandController.init();
		listenForConnections();
		sendTime();
	}

	/**
	 * Code to run on a new client connection
	 * @param clientSocket
	 * @throws IOException
	 */
	private static void onConnect(Socket clientSocket) throws IOException {
		SocketController controller = new SocketController(clientSocket);
		controller.listen();
	}
	
	@SocketCommand
	public static void login(int playerId) throws IOException {
		SocketController controller = getCurrentSocketController();
		if(!ProfileService.getInstance().isOnline(playerId)) {
			ProfileService.getInstance().setOffline(controller.clientId);
			ProfileService.getInstance().setOnline(playerId);
			controller.clientId = playerId;
			controllersPerClientId.put(playerId, controller);
			controller.hasRegistered = true;
		}
		else {
			controller.send("alreadyLoggedIn");
		}
	}
	
	@SocketCommand
	public static void logout() {
		SocketController controller = getCurrentSocketController();
		for(ConnectionLifecycleListener listener : controller.connectionListeners)
			listener.onLogout();
		
		ProfileService.getInstance().setOffline(controller.clientId);
		controllersPerClientId.remove(controller.clientId);
		controller.hasRegistered = false;
	}
	
	/**
	 * Code to run on a client disconnect
	 */
	private void onDisconnect() {
		System.out.println("Client " + clientId + " disconnected.");
		for(ConnectionLifecycleListener listener : connectionListeners) 
			listener.onDisconnect();
		
		logout();

		try {
			clientSocket.close();
		} catch (IOException e) {
			ExceptionPrinter.accept(e);
		}
	}
	
	public interface ConnectionLifecycleListener {
		default void onDisconnect() {}
		default void onLogout() {}
	}
	
	public boolean addConnectionListener(ConnectionLifecycleListener listener) {
		return connectionListeners.add(listener);
	}
	
	/**
	 * Starts sending time to all connections
	 */
	private static void sendTime() {
		new Timer().scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				try {
					sendToAll("set_time", LocalTime.now());	
				} catch (IOException e) {
					ExceptionPrinter.accept(e);
				}
			}
		
		}, 0l, 1000l);
	}
	
	/**
	 * Aynchronously listen for and handle connections
	 */
	private static void listenForConnections() 
	{
		App.runAsync(()->
		{
			while(true) 
			{
				try {
					onConnect(serverSocket.accept());
				} catch (IOException e) {
					ExceptionPrinter.accept(e);
				}
			}
		});
	}

	/**
	 * Asynchronously listen to current client connection (non-static, per-client)
	 */
	private void listen() 
	{
		App.runAsync(()-> 
		{
			controllersPerThread.put(Thread.currentThread(), this);
			while(clientSocket.isConnected()) 
			{
                try {
                	String command = inputStream.readUTF();
                	//Timer timer = scheduleDisconnect();
                    CommandController.dispatch(command, inputStream);
    				//timer.cancel();
                } catch (Exception e) {
                	ExceptionPrinter.accept(e);
                	if(isFatal(e))
                		break;
                }
			}
    		onDisconnect();
			controllersPerThread.remove(Thread.currentThread());
		});
	}
	
//	private Timer scheduleDisconnect() {
//		Timer timer = new Timer();
//    	timer.schedule(new TimerTask(){
//			@Override
//			public void run() {
//				System.out.println("Client " + clientId + " socket controller timed out.");
//				onDisconnect();
//			}
//		}, 5000l);
//    	return timer;
//	}
	
	private boolean isFatal(Exception e) {
		 return true; //TODO
	}
	
	/**
	 * Converts data into bytes
	 * @param params Objects/data
	 * @return Corresponding byte array
	 * @throws IOException
	 */
	public static byte[] toBytes(Object... params) throws IOException {
		ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();
		BetterOutputStream outputStream = new BetterOutputStream(outputBytes);
		
        for(Object param : params)
        	outputStream.write(param);
        
        outputStream.close();
        
        return outputBytes.toByteArray();
	}

	/**
	 * Sends data/commands to all client connections
	 * @param params
	 * @throws IOException
	 */
	public static void sendToAll(Object... params) throws IOException {
		byte[] bytes = toBytes(params);
		for(SocketController controller : controllersPerClientId.values())
			controller.send(bytes);
	}
	
	/**
	 * Sends data/commands to all client connections, but not to the specified device
	 * @param params
	 * @throws IOException
	 */
	public static void sendToAllBut(int badId, Object... params) throws IOException {
		byte[] bytes = toBytes(params);
		for(SocketController controller : controllersPerClientId.values())
			if(controller.clientId != badId)
				controller.send(bytes);
	}
	
	/**
	 * Sends data/commands to this client connection (non-static)
	 * @param params
	 * @throws IOException
	 */
	public synchronized void send(Object... params) {
		try {
			for(Object param : params)
				outputStream.write(param);
		} catch (IOException e) {
			ExceptionPrinter.accept(e);
			if(!isDisconnecting)
				onDisconnect();
			isDisconnecting = true;
		}
	}

    /**
     * Semds command wrapper to this client connection 
     * @param command
     * @throws IOException
     */
    public void send(Command command) throws IOException {
        send(command.getName(), command.getParams());
    }
    
    public static SocketController getConnection(int clientId) {
    	return controllersPerClientId.get(clientId);
    }
    
    public static void sendToClient(int clientId, Object...params) throws IOException {
    	Objects.requireNonNull(getConnection(clientId)).send(params);
    }
    
    public static void sendToClients(Iterable<Integer> clientIds, Object... params) throws IOException {
    	byte[] bytes = toBytes(params);
    	for(Integer id : clientIds)
    		sendToClient(id, bytes);
    }
    
    public static void trySendToClient(int clientId, Object...params) throws IOException {
    	SocketController connection = getConnection(clientId);
    	if(connection != null)
    		connection.send(params);
    }
    
    public static void trySendToClients(Iterable<Integer> clientIds, Object... params) throws IOException {
    	byte[] bytes = toBytes(params);
    	for(Integer id : clientIds)
    		trySendToClient(id, bytes);
    }
    
    public static SocketController getSocketControllerForClient(int clientId) {
    	return controllersPerClientId.get(clientId);
    }
    
    public static SocketController getCurrentSocketController() {
    	return controllersPerThread.get(Thread.currentThread());
    }
    
    public int getClientId() {
    	if(hasRegistered)
    		return clientId;
    	else
    		throw new GeneralException("Client thread has not registered itself.");
    }
    
    public static int getThreadClientId() {
    	return getCurrentSocketController().getClientId();
    }

	public boolean hasRegistered() {
		return hasRegistered;
	}
}
