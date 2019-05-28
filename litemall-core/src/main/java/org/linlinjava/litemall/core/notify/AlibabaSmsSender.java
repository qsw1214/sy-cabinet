package org.linlinjava.litemall.core.notify;


import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

public class AlibabaSmsSender {

	public SmsResult sendWithTemplate(String phone, String name, String templatecode, String params,String appid,String appkey) {
		DefaultProfile profile = DefaultProfile.getProfile("default", appid,
				appkey);
		IAcsClient client = new DefaultAcsClient(profile);

		CommonRequest request = new CommonRequest();
		request.setMethod(MethodType.POST);
		request.setDomain("dysmsapi.aliyuncs.com");
		request.setVersion("2017-05-25");
		request.setAction("SendSms");
		request.putQueryParameter("PhoneNumbers", phone);
		request.putQueryParameter("SignName", name);
		request.putQueryParameter("TemplateCode", templatecode);
		request.putQueryParameter("TemplateParam", params);
		try {
			CommonResponse response = client.getCommonResponse(request);
			SmsResult smsResult = new SmsResult();
			smsResult.setSuccessful(true);
			smsResult.setResult(response.getData());
			return smsResult;
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}

		return null;
	}

}
