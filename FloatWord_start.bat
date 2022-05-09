::win启动脚本，根据需要修改路径即可
%1 mshta vbscript:CreateObject("WScript.Shell").Run("%~s0 ::",0,FALSE)(window.close)&&exit

java -jar ./floatword-1.0-SNAPSHOT.jar .\word.csv