package me.jiangcai.lib.notice.supplier;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import me.jiangcai.lib.notice.Content;
import me.jiangcai.lib.notice.JsonHandler;
import me.jiangcai.lib.notice.StatusReport;
import me.jiangcai.lib.notice.To;
import me.jiangcai.lib.notice.ZjyNoticeSupplier;
import me.jiangcai.lib.notice.exception.NoticeException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author CJ
 */
@Service
public class ZjyNoticeSupplierImpl implements ZjyNoticeSupplier {
    private static final Log log = LogFactory.getLog(ZjyNoticeSupplierImpl.class);

    private final ApplicationEventPublisher applicationEventPublisher;
    private final String customerId;
    private final String key;

    @Autowired
    public ZjyNoticeSupplierImpl(Environment environment, ApplicationEventPublisher applicationEventPublisher) {
        customerId = environment.getProperty("me.jiangcai.zyj.cid");
        key = environment.getProperty("me.jiangcai.zyj.key");
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public boolean working() {
        return customerId != null && key != null;
    }

    @Override
    public void send(To to, Content content) throws NoticeException {
        try {
            try (CloseableHttpClient client = createClient()) {
                HttpRequestBase request = newRequest("/SendAPI", HttpGet.METHOD_NAME, toDist(to) + content.asText()
                        , new BasicNameValuePair("mobile", toDist(to))
                        , new BasicNameValuePair("content", content.asText()));
                final JsonNode node = client.execute(request, new JsonHandler());
                if (!node.get("status").asText().equalsIgnoreCase("1"))
                    throw new NoticeException("无法发送到" + node.get("msg").asText());
            }
        } catch (IOException e) {
            throw new NoticeException(e);
        }
    }

    private HttpRequestBase newRequest(String uri, String methodName, String toKey, BasicNameValuePair... pairs)
            throws UnsupportedEncodingException {
        StringBuilder rs = new StringBuilder("http://59.110.53.172/sms.web.api/api/").append(uri);
        // 添加
        if (HttpGet.METHOD_NAME.equalsIgnoreCase(methodName)) {
            rs.append("?cid=").append(customerId);
            for (NameValuePair pair : pairs) {
                rs.append("&").append(pair.getName()).append("=").append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            }
            rs.append("&sign=").append(sign(toKey));
            return new HttpGet(rs.toString());
        } else if (HttpPost.METHOD_NAME.equalsIgnoreCase(methodName)) {
            HttpPost post = new HttpPost(rs.toString());
            ArrayList<NameValuePair> list = new ArrayList<>();
            Collections.addAll(list, pairs);
            list.add(new BasicNameValuePair("cid", customerId));
            list.add(new BasicNameValuePair("sign", sign(toKey)));
            post.setEntity(EntityBuilder.create()
                    .setParameters(list)
                    .build());
            return post;
        } else
            throw new IllegalArgumentException("unsupported for " + methodName);
    }

    @SneakyThrows({NoSuchAlgorithmException.class, UnsupportedEncodingException.class})
    private String sign(String toKey) {
        return new String(Hex.encodeHex(MessageDigest.getInstance("SHA1").digest((toKey + key)
                .getBytes("UTF-8")), true));
    }

    private String toDist(To... tos) {
        return Stream.of(tos)
                .map(To::mobilePhone)
                .collect(Collectors.joining(","));
    }

    @Override
    public void statusReport() throws IOException {
        try (CloseableHttpClient client = createClient()) {
            String random = String.valueOf(System.currentTimeMillis());
            HttpRequestBase request = newRequest("/GetBalanceAPI", HttpPost.METHOD_NAME, random, new BasicNameValuePair("random", random));

            JsonNode node = client.execute(request, new JsonHandler());
            applicationEventPublisher.publishEvent(new StatusReport(this, node.get("balance").asLong()));
        }
    }

    private CloseableHttpClient createClient() {
        return HttpClientBuilder.create()
//                .setHttpProcessor()
                .build();
    }
}
