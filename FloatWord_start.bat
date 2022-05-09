::win启动脚本，根据需要修改路径即可
%1 mshta vbscript:CreateObject("WScript.Shell").Run("%~s0 ::",0,FALSE)(window.close)&&exit

java -classpath ".;.\lib\*" -jar ./floatword-1.0-SNAPSHOT.jar "%~dp0word.csv" "%~dp0sound"