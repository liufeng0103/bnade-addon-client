@echo off
set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ����: �Ҳ���java����
echo.
echo �밲װjava 8���л�����http://www.java.com/
echo ������Ѱ�װ����ȷ����java��binĿ¼���õ��˻�������PATH��
pause 
exit

:init
start javaw -cp res/* com.bnade.wow.addon.ClientGui