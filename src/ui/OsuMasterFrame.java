package ui;

import collection.Collection;
import collection.CollectionDb;
import collection.CollectionDecoder;
import collection.CollectionEncoder;
import http.Http;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * Created by Bovink on 2016/5/15 0015.
 */
public class OsuMasterFrame extends JFrame {

    private HashMap<String, Component> uiLib = new HashMap<>();

    private static final String COLLECTION_PATH = "collection_path";
    private static final String DOWNLOAD_PATH = "download_path";
    private static final String OUTPUT_PATH = "output_path";
    private static final String COLLECTION_NAME = "collection_name";
    private static final String START_TIME = "start_time";
    private static final String END_TIME = "end_time";
    private static final String MODS = "mods";
    private static final String MAX_PP = "max_pp";
    private static final String MIN_PP = "min_pp";


    public OsuMasterFrame() {

        setTitle("OsuMaster");
        setSize(1024, 768);
        setLocationByPlatform(true);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
        setLocation((width - 1024) / 2, (height - 768) / 2);

        init();
    }

    private void init() {

        Container pane = getContentPane();

        // 输入面板
        JPanel input = new JPanel();
        input.setLayout(new BoxLayout(input, BoxLayout.Y_AXIS));


        createEditArea("collectionPath", input, COLLECTION_PATH);
        createEditArea("下载地址文件输出路径", input, DOWNLOAD_PATH);
        createEditArea("收藏夹文件输出路径", input, OUTPUT_PATH);
        createEditArea("名字", input, COLLECTION_NAME);
        createEditArea("开始时间", input, START_TIME);
        createEditArea("限制时间", input, END_TIME);
        createEditArea("Mod", input, MODS);
        createEditArea("PP最大值", input, MAX_PP);
        createEditArea("PP最小值", input, MIN_PP);

        // 输出面板
        JPanel output = new JPanel();
        output.setLayout(new BoxLayout(output, BoxLayout.Y_AXIS));
        output.add(Box.createVerticalStrut(10));

        JButton start = new JButton();

        start.setText("start");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                SwingWorker<Void, Void> w = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {

                        CollectionDecoder decoder = new CollectionDecoder();
                        decoder.setCollectionDbPath(findString(COLLECTION_PATH));
                        decoder.decode();

                        CollectionDb collectionDb = decoder.getCollectionDb();

                        JTextArea text = (JTextArea) uiLib.get("old_collection");
                        text.setText(decoder.getCollectionInfo().toString());
//
                        Http http = new Http();
                        http.setDownloadPath(findString(DOWNLOAD_PATH));
                        http.setMods(findString(MODS));
                        http.setStartTime(findString(START_TIME));
//                        http.setEndTime(findString(END_TIME));
                        http.setMaxPP(findString(MAX_PP));
                        http.setMinPP(findString(MIN_PP));

                        http.setListener(new Http.OnGetScoreListener() {
                            @Override
                            public void getScore(String rest) {
                                JLabel label = (JLabel) uiLib.get("rest");
                                label.setText(rest);
                                System.out.println("request");

                            }
                        });
                        http.getBeatmap();

                        // 初始化需要添加的收藏夹
                        Collection add = new Collection();
//                        add.setName("None:190-200");
                        add.setName(findString(COLLECTION_NAME));
                        add.setBeatmapTotal(http.getTargetMd5().size());
                        add.setBeatmapMD5List(http.getTargetMd5());

                        // 将收藏夹添加到文件中
                        collectionDb.addCollection(add);


                        CollectionEncoder encoder = new CollectionEncoder();
                        encoder.setCollectionPath(findString(OUTPUT_PATH));
                        encoder.setCollectionDb(collectionDb);
                        encoder.encode();

                        CollectionDecoder decoder1 = new CollectionDecoder();
                        decoder1.setCollectionDbPath(findString(OUTPUT_PATH));
                        decoder1.decode();
                        JTextArea textArea2 = (JTextArea) uiLib.get("new_collection");
                        textArea2.setText(decoder1.getCollectionInfo().toString());

                        JTextArea textArea = (JTextArea) uiLib.get("time");
                        textArea.setText(http.getStartTime());
                        return null;
                    }
                };
                w.execute();
            }
        });
        output.add(start);

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.X_AXIS));

        JTextArea collectionInfo = new JTextArea();
        collectionInfo.setEditable(false);
        uiLib.put("old_collection", collectionInfo);
        text.add(collectionInfo);

        JTextArea newCollection = new JTextArea();
        newCollection.setEditable(false);
        uiLib.put("new_collection", newCollection);
        text.add(newCollection);

        output.add(text);


        // 相关信息
        JPanel other = new JPanel();
        other.setSize(300, 300);
        other.setLayout(new BoxLayout(other, BoxLayout.Y_AXIS));

        JLabel restTitle = new JLabel();
        uiLib.put("rest", restTitle);
        restTitle.setText("剩余需解析数：0");
        other.add(restTitle);

        JTextArea time = new JTextArea();
        time.setLineWrap(true);
        time.setEditable(false);
        time.setText("2012-02-03-12:22:32");
        uiLib.put("time", time);
        other.add(time);


        pane.add(input, BorderLayout.NORTH);

        pane.add(output, BorderLayout.CENTER);
        pane.add(other, BorderLayout.EAST);
    }

    private String findString(String name) {
        JTextField textField = (JTextField) uiLib.get(name);

        return textField.getText();
    }
    private void createEditArea(String content, JPanel container, String name) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        panel.add(Box.createHorizontalStrut(10));

        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(100, 30));
        label.setText(content);
        panel.add(label);

        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(500, 30));
        textField.setName(name);
        uiLib.put(name, textField);
        panel.add(textField);

        panel.add(Box.createHorizontalStrut(10));

        container.add(Box.createVerticalStrut(10));
        container.add(panel);
    }




}
