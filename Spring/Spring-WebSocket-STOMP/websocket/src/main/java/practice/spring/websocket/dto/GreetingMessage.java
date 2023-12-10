/**
 * author         : 우태균
 * description    :
 */
package practice.spring.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GreetingMessage {
  private String content;
}
