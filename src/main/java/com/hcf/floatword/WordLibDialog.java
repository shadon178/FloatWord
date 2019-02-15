package com.hcf.floatword;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

/**
 * 单词选择对话框
 */
public class WordLibDialog extends JDialog {

    private static final Logger logger = LoggerFactory.getLogger(WordLibDialog.class);

    private String[] head = {"单词", "翻译"};

    public WordLibDialog(WordFrame frame) {
        this.setTitle("单词选择");
        this.setSize(400, 500);
        this.setLocationRelativeTo(null);

        String[][] data = readWord();

        DefaultTableModel tableModel = new DefaultTableModel(data, head);
        JTable table = new JTable(tableModel);
        JScrollPane pane = new JScrollPane(table);

        JButton newBtn = new JButton("新增");
        newBtn.addActionListener((e) -> {
            new NewWordDialog(tableModel).setVisible(true);
        });
        JButton delBtn = new JButton("删除");
        delBtn.addActionListener((e) -> {
            int selectedRow = table.getSelectedRow();
            tableModel.removeRow(selectedRow);
        });
        JButton saveBtn = new JButton("保存");
        saveBtn.addActionListener((e) -> {
            Vector dataVector = tableModel.getDataVector();
            String[][] newData = new String[dataVector.size()][2];
            for (int i = 0, size = dataVector.size(); i < size; i++) {
                Vector rowData = (Vector) dataVector.get(i);
                String word = (String) rowData.get(0);
                String translate = (String) rowData.get(1);
                newData[i][0] = word;
                newData[i][1] = translate;
            }
            writeWord(newData);
            frame.updateAllWord();
            WordLibDialog.this.setVisible(false);
        });

        JPanel btnPanel = new JPanel();
        btnPanel.add(newBtn);
        btnPanel.add(delBtn);
        btnPanel.add(saveBtn);

        this.setLayout(new BorderLayout());
        this.add(pane, BorderLayout.CENTER);
        this.add(btnPanel, BorderLayout.NORTH);
    }

    private void writeWord(String[][] data) {
        try {
            Properties pro = new Properties();
            for (int i = 0; i < data.length; i++) {
                pro.put(data[i][0], data[i][1]);
            }
            File file = new File("word.properties");
            FileOutputStream oFile = new FileOutputStream(file);
            pro.store(new OutputStreamWriter(oFile, "GBK"), "Comment");
            oFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[][] readWord() {
        try {
            Properties pro = new Properties();
            FileInputStream in = new FileInputStream(new File("word.properties"));
            pro.load(new InputStreamReader(in, "GBK"));
            Enumeration<String> propertyNames = (Enumeration<String>) pro.propertyNames();
            int count = pro.size();
            String[][] data = new String[count][2];
            int i = 0;
            while (propertyNames.hasMoreElements()) {
                String word = propertyNames.nextElement();
                String translate = (String) pro.get(word);
                String[] row = new String[]{word, translate};
                data[i] = row;
                ++ i;
            }

            in.close();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
