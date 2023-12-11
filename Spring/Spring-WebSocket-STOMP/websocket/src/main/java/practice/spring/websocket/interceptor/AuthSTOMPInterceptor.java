package practice.spring.websocket.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthSTOMPInterceptor implements ChannelInterceptor {
  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
    log.info("Command: {}", headerAccessor.getCommand());
    log.info("Host: {}", headerAccessor.getHost());
    log.info("SessionID: {}", headerAccessor.getSessionId());
    log.info("Destination: {}", headerAccessor.getDestination());
    log.info("myHeader: {}", headerAccessor.getNativeHeader("myHeader"));

    return message;
  }


}
