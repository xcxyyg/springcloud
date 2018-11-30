package com.cn.readPic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.stream.FileImageInputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.aip.ocr.AipOcr;

public class Sample {
	//设置APPID/AK/SK
    public static final String APP_ID = "14986616";
    public static final String API_KEY = "0eu60gIt0ckjwdwGzk1oPFLO";
    public static final String SECRET_KEY = "wI4RPLqQ7ocA0He2tR6HiXr2dAnoosLx";
    
    public static byte[] readFile(String path){
        byte[] data = null;
        FileImageInputStream input = null;
        try {
          input = new FileImageInputStream(new File(path));
          ByteArrayOutputStream output = new ByteArrayOutputStream();
          byte[] buf = new byte[1024];
          int numBytesRead = 0;
          while ((numBytesRead = input.read(buf)) != -1) {
          output.write(buf, 0, numBytesRead);
          }
          data = output.toByteArray();
          output.close();
          input.close();
        }
        catch (FileNotFoundException ex1) {
          ex1.printStackTrace();
        }
        catch (IOException ex1) {
          ex1.printStackTrace();
        }
        return data;
      }

    

    public static void main(String[] args) {
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
//        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

//        // 调用接口
//        String path = "C:/Users/Administrator/Desktop/readpic/2.jpg";
//        JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
//        System.out.println(res.toString(2));
        
        
        
     // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
//        options.put("detect_direction", "true");
//        options.put("probability", "true");
        options.put("recognize_granularity", "big");
        
        // 参数为本地路径
        String image = "C:/Users/Administrator/Desktop/readpic/5.jpg";
//        JSONObject res = client.basicAccurateGeneral(image, options);
//        System.out.println(res.toString(2));
        
        
        JSONObject res = client.handwriting(image, options);
        System.out.println(res.toString(2));
        
        // 参数为二进制数组
//        byte[] file = readFile(image);
//        JSONObject res = client.basicAccurateGeneral(file, options);
//        System.out.println(res.toString(2));
        
        // 参数为二进制数组
//        byte[] file = readFile(image);
//        JSONObject res = client.handwriting(file, options);
//        System.out.println(res.toString(2));
        
        JSONObject obj = new JSONObject(res.toString(2));
        JSONArray arr = obj.getJSONArray("words_result");
        for (int i = 0; i < arr.length(); i++) {
        	System.out.println(arr.getJSONObject(i).get("words"));
		}
        
    }
}
