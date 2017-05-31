package com.abcs.huaqiaobang.ytbt.biz;

/**
* 
* 监控输出日志接口
*/
public interface LogcatObserver {
/**
* 
* @param info
* 输出的日志信息
*/
public void handleLog(String info);
}
