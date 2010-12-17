package net.silencily.sailing.framework.persistent.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * ����ҳ���ҳ�İ�����, ���浱ǰҳ�����ݵ���������, ÿҳ����, ��ǰҳ������. �����ͨ�����ڴ�ǰ�� �����������װ��ǰҳ��(����<b>0</b>),
 * ÿҳ����������. �������ŵ���������Ϊ�ײ�ķ������� ���ܺ����������ṩ�������, ������Ȼ�������Ҫ����ǰ�˵�û�и����κα��ֲ������,
 * �������ض��� ������Ҳ����֧�ֺ�˵Ĵ���
 * 
 * @author scott
 * @since 2006-4-18
 * @version $Id: Paginater.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 * 
 */
public class Paginater implements Serializable {
	/** ������Ҫ��ҳʱ�������ʵ�� */
	public static final Paginater NOT_PAGINATED = new Paginater() {
		public String toString() {
			return "not paginated";
		}
	};

	public static final int DEFAULT_PAGE_SIZE = 14;

	/** ÿҳ����, ͨ����ҳ���п����޸�������� */
	private int pageSize = DEFAULT_PAGE_SIZE;

	/** ��ǰ�ǵڼ�ҳ, ��<b>0</b>��ʼ���� */
	private int page;

	/** ���е����� */
	private int count;

	/**
	 * ��ִ�з�ҳ�����ı�־, �������������ʵ���ķ�ҳ��־. ����Ӧ�ö����ʵ��ִ�з�ҳ. �� �ܹ��Զ�ִ�з�ҳ����ʱ, ��Ϊ���ʵ����<code>BaseActionForm</code>��һ������,
	 * ���� ��һ��<code>action</code>ʱ, �����������<code>paginater.page=1</code>�Ȳ���,
	 * <code>web</code>��ͻ���װ���ʵ��, �Ӷ�ʹ������Գ�Ϊ<code>false</code>. ���û��
	 * ��ȷ���������ʵ�������Լܹ��Ͳ�ִ�з�ҳ. ��<code>struts</code>�ܹ����ж��Ƿ��ҳ Ҫ��<code>RequestProcessor's populate</code>֮��ִ��
	 * 
	 * @see BaseActionForm#getPaginater()
	 * @see #setCount(int)
	 * @see #setPage(int)
	 * @see #setPageSize(int)
	 */
	private boolean notPaginated = true;

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		this.notPaginated = false;
		if (this.pageSize < 1) {
			this.pageSize = DEFAULT_PAGE_SIZE;
		}
	}

	/** ����һҳ�б��ֶ��������� */
	public int getPageSize() {
		return this.pageSize;
	}

	/** ���õ�ǰʵ���ڵ�<code>page</code>ҳ */
	public void setPage(int page) {
		this.notPaginated = false;
		this.page = page;
	}

	/** ���ص�ǰʵ���ڵڼ�ҳ */
	public int getPage() {
		return page;
	}

	/** ���õ�ǰʵ�����ж��������� */
	public void setCount(int count) {
		this.count = count;
		this.notPaginated = false;
	}

	/** �������е��������� */
	public int getCount() {
		return count;
	}

	/**
	 * ���ص�ǰʵ���ܹ���ҳ��, ���û�����ݷ���<b>0</b>, ������ݲ���һҳ����<b>1</b>
	 * 
	 * @return ��ǰʵ���ܹ���ҳ��
	 */
	public final int getPageCount() {
		int remainder = count % pageSize;
		if (count != 0 && remainder == count) {
			return 1;
		} else if (remainder == 0) {
			return (count / pageSize);
		} else {
			return (count / pageSize) + 1;
		}
	}

	/**
	 * �ж��Ƿ��ǵ�һҳ, �����ǰʵ��û�����ݻ�ֻ��һҳ����<code>true</code>
	 * 
	 * @return
	 */
	public boolean isFirst() {
		return getPageCount() < 2 || page == 0;
	}

	/**
	 * �ж��Ƿ������һҳ, �����ǰʵ��û�����ݻ�ֻ��һҳ����<code>true</code>
	 * 
	 * @return �Ƿ������һҳ
	 */
	public boolean isLast() {
		return (getPageCount() < 2) || (page == (getPageCount() - 1));
	}

	/** �ж��Ƿ������һҳ */
	public boolean isPreviousPageAvailable() {
		return getPageCount() > 1 && page > 0;
	}

	/** �ж��Ƿ������һҳ */
	public boolean isNextPageAvailable() {
		return getPageCount() > 1 && page < getPageCount() - 1;
	}

	public boolean isNotPaginated() {
		return this.notPaginated;
	}

	/**
	 * ���ݴ����LIST���������÷�ҳ�Ĳ���
	 * ��Ϊ�˷��������õ�ʱ���Ƚ����ܸ����ڴ�֮ǰΪ0��paginater.page���ض���Ϊ0�����Ա������¸�ֵ
	 * ���ô˷������и������ص��������壬���������ǳ����ʱ��ÿ��ȡ�����ݷǳ��࣡
	 * 
	 */
	public List dealList(List listTemp, HttpServletRequest request) {

		// ��ȡpaginater.page
		String pageUrl = (request.getParameter("paginater.page")) == null ? "0"
				: ((String) request.getParameter("paginater.page"));

		if (null == listTemp) {
			listTemp = new ArrayList(0);
		}
		// �м����
		List list = new ArrayList();
		// ���ݴ����LIST����������
		setCount(listTemp.size());
		// �õ��Ѿ����õģ�ÿҳ������
		int pageSizeTemp = getPageSize();
		// �ܵ�ҳ��Ŀ
		int pageCountTemp = getPageCount();
		// ����ҳ��,�޸�ҳ����ԶΪ0��Σ��
		try {
			if (Integer.parseInt(pageUrl) <= pageCountTemp) {
				setPage(Integer.parseInt(pageUrl));
			}

		} catch (Exception e) {
			setPage(0);
		}
		// ��ǰ�ǵڼ�ҳ
		int pageTemp = getPage();
		// �߼�����
		if (isFirst()) {
			// ��ҳ�Ĵ���
			if (listTemp.size() < pageSizeTemp) {
				list = listTemp;
				// ��һҳ��ȫ����ʾ,��������
			} else {
				// ��һҳ�ܲ���ȫ����ʾ,����ǰpageSizeTemp����
				for (int i = 0; i < pageSizeTemp; i++) {
					list.add(listTemp.get(i));
				}
			}
		} else {
			// ���һҳ�Ĵ���
			if (isLast()) {
				// �������һҳ
				for (int j = (pageSizeTemp * (pageCountTemp - 1)); j < listTemp
						.size(); j++) {
					list.add(listTemp.get(j));
				}
			} else {
				// �м�ҳ�Ĵ���
				int temp = (pageSizeTemp * pageTemp - 1);
				for (int j = temp; j < temp + pageSizeTemp; j++) {
					list.add(listTemp.get(j));
				}
			}
		}
		// ���µ�ǰ����Ĳ���
		return list;
	}

}
