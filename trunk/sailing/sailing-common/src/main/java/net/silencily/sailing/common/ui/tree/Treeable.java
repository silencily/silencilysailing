package net.silencily.sailing.common.ui.tree;

/**
 * ��Ϊһ�����Ľڵ㣬��ô��Ӧ�������µ�����
 * 
 */
public interface Treeable extends Comparable {

	/**
	 * ��id
	 * <p>
	 * ����ڵ�ĸ�idΪ��
	 * @return
	 */
	public String getParentId();
	
	/**
	 * ˳��
	 * @return
	 */
	public Integer getSequence();
	
	/**
	 * identity
	 * @return
	 */
	public String getId();
	
	/**
	 * ���ýڵ�Ĳ���
	 * @param level
	 */
	public void setLevel(Integer level);
	
}
