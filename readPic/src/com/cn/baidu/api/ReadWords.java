package com.cn.baidu.api;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.aip.ocr.AipOcr;

public class ReadWords {
	
	//设置APPID/AK/SK
    private static final String APP_ID = "14986616";
    private static final String API_KEY = "0eu60gIt0ckjwdwGzk1oPFLO";
    private static final String SECRET_KEY = "wI4RPLqQ7ocA0He2tR6HiXr2dAnoosLx";
    private AipOcr client;
    
	public ReadWords() {
		// 初始化一个AipOcr
		this.client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
	}
	
	/**
	 * 手写
	 * @param path
	 * @return
	 */
	public Map<String,String> handWriting(String path) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("recognize_granularity", "big");
        JSONObject res = this.client.handwriting(path, options);
        System.out.println(res.toString(2));
        return json2Str(res.toString(2));
	}
	
	/**
	 * 高精度通用文字
	 * @param path
	 * @return
	 */
	public Map<String,String> basicAccurateGeneral(String path) {
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("detect_direction", "true");
	    options.put("probability", "true");
		JSONObject res = this.client.basicAccurateGeneral(path, options);
		System.out.println(res.toString(2));
		return json2Str(res.toString(2));
	}
	
	/**
	 * 网络图片
	 * @param path
	 * @return
	 */
	public Map<String,String> webImage(String path) {
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("detect_direction", "true");
		options.put("detect_language", "true");
		JSONObject res = this.client.webImage(path, options);
		System.out.println(res.toString(2));
		return json2Str(res.toString(2));
	}
	
	/**
	 * 返回转文字
	 * @param res
	 * @return
	 */
	private Map<String,String> json2Str(String res) {
        JSONObject obj = new JSONObject(res);
        try{
        	 Object error_code =  obj.get("error_code");
        	 return getErrInfo((String)error_code);
        } catch (Exception e) {
        	Map<String,String> ret = new HashMap<String,String>();
        	ret.put("error_code", "0000");
        	JSONArray arr = obj.getJSONArray("words_result");
            String words = "";
            for (int i = 0; i < arr.length(); i++) {
            	words+=arr.getJSONObject(i).get("words")+System.getProperty("line.separator");
    		}
            ret.put("data", words);
            return ret;
        }
	}
	
	/**
	 * 错误信息
	 * @param error_code
	 * @return
	 */
	private Map<String,String> getErrInfo(String error_code) {
		String msg = "错误";
		switch (error_code.toUpperCase()) {
		case "SDK100":
			msg = "图片大小超限";
			break;
		case "SDK101":
			msg = "图片边长不符合要求";
			break;
		case "SDK102":
			msg = "读取图片文件错误";
			break;
		case "SDK108":
			msg = "连接超时或读取数据超时";
			break;
		case "SDK109":
			msg = "不支持的图片格式";
			break;
		default:
			break;
		}
		Map<String,String> ret = new HashMap<String,String>();
    	ret.put("error_code", error_code);
    	ret.put("error_msg", msg);
		return ret;
	}

	public static void main(String[] args) {
		ReadWords read = new ReadWords();
		String path = "C:/Users/Administrator/Desktop/readpic/6.jpg";
//		String path = "C:/Users/Administrator/Desktop/readpic/5.png";
//		System.out.println(read.handWriting(path));
		System.out.println(read.webImage(path));
	}

}
