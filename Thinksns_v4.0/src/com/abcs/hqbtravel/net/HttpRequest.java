package com.abcs.hqbtravel.net;//package com.example.hqbtravel.net;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.URI;
//import java.net.URL;
//import java.net.URLConnection;
//
//
//@SuppressWarnings("deprecation")
//public class HttpRequest {
//	/**
//	 * 向指定URL发�?GET方法的请�?
//	 *
//	 * @param url
//	 *            发�?请求的URL
//	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式�?
//	 * @return URL �?��表远程资源的响应结果
//	 */
//	public static String sendGet(String url) {
//		String result = "";
//		BufferedReader in = null;
//		try {
//			URL realUrl = new URL(url);
//			// 打开和URL之间的连�?
//			URLConnection connection = realUrl.openConnection();
//			 // 设置通用的请求属性
//            connection.setRequestProperty("accept", "*/*");
//            connection.setRequestProperty("connection", "Keep-Alive");
//            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
//            Thread.sleep(2000);
//            connection.setConnectTimeout(3000000);
//			connection.connect();
//			in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
//			String line;
//			while ((line = in.readLine()) != null) {
//				result += line;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				if (in != null) {
//					in.close();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return result;
//	}
//
//	@SuppressWarnings("finally")
//	public static String executeGet(String url){
//        BufferedReader in = null;
//        String content = null;
//        try {
//            // 定义HttpClient
//            @SuppressWarnings("resource")
//			HttpClient client = new DefaultHttpClient();
//            // 实例化HTTP方法
//            HttpGet request = new HttpGet();
//            request.setURI(new URI(url));
//            HttpResponse response = client.execute(request);
//            in = new BufferedReader(new InputStreamReader(response.getEntity()
//                    .getContent(),"utf-8"));
//            StringBuffer sb = new StringBuffer("");
//            String line = "";
//            String NL = System.getProperty("line.separator");
//            while ((line = in.readLine()) != null) {
//                sb.append(line + NL);
//            }
//            in.close();
//            content = sb.toString();
//        }catch(Exception e){
//        	e.printStackTrace();
//        }finally {
//            if (in != null) {
//                try {
//                    in.close();// 最后要关闭BufferedReader
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            return content;
//        }
//    }
//
//    public static JSONObject doPost(String url, Object params){
//    	@SuppressWarnings("resource")
//		DefaultHttpClient client = new DefaultHttpClient();
//    	HttpPost post = new HttpPost(url);
//    	JSONObject response = null;
//    	try {
//    		StringEntity s = new StringEntity(params.toString());
//    		s.setContentEncoding("UTF-8");
//    		s.setContentType("application/json");//发送json数据需要设置contentType
//    		post.setEntity(s);
//    		HttpResponse res = client.execute(post);
//    		if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
//    			String result = EntityUtils.toString(res.getEntity());// 返回json格式：
//    			response = new JSONObject(result);
//    		}
//    	} catch (Exception e) {
//    		throw new RuntimeException(e);
//    	}
//    	return response;
//    }
//
////	public static void main(String[] args) {
////		JSONObject json = new JSONObject();
////		JSONArray array = new JSONArray();
////		array.add("3036505");
////		json.put("ids", array);
////		System.err.println(json.toString());
////
////	}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//}
