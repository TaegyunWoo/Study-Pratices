/**
 * author         : 우태균
 * description    :
 */
package practice.spring.websocket.controller;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;
import practice.spring.websocket.dto.GreetingMessage;
import practice.spring.websocket.dto.HelloMessage;

@Controller
public class GreetingController {
  @MessageMapping("/hello")
  @SendTo("/topic/greetings")
  public GreetingMessage greeting(HelloMessage message) throws Exception {
    Thread.sleep(1000); // simulated delay
    return new GreetingMessage("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
  }

  @SubscribeMapping("/greetings")
  public void greetingSub(MessageHeaders headers, MessageHeaderAccessor accessor) throws Exception {
    System.out.println(headers.toString() + "\n");
    System.out.println(accessor.toString());
  }
}
