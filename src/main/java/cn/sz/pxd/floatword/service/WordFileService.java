package cn.sz.pxd.floatword.service;

import cn.sz.pxd.floatword.Word;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author https://github.com/shadon178
 */
public class WordFileService {

    private static final Logger logger = LoggerFactory.getLogger(WordFileService.class);

    public static List<Word> readWord(String filePath) {
        try {
            List<String> lines = FileUtils.readLines(new File(filePath), "UTF-8");
            List<Word> words = new ArrayList<>();
            for (String line : lines) {
                String[] split = StringUtils.split(line, "\t");
                Word word = new Word();
                word.setEnglish(split[0]);
                if (split.length > 1) {
                    word.setSoundmark(split[1]);
                } else {
                    word.setSoundmark("");
                }
                if (split.length > 2) {
                    word.setChinese(split[2]);
                } else {
                    word.setChinese("");
                }
                words.add(word);
            }
            return words;
        } catch (IOException e) {
            logger.error("IOException", e);
        }
        return new ArrayList<>();
    }

    public static void write(List<String> lines, String filePath) {
        try {
            FileUtils.writeLines(new File(filePath),
                    "UTF-8", lines, false);
        } catch (IOException e) {
            logger.error("IOException", e);
        }
    }

}
