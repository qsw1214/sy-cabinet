package org.linlinjava.litemall.wx.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.linlinjava.litemall.core.util.ResponseUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 打开箱门
 * @author Administrator
 *
 */

@RestController
@RequestMapping("/wx/box")
@Validated
public class WxOppenBoxController {
	
	@GetMapping("oppen")
	public Object OppenBox(String mainCabNum,String subCabNum) throws IOException{
		String inputLine = null;
		try {
			URL url = new URL("http://box-control.fycld.com/Box/GetBoxOpen?MainNo="+mainCabNum+"&BoxNo="+subCabNum);
			  URLConnection urlcon = url.openConnection();
			  BufferedReader br = new BufferedReader(new InputStreamReader(urlcon.getInputStream()));
			  inputLine = br.readLine();
			  Map<String, Object> data = new HashMap<String, Object>();
		       data.put("data", inputLine);
			return ResponseUtil.ok(data);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return ResponseUtil.fail();
		}
	}
}
