package net.silencily.sailing.common.basiccode.web;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.common.basiccode.service.CommonBasicCodeInitService;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.common.dict.domain.CommonBasicCode;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.web.struts.BaseDispatchAction;
import net.silencily.sailing.utils.Tools;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 * 基础编码初始化类。 可通过theForm.message来得到返回的处理结果信息。
 * 通过theForm.flag[]来取得画面按钮活性化状态，这个数组有长度为9，分别对应每一个子系统的按钮活性化状态。
 * 子系统不能进行操作时，对应位数组内容为disabled，否则为空。
 * 方法可以针对每一个子系统分别对其基础编码数据进行初始化，也可以分别删除每个子系统的基础编码全部数据。
 * 
 * @author 李鹤
 * 
 */
public class CommonBasicCodeInitAction extends BaseDispatchAction {
	/**
	 * 取得服务
	 * 
	 * @return 服务
	 */
	public static CommonBasicCodeInitService service() {
		return (CommonBasicCodeInitService) ServiceProvider
				.getService(CommonBasicCodeInitService.SERVICE_NAME);
	}
	/**
	 * 
	 * 功能描述 取得共通的服务
	 * @return 共通的service
	 * 2007-11-19 下午06:07:45
	 * @version 1.0
	 * @author lihe
	 */
	public static CommonService commonService() {
		return (CommonService) ServiceProvider
				.getService(CommonService.SERVICE_NAME);
	}

	/**
	 * 进入页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CommonBasicCodeInitForm theForm = (CommonBasicCodeInitForm) form;
		theForm.setFlag(getActiveFlag());
		return mapping.findForward("init");
	}

	/**
	 * 把Properties文件中的内容添加到数据库中
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return 跳转信息
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CommonBasicCodeInitForm theForm = (CommonBasicCodeInitForm) form;
		Properties propFile;
		String id = null;
		// 子系统分类取得
		id = request.getParameter("oid");
		// 初始化Properties文件，得到Properties类的对象
		propFile = fileRead(id);
		if (null == propFile) {
			theForm.setMessage("  properties文件错误或没有找到!失败！");
			theForm.setFlag(getActiveFlag());
			return mapping.findForward("init");
		}

		// 删除该子系统所有基础编码数据
		service().reset(id.toUpperCase());
		// 对得到的处理对象进行处理(追加到数据库)
		if (dealing(propFile)) {
			theForm.setMessage(" 数据更新成功!");
		} else {
			theForm.setMessage(" 没有数据更新!");
		}
		// 获得没有初始化的子系统
		theForm.setFlag(getActiveFlag());
		return mapping.findForward("init");
	}

	/**
	 * 将已有数据删除，回到系统初始化状态
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward reInit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CommonBasicCodeInitForm theForm = (CommonBasicCodeInitForm) form;
		// 初始化选中的子系统，并设置处理结果
		theForm.setMessage(service().reset(request.getParameter("subID")));
		// 获得没有初始化的子系统
		theForm.setFlag(getActiveFlag());
		return mapping.findForward("init");
	}

	/**
	 * 获得没有初始化的子系统
	 * 
	 * @return 判断子系统基础编码是否初始化的字符串数组
	 */
	private String[] getActiveFlag() {
		// 安全性检查
		if (service().find("root").size() == 0) {
			CommonBasicCode bc = new CommonBasicCode();
			bc.setId("root");
			bc.setDisplayName("系统编码维护");
			bc.getParent().setId("");
			commonService().saveOrUpdate(bc);
		}
		// 子系统基础编码是否初始化的字符串数组初始化
		String flag[] = { "", "", "", "", "", "", "", "", "", "" };
		// 子系统代码
		String b[] = { "HR", "GR", "AM", "RM", "FM", "ST", "UA", "OF", "SM", "CM" };
		List l = null;
		for (int i = 0; i < 10; i++) {
			l = service().load(b[i]);
			if (l.size() > 0) {
				// 如果已经初始化，把按钮非活性化
				flag[i] = "disabled";
			}
		}
		return flag;
	}

	/**
	 * 读取Properties文件
	 * 
	 * @param id
	 *            子系统分类id
	 * @return Properties类对象,处理失败返回null
	 */
	private Properties fileRead(String id) {
//		String path = this.getClass().getResource("/").getPath();
//      String fileName = path + "/basiccode/" + id + "BasicCode.properties";
        String path = this.getServlet().getServletContext().getRealPath("/");
        String fileName = path + "/WEB-INF/classes/basiccode/" + id + "BasicCode.properties";
		Properties dealRes = new Properties();

		try {
			FileInputStream FIS = new FileInputStream(fileName);
			dealRes.load(FIS);
		} catch (FileNotFoundException e) {
			// 读取文件异常
			return null;
		} catch (IOException e) {
			// Properties文件加载异常
			return null;
		}

		return dealRes;
	}

