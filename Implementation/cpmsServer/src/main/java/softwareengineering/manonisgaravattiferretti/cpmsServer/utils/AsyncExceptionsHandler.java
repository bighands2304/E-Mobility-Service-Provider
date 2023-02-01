package softwareengineering.manonisgaravattiferretti.cpmsServer.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class AsyncExceptionsHandler implements AsyncUncaughtExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(AsyncExceptionsHandler.class);

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
        logger.warn("Exception message - " + throwable.getMessage());
        logger.warn("Method name - " + method.getName());
        for (Object param : obj) {
            logger.warn("Parameter value - " + param);
        }
    }
}
