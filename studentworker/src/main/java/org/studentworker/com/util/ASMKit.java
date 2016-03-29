package org.studentworker.com.util;

import java.lang.reflect.Field;

import com.jfinal.plugin.activerecord.TableMapping;

public class ASMKit {
	 /**
     * @Description: 从对象中获取字段的值, 可以获取对象的私有字段
     * @author: luoyizhu
     * @time:2014年11月6日 下午7:14:36
     * @title getFieldValue
     * @param obj 从这个对象获取值
     * @param filedName 需要获取的字段名
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object obj,
                                      String filedName) {
        final Class<?> cls = obj.getClass();
        try {
            Field field = cls.getDeclaredField(filedName);
            field.setAccessible(true);
            return (T) field.get(TableMapping.me());
        } catch (Exception e) {
        	e.printStackTrace();
        } 
        return null;
    }
}
