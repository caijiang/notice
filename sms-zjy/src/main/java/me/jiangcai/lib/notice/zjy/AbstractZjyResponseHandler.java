package me.jiangcai.lib.notice.zjy;

import me.jiangcai.lib.notice.exception.NoticeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author CJ
 */
public abstract class AbstractZjyResponseHandler<T> extends AbstractResponseHandler<T> {

    private static final Log log = LogFactory.getLog(AbstractZjyResponseHandler.class);

    @Override
    public T handleEntity(HttpEntity entity) throws IOException {
        List<NameValuePair> list = getNameValuePairs(entity);

        list.stream()
                .filter(nameValuePair -> !nameValuePair.getValue().equals("成功"))
                .filter(nameValuePair -> !nameValuePair.getValue().equals("发送成功！"))
                .filter(nameValuePair -> nameValuePair.getName().equalsIgnoreCase("err"))
                .findAny()
                .ifPresent(nameValuePair -> {
                    throw new NoticeException(nameValuePair.getValue());
                });

        return handlePairs(list);
    }

    abstract T handlePairs(List<NameValuePair> list);


    private List<NameValuePair> getNameValuePairs(HttpEntity entity) throws IOException {
        String response = EntityUtils.toString(entity, "GB2312");

        log.debug("response:" + response);

        String[] parts = response.split("&");
        return Stream.of(parts)
                .map(input -> {
                    int first = input.indexOf("=");
                    String name = input.substring(0, first);
                    return (NameValuePair) new BasicNameValuePair(name, input.substring(first + 1));
                })
                .collect(Collectors.toList());
    }
}
