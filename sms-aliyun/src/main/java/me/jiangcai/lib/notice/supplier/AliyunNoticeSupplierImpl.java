package me.jiangcai.lib.notice.supplier;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import me.jiangcai.lib.notice.AliyunNoticeSupplier;
import me.jiangcai.lib.notice.Content;
import me.jiangcai.lib.notice.To;
import me.jiangcai.lib.notice.exception.BadToException;
import me.jiangcai.lib.notice.exception.NoticeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author CJ
 */
@Service
public class AliyunNoticeSupplierImpl implements AliyunNoticeSupplier {

    //产品名称:云通信短信API产品,开发者无需替换
    private static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    private static final String domain = "dysmsapi.aliyuncs.com";
    private final IAcsClient acsClient;

    @Autowired
    public AliyunNoticeSupplierImpl(Environment environment) throws ClientException {
        String accessKeyId = environment.getRequiredProperty("com.aliyun.ram.accessKeyId");
        String accessKeySecret = environment.getRequiredProperty("com.aliyun.ram.accessKeySecret");
        String regionId = environment.getProperty("com.aliyun.ram.regionId", "cn-hangzhou");

        IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint(regionId, regionId, product, domain);
        acsClient = new DefaultAcsClient(profile);
    }

    @Override
    public void send(To to, Content content) throws NoticeException {
//组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(to.mobilePhone());
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(content.signName());
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(content.templateName());
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
//        request.setTemplateParam("{\"name\":\"Tom\", \"code\":\"123\"}");
        final StringBuilder sb = new StringBuilder("{");
        content.templateParameters().forEach((name, value) -> {
            sb.append("\"").append(name).append("\"").append(":");

            if (value == null) {
                sb.append("null");
            } else {
                String str = value.toString();
                // 带有" 的那就是不安全的
                str = str.replaceAll("\"", "\\\"");
                sb.append("\"").append(str).append("\"");
            }
            sb.append(",");
        });
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 1);
        }
        sb.append("}");
        request.setTemplateParam(sb.toString());
        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//        request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        try {
            SendSmsResponse response = acsClient.getAcsResponse(request);
            if (!response.getCode().equalsIgnoreCase("OK")) {
                if (response.getMessage().startsWith("触发"))
                    throw new BadToException(response.getMessage(), to);
                throw new NoticeException(response.getRequestId() + ":" + response.getMessage());
            }
        } catch (ClientException e) {
            throw new NoticeException(e);
        }
    }

    @Override
    public void statusReport() throws IOException {

    }
}
