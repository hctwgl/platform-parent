package fid.platform.modelhelper.socket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

		@Override
		public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
				webSocketHandlerRegistry.addHandler(new Word2VecWebSocketHandler(),"/ws").addInterceptors(new HttpSessionHandshakeInterceptor());
				webSocketHandlerRegistry.addHandler(new Word2VecWebSocketHandler(),"/ws/js").addInterceptors(new HttpSessionHandshakeInterceptor()).withSockJS();
		}


}
