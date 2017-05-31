package com.abcs.haiwaigou.view.autosrollup;

/**
 * 自动滚动TextView的数据
 * 
 *
 * @param <T>
 */
public interface AutoScrollData<T> {

	/**
	 * * 获取标题
	 * 
	 * @param data
	 * @return
	 */
	public String getTextTitle(T data);

	/**
	 * 获取内容
	 * 
	 * @param data
	 * @return
	 */
	public String getIcon(T data);
	public String getTextIcon(T data);
	public String getTips(T data);
	public long getTime(T data);

}
