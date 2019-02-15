package com.hcf.floatword;

import com.hcf.floatword.service.WordFileService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

/**
 * 主界面
 */
public class WordFrame extends JFrame {

    private static final Logger logger = LoggerFactory.getLogger(WordFrame.class);

    private JToolBar toolBar;

    private int mousePressedX;
    private int mousePressedY;

    private Timer timer;

    private java.util.List<Word> allWord;

    private int showIndex = 0;

    private String wordFilePath;

    private JLabel wordLabel;

    public WordFrame(String wordFilePath) {
        this.wordFilePath = wordFilePath;

        JPanel jPanel = new JPanel();
        jPanel.setSize(600, 200);
        jPanel.setLayout(new BorderLayout());
        wordLabel = new JLabel("<html><body>准备好啦...</body></html>");
        wordLabel.setFont(new Font("GWIPA", Font.BOLD,25));
        wordLabel.setVerticalAlignment(SwingConstants.TOP);
        wordLabel.setForeground(new Color(82, 181, 65, 255));
        jPanel.add(wordLabel, BorderLayout.CENTER);
        jPanel.setOpaque(true);

        //JToolBar
        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(300, 40));
        topPanel.setBackground(new Color(0,0,0,0));
        JToolBar tb = new JToolBar();
        tb.setVisible(false);

        JButton removeBtn = new JButton("remove");
        removeBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                logger.info("删除单词：{}", allWord.get(showIndex - 1));

                File file = new File("word.properties");

                try {
                    java.util.List<String> lines =
                            FileUtils.readLines(file, "UTF-8");
                    lines.remove(showIndex-1);
                    FileUtils.writeLines(file, lines);
                    WordFrame.this.updateAllWord();
                    updateWord();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        });

        tb.add(removeBtn);
        tb.add(new JButton("tt1"));

        JButton wordLibBtn = new JButton("词库选择");
        wordLibBtn.addActionListener((e) -> {
            new WordLibDialog(WordFrame.this, allWord).setVisible(true);
        });
        tb.add(wordLibBtn);
        tb.addSeparator();

        JButton closeBtn = new JButton("×");
        closeBtn.addActionListener((e) -> {
            System.exit(0);
        });
        tb.add(closeBtn);
        tb.setFloatable(false);
        topPanel.add(tb);
        jPanel.add(topPanel, BorderLayout.NORTH);

        Border border = new LineBorder(new Color(0, 0, 0, 0));
        jPanel.setBorder(border);

        this.setLayout(null);
        this.setContentPane(jPanel);

        this.toolBar = tb;

        this.addMouseMotionListener(new MouseMotionAdapter() {

            private boolean top = false;
            private boolean down = false;
            private boolean left = false;
            private boolean right = false;
            private boolean drag = false;
            private Point lastPoint = null;
            private Point draggingAnchor = null;

            @Override
            public void mouseMoved(MouseEvent e) {
                if (e.getPoint().getY() == 0) {
                    WordFrame.this.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                    top = true;
                } else if (Math.abs(e.getPoint().getY() - WordFrame.this.getSize().getHeight()) <= 5) {
                    WordFrame.this.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                    down = true;
                } else if (e.getPoint().getX() == 0) {
                    WordFrame.this.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                    left = true;
                } else if (Math.abs(e.getPoint().getX() - WordFrame.this.getSize().getWidth()) <= 5) {
                    WordFrame.this.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                    right = true;
                } else {
                    WordFrame.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    draggingAnchor = new Point(e.getX() + jPanel.getX(), e.getY() + jPanel.getY());
                    top = false;
                    down = false;
                    left = false;
                    right = false;
                    drag = true;
                }

            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Dimension dimension = WordFrame.this.getSize();
                if (top) {

                    dimension.setSize(dimension.getWidth(), dimension.getHeight() - e.getY());
                    WordFrame.this.setSize(dimension);
                    WordFrame.this.setLocation(WordFrame.this.getLocationOnScreen().x, WordFrame.this.getLocationOnScreen().y + e.getY());

                } else if (down) {

                    dimension.setSize(dimension.getWidth(), e.getY());
                    WordFrame.this.setSize(dimension);

                } else if (left) {

                    dimension.setSize(dimension.getWidth() - e.getX(), dimension.getHeight());
                    WordFrame.this.setSize(dimension);
                    WordFrame.this.setLocation(WordFrame.this.getLocationOnScreen().x + e.getX(), WordFrame.this.getLocationOnScreen().y);

                } else if (right) {

                    dimension.setSize(e.getX(), dimension.getHeight());
                    WordFrame.this.setSize(dimension);

                } else {
                    /**
                    int onScreenX = e.getXOnScreen();
                    int onScreenY = e.getYOnScreen();
                    int xx = onScreenX - mousePressedX;
                    int yy = onScreenY - mousePressedY;
                    WordFrame.this.setLocation(xx, yy); **/
                    WordFrame.this.setLocation(e.getLocationOnScreen().x - draggingAnchor.x, e.getLocationOnScreen().y - draggingAnchor.y);
                }
            }

        });

        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                mousePressedX = e.getX();
                mousePressedY = e.getY();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                WordFrame.this.setBackground(new Color(71,155,232,100));
                WordFrame.this.setOpacity(1f);

                toolBar.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Point location = toolBar.getLocationOnScreen();
                Dimension toolBarSize = toolBar.getSize();

                if (e.getXOnScreen() >= location.x
                        && e.getXOnScreen() <= (location.x + toolBarSize.width)
                        && e.getYOnScreen() >= location.y
                        && e.getYOnScreen() <= (location.y + toolBarSize.height)
                        ) { // 如果鼠标在工具栏中
                    return;
                }

                WordFrame.this.setBackground(new Color(0,0,0,0));
                WordFrame.this.setOpacity(1f);

                toolBar.setVisible(false);
            }

        });

        allWord = WordFileService.readWord(wordFilePath);

        timer = new Timer(8000, (e) -> {
            if (showIndex > (allWord.size() - 1)) {
                showIndex = 0;
            }
            updateWord();
            ++ showIndex;
        });
        timer.start();
    }

    public void updateWord() {
        Word word = allWord.get(showIndex);
        wordLabel.setText("<html><body>" + word.getEnglish() + "<br>" + word.getChinese() + "</body></html>");
    }

    /**
     * 更新单词
     */
    public void updateAllWord() {
        allWord = WordFileService.readWord(wordFilePath);
        if (showIndex >= allWord.size()) {
            showIndex = 0;
        }
    }

    public static void main(String[] args) {
        WordFrame frame = new WordFrame(args[0]);
        frame.setUndecorated(true);
        frame.setSize(600, 200);
        frame.setLocationRelativeTo(null);
        frame.setBackground(new Color(0,0,0,0));
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
    }

}
