/*
 * Copyright 2004-2005 wangz.
 * Project SummerFragrance
 */
package net.silencily.sailing.framework.utils;

import org.hibernate.id.UUIDHexGenerator;

/**
 * @since 2005-5-22
 * @author ÍõÕþ
 * @version $Id: UUIDHexGen.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 */
public class UUIDHexGen {
    
    public static String getUUIDHex() {
        return (String) new UUIDHexGenerator().generate(null, null);
    }
    
    public static void main(String[] args) {
        for (int i = 0 ; i < 28; i++) {
            System.out.println(getUUIDHex()); 
        }      
    }
}



