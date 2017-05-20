package me.jiangcai.lib.notice.supplier;

import me.jiangcai.lib.notice.Content;
import me.jiangcai.lib.notice.StatusReport;
import me.jiangcai.lib.notice.To;
import me.jiangcai.lib.notice.ZjyNoticeSupplier;
import me.jiangcai.lib.notice.exception.NoticeException;
import me.jiangcai.lib.notice.zjy.Fee;
import me.jiangcai.lib.notice.zjy.FeeHandler;
import me.jiangcai.lib.notice.zjy.SendResult;
import me.jiangcai.lib.notice.zjy.SendResultHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
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

/**
 * @author CJ
 */
@Service
public class ZjyNoticeSupplierImpl implements ZjyNoticeSupplier {
    private static final Log log = LogFactory.getLog(ZjyNoticeSupplierImpl.class);
    private final String urlRoot;
    private final String username;
    private final String password;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public ZjyNoticeSupplierImpl(Environment environment, ApplicationEventPublisher applicationEventPublisher) {
        String value = environment.getProperty("me.jiangcai.zjy.root", "http://www.zjysms.com/");
        if (value.endsWith("/")) {
            urlRoot = value;
        } else
            urlRoot = value + "/";
        username = environment.getProperty("me.jiangcai.zjy.username", (String) null);
        password = environment.getProperty("me.jiangcai.zjy.password", (String) null);
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public boolean working() {
        return username != null && password != null;
    }

    @Override
    public void send(To to, Content content) throws NoticeException {
        try {
            try (CloseableHttpClient client = createClient()) {
                HttpGet get = newGet("send/gsend.asp"
                        , new BasicNameValuePair("name", username)
                        , new BasicNameValuePair("pwd", password)
                        , new BasicNameValuePair("dst", toDist(to))
                        , new BasicNameValuePair("msg", content.asText())
                );
                SendResult result = client.execute(get, new SendResultHandler());
//                applicationEventPublisher.publishEvent(new StatusReport(this, fee));
                if (result.getNumber() == 0)
                    throw new NoticeException("无法发送到" + result.getFail());
            }
        } catch (IOException e) {
            throw new NoticeException(e);
        }
    }

    private String toDist(To... tos) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < tos.length; i++) {
            if (i > 0)
                stringBuilder.append(",");
            stringBuilder.append(tos[i].mobilePhone());
        }
        return stringBuilder.toString();
    }

    @Override
    public void statusReport() throws IOException {
        try (CloseableHttpClient client = createClient()) {
            HttpGet get = newGet("send/getfee.asp"
                    , new BasicNameValuePair("name", username)
                    , new BasicNameValuePair("pwd", password));
            Fee fee = client.execute(get, new FeeHandler());
            applicationEventPublisher.publishEvent(new StatusReport(this, fee));
        }
    }

    private HttpGet newGet(String uri, NameValuePair... parameters) throws UnsupportedEncodingException {
        StringBuilder stringBuilder = new StringBuilder(urlRoot);
        stringBuilder.append(uri);
        for (int i = 0; i < parameters.length; i++) {
            if (i == 0)
                stringBuilder.append("?");
            else
                stringBuilder.append("&");
            stringBuilder.append(parameters[i].getName());
            stringBuilder.append("=");
            stringBuilder.append(URLEncoder.encode(parameters[i].getValue(), "GB2312"));
        }
        return new HttpGet(stringBuilder.toString());
    }

    private CloseableHttpClient createClient() {
        return HttpClientBuilder.create()
//                .setHttpProcessor()
                .build();
    }
}
