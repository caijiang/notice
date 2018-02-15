package me.jiangcai.lib.notice.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author CJ
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailAddress {
    private String name;
    private String email;
}
