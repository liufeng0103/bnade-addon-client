@echo off
set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo 错误: 找不到java命令
echo.
echo 请安装java 8运行环境，http://www.java.com/
echo 如果你已安装，请确定把java的bin目录配置到了环境变量PATH下
pause 
exit

:init
start javaw -cp ./res/addon-client.jar;./res/gson-2.5.jar com.bnade.wow.addon.ClientGui