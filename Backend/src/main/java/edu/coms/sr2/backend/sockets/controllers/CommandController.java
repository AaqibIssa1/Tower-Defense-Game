package edu.coms.sr2.backend.sockets.controllers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import edu.coms.sr2.backend.App;
import edu.coms.sr2.backend.sockets.SocketController;
import edu.coms.sr2.backend.utils.BetterInputStream;

/**
 * Class that maps strings to commands/methods, and deserializes incoming data to match
 * the parameters of the appropriate command
 * @author Nathan
 *
 */
public class CommandController {
	public static final String TAG = CommandController.class.getSimpleName();

	private static HashMap<String, CommandHandler> commandMapping;

	/**
	 * Inner class to provide simple invoke() interface for calling commands 
	 * (takes care of getting the parameter types and deserializing to those types)
	 * @author Nathan
	 *
	 */
	private static class CommandHandler {
		Method method;
		Object target;
		Class<?>[] paramTypes;

		CommandHandler(Method method, Object target) {
			this.method = method;
			this.target = target;
			paramTypes = method.getParameterTypes();
		}

		void invoke(BetterInputStream stream) {
			Object[] params = new Object[paramTypes.length];
			try {
				for (int i = 0; i < params.length; ++i)
					params[i] = stream.read(paramTypes[i]);
				method.invoke(target, params);
			} catch(Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Annotation to mark methods as Socket Commands, mapping them to a string name (default is the method name)
	 * @author Nathan
	 *
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface SocketCommand {
		enum Target { NEW_INSTANCE, SINGLETON }
		String value() default "";
		Target target() default Target.NEW_INSTANCE;
	}

	/**
	 * Initialization method to be run at boot, maps annotated commands in the class path
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public static void init() 
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		commandMapping = new HashMap<>();
		for(Method m : App.reflections.getMethodsAnnotatedWith(SocketCommand.class)){
			SocketCommand annotation = m.getAnnotation(SocketCommand.class);
			commandMapping.put(!annotation.value().isEmpty() ? annotation.value() : m.getName(), 
					new CommandHandler(m, 
							Modifier.isStatic(m.getModifiers()) ? 
								null : 
								getTarget(m.getDeclaringClass(), annotation.target())));
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getTarget(Class<T> clazz, SocketCommand.Target target) 
			throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		switch(target) {
		case NEW_INSTANCE:
			return clazz.newInstance();
		case SINGLETON:
			Method getInstance = clazz.getMethod("getInstance");
			if(!Modifier.isStatic(getInstance.getModifiers()))
				throw new NoSuchMethodException(getInstance.toString() + " is not static.");
			if(getInstance.getReturnType() != clazz) 
				throw new NoSuchMethodException(getInstance.toString() + 
						" does not return type: " + clazz.toString());
			return (T) getInstance.invoke(null);
			default:
				throw new RuntimeException("Unexpected value: " + target);
		}
	}

	/**
	 * Exception class for a command string that is not mapped to a command
	 * @author Nathan
	 *
	 */
	public static class BadCommandMappingException extends RuntimeException {
		private static final long serialVersionUID = 4224345317840097311L;
		public BadCommandMappingException(String command) { 
			super("No method mapped to command string: \"" + command +"\"");
		}
	}
	
	/**
	 * Finds the command for the given command string, and invokes it
	 * @param command The name/mapped name to invoke on the string
	 * @param bodyStream The stream to get command parameters from
	 */
	public static void dispatch(String command, BetterInputStream bodyStream) {
		if(SocketController.getCurrentSocketController().hasRegistered() && !command.equals("arrow"))
			System.out.println("Dispatching command: \"" + command + "\"" + 
				" from client " + SocketController.getThreadClientId());
		CommandHandler handler = commandMapping.get(command);
		if(handler != null)
			handler.invoke(bodyStream);
		else
			throw new BadCommandMappingException(command);
		
		if(SocketController.getCurrentSocketController().hasRegistered() && !command.equals("arrow"))
			System.out.println("Finished command: \"" + command + "\"" + 
				" from client " + SocketController.getThreadClientId());
	}	
}
