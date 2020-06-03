package com.drk.todolist.lib;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogLib {

    public static void ErrorLogging(String errorMsg, Exception e){
      log.error("Unhaldled ERROR");
      log.error("=========================================");
      log.error(errorMsg);
      e.printStackTrace();
      log.error("=========================================");
    }
}