package net.silencily.sailing.utils;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.xml.sax.SAXException;

 /**
 * @author zhaoyifei
 * @version 2007-3-1
 * @see
 */
public class Betwixts {
	private String a;
	private String b;
	private HashMap kks=new HashMap();
	private List sss=new ArrayList();
	public void addKk(String k,String v)
	{
		kks.put(k,v);
	}
	public void addSs(String ss)
	{
		sss.add(ss);
	}
	/**
	 * @param
	 * @version 2007-3-1
	 * @return
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Betwixts bet=new Betwixts();
		try {
			bet.testReadAndWrite();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the a
	 */
	public String getA() {
		return a;
	}
	/**
	 * @param a the a to set
	 */
	public void setA(String a) {
		this.a = a;
	}
	/**
	 * @return the b
	 */
	public String getB() {
		return b;
	}
	/**
	 * @param b the b to set
	 */
	public void setB(String b) {
		this.b = b;
	}
	/**
	 * @return the kks
	 */
	public HashMap getKks() {
		return kks;
	}
	/**
	 * @return the sss
	 */
	public List getSss() {
		return sss;
	}
	
	public void testReadAndWrite(){
		Betwixts bet=new Betwixts();
		bet.a="zhaoyifei";
		bet.b="callcenter";
		bet.addSs("a");
		bet.addSs("b");
		bet.kks.put("1","one");
		bet.kks.put("2","two");
		try {
			Writer ow=new FileWriter("d:\\betwixt.xml");
			BeanWriter bw=new BeanWriter(ow);
			bw.setEndOfLine("\r\n");
			bw.setIndent("\t");
			bw.enablePrettyPrint();
			bw.setWriteEmptyElements(true);
			bw.write(bet);
			ow.close();
			
			//InputStream is=new FileInputStream(new File("d:\\betwixt.xml"));
			BeanReader br=new BeanReader();
			br.getXMLIntrospector().setWrapCollectionsInElement(false);
			br.registerBeanClass(Betwixts.class);
			
			br.getBindingConfiguration().setMapIDs(false);
			Betwixts beta=(Betwixts)br.parse(new File("d:\\betwixt.xml"));
			//is.close();
			beta.a="zhaoyifei-";
			beta.b="xml-";
			beta.kks.put("3","three");
			
			Writer owa=new FileWriter("d:\\betwixt.xml");
			BeanWriter bwa=new BeanWriter(owa);
			bwa.setEndOfLine("\r\n");
			bwa.setIndent("\t");
			bwa.enablePrettyPrint();
			bwa.setWriteEmptyElements(true);
			bwa.write(beta);
			owa.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void testList()
	{
		String xml = "<?xml version='1.0'?>" +
        "<PoemBeana>" +
        "<lines>" +
        "<line>It is an ancient Mariner,</line>" +
        "<line>And he stoppeth one of three.</line>" +
        "<line>\"By thy long grey beard and the glittering eye,</line>" +
        "<line>Now wherefore stopp'st thou me?\"</line>" +
        "</lines>" +
        "</PoemBeana>";
        BeanReader reader = new BeanReader();
        try {
			reader.registerBeanClass(PoemBeana.class);
			PoemBeana bean = (PoemBeana) reader.parse(new StringReader(xml));
		      // bean.addLine("aaaaaaaaaaaaaaaaaaaaaaa");
	        //Object[] lines = bean.getLines().toArray();
	        Writer ow=new FileWriter("d:\\list.xml");
			BeanWriter bw=new BeanWriter(ow);
			bw.setEndOfLine("\r\n");
			bw.setIndent("\t");
			bw.enablePrettyPrint();
			bw.setWriteEmptyElements(true);
			bw.write(bean);
			ow.close();
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
public void testHashMapWriteEmpty() throws Exception {
        
        Map hash = new Hashtable();
        hash.put("one", "un");
        hash.put("two", "deux");
        hash.put("three", "trois");

        String expected = "<?xml version='1.0'?>" +
        		"<Hashtable>" +
        		"	<empty>false</empty>" +
        		"    <entry>" +
        		"      <key>two</key>" +
        		"      <value>deux</value>" +
        		"    </entry>" +
        		"   <entry>" +
        		"      <key>one</key>" +
        		"      <value>un</value>" +
        		"    </entry>" +
        		"    <entry>" +
        		"      <key>three</key>" +
        		"      <value>trois</value>" +
        		"    </entry>" +
        		"  </Hashtable>";
        
        StringWriter out = new StringWriter();
        
        BeanWriter beanWriter = new BeanWriter(out);
        beanWriter.setEndOfLine("\n");
        beanWriter.enablePrettyPrint();
        beanWriter.setWriteEmptyElements(false);
        beanWriter.getBindingConfiguration().setMapIDs(false);
        beanWriter.setXMLIntrospector(new XMLIntrospector());
        beanWriter.write(hash);

        //xmlAssertIsomorphic(parseString(expected), parseString(out));
    }
	


}
