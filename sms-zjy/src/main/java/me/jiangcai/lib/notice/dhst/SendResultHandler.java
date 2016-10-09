package me.jiangcai.lib.notice.dhst;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * @author CJ
 */
public class SendResultHandler extends AbstractZjyResponseHandler<SendResult> {
    @Override
    SendResult handlePairs(List<NameValuePair> list) {
        SendResult fee = new SendResult();
        fee.setNumber(list.stream()
                .filter(nameValuePair -> nameValuePair.getName().equalsIgnoreCase("num"))
                .map(nameValuePair -> Integer.parseInt(nameValuePair.getValue()))
                .findAny()
                .orElse(0)
        );
        fee.setSuccess(list.stream()
                .filter(nameValuePair -> nameValuePair.getName().equalsIgnoreCase("success"))
                .map(NameValuePair::getValue)
                .findAny()
                .orElse(null)
        );
        fee.setFail(list.stream()
                .filter(nameValuePair -> nameValuePair.getName().equalsIgnoreCase("faile"))
                .map(NameValuePair::getValue)
                .findAny()
                .orElse(null)
        );
        return fee;
    }
}
