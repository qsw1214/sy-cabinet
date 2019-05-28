package org.linlinjava.litemall.wx.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallFeedback;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 生成二维码
 */
@RestController
@RequestMapping("/wx/qrcode")
@Validated
public class WxQrcodeController {
	private final Log logger = LogFactory.getLog(WxQrcodeController.class);

	/**
	 * 獲取token
	 * 
	 * @return
	 */
	@PostMapping("gettoken")
	public Object gettoken() {

		try {
			URL url = new URL(
					"https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxa5b486c6b918ecfb&secret=e04004829d4c383b4db7769d88dfbca1");
			InputStream in = url.openStream();
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader bufr = new BufferedReader(isr);
			Map<Object, Object> result = new HashMap<Object, Object>();
			result.put("token", bufr.readLine());
			bufr.close();
			isr.close();
			in.close();
			return ResponseUtil.ok(result);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.fail();
		}

	}

	   /*
     * 获取二维码
　　　* 这里的 post 方法 为 json post【重点】
     */
    @RequestMapping("/getqrcode")
    public String getqrcode(@RequestBody LitemallFeedback feedback) throws Exception {
        
        String imei ="erweima";
        String token =  feedback.getContent();   // 得到token
        
        Map<String, Object> params = new HashMap<>();
        params.put("scene", feedback.getMobile());  //参数
        params.put("page", "page/index/index"); //位置
        params.put("width", 430);

        CloseableHttpClient  httpClient = HttpClientBuilder.create().build();

        HttpPost httpPost = new HttpPost("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+token);  // 接口
        httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
        JSONObject paramJson = new JSONObject(params);
        String body = paramJson.toString();           //必须是json模式的 post      
        StringEntity entity;
        entity = new StringEntity(body);
        entity.setContentType("image/png");
        httpPost.setEntity(entity);
        HttpResponse response;
        response = httpClient.execute(httpPost);
        InputStream inputStream = response.getEntity().getContent();
        String name = imei+".png";
        saveToImgByInputStream(inputStream,"storage/",name);  //保存图片
        return null;
    }
    /**
     * 将二进制转换成文件保存
     * @param instreams 二进制流
     * @param imgPath 图片的保存路径
     * @param imgName 图片的名称
     * @return 
     *      1：保存正常
     *      0：保存失败
     */
    public static int saveToImgByInputStream(InputStream instreams,String imgPath,String imgName){
        int stateInt = 1;
        if(instreams != null){
            try {
                File file=new File(imgPath,imgName);//可以是任何图片格式.jpg,.png等
                FileOutputStream fos=new FileOutputStream(file);
                byte[] b = new byte[1024];
                int nRead = 0;
                while ((nRead = instreams.read(b)) != -1) {
                    fos.write(b, 0, nRead);
                }
                fos.flush();
                fos.close();                
            } catch (Exception e) {
                stateInt = 0;
                e.printStackTrace();
            } finally {
            }
        }
        return stateInt;
    }
}
