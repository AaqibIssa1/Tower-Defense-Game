package edu.coms.sr2.backend;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.coms.sr2.backend.sockets.SocketController;


/**
 * Main application class
 * @author Nathan
 *
 */
@SpringBootApplication
@Component
public class App 
{	
	public static final Reflections reflections = new Reflections(App.class.getPackage().getName(), new TypeAnnotationsScanner(), new SubTypesScanner(), new MethodAnnotationsScanner());
    private static ConfigurableApplicationContext appContext;
    private static ObjectMapper jsonMapper;

    
	/**
	 * Main method, inits our socket controller, and starts the Spring application
	 * @param args
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
    public static void main( String[] args ) throws IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException
    {
        appContext = SpringApplication.run(App.class, args);
        jsonMapper = getBean(Jackson2ObjectMapperBuilder.class).build();
    	SocketController.init();
    }
    
    public static <T> T getBean(Class<T> clazz) {
    	return appContext.getBean(clazz);
    }
    
    /**
     * Convenience method to post asynchronous tasks
     * @param runnable
     */
    public static void runAsync(Runnable runnable) {
    	new Thread(runnable).start();
    }
    
    /**
     * Gets the Jackson mapper being used by Spring
     * @return
     */
    public static ObjectMapper getJsonMapper() {
    	while(jsonMapper == null) {/*spin*/}
    	return jsonMapper;
    }
}
