package com.hcf.floatword.ld2;

import com.hcf.floatword.Word;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LD2Utils {

    private static final Logger logger = LoggerFactory.getLogger(LD2Utils.class);

    public static boolean isChinese(String str) {
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find())
            flg = true;

        return flg;
    }

    /**
     * 将LD2产生的output文件解析后输出
     */
    public static void ld2Output2Text() throws IOException {
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

    /**
     * 将有道导出的xml格式转换成Word
     * @param path xml文件路径
     * @return List<Word>
     */
    public static List<Word> parseYoudaoXml(String path) {
        List<Word> words = new ArrayList<>();
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new File(path));
            Element root = document.getRootElement();

            List<Element> elements = root.elements();
            logger.info("解析到的单词总个数：{}", elements.size());
            for (Element element : elements) {
                String english = element.elementText("word");
                String trans = element.elementText("trans");
                String phonetic = element.elementText("phonetic");

                if (StringUtils.isBlank(trans)) {
                    trans = "*";
                }
                if (StringUtils.isBlank(phonetic)) {
                    trans = "[]";
                }

                Word word = new Word();
                word.setEnglish(english);
                word.setChinese(trans);
                word.setSoundmark(phonetic);
                words.add(word);
            }
        } catch (DocumentException e) {
            logger.error("DocumentException", e);
        }
        return words;
    }

    /**
     * 将单词写入文件中
     * @param words 单词
     * @param filePath 写入的文件路径
     */
    public static void word2File(List<Word> words, String filePath) {
        List<String> lines = new ArrayList<>();
        for (Word word : words) {
            String english = word.getEnglish();
            String chinese = word.getChinese();
            String soundmark = word.getSoundmark();
            chinese = StringUtils.replaceEach(chinese,
                    new String[]{"\t", "\n", " ", "；", "，"},
                    new String[]{"", "", "", ",", ","});

            String line = english + "\t" + soundmark + "\t" + chinese;
            lines.add(line);
        }
        try {
            FileUtils.writeLines(new File(filePath),
                    "UTF-8", lines, false);
        } catch (IOException e) {
            logger.error("IOException", e);
        }
    }

    public static void main(String[] args) {
        List<Word> words = parseYoudaoXml("C:\\Users\\Shadon\\Desktop\\word.xml");
        word2File(words, "C:\\Users\\Shadon\\Desktop\\word.csv");
    }

}
