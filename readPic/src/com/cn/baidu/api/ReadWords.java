package com.cn.baidu.api;

import java.util.HashMap;

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
	public String handWriting(String path) {
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
	public String basicAccurateGeneral(String path) {
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
	public String webImage(String path) {
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
	private String json2Str(String res) {
        JSONObject obj = new JSONObject(res);
        JSONArray arr = obj.getJSONArray("words_result");
        String words = "";
        for (int i = 0; i < arr.length(); i++) {
        	words+=arr.getJSONObject(i).get("words")+System.getProperty("line.separator");
		}
        return words;
	}

	public static void main(String[] args) {
		ReadWords read = new ReadWords();
		String path = "C:/Users/Administrator/Desktop/readpic/6.jpg";
//		String path = "C:/Users/Administrator/Desktop/readpic/5.png";
//		System.out.println(read.handWriting(path));
		System.out.println(read.webImage(path));
	}

}
