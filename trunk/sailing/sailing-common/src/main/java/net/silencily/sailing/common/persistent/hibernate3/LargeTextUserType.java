package net.silencily.sailing.common.persistent.hibernate3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import net.silencily.sailing.common.domain.LargeText;
import net.silencily.sailing.common.fileupload.DefaultUploadFilePathGenerator;
import net.silencily.sailing.common.fileupload.UploadFilePathGenerator;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.persistent.Entity;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import com.power.vfs.FileObject;
import com.power.vfs.FileObjectManager;
import com.power.vfs.FileObjectManagerFactory;
import com.power.vfs.FileObjectNoExistsException;


/**
 * <p>���ڱ���ͼ���ҵ��ʵ���е�{@link LargeText}��������, ����������ҳ����ʹ�ô��ı���
 * <code>html</code>���߱༭���༭, ���ȿ��ܳ������ݿ�<code>VARCHAR</code>���͵��б���
 * ����, ͨ�����ǲ�����<code>CLOB</code>��������Щ����, �����ǳ�ʼ��ϵͳ������, ��������
 * ��ʼ��ϵͳʱ������<code>dmp</code>, �������ݿ�֮�����ֲҲ������</p>
 * <p>������浽���ݿ��е���·��, ʵ�ʵ������Զ����Ƹ�ʽ���浽�����ļ�ϵͳ��</p>
 * @author zhangli
 * @version $Id: LargeTextUserType.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @since 2007-4-25
 */
public class LargeTextUserType implements UserType, ParameterizedType {
    
    private static Log logger = LogFactory.getLog(LargeTextUserType.class);
    
    public static final String KEY_ENCODING = "encoding";
    
    public static final String KEY_GENERATOR_CLASS = "generator.class";
    
    public static final String DEFAULT_ENCODING = "utf-8";
    
    private String encoding = DEFAULT_ENCODING;
    
    private UploadFilePathGenerator pathGenerator = new DefaultUploadFilePathGenerator();
    
    private static int[] TYPES = new int[] {Hibernate.STRING.sqlType()};

    public Object assemble(Serializable serializable, Object owner) throws HibernateException {
        return serializable;
    }

    public Object deepCopy(Object value) throws HibernateException {
        if (value != null) {
            return ((LargeText) value).clone();
        }
        return value;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    public boolean equals(Object o1, Object o2) throws HibernateException {
        if (o1 == o2) {
            return true;
        } else if (o1 == null || o2 == null) {
            return false;
        } else if (o1.getClass() != o2.getClass()) {
            return false;
        } else {
            LargeText lt1 = (LargeText) o1;
            LargeText lt2 = (LargeText) o2;
            if (StringUtils.isBlank(lt1.getContent())) {
                return false;
            }
            return lt1.getContent().equals(lt2.getContent());
        }
    }

    public int hashCode(Object value) throws HibernateException {
        LargeText lt = (LargeText) value;
        if (lt == null || lt.getContent() == null) {
            return LargeText.class.hashCode();
        } else {
            return LargeText.class.hashCode() * 29 + lt.getContent().hashCode();
        }
    }

    public boolean isMutable() {
        return true;
    }

    public Object nullSafeGet(ResultSet rs, String[] columns, Object owner) throws HibernateException, SQLException {
        String value = rs.getString(columns[0]);
        LargeText ret = new LargeText((Entity) owner);
        if (StringUtils.isNotBlank(value)) {
            InputStream in = null;
            try {
                in = getFileObjectManager().getBinaryContent(value);
                String content = readContentFromStream(in);
                ret.setContent(content);
            } catch (FileObjectNoExistsException e) {
                logger.warn("LargeText���͵�������vfs��û�ж�Ӧ���ļ�,·��[" 
                    + value 
                    + "],ͨ������vfs�����ݿⲻͬ����ɵ�");
            } catch (Exception e) {
                throw new UnexpectedException("ת��LargeTexrʱ���ܶ�ȡ������vfs������", e);
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException ex) {
                    in = null;
                }
            }
        }
        return ret;
    }

    public void nullSafeSet(PreparedStatement ps, Object value, int index) throws HibernateException, SQLException {
        LargeText lt = (LargeText) value;
        if (lt == null || StringUtils.isBlank(lt.getContent())) {
            ps.setNull(index, Hibernate.STRING.sqlType());
        } else {
            try {
                FileObjectManager manager = getFileObjectManager();
                String fileName = this.pathGenerator.path(lt.getOwner()) + "/" + String.valueOf(index);
                FileObject fo = new FileObject(fileName);
                if (manager.exists(fileName)) {
                    manager.delete(fo);
                }
                manager.create(fo, new ByteArrayInputStream(lt.getContent().getBytes(encoding)));
                ps.setString(index, fileName);
            } catch (Exception e) {
                throw new UnexpectedException("�޷���LargeText������д��vfs", e);
            }
        }
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    public Class returnedClass() {
        return LargeText.class;
    }

    public int[] sqlTypes() {
        return TYPES;
    }
    
    private FileObjectManager getFileObjectManager() {
        FileObjectManagerFactory factory = (FileObjectManagerFactory) ServiceProvider
            .getService(FileObjectManagerFactory.SERVICE_NAME);
        return factory.getFileObjectManager(UploadFilePathGenerator.FIRST_LEVEL_PATH);
    }
    
    private String readContentFromStream(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(in.available());
        byte[] buffer = new byte[1024];
        int length = -1;
        while ((length = in.read(buffer)) != -1) {
            out.write(buffer, 0, length);
        }
        return new String(out.toByteArray(), encoding);
    }

    public void setParameterValues(Properties props) {
        if (props != null) {
            String en = props.getProperty(KEY_ENCODING);
            if (StringUtils.isNotBlank(en)) {
                encoding = en;
            }
            String className = props.getProperty(KEY_GENERATOR_CLASS);
            if (StringUtils.isNotBlank(className)) {
                try {
                    Class clazz = Class.forName(className);
                    this.pathGenerator = (UploadFilePathGenerator) clazz.newInstance();
                } catch (Exception e) {
                    throw new UnexpectedException("VfsLargeTextUserType's UploadFilePathGenerator��������", e);
                }
            }
        }
    }
}
