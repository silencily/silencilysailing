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
 * ���������ʼ���ࡣ ��ͨ��theForm.message���õ����صĴ�������Ϣ��
 * ͨ��theForm.flag[]��ȡ�û��水ť���Ի�״̬����������г���Ϊ9���ֱ��Ӧÿһ����ϵͳ�İ�ť���Ի�״̬��
 * ��ϵͳ���ܽ��в���ʱ����Ӧλ��������Ϊdisabled������Ϊ�ա�
 * �����������ÿһ����ϵͳ�ֱ��������������ݽ��г�ʼ����Ҳ���Էֱ�ɾ��ÿ����ϵͳ�Ļ�������ȫ�����ݡ�
 * 
 * @author ���
 * 
 */
public class CommonBasicCodeInitAction extends BaseDispatchAction {
	/**
	 * ȡ�÷���
	 * 
	 * @return ����
	 */
	public static CommonBasicCodeInitService service() {
		return (CommonBasicCodeInitService) ServiceProvider
				.getService(CommonBasicCodeInitService.SERVICE_NAME);
	}
	/**
	 * 
	 * �������� ȡ�ù�ͨ�ķ���
	 * @return ��ͨ��service
	 * 2007-11-19 ����06:07:45
	 * @version 1.0
	 * @author lihe
	 */
	public static CommonService commonService() {
		return (CommonService) ServiceProvider
				.getService(CommonService.SERVICE_NAME);
	}

	/**
	 * ����ҳ��
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
	 * ��Properties�ļ��е�������ӵ����ݿ���
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ��ת��Ϣ
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CommonBasicCodeInitForm theForm = (CommonBasicCodeInitForm) form;
		Properties propFile;
		String id = null;
		// ��ϵͳ����ȡ��
		id = request.getParameter("oid");
		// ��ʼ��Properties�ļ����õ�Properties��Ķ���
		propFile = fileRead(id);
		if (null == propFile) {
			theForm.setMessage("  properties�ļ������û���ҵ�!ʧ�ܣ�");
			theForm.setFlag(getActiveFlag());
			return mapping.findForward("init");
		}

		// ɾ������ϵͳ���л�����������
		service().reset(id.toUpperCase());
		// �Եõ��Ĵ��������д���(׷�ӵ����ݿ�)
		if (dealing(propFile)) {
			theForm.setMessage(" ���ݸ��³ɹ�!");
		} else {
			theForm.setMessage(" û�����ݸ���!");
		}
		// ���û�г�ʼ������ϵͳ
		theForm.setFlag(getActiveFlag());
		return mapping.findForward("init");
	}

	/**
	 * ����������ɾ�����ص�ϵͳ��ʼ��״̬
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
		// ��ʼ��ѡ�е���ϵͳ�������ô�����
		theForm.setMessage(service().reset(request.getParameter("subID")));
		// ���û�г�ʼ������ϵͳ
		theForm.setFlag(getActiveFlag());
		return mapping.findForward("init");
	}

	/**
	 * ���û�г�ʼ������ϵͳ
	 * 
	 * @return �ж���ϵͳ���������Ƿ��ʼ�����ַ�������
	 */
	private String[] getActiveFlag() {
		// ��ȫ�Լ��
		if (service().find("root").size() == 0) {
			CommonBasicCode bc = new CommonBasicCode();
			bc.setId("root");
			bc.setDisplayName("ϵͳ����ά��");
			bc.getParent().setId("");
			commonService().saveOrUpdate(bc);
		}
		// ��ϵͳ���������Ƿ��ʼ�����ַ��������ʼ��
		String flag[] = { "", "", "", "", "", "", "", "", "", "" };
		// ��ϵͳ����
		String b[] = { "HR", "GR", "AM", "RM", "FM", "ST", "UA", "OF", "SM", "CM" };
		List l = null;
		for (int i = 0; i < 10; i++) {
			l = service().load(b[i]);
			if (l.size() > 0) {
				// ����Ѿ���ʼ�����Ѱ�ť�ǻ��Ի�
				flag[i] = "disabled";
			}
		}
		return flag;
	}

