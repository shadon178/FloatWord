package cn.sz.pxd.floatword;

import cn.sz.pxd.floatword.dialog.SettingDialog;
import cn.sz.pxd.floatword.dialog.WordLibDialog;
import cn.sz.pxd.floatword.service.WordFileService;
import cn.sz.pxd.floatword.utils.DownloadFile;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 主界面
 * @author https://github.com/shadon178
 */
public class WordFrame extends JWindow {

    private static final Logger logger = LoggerFactory.getLogger(WordFrame.class);

    private final JToolBar toolBar;

    private Timer timer;

    private java.util.List<Word> allWord;

    private int showIndex = 0;

    private final String wordFilePath;

    private final JLabel wordLabel;

    private final ExecutorService executors = Executors.newSingleThreadExecutor();

    public WordFrame(String wordFilePath) {
        AppConf.wordFilePath = wordFilePath;
        this.wordFilePath = wordFilePath;

        JPanel jPanel = new JPanel();
        jPanel.setSize(600, 200);
        jPanel.setLayout(new BorderLayout());
        wordLabel = new JLabel("<html><body>准备好啦...</body></html>");
        wordLabel.setFont(new Font("GWIPA", Font.BOLD,30));
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
        removeBtn.addActionListener((event) -> {
            logger.info("删除单词：{}", allWord.get(showIndex));

            File file = new File(wordFilePath);

            try {
                java.util.List<String> lines =
                        FileUtils.readLines(file, "UTF-8");
                lines.remove(showIndex);
                FileUtils.writeLines(file, "UTF-8", lines, false);
                WordFrame.this.updateAllWord();
                updateWord();
            } catch (IOException e) {
                logger.error("删除单词出现异常", e);
            }
        });

        tb.add(removeBtn);

        JButton previousBtn = new JButton("<<");
        previousBtn.addActionListener((e) -> previousWord());
        tb.add(previousBtn);
        JButton stopBtn = new JButton("||");
        stopBtn.addActionListener((e) -> {
            if (timer.isRunning()) {
                timer.stop();
            } else {
                nextWord();
                timer.start();
            }
        });
        tb.add(stopBtn);

        JButton nextBtn = new JButton(">>");
        nextBtn.addActionListener((e) -> nextWord());
        tb.add(nextBtn);

        JButton wordLibBtn = new JButton("词库选择");
        wordLibBtn.addActionListener((e) -> new WordLibDialog(WordFrame.this, allWord).setVisible(true));
        tb.add(wordLibBtn);

        JButton settingBtn = new JButton("Setting");
        settingBtn.addActionListener((e) -> new SettingDialog(this).setVisible(true));
        tb.add(settingBtn);

        JButton setTopBtn = new JButton("Top");
        setTopBtn.addActionListener((e) -> WordFrame.this.setAlwaysOnTop(true));
        tb.add(setTopBtn);

        tb.addSeparator();

        JButton closeBtn = new JButton("×");
        closeBtn.addActionListener((e) -> System.exit(0));
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
                }

            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Dimension dimension = WordFrame.this.getSize();
                if (top) {

                    dimension.setSize(dimension.getWidth(), dimension.getHeight() - e.getY());
                    WordFrame.this.setSize(dimension);
                    WordFrame.this.setLocation(
                            WordFrame.this.getLocationOnScreen().x,
                            WordFrame.this.getLocationOnScreen().y + e.getY());

                } else if (down) {

                    dimension.setSize(dimension.getWidth(), e.getY());
                    WordFrame.this.setSize(dimension);

                } else if (left) {

                    dimension.setSize(dimension.getWidth() - e.getX(), dimension.getHeight());
                    WordFrame.this.setSize(dimension);
                    WordFrame.this.setLocation(
                            WordFrame.this.getLocationOnScreen().x + e.getX(),
                            WordFrame.this.getLocationOnScreen().y);

                } else if (right) {

                    dimension.setSize(e.getX(), dimension.getHeight());
                    WordFrame.this.setSize(dimension);

                } else {
                    WordFrame.this.setLocation(
                            e.getLocationOnScreen().x - draggingAnchor.x,
                            e.getLocationOnScreen().y - draggingAnchor.y);
                }
            }

        });

        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                WordFrame.this.setBackground(new Color(71,155,232,100));
                WordFrame.this.setOpacity(1f);

                toolBar.setVisible(true);
                timer.stop();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                timer.start();
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
        updateWord();

        timer = new Timer(AppConf.SWITCH_TIME_SECOND * 1000, (e) -> {
            if (showIndex > (allWord.size() - 1)) {
                showIndex = 0;
            }
            nextWord();
        });
        timer.start();
    }

    public void nextWord() {
        ++ showIndex;
        if (showIndex >= allWord.size()) {
            showIndex = 0;
        }
        updateWord();
    }

    public void previousWord() {
        -- showIndex;
        if (showIndex < 0) {
            showIndex = 0;
        }
        updateWord();
    }

    public void updateWord() {
        Word word = allWord.get(showIndex);
        wordLabel.setText("<html><body>"
            + word.getEnglish() + "<br>"
            + word.getSoundmark() + "<br>"
            + word.getChinese() + "</body></html>"
        );
        executors.execute(() -> play(word.getEnglish()));
    }

    public void play(String word) {
        String filePath = Objects.requireNonNull(
            WordFrame.class.getClassLoader().getResource("sound")
        ).getPath() + "/" + word + "_us.mp3";

        File soundFile = new File(filePath);
        if (!soundFile.exists()) {
            DownloadFile.downloadByGet(word);
        }
        try (FileInputStream stream = new FileInputStream(filePath)) {
            Player player = new Player(stream);
            player.play();
        } catch (FileNotFoundException e) {
            logger.info("音频文件不存在，不播放。 path: {}", filePath);

        } catch (JavaLayerException | IOException e) {
            logger.error("播放音频文件时出错，不播放。", e);
        }
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

    public void updateSwitchTime(int switchTime) {
        timer.setDelay(switchTime * 1000);
        AppConf.SWITCH_TIME_SECOND = switchTime;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("缺少必要的‘单词文件路径’参数");
            System.exit(-1);
        }
        WordFrame frame = new WordFrame(args[0]);
        frame.setSize(600, 200);
        frame.setLocationRelativeTo(null);
        frame.setBackground(new Color(0,0,0,0));
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
    }

}
