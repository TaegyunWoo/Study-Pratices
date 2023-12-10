/**
 * author         : 우태균
 * description    :
 */
package practice.spring.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HelloMessage {
  private String name;
}
