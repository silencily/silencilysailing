package net.silencily.sailing.common.crud.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.silencily.sailing.common.crud.domain.CommonTableView;
import net.silencily.sailing.common.crud.domain.CommonTableViewSet;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.common.crud.service.CommonTableViewService;
import net.silencily.sailing.common.crud.service.ViewBean;
import net.silencily.sailing.common.dict.service.BasicCodeService;
import net.silencily.sailing.framework.codename.UserCodeName;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.framework.persistent.filter.Paginater;
import net.silencily.sailing.hibernate3.EntityPlus;
import net.silencily.sailing.struts.BaseFormPlus;
import net.silencily.sailing.utils.Tools;

import org.apache.commons.beanutils.MethodUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;






/**
 * @author zhaoyifei
 *
 */
public class CommonServiceImpl implements CommonService {

    CommonTableViewService ctvs;
    BasicCodeService bcs;
    private HibernateTemplate hibernateTemplate;
    /**
     * @return the hibernateTemplate
     */
    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    /**
     * @param hibernateTemplate the hibernateTemplate to set
     */
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    /* (non-Javadoc)
     * @see com.qware.common.crud.service.CommonService#add(com.coheg.framework.persistent.Entity)
     */
    public void add(Object e) {
        // TODO Auto-generated method stub
        hibernateTemplate.save(e);
        hibernateTemplate.flush();
        //hibernateTemplate.clear();
    }

    /* (non-Javadoc)
     * @see com.qware.common.crud.service.CommonService#delete(com.coheg.framework.persistent.Entity)
     */
    public void delete(Object e) {
        // TODO Auto-generated method stub
        hibernateTemplate.delete(e);
        hibernateTemplate.flush();

    }

    

    /* (non-Javadoc)
     * @see com.qware.common.crud.service.CommonService#load(java.lang.Class, java.lang.String)
     */
    public Object load(Class c, String id) {
        // TODO Auto-generated method stub
        return hibernateTemplate.load(c, id);
    }

    /* (non-Javadoc)
     * @see com.qware.common.crud.service.CommonService#update(com.coheg.framework.persistent.Entity)
     */
    public void update(Object e) {
        // TODO Auto-generated method stub
        hibernateTemplate.update(e);
        hibernateTemplate.flush();

    }

    public ViewBean fetchAll(Class c, String user,String pageId) {
        // TODO Auto-generated method stub
        List ascl=new ArrayList();
        List descl=new ArrayList();
//      2008/02/19 modify by tongjq start
//      List al=this.ctvs.getRows(user, pageId, "asc");
//      List dl=this.ctvs.getRows(user, pageId, "desc");
//      for(int i=0,size=al.size();i<size;i++)
//      {
//          CommonTableViewSet ctvsa=(CommonTableViewSet)al.get(i);
//          ascl.add(ctvsa.getCommonTableView().getRowName());
//      }
//      for(int i=0,size=dl.size();i<size;i++)
//      {
//          CommonTableViewSet ctvsa=(CommonTableViewSet)dl.get(i);
//          descl.add(ctvsa.getCommonTableView().getRowName());
//      }
        List orderList = this.ctvs.getDefOrders(pageId);
        for(int i=0,size=orderList.size();i<size;i++)
        {
            CommonTableView ctvsa=(CommonTableView)orderList.get(i);
            if ("ASC".equalsIgnoreCase(ctvsa.getOrderAsc())) {
                ascl.add(ctvsa.getRowName());
            } else {
                descl.add(ctvsa.getRowName());
            }
        }
//      2008/02/19 modify by tongjq end

        return this.fetchAll(c, user,pageId, ascl,  descl);
    }
    

