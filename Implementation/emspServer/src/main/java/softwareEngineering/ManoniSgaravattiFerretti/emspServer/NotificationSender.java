package softwareEngineering.ManoniSgaravattiFerretti.emspServer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.AuthManager.LoginManager;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.AuthManager.TokenManager;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingSessionManager.NotificationGenerator;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.UserDataModel.Model.User;

import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class NotificationSender implements WebSocketMessageBrokerConfigurer, HandshakeInterceptor {
    @Autowired
    LoginManager userDetails;
    @Autowired
    TokenManager tokenManager;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    private Map<Long,String> session;
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/notification");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/notification");
        registry.addEndpoint("/notification").withSockJS();
    }


    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
            String token = httpServletRequest.getParameter("token");
            HttpSession session = servletRequest.getServletRequest().getSession();

            String username=tokenManager.extractUsername(token);
            UserDetails userDetails = this.userDetails.loadUserByUsername(username);
            if(tokenManager.validateToken(token, userDetails)){
                attributes.put("sessionId", session.getId());
                this.session.put(((User) userDetails).getId(),session.getId());
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

    public void sendToSpecificUser(NotificationGenerator message) {
        simpMessagingTemplate.convertAndSendToUser(message.getTo().toString(), "/notification", message);
    }
}
