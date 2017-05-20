package me.jiangcai.lib.notice.zjy;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * @author CJ
 */
public class FeeHandler extends AbstractZjyResponseHandler<Fee> {

    @Override
    Fee handlePairs(List<NameValuePair> list) {
        Fee fee = new Fee();
        fee.setPoint(list.stream()
                .filter(nameValuePair -> nameValuePair.getName().equalsIgnoreCase("id"))
                .map(nameValuePair -> Integer.parseInt(nameValuePair.getValue()))
                .findAny()
                .orElse(0)
        );
        fee.setName(list.stream()
                .filter(nameValuePair -> nameValuePair.getName().equalsIgnoreCase("short"))
                .map(NameValuePair::getValue)
                .findAny()
                .orElse(null)
        );
        return fee;
    }
}
