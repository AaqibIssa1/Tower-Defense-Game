package edu.coms.sr2.game.sockets.controllers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.HashMap;

import edu.coms.sr2.game.Application;
import edu.coms.sr2.game.screens.LobbyScreen;
import edu.coms.sr2.game.screens.LoginScreen;
import edu.coms.sr2.game.screens.game.GameScreen;
import edu.coms.sr2.game.screens.game.SpectateScreen;
import edu.coms.sr2.game.screens.social.FriendListScreen;
import edu.coms.sr2.game.utils.BetterInputStream;


public abstract class CommandController {
    public static final String TAG = CommandController.class.getSimpleName();

    private static CommandController[] controllers = {
            new DebugController(), new LobbyScreen.LobbyCommandController(),
            new GameScreen.GameScreenCommandController(), new SpectateScreen.SpectateCommandController(),
            new LoginScreen.LoginCommandController(), new FriendListScreen.FriendListCommandController()
        };
    private static HashMap<String, CommandHandler> commandMapping;

    private static class CommandHandler {
        Method method;
        Object target;
        Class<?>[] paramTypes;
        Object[] params;

        CommandHandler(Method method, Object target) {
            this.method = method;
            this.target = target;
            paramTypes = method.getParameterTypes();
            params = new Object[paramTypes.length];
        }

        void invoke(BetterInputStream stream) {
            try {
                for (int i = 0; i < params.length; ++i)
                    params[i] = stream.read(paramTypes[i]);
                method.invoke(target, params);
            } catch(Exception e) {
                Application.error(TAG, e);
            }
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface SocketCommand {
        String value() default "";
    }

    static { initCommandMapping(); }
    private static void initCommandMapping() {
        commandMapping = new HashMap<>();
        for(CommandController controller : controllers)
            controller.registerCommands();
    }

    private void registerCommands() {
        for(Method m : getClass().getDeclaredMethods())
            if(m.isAnnotationPresent(SocketCommand.class)) {
                String annotatedName = m.getAnnotation(SocketCommand.class).value();
                commandMapping.put(!annotatedName.isEmpty()? annotatedName : m.getName(), new CommandHandler(m, this));
            }
    }

    public static void dispatch(String command, BetterInputStream bodyStream) {
        //Application.log(TAG, "Dispatching command: " + command);
        CommandHandler handler = commandMapping.get(command);
        if(handler != null)
            handler.invoke(bodyStream);
        else
            throw new RuntimeException("No method mapped to command string: \"" + command + "\"");
    }

    public static class EntityIdSetterKey {
        private EntityIdSetterKey() {}
    }

    protected EntityIdSetterKey entityIdSetterKey = new EntityIdSetterKey();
}
