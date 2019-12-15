
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.gugua.utils.utils.PropertiesLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: 袁重阳
 * @description: 文件上传
 * @create: 2019-11-28 18:16
 * @modifyBy: 袁重阳
 * @modifyAt: 2019-11-28 18:16
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "file")
public class FileUpload {

    @ResponseBody
    @RequestMapping(value = "upload",method = RequestMethod.GET)
    public AssumeRoleResponse.Credentials upload() throws ClientException {
        PropertiesLoader propertiesLoader = new PropertiesLoader("aliyun.properties");
        IClientProfile profile = DefaultProfile.getProfile(propertiesLoader.getProperty("aliyun.REGION_CN_HANGZHOU"), propertiesLoader.getProperty("aliyun.accessKeyId"), propertiesLoader.getProperty("aliyun.accessKeySecret"));
        DefaultAcsClient client = new DefaultAcsClient(profile);

        // 创建一个 AssumeRoleRequest 并设置请求参数
        final AssumeRoleRequest request = new AssumeRoleRequest();
        request.setVersion(propertiesLoader.getProperty("aliyun.STS_API_VERSION"));
        request.setMethod(MethodType.POST);
        request.setProtocol(ProtocolType.HTTPS);

        request.setRoleArn(propertiesLoader.getProperty("aliyun.roleArn"));
        request.setRoleSessionName(propertiesLoader.getProperty("aliyun.roleSessionName"));
        request.setPolicy(propertiesLoader.getProperty("aliyun.policy"));
        request.setDurationSeconds(Long.parseLong(propertiesLoader.getProperty("aliyun.durationSeconds")));
        // 发起请求，并得到response
        final AssumeRoleResponse response = client.getAcsResponse(request);
        return response.getCredentials();
    }
}