	/**
	 * 把Properties中的内容更新到数据中
	 * 
	 * @param propFile
	 *            要处理的文件对象
	 * @return 处理成功返回true，失败false
	 */
	private boolean dealing(Properties propFile) {
		// TypeCode的key
		String keyID = "key";
		// 对就的TypeCode的值
		String keyRs = null;
		// 对应于TypeCode的显示名称的key
		String valID = "val";
		// 对应于TypeCode的显示名称的值
		String valRs = null;
		// 更新用的bean
		CommonBasicCode objMe = null;
		// 执行更新的标志，true时更新
		boolean flag = true;
		// 最大条数
		int max = propFile.size();
		max = max / 2;
		if (max == 0) {
			return false;
		}
		// 要更新的字段
		String id = null;
		String subid = null;
		String typeCode = null;
		String codeName = null;
		CommonBasicCode parent = new CommonBasicCode();
		CommonBasicCode parentMem = new CommonBasicCode();
		// 处理循环
		for (int i = 1; i <= max; i++) {
			// 更新执行
			flag = true;
			// 如果文件中包含key为keyID的记录
			if (propFile.containsKey(keyID)) {
				// 取得对应的TypeCode
				keyRs = propFile.getProperty(keyID);
				// 如果文件中包含key为valID的记录
				if (propFile.containsKey(valID)) {
					// 取得对应的displayName
					valRs = propFile.getProperty(valID);
				} else {
					// 如果不存在，不执行更新操作
					flag = false;
				}
			} else {
				// 如果不存在，不执行更新操作
				flag = false;
			}
			if (flag) {
				// 更新用的bean初始化
				objMe = new CommonBasicCode();
				// 字符串转换
				try {
					valRs = new String(valRs.getBytes("ISO-8859-1"), "GBK");
				} catch (UnsupportedEncodingException e) {
					// 字符串转换异常
					e.printStackTrace();
				}
				// 如果是根结点
				if (keyID.equals("key")) {
					parent = (CommonBasicCode)commonService().load(CommonBasicCode.class, "root");
					// parent = null;
					subid = keyRs;
					id = subid;
					objMe.setDisplayName(valRs);
					objMe.setDeleteState("0");
					objMe.setLayerNum(Integer.valueOf("1"));
				} else {// 非根结点
					parent = parentMem;
					id=Tools.getPKCode();
				}
				typeCode = keyRs;
				codeName = valRs;
				// 存在三层结点
				if (keyRs.indexOf(",") > 0) {
					this.theThird(subid, typeCode, codeName, parent);
				} else {
					// 更新用的bean设置值
					objMe.setSubid(subid);
					objMe.setDeleteState("0");
					if(typeCode.indexOf("#")>=0){
						objMe.setDeleteState("2");
						typeCode = typeCode.substring(1);
					}
					objMe.setId(id);
					objMe.setTypeCode(typeCode);
					objMe.setName(codeName);
					objMe.setParent(parent);
					if(null==objMe.getLayerNum()){
						objMe.setLayerNum(Integer.valueOf("2"));
					}
					// 执行更新操作
					try {
						commonService().saveOrUpdate(objMe);
					} catch (RuntimeException e) {
						
						e.printStackTrace();
					}
				}
				// 记录根结点
				if (keyID.equals("key")) {
					parentMem = objMe;
				}
				// 设置下一条记录的key
				keyID = "key" + i;
				valID = "val" + i;
			}
		}
		return true;
	}

	/**
	 * 添加三级结点
	 * 
	 * @param id
	 *            ID
	 * @param subid
	 *            子系统ID
	 * @param typeCode
	 *            typeCode
	 * @param codeName
	 *            显示的名称
	 * @param parent
	 *            父结点ID
	 */
	private void theThird(String subid, String typeCode,
			String codeName, CommonBasicCode parent) {
		// 更新用的bean
		CommonBasicCode objMe = new CommonBasicCode();
		// typeCode
		String typeCd = typeCode;
		// 第二节点名称
		String secondName = codeName;
		// 三层结点code
		String code = "";
		// 三层结点名称
		String thirdName = "";
		// 更新二层结点
		typeCd = typeCode.substring(0, typeCode.indexOf(","));
		secondName = codeName.substring(0, codeName.indexOf(","));
		objMe.setSubid(subid);
		objMe.setDeleteState("0");
		if(typeCd.indexOf("#")>=0){
			objMe.setDeleteState("2");
			typeCd = typeCd.substring(1);
		}
		objMe.setId(Tools.getPKCode());
		objMe.setTypeCode(typeCd);
		objMe.setName(secondName);
		objMe.setParent(parent);
		objMe.setLayerNum(Integer.valueOf("2"));
		// 执行更新操作
		commonService().saveOrUpdate(objMe);
		// 更新第三层结点
		parent = objMe;
		int i = 0;
		while (true) {
			i++;
			// 还有未更新的三层结点
			if (typeCode.indexOf(",") > 0) {
				typeCode = typeCode.substring(typeCode.indexOf(",") + 1);
				codeName = codeName.substring(codeName.indexOf(",") + 1);
			} else {
				// 全更新完，退出循环
				break;
			}
			objMe = new CommonBasicCode();
			// 不是最后一个三层结点
			if (typeCode.indexOf(",") > 0) {
				code = typeCode.substring(0, typeCode.indexOf(","));
				thirdName = codeName.substring(0, codeName.indexOf(","));
			} else {
				code = typeCode;
				thirdName = codeName;
			}
			objMe.setId(Tools.getPKCode());
			objMe.setShowSequence(new Integer(i));
			objMe.setSubid(subid);
			objMe.setTypeCode(typeCd);
			objMe.setName(thirdName);
			objMe.setCode(code);
			objMe.setParent(parent);
			objMe.setDeleteState("0");
			objMe.setLayerNum(Integer.valueOf("3"));
			// 执行更新操作
			commonService().saveOrUpdate(objMe);
		}
	}
}