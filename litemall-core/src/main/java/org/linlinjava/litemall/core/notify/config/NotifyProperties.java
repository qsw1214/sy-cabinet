package org.linlinjava.litemall.core.notify.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "litemall.notify")
public class NotifyProperties {
	private Mail mail;
	private Sms sms;
	private Wx wx;
	private Aliyunsms alisms;

	public Aliyunsms getAlisms() {
		return alisms;
	}

	public void setAlisms(Aliyunsms alisms) {
		this.alisms = alisms;
	}

	public Mail getMail() {
		return mail;
	}

	public void setMail(Mail mail) {
		this.mail = mail;
	}

	public Sms getSms() {
		return sms;
	}

	public void setSms(Sms sms) {
		this.sms = sms;
	}

	public Wx getWx() {
		return wx;
	}

	public void setWx(Wx wx) {
		this.wx = wx;
	}

	public static class Mail {
		private boolean enable;
		private String host;
		private String username;
		private String password;
		private String sendfrom;
		private String sendto;

		public boolean isEnable() {
			return enable;
		}

		public void setEnable(boolean enable) {
			this.enable = enable;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getSendfrom() {
			return sendfrom;
		}

		public void setSendfrom(String sendfrom) {
			this.sendfrom = sendfrom;
		}

		public String getSendto() {
			return sendto;
		}

		public void setSendto(String sendto) {
			this.sendto = sendto;
		}
	}

	public static class Sms {
		private boolean enable;
		private int appid;
		private String appkey;
		private List<Map<String, String>> template = new ArrayList<>();

		public boolean isEnable() {
			return enable;
		}

		public void setEnable(boolean enable) {
			this.enable = enable;
		}

		public int getAppid() {
			return appid;
		}

		public void setAppid(int appid) {
			this.appid = appid;
		}

		public String getAppkey() {
			return appkey;
		}

		public void setAppkey(String appkey) {
			this.appkey = appkey;
		}

		public List<Map<String, String>> getTemplate() {
			return template;
		}

		public void setTemplate(List<Map<String, String>> template) {
			this.template = template;
		}
	}

	public static class Aliyunsms {
		private boolean enable;
		private String key;
		private String appid;
		private String appkey;
		private List<sign> sign = new ArrayList<>();
		
		
		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public boolean isEnable() {
			return enable;
		}

		public void setEnable(boolean enable) {
			this.enable = enable;
		}


		public String getAppid() {
			return appid;
		}

		public void setAppid(String appid) {
			this.appid = appid;
		}

		public String getAppkey() {
			return appkey;
		}

		public void setAppkey(String appkey) {
			this.appkey = appkey;
		}

		public List<sign> getSign() {
			return sign;
		}

		public void setSign(List<sign> sign) {
			this.sign = sign;
		}


	}
	public static class sign{
		private String key;
		private String name;
		private String templatecode;
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getTemplatecode() {
			return templatecode;
		}
		public void setTemplatecode(String templatecode) {
			this.templatecode = templatecode;
		}
		
	}
	public static class Wx {
		private boolean enable;
		private List<Map<String, String>> template = new ArrayList<>();

		public boolean isEnable() {
			return enable;
		}

		public void setEnable(boolean enable) {
			this.enable = enable;
		}

		public List<Map<String, String>> getTemplate() {
			return template;
		}

		public void setTemplate(List<Map<String, String>> template) {
			this.template = template;
		}
	}

}
