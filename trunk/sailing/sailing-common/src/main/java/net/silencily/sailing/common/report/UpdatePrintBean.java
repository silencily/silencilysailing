package net.silencily.sailing.common.report;

public class UpdatePrintBean {
	/**
	 * ��������
	 */
	private String value;
	
	/**
	 * ��Ŀ������
	 */
	private int xLocation;
	
	/**
	 * ��Ŀ������
	 * 
	 */
	private int yLocation;
	
	/**
	 * ���뷽ʽ
	 */
	private String align;
	
	/**
	 * Ӧ����ʽ:Ĭ�� ����9�ű�׼�֣�
	 * 0������10�żӴ֣�
	 * 1������16�żӴ� 
	 * 2������20�żӴ֣�
	 * 3������24�żӴ֣�
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
