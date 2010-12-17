/*
 * Copyright 2004-2005 wangz.
 * Project blogTime
 */
package net.silencily.sailing.framework.utils;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

/**
 * @since 2005-5-12
 * @author wangz
 * @version $Id: IOUtils.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public abstract class IOUtils {
	
	
	/**
	 * 将字符串数组写到输出流中, 每个元素一行
	 * @param array the string array
	 * @param out the output stream
	 */
	public static void writeStringArray2OutputStream(String[] array, OutputStream out) {	
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
		try {
			for (int i = 0; i < array.length; i++) {	
				writer.write(array[i]);
				writer.write("\r\n");
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException("写数据发生异常 ", e);
		}
	}
	
    /**
     * 将给定字节数据写入到输出流中
     * @param bytes byte 数组
     * @param out 输出流
     * @param bufferSize 缓冲区大小
     * @throws IOException 如果写数据时出错
     */
    public static void writeBytes(byte[] bytes, OutputStream out, int bufferSize) 
    	throws IOException {
        
        int blockNumber = bytes.length / bufferSize;
        if (bytes.length % bufferSize != 0 && bytes.length > bufferSize) {
            blockNumber++;
        }
        
        for (int i = 0; i < blockNumber; i++) {
            int beginPos = bufferSize * i;
            int endPos = beginPos + bufferSize < bytes.length ? beginPos + bufferSize : bytes.length;
            byte[] targetBytes = new byte[bufferSize];
            int length = endPos - beginPos;
            System.arraycopy(bytes, beginPos, targetBytes, 0, length);
            out.write(targetBytes, 0, length);
        }
        
        out.flush();
        out.close();
    }
    
    /**
     * 
     * @param fileUrl
     * @return
     * @throws IOException
     */
    public static byte[] getBytesFromUrl(String fileUrl) throws IOException {
		URL url = new URL(fileUrl);
		InputStream in = url.openStream();
		ReturnBytesInputStreamCallback callback = new ReturnBytesInputStreamCallback();
		callback.processInputStream(in);
		in.close();
		return callback.getResultBytes();
    }
    
    public static final class ReturnBytesInputStreamCallback {
        
        private byte[] resultBytes = new byte[0];
         
        /**
         * @return Returns the resultBytes.
         */
        public byte[] getResultBytes() {
            return resultBytes;
        }

        /**
         * @param resultBytes The resultBytes to set.
         */
        public void setResultBytes(byte[] resultBytes) {
            this.resultBytes = resultBytes;
        }

        public void processInputStream(InputStream inputStream) throws IOException {
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
          
            while ((length = inputStream.read(buffer, 0, bufferSize)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
          
            setResultBytes(byteArrayOutputStream.toByteArray());        
        } 
    }
    
    
    public static void main(String[] args) {
    }
}



