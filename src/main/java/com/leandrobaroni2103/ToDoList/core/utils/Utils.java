package com.leandrobaroni2103.ToDoList.core.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {
  public static String[] getNullPropertyNames(Object source) {
    final BeanWrapper src = new BeanWrapperImpl(source);

    PropertyDescriptor[] pds = src.getPropertyDescriptors();

    Set<String> emptyNames = new HashSet<>();

    for (PropertyDescriptor ps : pds) {
      Object srcValue = src.getPropertyValue(ps.getName());
      if (srcValue == null) {
        emptyNames.add(ps.getName());
      }
    }

    String[] result = new String[emptyNames.size()];
    return emptyNames.toArray(result);
  }

  public static void copyNonNullProperties(Object source, Object target) {
    BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
  }
}
