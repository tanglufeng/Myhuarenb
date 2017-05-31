package com.abcs.huaqiaobang.util;

import android.os.Handler;
import android.util.Log;

import org.jsoup.helper.HttpConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.net.ssl.HttpsURLConnection;

public class HttpRequest {
	private static final int count = Runtime.getRuntime().availableProcessors() * 3 + 2;
	public static final ExecutorService scheduledTaskFactoryExecutor = Executors
			.newFixedThreadPool(count, new ThreadFactoryTest());// 按指定工厂模式来执行的线程池;

	/** 线程工厂初始化方式二 */
	private static class ThreadFactoryTest implements ThreadFactory {
		@Override
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r);
			thread.setName("XiaoMaGuo_ThreadFactory");
			// thread.setDaemon(true); // 将用户线程变成守护线程,默认false
			return thread;
		}
	}

	public static void sendPost(final String url, final String param,
			final HttpRevMsg rev) {
		scheduledTaskFactoryExecutor.submit(new Runnable() {
			@Override
			public void run() {
				PrintWriter out = null;
				BufferedReader in = null;
				String result = "";
				try {
					URL realUrl = new URL(url);
					// 打开和URL之间的连接
					URLConnection conn = realUrl.openConnection();
					// 设置通用的请求属性
					conn.setRequestProperty("accept", "*/*");
					conn.setRequestProperty("connection", "Keep-Alive");
					conn.setRequestProperty("user-agent",
							"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
					conn.setRequestProperty("Charsert", "UTF-8");
					// 发送POST请求必须设置如下两行
					conn.setDoOutput(true);
					conn.setDoInput(true);
					// 获取URLConnection对象对应的输出流
					out = new PrintWriter(conn.getOutputStream());
					// 发送请求参数
					out.print(param);
					// flush输出流的缓冲
					out.flush();
					// 定义BufferedReader输入流来读取URL的响应
					in = new BufferedReader(new InputStreamReader(conn
							.getInputStream()));
					String line;
					while ((line = in.readLine()) != null) {
						result += line;
					}
				} catch (Exception e) {
					// e.printStackTrace();
				}
				// 使用finally块来关闭输出流、输入流
				finally {
					try {
						if (out != null) {
							out.close();
						}
						if (in != null) {
							in.close();
						}
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					rev.revMsg(result);
				}

			}
		});

	}

	public static void sendPost(final String url, final String param,
								final HttpRevMsg rev,final int timeOut) {
		scheduledTaskFactoryExecutor.submit(new Runnable() {
			@Override
			public void run() {
				PrintWriter out = null;
				BufferedReader in = null;
				String result = "";
				try {
					URL realUrl = new URL(url);
					// 打开和URL之间的连接
					URLConnection conn = realUrl.openConnection();
					conn.setConnectTimeout(timeOut);
					conn.setReadTimeout(timeOut);
					// 设置通用的请求属性
					conn.setRequestProperty("accept", "*/*");
					conn.setRequestProperty("connection", "Keep-Alive");
					conn.setRequestProperty("user-agent",
							"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
					conn.setRequestProperty("Charsert", "UTF-8");
					// 发送POST请求必须设置如下两行
					conn.setDoOutput(true);
					conn.setDoInput(true);
					// 获取URLConnection对象对应的输出流
					out = new PrintWriter(conn.getOutputStream());
					// 发送请求参数
					out.print(param);
					// flush输出流的缓冲
					out.flush();
					// 定义BufferedReader输入流来读取URL的响应
					in = new BufferedReader(new InputStreamReader(conn
							.getInputStream()));
					String line;
					while ((line = in.readLine()) != null) {
						result += line;
					}
				} catch (Exception e) {
					// e.printStackTrace();
				}
				// 使用finally块来关闭输出流、输入流
				finally {
					try {
						if (out != null) {
							out.close();
						}
						if (in != null) {
							in.close();
						}
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					rev.revMsg(result);
				}

			}
		});

	}


	public static void sendPost(final String url, final String param) {
		scheduledTaskFactoryExecutor.submit(new Runnable() {
			@Override
			public void run() {
				PrintWriter out = null;
				BufferedReader in = null;
				String result = "";
				try {
					URL realUrl = new URL(url);
					// 打开和URL之间的连接
					URLConnection conn = realUrl.openConnection();
					// 设置通用的请求属性
					conn.setRequestProperty("accept", "*/*");
					conn.setRequestProperty("connection", "Keep-Alive");
					conn.setRequestProperty("user-agent",
							"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
					conn.setRequestProperty("Charsert", "UTF-8");
					// 发送POST请求必须设置如下两行
					conn.setDoOutput(true);
					conn.setDoInput(true);
					// 获取URLConnection对象对应的输出流
					out = new PrintWriter(conn.getOutputStream());
					// 发送请求参数
					out.print(param);
					// flush输出流的缓冲
					out.flush();
					// 定义BufferedReader输入流来读取URL的响应
					in = new BufferedReader(new InputStreamReader(conn
							.getInputStream()));
					String line;
					while ((line = in.readLine()) != null) {
						result += line;
					}
				} catch (Exception e) {
					// e.printStackTrace();
				}
				// 使用finally块来关闭输出流、输入流
				finally {
					try {
						if (out != null) {
							out.close();
						}
						if (in != null) {
							in.close();
						}
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}

			}
		});

	}

	public static void sendGet(final String url, final String param,
			final HttpRevMsg rev, final int timeOut) {
		scheduledTaskFactoryExecutor.submit(new Runnable() {
			@Override
			public void run() {
				String result = "";
				BufferedReader in = null;
				try {
					String urlNameString = url + "?" + param;
					URL realUrl = new URL(urlNameString);
					// 打开和URL之间的连接
					URLConnection connection = realUrl.openConnection();
					connection.setConnectTimeout(timeOut);
					// 设置通用的请求属性
					connection.setRequestProperty("accept", "*/*");
					connection.setRequestProperty("connection", "Keep-Alive");
					connection
							.setRequestProperty("user-agent",
									"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
					connection.setRequestProperty("Charsert", "UTF-8");
					// 建立实际的连接
					connection.connect();
					// 获取所有响应头字段
					Map<String, List<String>> map = connection
							.getHeaderFields();
					// 定义 BufferedReader输入流来读取URL的响应
					in = new BufferedReader(new InputStreamReader(connection
							.getInputStream()));
					String line;
					while ((line = in.readLine()) != null) {
						result += line;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 使用finally块来关闭输入流
				finally {
					try {
						if (in != null) {
							in.close();
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
				rev.revMsg(result);
			}
		});
	}

	public static void sendGet(final String url, final String param,
			final HttpRevMsg rev) {

		scheduledTaskFactoryExecutor.submit(new Runnable() {
			@Override
			public void run() {
				String result = "";
				BufferedReader in = null;
				try {
					String urlNameString = url + "?" + param;

					Log.e("UrlNameString", urlNameString);

					URL realUrl = new URL(urlNameString);
					// 打开和URL之间的连接
					URLConnection connection = realUrl.openConnection();
					// 设置通用的请求属性
					connection.setRequestProperty("accept", "*/*");
					connection.setRequestProperty("connection", "Keep-Alive");
					connection
							.setRequestProperty("user-agent",
									"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
					connection.setRequestProperty("Charsert", "UTF-8");
					// 建立实际的连接
					connection.connect();
					// 获取所有响应头字段
					Map<String, List<String>> map = connection
							.getHeaderFields();
					// 定义 BufferedReader输入流来读取URL的响应
					in = new BufferedReader(new InputStreamReader(connection
							.getInputStream()));
					String line;
					while ((line = in.readLine()) != null) {
						result += line;
					}
				} catch (Exception e) {
					// e.printStackTrace();
				}
				// 使用finally块来关闭输入流
				finally {
					try {
						if (in != null) {
							in.close();
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
				rev.revMsg(result);
			}
		});

	}
	public static void sendHttpsGet(final String url, final String param,
			final HttpRevMsg rev) {
		
		scheduledTaskFactoryExecutor.submit(new Runnable() {
			@Override
			public void run() {
				String result = "";
				BufferedReader in = null;
				try {
					String urlNameString = url + "?" + param;
					
					Log.e("UrlNameString", urlNameString);
					
					URL realUrl = new URL(urlNameString);
					if ("https".equalsIgnoreCase(realUrl.getProtocol())) {
						SslUtils.ignoreSsl();
					}
					// 打开和URL之间的连接
					URLConnection connection = realUrl.openConnection();
					// 设置通用的请求属性
					connection.setRequestProperty("accept", "*/*");
					connection.setRequestProperty("connection", "Keep-Alive");
					connection
					.setRequestProperty("user-agent",
							"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
					connection.setRequestProperty("Charsert", "UTF-8");
					// 建立实际的连接
					connection.connect();
					// 获取所有响应头字段
					Map<String, List<String>> map = connection
							.getHeaderFields();
					// 定义 BufferedReader输入流来读取URL的响应
					in = new BufferedReader(new InputStreamReader(connection
							.getInputStream()));
					String line;
					while ((line = in.readLine()) != null) {
						result += line;
					}
				} catch (Exception e) {
					// e.printStackTrace();
				}
				// 使用finally块来关闭输入流
				finally {
					try {
						if (in != null) {
							in.close();
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
				rev.revMsg(result);
			}
		});
		
	}

	public static void sendHttpsPost(final String url, final String param,
			final HttpRevMsg rev) {
		scheduledTaskFactoryExecutor.submit(new Runnable() {
			@Override
			public void run() {
				PrintWriter out = null;
				BufferedReader in = null;
				String result = "";
				try {
					URL realUrl = new URL(url);
					if ("https".equalsIgnoreCase(realUrl.getProtocol())) {
						SslUtils.ignoreSsl();
					}
					// 打开和URL之间的连接
					HttpsURLConnection conn = (HttpsURLConnection) realUrl
							.openConnection();
					conn.setRequestMethod("POST");
					// 设置通用的请求属性
					conn.setRequestProperty("accept", "*/*");
					conn.setRequestProperty("connection", "Keep-Alive");
					conn.setRequestProperty("user-agent",
							"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
					conn.setRequestProperty("Charsert", "UTF-8");
					// 发送POST请求必须设置如下两行
					conn.setDoOutput(true);
					conn.setDoInput(true);
					// 获取URLConnection对象对应的输出流
					out = new PrintWriter(conn.getOutputStream());
					// 发送请求参数
					out.print(param);
					// flush输出流的缓冲
					out.flush();
					// 定义BufferedReader输入流来读取URL的响应
					in = new BufferedReader(new InputStreamReader(conn
							.getInputStream()));
					String line;
					while ((line = in.readLine()) != null) {
						result += line;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 使用finally块来关闭输出流、输入流
				finally {
					try {
						if (out != null) {
							out.close();
						}
						if (in != null) {
							in.close();
						}
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					rev.revMsg(result);
				}

			}
		});

	}

}
