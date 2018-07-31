package com.syproject.service;

import com.syproject.domian.Racetrack;

import java.util.List;
import java.util.Map;

/**
 * @author:lee
 * @Date:2018/7/27
 * @description:
 */
public interface EntityQuery {

  List<Map<String,Object>> query(Racetrack racetrack, QueryEntityDescriptor queryEntityDescriptor);
}
