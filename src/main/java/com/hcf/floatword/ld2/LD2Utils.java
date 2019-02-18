package com.hcf.floatword.ld2;

import com.hcf.floatword.Word;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LD2Utils {

    public static boolean isChinese(String str) {
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find())
            flg = true;

        return flg;
    }

    public static void main(String[] args) throws IOException {
        String filePath = "C:\\Users\\Shadon\\Desktop\\Postgraduate Examination 2009.output";
        File file = new File(filePath);

        List<String> lines = FileUtils.readLines(file, "UTF-8");
        for (String line : lines) {
            int index = StringUtils.indexOf(line, "=");
            String word = StringUtils.substring(line, 0, index);

            int index2 = StringUtils.indexOf(line, "[");
            int index3 = StringUtils.indexOf(line, "]") + 1;
            String soundMark = StringUtils.substring(line, index2, index3);

            if (isChinese(soundMark)) {
                index2 = StringUtils.ordinalIndexOf(line, "[", 2);
                index3 = StringUtils.ordinalIndexOf(line, "]", 2) + 1;
                soundMark = StringUtils.substring(line, index2, index3);
            }

            String chinese = StringUtils.substring(line, index3);

            System.out.println(word + "#" + soundMark + "#" + chinese);


        }


    }

}
