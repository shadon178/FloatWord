package cn.sz.pxd.floatword.dialog;

import cn.sz.pxd.floatword.AppConf;
import cn.sz.pxd.floatword.Word;
import cn.sz.pxd.floatword.WordFrame;
import cn.sz.pxd.floatword.service.WordFileService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * 单词选择对话框
 * @author https://github.com/shadon178
 */
public class WordLibDialog extends JDialog {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public WordLibDialog(WordFrame frame, java.util.List<Word> words) {
        this.setTitle("单词选择");
        this.setSize(400, 500);
        this.setLocationRelativeTo(null);

        String[][] tableData = new String[words.size()][3];
        for (int i = 0, size = words.size(); i < size; i++) {
            Word word = words.get(i);
            tableData[i][0] = word.getEnglish();
            tableData[i][1] = word.getSoundmark();
            tableData[i][2] = word.getChinese();
        }

        String[] head = {"单词", "音标", "翻译"};
        DefaultTableModel tableModel = new DefaultTableModel(tableData, head);
        RowSorter sorter = new TableRowSorter(tableModel);
        JTable table = new JTable(tableModel);
        table.setRowSorter(sorter);
        JScrollPane pane = new JScrollPane(table);

        JButton newBtn = new JButton("新增");
        newBtn.addActionListener((e) -> new NewWordDialog(tableModel).setVisible(true));
        JButton delBtn = new JButton("删除");
        delBtn.addActionListener((e) -> {
            int selectedRow = table.getSelectedRow();
            int i = table.convertRowIndexToModel(selectedRow);
            tableModel.removeRow(i);
        });
        JButton saveBtn = new JButton("保存");
        saveBtn.addActionListener((e) -> {
            Vector dataVector = tableModel.getDataVector();
            java.util.List<String> lines = new ArrayList<>();
            for (Object o : dataVector) {
                Vector rowData = (Vector) o;
                String word = (String) rowData.get(0);
                String soundMark = (String) rowData.get(1);
                String translate = (String) rowData.get(2);
                String line = word + "\t" + soundMark + "\t" + translate;
                lines.add(line);
            }
            WordFileService.write(lines, AppConf.wordFilePath);
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

}
