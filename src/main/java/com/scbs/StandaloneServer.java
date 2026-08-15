package com.scbs;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

public class StandaloneServer {

    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();

        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = String.valueOf(PORT);
        }
        tomcat.setPort(Integer.parseInt(webPort));
        tomcat.getConnector(); // Initialize connector

        File projectDir = findProjectRoot();
        File docBase = new File(projectDir, "src/main/webapp");
        if (!docBase.exists()) {
            docBase = new File(projectDir, "target/scbs");
        }

        System.out.println("------------------------------------------------------------------");
        System.out.println("📂 Project Root: " + projectDir.getAbsolutePath());
        System.out.println("🌐 WebApp Base:  " + docBase.getAbsolutePath());
        System.out.println("------------------------------------------------------------------");

        StandardContext ctx = (StandardContext) tomcat.addWebapp("", docBase.getAbsolutePath());
        ctx.setParentClassLoader(StandaloneServer.class.getClassLoader());

        // Ensure target/classes are loaded for servlets & listeners
        File additionWebInfClasses = new File(projectDir, "target/classes");
        if (additionWebInfClasses.exists()) {
            WebResourceRoot resources = new StandardRoot(ctx);
            resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                    additionWebInfClasses.getAbsolutePath(), "/"));
            ctx.setResources(resources);
        }

        tomcat.start();
        System.out.println("==================================================================");
        System.out.println("🚀 SCBS (Smart Collective Bunk System) is LIVE!");
        System.out.println("👉 Access Web App: http://localhost:" + webPort + "/login");
        System.out.println("👤 Demo Student: alex@scbs.edu  / student123");
        System.out.println("👑 Demo Admin:   admin@scbs.edu / admin123");
        System.out.println("==================================================================");
        tomcat.getServer().await();
    }

    private static File findProjectRoot() {
        // Try current working directory
        File cwd = new File(".").getAbsoluteFile();
        if (new File(cwd, "src/main/webapp").exists()) {
            return cwd;
        }

        // Try parent or specific paths
        String[] possiblePaths = {
            "E:/banksystem",
            "C:/Users/vaibh/.gemini/antigravity/scratch/scbs-jsp-servlet",
            "scbs-jsp-servlet",
            "../scbs-jsp-servlet"
        };

        for (String p : possiblePaths) {
            File f = new File(p).getAbsoluteFile();
            if (new File(f, "src/main/webapp").exists()) {
                return f;
            }
        }

        return cwd;
    }
}
