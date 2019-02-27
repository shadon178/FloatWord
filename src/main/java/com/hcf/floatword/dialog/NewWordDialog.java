package com.hcf.floatword.dialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class NewWordDialog extends JDialog {

    public NewWordDialog(DefaultTableModel tableModel) {
        this.setTitle("新建单词");
        this.setSize(400, 264);
        this.setModal(true);
        this.setLocationRelativeTo(null);

        JLabel wordLabel = new JLabel("单词");
        JTextField wordTextField = new JTextField(30);

        JLabel soundMarkLabel = new JLabel("音标");
        JTextField soundMarkField = new JTextField(30);

        JLabel translateLabel = new JLabel("翻译");
        JTextArea translateTextArea = new JTextArea(10, 30);

        JButton saveBtn = new JButton("保存");
        saveBtn.addActionListener((e) -> {
            tableModel.addRow(new String[]{wordTextField.getText(), translateTextArea.getText()});
            NewWordDialog.this.setVisible(false);
        });

        JPanel panel = new JPanel();
        panel.add(wordLabel);
        panel.add(wordTextField);
        panel.add(soundMarkLabel);
        panel.add(soundMarkField);
        panel.add(translateLabel);
        panel.add(translateTextArea);
        panel.add(saveBtn);
        this.add(panel);
    }

}