	/**
	 * ��ȡProperties�ļ�
	 * 
	 * @param id
	 *            ��ϵͳ����id
	 * @return Properties�����,����ʧ�ܷ���null
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
			// ��ȡ�ļ��쳣
			return null;
		} catch (IOException e) {
			// Properties�ļ������쳣
			return null;
		}

		return dealRes;
	}

	/**
	 * ��Properties�е����ݸ��µ�������
	 * 
	 * @param propFile
	 *            Ҫ������ļ�����
	 * @return ����ɹ�����true��ʧ��false
	 */
	private boolean dealing(Properties propFile) {
		// TypeCode��key
		String keyID = "key";
		// �Ծ͵�TypeCode��ֵ
		String keyRs = null;
		// ��Ӧ��TypeCode����ʾ���Ƶ�key
		String valID = "val";
		// ��Ӧ��TypeCode����ʾ���Ƶ�ֵ
		String valRs = null;
		// �����õ�bean
		CommonBasicCode objMe = null;
		// ִ�и��µı�־��trueʱ����
		boolean flag = true;
		// �������
		int max = propFile.size();
		max = max / 2;
		if (max == 0) {
			return false;
		}
		// Ҫ���µ��ֶ�
		String id = null;
		String subid = null;
		String typeCode = null;
		String codeName = null;
		CommonBasicCode parent = new CommonBasicCode();
		CommonBasicCode parentMem = new CommonBasicCode();
		// ����ѭ��
		for (int i = 1; i <= max; i++) {
			// ����ִ��
			flag = true;
			// ����ļ��а���keyΪkeyID�ļ�¼
			if (propFile.containsKey(keyID)) {
				// ȡ�ö�Ӧ��TypeCode
				keyRs = propFile.getProperty(keyID);
				// ����ļ��а���keyΪvalID�ļ�¼
				if (propFile.containsKey(valID)) {
					// ȡ�ö�Ӧ��displayName
					valRs = propFile.getProperty(valID);
				} else {
					// ��������ڣ���ִ�и��²���
					flag = false;
				}
			} else {
				// ��������ڣ���ִ�и��²���
				flag = false;
			}
			if (flag) {
				// �����õ�bean��ʼ��
				objMe = new CommonBasicCode();
				// �ַ���ת��
				try {
					valRs = new String(valRs.getBytes("ISO-8859-1"), "GBK");
				} catch (UnsupportedEncodingException e) {
					// �ַ���ת���쳣
					e.printStackTrace();
				}
				// ����Ǹ����
				if (keyID.equals("key")) {
					parent = (CommonBasicCode)commonService().load(CommonBasicCode.class, "root");
					// parent = null;
					subid = keyRs;
					id = subid;
					objMe.setDisplayName(valRs);
					objMe.setDeleteState("0");
					objMe.setLayerNum(Integer.valueOf("1"));
				} else {// �Ǹ����
					parent = parentMem;
					id=Tools.getPKCode();
				}
				typeCode = keyRs;
				codeName = valRs;
				// ����������
				if (keyRs.indexOf(",") > 0) {
					this.theThird(subid, typeCode, codeName, parent);
				} else {
					// �����õ�bean����ֵ
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
					// ִ�и��²���
					try {
						commonService().saveOrUpdate(objMe);
					} catch (RuntimeException e) {
						
						e.printStackTrace();
					}
				}
				// ��¼�����
				if (keyID.equals("key")) {
					parentMem = objMe;
				}
				// ������һ����¼��key
				keyID = "key" + i;
				valID = "val" + i;
			}
		}
		return true;
	}

	/**
	 * ����������
	 * 
	 * @param id
	 *            ID
	 * @param subid
	 *            ��ϵͳID
	 * @param typeCode
	 *            typeCode
	 * @param codeName
	 *            ��ʾ������
	 * @param parent
	 *            �����ID
	 */
	private void theThird(String subid, String typeCode,
			String codeName, CommonBasicCode parent) {
		// �����õ�bean
		CommonBasicCode objMe = new CommonBasicCode();
		// typeCode
		String typeCd = typeCode;
		// �ڶ��ڵ�����
		String secondName = codeName;
		// ������code
		String code = "";
		// ����������
		String thirdName = "";
		// ���¶�����
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
		// ִ�и��²���
		commonService().saveOrUpdate(objMe);
		// ���µ�������
		parent = objMe;
		int i = 0;
		while (true) {
			i++;
			// ����δ���µ�������
			if (typeCode.indexOf(",") > 0) {
				typeCode = typeCode.substring(typeCode.indexOf(",") + 1);
				codeName = codeName.substring(codeName.indexOf(",") + 1);
			} else {
				// ȫ�����꣬�˳�ѭ��
				break;
			}
			objMe = new CommonBasicCode();
			// �������һ��������
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
			// ִ�и��²���
			commonService().saveOrUpdate(objMe);
		}
	}
}