    public ViewBean fetchAll(Class c,String user,String pageId, List orderAsc, List orderDesc) {
        // TODO Auto-generated method stub

//      2008/02/19 insert by tongjq start
        ContextInfo.concealQuery();
        List rs=ctvs.getRows(user, pageId);
        List orderList = new ArrayList();
        String firstField = "";//列表画面第一列的字段名
        boolean sorted = false;
        //用户已经定制过显示字段的时候
        if (rs.size() > 0 && CommonTableViewSet.class.isAssignableFrom(rs.get(0).getClass())) {
            //firstField = ((CommonTableViewSet)rs.get(0)).getCommonTableView().getRowName();
            //取得已经定制的排序字段
            for (int i=0,size=rs.size();i<size;i++) {
                if (!"1".equals(((CommonTableViewSet)rs.get(i)).getCommonTableView().getIsDbField())) {
                    continue;
                }
                String orderField = ((CommonTableViewSet)rs.get(i)).getCommonTableView().getRowName();
//              由于对子表项目的排序只能使用内连接查询，在子表数据不存在的时候会使主表数据也查询不出来
//              因此追加此段if代码
                if (orderField.split("\\.").length > 1) {
                    continue;
                }
                if (firstField.equals("")) {
                    firstField = orderField;
                }
                if (orderAsc.contains(orderField)) {
                    orderList.add(orderField);
                } else if (orderDesc.contains(orderField)) {
                    orderList.add(orderField);
                }
            }
        } else if (rs.size() > 0) {
            orderList.addAll(orderAsc);
            orderList.addAll(orderDesc);
            for (int i=0,size=rs.size();i<size;i++) {
                String orderField = ((CommonTableView)rs.get(i)).getRowName();
                if (!"1".equals(((CommonTableView)rs.get(i)).getIsDbField())) {
                    orderList.remove(orderField);
                    continue;
                }
//              由于对子表项目的排序只能使用内连接查询，在子表数据不存在的时候会使主表数据也查询不出来
//              因此追加此段if代码
                if (orderField.split("\\.").length > 1) {
                    continue;
                }
                if (firstField.equals("")) {
                    firstField = orderField;
                }
            }
        }
//      2008/02/19 insert by tongjq end

        ContextInfo.recoverQuery();
        DetachedCriteria d=DetachedCriteria.forClass(c);
        if(EntityPlus.class.isAssignableFrom(c))
        {
            d.add(Restrictions.eq("delFlg","0"));
        }
        Set created = new HashSet();
        for(int i=0,size=orderAsc.size();i<size;i++)
        {
            if (orderList.contains((String)orderAsc.get(i))) {//2008/02/19 insert by tongjq
                //d.addOrder(Order.asc((String)orderAsc.get(i)));
                if (this.addOrder(d,(String)orderAsc.get(i),true,created)) {
                    sorted = true;
                }
                
            }
        }
        for(int i=0,size=orderDesc.size();i<size;i++)
        {
            if (orderList.contains((String)orderDesc.get(i))) {//2008/02/19 insert by tongjq
                //d.addOrder(Order.desc((String)orderDesc.get(i)));
                if (this.addOrder(d,(String)orderDesc.get(i),false,created)) {
                    sorted = true;
                }
            }
        }
//      2008/02/19 insert by tongjq start
        if (!sorted && !"".equals(firstField)) {
            this.addOrder(d,firstField,true,created);
            //d.addOrder(Order.asc(firstField));
        }
        ContextInfo.setAliasSet(created);
//      2008/02/19 insert by tongjq end
        List l=hibernateTemplate.findByCriteria(d);

        ViewBean vb=new ViewBean();
//      List rs=ctvs.getRows(user, pageId); //2008/02/19 delete by tongjq
        vb.setViewRow(rs);
        //this.change(l,rs);
        vb.setViewList(l);
        return vb;
    }

    private boolean addOrder(DetachedCriteria d,String field,boolean isAsc,Set created) {
        String alias[] = field.split("\\.");
        String path = "";
        String newField = field;
        if (alias.length > 1) {
//          由于对子表项目的排序只能使用内连接查询，在子表数据不存在的时候会使主表数据也查询不出来
//          因此注释掉此段代码
            return false; 
//            for (int i = 0;i < alias.length - 1;i++) {
//                if (path.equals("")) {
//                    path = alias[i];
//                } else {
//                    path = path + "." + alias[i];
//                }
//                if (!created.contains(path)) {
////                    d.setFetchMode(path, FetchMode.JOIN);
//                    d.createAlias(path, alias[i]);
//                    created.add(alias[i]);
//                }
//            }
//            newField = alias[alias.length - 2] + "." + alias[alias.length - 1];
        }
        if (isAsc) {
            d.addOrder(Order.asc(newField));
        } else {
            d.addOrder(Order.desc(newField));
        }
        return true;
    }
    /**
     * @return the ctvs
     */
    public CommonTableViewService getCtvs() {
        return ctvs;
    }

    /**
     * @param ctvs the ctvs to set
     */
    public void setCtvs(CommonTableViewService ctvs) {
        this.ctvs = ctvs;
    }



    /**
     * @return the bcs
     */
    public BasicCodeService getBcs() {
        return bcs;
    }

    /**
     * @param bcs the bcs to set
     */
    public void setBcs(BasicCodeService bcs) {
        this.bcs = bcs;
    }

