package net.silencily.sailing.framework.persistent.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * 用于页面分页的帮助类, 保存当前页面数据的所有行数, 每页行数, 当前页等属性. 这个类通常用于从前端 请求参数中组装当前页数(基于<b>0</b>),
 * 每页行数等属性. 把这个类放到这里是因为底层的服务必须对 性能和容量问题提供解决方案, 而且虽然这个类主要用于前端但没有附加任何表现层的特征,
 * 所以在特定的 条件下也可以支持后端的处理
 * 
 * @author scott
 * @since 2006-4-18
 * @version $Id: Paginater.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 * 
 */
public class Paginater implements Serializable {
	/** 当不需要分页时返回这个实例 */
	public static final Paginater NOT_PAGINATED = new Paginater() {
		public String toString() {
			return "not paginated";
		}
	};

	public static final int DEFAULT_PAGE_SIZE = 14;

	/** 每页行数, 通常在页面中可以修改这个参数 */
	private int pageSize = DEFAULT_PAGE_SIZE;

	/** 当前是第几页, 从<b>0</b>开始计算 */
	private int page;

	/** 所有的行数 */
	private int count;

	/**
	 * 不执行分页操作的标志, 除非设置了这个实例的分页标志. 否则不应该对这个实例执行分页. 在 架构自动执行分页操作时, 因为这个实例是<code>BaseActionForm</code>的一个属性,
	 * 当请 求一个<code>action</code>时, 如果参数中有<code>paginater.page=1</code>等参数,
	 * <code>web</code>层就会组装这个实例, 从而使这个属性成为<code>false</code>. 如果没有
	 * 明确地设置这个实例的属性架构就不执行分页. 在<code>struts</code>架构中判断是否分页 要在<code>RequestProcessor's populate</code>之后执行
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

	/** 返回一页中表现多少行数据 */
	public int getPageSize() {
		return this.pageSize;
	}

	/** 设置当前实例在第<code>page</code>页 */
	public void setPage(int page) {
		this.notPaginated = false;
		this.page = page;
	}

	/** 返回当前实例在第几页 */
	public int getPage() {
		return page;
	}

	/** 设置当前实例共有多少行数据 */
	public void setCount(int count) {
		this.count = count;
		this.notPaginated = false;
	}

	/** 返回所有的数据行数 */
	public int getCount() {
		return count;
	}

	/**
	 * 返回当前实例总共的页数, 如果没有数据返回<b>0</b>, 如果数据不足一页返回<b>1</b>
	 * 
	 * @return 当前实例总共的页数
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
	 * 判断是否是第一页, 如果当前实例没有数据或只有一页返回<code>true</code>
	 * 
	 * @return
	 */
	public boolean isFirst() {
		return getPageCount() < 2 || page == 0;
	}

	/**
	 * 判断是否是最后一页, 如果当前实例没有数据或只有一页返回<code>true</code>
	 * 
	 * @return 是否是最后一页
	 */
	public boolean isLast() {
		return (getPageCount() < 2) || (page == (getPageCount() - 1));
	}

	/** 判断是否存在上一页 */
	public boolean isPreviousPageAvailable() {
		return getPageCount() > 1 && page > 0;
	}

	/** 判断是否存在下一页 */
	public boolean isNextPageAvailable() {
		return getPageCount() > 1 && page < getPageCount() - 1;
	}

	public boolean isNotPaginated() {
		return this.notPaginated;
	}

	/**
	 * 根据传入的LIST参数，设置分页的参数
	 * 因为此方法被调用的时机比较晚，总个数在此之前为0，paginater.page被重定向为0，所以必须重新赋值
	 * 调用此方法，有个很严重的性能陷阱，当数据量非常大的时候，每次取的数据非常多！
	 * 
	 */
	public List dealList(List listTemp, HttpServletRequest request) {

		// 获取paginater.page
		String pageUrl = (request.getParameter("paginater.page")) == null ? "0"
				: ((String) request.getParameter("paginater.page"));

		if (null == listTemp) {
			listTemp = new ArrayList(0);
		}
		// 中间变量
		List list = new ArrayList();
		// 根据传入的LIST，设置行数
		setCount(listTemp.size());
		// 得到已经设置的，每页的行数
		int pageSizeTemp = getPageSize();
		// 总的页数目
		int pageCountTemp = getPageCount();
		// 设置页数,修复页数永远为0的危险
		try {
			if (Integer.parseInt(pageUrl) <= pageCountTemp) {
				setPage(Integer.parseInt(pageUrl));
			}

		} catch (Exception e) {
			setPage(0);
		}
		// 当前是第几页
		int pageTemp = getPage();
		// 逻辑处理
		if (isFirst()) {
			// 首页的处理
			if (listTemp.size() < pageSizeTemp) {
				list = listTemp;
				// 第一页能全部显示,不做处理
			} else {
				// 第一页能不能全部显示,留其前pageSizeTemp行数
				for (int i = 0; i < pageSizeTemp; i++) {
					list.add(listTemp.get(i));
				}
			}
		} else {
			// 最后一页的处理
			if (isLast()) {
				// 计算最后一页
				for (int j = (pageSizeTemp * (pageCountTemp - 1)); j < listTemp
						.size(); j++) {
					list.add(listTemp.get(j));
				}
			} else {
				// 中间页的处理
				int temp = (pageSizeTemp * pageTemp - 1);
				for (int j = temp; j < temp + pageSizeTemp; j++) {
					list.add(listTemp.get(j));
				}
			}
		}
		// 更新当前传入的参数
		return list;
	}

}
