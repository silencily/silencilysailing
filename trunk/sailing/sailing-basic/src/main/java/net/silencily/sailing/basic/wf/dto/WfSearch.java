
package net.silencily.sailing.basic.wf.dto;

/**
 * ������������װ��ѯ���������˹�������ϵͳ��������������
 * 
 * @author wenjb
 * @version 1.0
 */
public class WfSearch {

	// ˵����ҵ������
	private String title;

	// ˵��������״̬,�洢��ֵΪWorkflowStatus�ж�Ӧ��KEYֵ
	private String state;

	// ˵�����ύ�û�
	private String committer;

	// ˵������ʼʱ��
	private String startTime;

	// ˵��������ʱ��
	private String endTime;

	public String getCommitter() {
		return committer;
	}

	public void setCommitter(String committer) {
		this.committer = committer;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTitle() {
		return title == null ? null : title.trim();
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