    private void change(List result,List rows)
    {
        Iterator i=rows.iterator();
        while(i.hasNext())
        {
            CommonTableViewSet ctvs=(CommonTableViewSet)i.next();
            String ss=ctvs.getCommonTableView().getSubSystem();
            String m=ctvs.getCommonTableView().getModule();
            String r=ctvs.getCommonTableView().getRowName();
            StringBuffer getMethod=new StringBuffer();
            getMethod.append("get");
            String first=r.substring(0,1);
            getMethod.append(first.toUpperCase());
            getMethod.append(r.substring(1));
            StringBuffer setMethod=new StringBuffer();
            setMethod.append("set");
            
            setMethod.append(first.toUpperCase());
            setMethod.append(r.substring(1));
            if(ss==null||"".equals(ss))
                continue;
            Iterator i1=result.iterator();
            while(i1.hasNext())
            {
                Object object=i1.next();
                Object id;
                try {
                    id = MethodUtils.invokeExactMethod(object,getMethod.toString(),null);
                    String display=bcs.getBasicCodeName(ss, m, (String)id);
                    //MethodUtils.invokeExactMethod(object,setMethod.toString(),display);
                    Map dict=(Map)MethodUtils.invokeExactMethod(object,"getDict",null);
                    dict.put(r,display);
                } catch (NoSuchMethodException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                
            }
        }
    }

    public void deleteLogic(Object e) {
        // TODO Auto-generated method stub
        Tools.setProperty(e,"delFlg","1");
//      Tools.setProperty(e,"deleter",ContextInfo.getContextUser());
//      Tools.setProperty(e,"deleterDept",new DepartmentCodeName().copyFrom(ContextInfo.getContextUser().getDepartment()));
//      Tools.setProperty(e,"deletedTime",new Date());
        this.saveOrUpdate(e);
        hibernateTemplate.flush();

    }

    public List getList(Class c, String user) {
        // TODO Auto-generated method stub
        ContextInfo.recoverQuery();
        DetachedCriteria d=DetachedCriteria.forClass(c);
        d.add(Restrictions.eq("delFlg","0"));
        return hibernateTemplate.findByCriteria(d);
    }

    public void saveOrUpdate(Object e) {
        // TODO Auto-generated method stub
        hibernateTemplate.saveOrUpdate(e);
        hibernateTemplate.flush();

    }

    public int getSize(DetachedCriteria dc) {
        // TODO Auto-generated method stub
        dc.add(Restrictions.eq("delFlg","0"));
        dc.setProjection(Projections.count("id"));
        Integer i=(Integer)hibernateTemplate.findByCriteria(dc).get(0);
        return i.intValue();
    }

    public ViewBean fetchAll(Class c, String pageId) {
        // TODO Auto-generated method stub
        UserCodeName ucn=ContextInfo.getContextUser();
//      return this.fetchAll(c, ucn.getUsername(),pageId, new ArrayList(),  new ArrayList());
        return this.fetchAll(c, ucn.getUsername(),pageId);
    }

    public List getList(Class c) {
        // TODO Auto-generated method stub
        UserCodeName ucn=ContextInfo.getContextUser();
        return this.getList(c,ucn.getUsername());
    }

    public int deleteAllLogic(Class c, List oids) {
        // TODO Auto-generated method stub
            int num = oids.size();
        for (int i = 0; i < num; i++) {
            String id = oids.get(i).toString().trim();
            this.deleteLogic(this.load(c, id));
        }
        return num;
    }

    public Set getSubSet(Set s, BaseFormPlus bfp) {
        // TODO Auto-generated method stub
        Object[] temp=s.toArray();
        Paginater p=bfp.getPaginater();
        p.setCount(temp.length);
        int begin=p.getPage()*p.getPageSize();
        int end=begin+p.getPageSize();
        if(end>temp.length)
            end=temp.length;
        Set result=new LinkedHashSet();
        for(int i=begin;i<end;i++)
        result.add(temp[i]);
        return result;
    }

    public int deleteAll(Class c, List oids) {
        // TODO Auto-generated method stub
        int num = oids.size();
        for (int i = 0; i < num; i++) {
            String id = oids.get(i).toString().trim();
            this.delete(this.load(c, id));
        }
        return num;
    }

    public int deleteAll(Collection c) {
        // TODO Auto-generated method stub
        hibernateTemplate.deleteAll(c);
        hibernateTemplate.flush();

        return c.size();
    }

    public int deleteAllLogic(Collection c) {
        // TODO Auto-generated method stub
        Iterator i=c.iterator();
        while(i.hasNext())
        {
            Object o=i.next();
            this.deleteLogic(o);
        }
        return c.size();
    }

    /**
     * 注意 此方法不推荐使用!!!请使用listSetToVB代替此方法
     */
    public ViewBean setVBByList(String pageId, List data) {
        return listSetToVB(pageId,data);
    }

    public ViewBean listSetToVB(String pageId, List data) {
        UserCodeName ucn=ContextInfo.getContextUser();
        String user =   ucn.getUsername();
        ViewBean vb=new ViewBean();
        List rs=ctvs.getRows(user, pageId);
        vb.setViewRow(rs);
        vb.setViewList(data);
        return vb;
    }

    public Object writeLoad(Class c, String id) {
        // TODO Auto-generated method stub
        return hibernateTemplate.load(c, id);
    }

    public List findByCriteria(DetachedCriteria dc) {
        // TODO Auto-generated method stub
        return hibernateTemplate.findByCriteria(dc);
    }

}
