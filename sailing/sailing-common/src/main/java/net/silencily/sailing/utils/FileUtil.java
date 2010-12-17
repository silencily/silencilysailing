package net.silencily.sailing.utils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.util.FileUtils;


 /**
 * @author zhaoyifei
 * @version 2006-9-12
 * @see
 */
public class FileUtil{
	private static Log log = LogFactory.getLog(FileUtil.class);
	public long newFile(String path,String name,InputStream is)
	{
		long size=0;
		StringBuffer sb=new StringBuffer();
		sb.append(path);
		if(!path.endsWith("/"))
			sb.append("/");
		sb.append(name);
		File f=new File(sb.toString());
		if(f.exists())
			return -1;
		try {
			if(!f.createNewFile())
				return -1;
			OutputStream os=new FileOutputStream(f);
			byte[] buffer=new byte[1024*10];
			int len=is.available();
			
			while(0<len)
			{
				int count=is.read(buffer);
				os.write(buffer);
				size+=count;
				len=is.available();
			}
			os.close();
			is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return size;
	}
	public long readFile(String pathname,OutputStream os)
	{
		long size=0;
		File sf=new File(pathname);
		if(!sf.exists())
			return -1;
		try {
			InputStream is=new FileInputStream(sf);
			byte[] buffer=new byte[1024*10];
			int len=is.available();
			
			while(0<len)
			{
				int count=is.read(buffer);
				os.write(buffer);
				size+=count;
				len=is.available();
			}
			os.close();
			is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return size;
	}
	public long copy(String source,String target)
	{
		long size=0;
		File sf=new File(source);
		File tf=new File(target);
		if(!sf.exists())
			return -1;
		if(tf.exists())
			return -1;		
			
		try {
			tf.createNewFile();
			InputStream is=new FileInputStream(sf);
			OutputStream os=new FileOutputStream(tf);
			byte[] buffer=new byte[1024*10];
			int len=is.available();
			
			while(0<len)
			{
				int count=is.read(buffer);
				os.write(buffer);
				size+=count;
				len=is.available();
			}
			os.close();
			is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return -1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return -1;
		}
		 
		return size;
	}
	public static long removeFile(String path)
	{
		long size=0;
	
		if(path.endsWith("/"))
			path=path.substring(0,path.length()-1);
				
		File sor=new File(path);
		if(!sor.exists())
			return 0;
		if(sor.isFile())
		if(sor.delete())
				return 1;
		if(sor.isDirectory())
		{
			Stack q=new Stack();
			
			q.push(sor);
			while(!q.isEmpty())
			{
				File temp=(File) q.peek();
				
				
				if(temp.isFile())
				{
					temp.delete();
					size++;
					q.pop();
					continue;
				}
				File[] files=temp.listFiles();
				if(files.length==0)
				{
					temp.delete();
					q.pop();
					size++;
					continue;
				}
				for(int i=0,size1=files.length;i<size1;i++)
				{
					q.push(files[i]);
				}
			}
			sor.delete();
		}
		
		return size;
	}
//	public List getFolders(String path)
//	{
//		List folders=new ArrayList();
//		Queue q=new LinkedList();
//		File root=new File(path);
//		q.add(root);
//		while(!q.isEmpty())
//		{
//			File temp=q.peek();
//			q.poll();
//			FileBean fb=new FileBean();
//			fb.setId(temp.getPath());
//			fb.setFolder(temp.isDirectory());
//			fb.setName(temp.getName());
//			fb.setParentId(temp.getParentFile().getPath());
//			folders.add(fb);
//			if(temp.isFile())
//				continue;
//			File[] files=temp.listFiles();
//			for(int i=0,size=files.length;i<size;i++)
//			{
//				q.add(files[i]);
//			}
//		}
//		return folders;
//	}
	public void writeToFile(String fileName,String s,boolean append){
		try{
			FileOutputStream fos=new FileOutputStream(fileName,append);		
			Writer out
			   = new BufferedWriter(new OutputStreamWriter(fos));
			out.write(s);
			out.flush();
			out.close();
		}catch (FileNotFoundException e) {
            log.error("Could not find requested file on the server.");
        } catch (IOException e) {
            log.error("Error handling a client: " + e);
        }
	}
	public static PrintWriter getFileWriter(String name)
	{
		try {
			File f=new File(name);
			
			if(!f.exists())
				FileUtils.getFileUtils().createNewFile(f, true);
			return new PrintWriter(new FileOutputStream(f));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public static  BufferedReader getFileReader(String name)
	{
		try {
			return new BufferedReader(new FileReader(name));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public static BufferedReader getFileReader(InputStream is)
	{
		return new BufferedReader(new InputStreamReader(is));
	}
	public static void main(String[] arg0)
	{
//		FileUtil fu=new FileUtil();
//		//List l=fu.getFolders("d:\\voicemail\\");
//		Iterator i=l.iterator();
//		while(i.hasNext())
//		{
//			//System.out.println(i.next().getId());
//		}
		/*File f=new File("E:/大城小爱.mp3");
		
		try {
			f.createNewFile();
			OutputStream os=new FileOutputStream(f);
			FileUtil fu=new FileUtil();
			System.out.println(fu.readFile("e:/mu/大城小爱.mp3", os));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
//		FileUtil fu=new FileUtil();
//		//fu.copy("e:/mu/一千零一夜.mp3", "e:/一千零一夜.mp3");
//		fu.removeFile("e:/aa/");
		//FileUtil fu=new FileUtil();
		//fu.writeToFile("c:/forTest.txt", "hello world", true);		
	}
}
