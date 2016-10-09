package me.jiangcai.lib.notice.locate;

import me.jiangcai.lib.notice.Content;
import me.jiangcai.lib.notice.StatusReport;
import me.jiangcai.lib.notice.SubNoticeService;
import me.jiangcai.lib.notice.To;
import me.jiangcai.lib.notice.dhst.Fee;
import me.jiangcai.lib.notice.dhst.FeeHandler;
import me.jiangcai.lib.notice.dhst.SendResult;
import me.jiangcai.lib.notice.dhst.SendResultHandler;
import me.jiangcai.lib.notice.exception.NoticeException;
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
public class DhstSubNoticeService implements SubNoticeService {
    private static final Log log = LogFactory.getLog(DhstSubNoticeService.class);
    private final String urlRoot;
    private final String username;
    private final String password;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public DhstSubNoticeService(Environment environment) {
        String value = environment.getProperty("me.jiangcai.dhst.root", "http://www.dh3t.com/json/sms/Submit");
        if (value.endsWith("/")) {
            urlRoot = value;
        } else
            urlRoot = value + "/";
        username = environment.getRequiredProperty("me.jiangcai.dhst.username");
        password = environment.getRequiredProperty("me.jiangcai.dhst.password");
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
