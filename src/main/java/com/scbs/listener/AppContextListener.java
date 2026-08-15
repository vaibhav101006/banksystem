package com.scbs.listener;

import com.scbs.dao.DBConnectionManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("==================================================");
        System.out.println("🚀 Initializing Smart Collective Bunk System (SCBS)");
        System.out.println("   Stack: JDBC + Servlets + JSP (Pure Java)");
        System.out.println("==================================================");
        DBConnectionManager.initializeDatabase();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("👋 SCBS Application Context Destroyed");
    }
}
