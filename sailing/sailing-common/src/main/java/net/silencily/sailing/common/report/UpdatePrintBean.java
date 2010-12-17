package net.silencily.sailing.common.report;

public class UpdatePrintBean {
	/**
	 * 更新内容
	 */
	private String value;
	
	/**
	 * 项目横坐标
	 */
	private int xLocation;
	
	/**
	 * 项目纵坐标
	 * 
	 */
	private int yLocation;
	
	/**
	 * 对齐方式
	 */
	private String align;
	
	/**
	 * 应用样式:默认 宋体9号标准字：
	 * 0：宋体10号加粗；
	 * 1：宋体16号加粗 
	 * 2：宋体20号加粗；
	 * 3：宋体24号加粗；
	 */
	private String style;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getXLocation() {
		return xLocation;
	}
	public void setXLocation(int location) {
		xLocation = location;
	}
	public int getYLocation() {
		return yLocation;
	}
	public void setYLocation(int location) {
		yLocation = location;
	}
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}

	public void setStyle(String style) {
		this.style = style;
	}
	public String getStyle() {
		return style;
	}

}
