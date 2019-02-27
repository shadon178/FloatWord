package cn.sz.pxd.floatword.dialog;

import cn.sz.pxd.floatword.AppConf;
import cn.sz.pxd.floatword.WordFrame;

import javax.swing.*;
import java.awt.*;

public class SettingDialog extends JDialog {

    private WordFrame parent;

    public SettingDialog(WordFrame parent) {
        this.parent = parent;
        initUI();
    }

    private void initUI() {
        this.setTitle("设置");
        this.setSize(400, 500);
        this.setLocationRelativeTo(null);

        JTabbedPane tabPane = new JTabbedPane();

        JPanel uiShowPanel = new JPanel();

        JLabel timeLabel = new JLabel("切换时间");
        JTextField timeText = new JTextField(10);
        timeText.setText(String.valueOf(AppConf.SWITCH_TIME_SECOND));
        uiShowPanel.add(timeLabel);
        uiShowPanel.add(timeText);

        tabPane.addTab("界面显示", uiShowPanel);

        this.setLayout(new BorderLayout());
        this.add(tabPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();

        JButton okBtn = new JButton("OK");
        okBtn.addActionListener((e) -> {

            int switchTime = Integer.valueOf(timeText.getText());
            if (AppConf.SWITCH_TIME_SECOND != switchTime) {
                parent.updateSwitchTime(switchTime);
            }




            SettingDialog.this.setVisible(false);
        });
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener((e) -> {
            SettingDialog.this.setVisible(false);
        });
        btnPanel.add(okBtn);
        btnPanel.add(cancelBtn);
        this.add(btnPanel, BorderLayout.SOUTH);
    }

}
