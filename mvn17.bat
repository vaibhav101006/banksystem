@echo off
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.20.8-hotspot"
set "PATH=%JAVA_HOME%\bin;%PATH%"
call "C:\Program Files\Apache NetBeans\java\maven\bin\mvn.cmd" %*
