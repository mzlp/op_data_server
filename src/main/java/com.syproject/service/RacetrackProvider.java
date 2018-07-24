package com.syproject.service;

import com.syproject.domian.Racetrack;

/**
 * @author:lee
 * @Date:2018/7/20
 * @description:
 */
public interface RacetrackProvider {

  Racetrack getRacetrack(String connName);
}
