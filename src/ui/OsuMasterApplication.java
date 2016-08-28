package ui;

import collection.Collection;
import collection.CollectionDb;
import collection.CollectionDecoder;
import collection.CollectionEncoder;
import http.Http;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by bovink on 2016/5/17.
 */
public class OsuMasterApplication extends Application {

    private Stage stage;
    private GridPane mainPane;
    private MenuBar menuBar;
    private Dialog pathDialog;
    private VBox layout;

    private CollectionDb collectionDb;

    private String collectionPathString = "";
    private String downloadString = "";
    private String outputString = "";
    private Boolean alwaysStart = false;

    private String[] mods = {"0", "16", "64"};

    public static void main(String[] args) {
        launch(args);

    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        primaryStage.setTitle("OsuMaster");

        mainPane = new GridPane();
        mainPane.setPadding(new Insets(10, 10, 10, 10));
        mainPane.setHgap(5);
        mainPane.setVgap(10);
        initMainPane();

        menuBar = new MenuBar();
        initMenuBar();

        layout = new VBox();
        layout.getChildren().addAll(menuBar, mainPane);

        Scene scene = new Scene(layout, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void initMenuBar() {
        Menu menuFile = new Menu("File");
        MenuItem itemLoad = new MenuItem("Load");
        pathDialog = makeDialog();
        itemLoad.setOnAction(event -> pathDialog.showAndWait());
        MenuItem itemExit = new MenuItem("Exit");
        menuFile.getItems().addAll(itemLoad, new SeparatorMenuItem(), itemExit);
        menuBar.getMenus().addAll(menuFile);


    }

    private Dialog makeDialog() {
        Dialog dialog = new Dialog();
        dialog.setTitle("选择路径");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        Button close = (Button) dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        close.setVisible(false);

        // pane
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        TextField collectionPath = new TextField();
        collectionPath.setId("collection-path");
        collectionPath.setPrefWidth(400);
        TextField downloadPath = new TextField();
        downloadPath.setId("download-path");
        TextField outputPath = new TextField();
        outputPath.setId("output-path");

        // FileChooser
        DirectoryChooser directoryChooser = new DirectoryChooser();

        Button collectionPathBtn = new Button("...");
        collectionPathBtn.setOnAction(event -> {
            directoryChooser.setTitle("收藏夹路径");
            File file = directoryChooser.showDialog(stage);
            if (file != null) {
                collectionPath.setText(file.getAbsolutePath());
                collectionPathString = file.getAbsolutePath();
            }
        });
        Button downloadPathBtn = new Button("...");
        downloadPathBtn.setOnAction(event -> {
            directoryChooser.setTitle("请选择下载地址路径");
            File file = directoryChooser.showDialog(stage);
            if (file != null) {
                downloadPath.setText(file.getAbsolutePath());
            }
        });
        Button outputPathBtn = new Button("...");
        outputPathBtn.setOnAction(event -> {
            directoryChooser.setTitle("请选择输出收藏夹路径");
            File file = directoryChooser.showDialog(stage);
            if (file != null) {
                outputPath.setText(file.getAbsolutePath());

            }
        });

        gridPane.add(new Label("收藏夹路径："), 0, 0);
        gridPane.add(new Label("下载文件存放路径："), 0, 1);
        gridPane.add(new Label("输出收藏夹路径："), 0, 2);

        gridPane.add(collectionPath, 1, 0);
        gridPane.add(collectionPathBtn, 2, 0);
        gridPane.add(downloadPath, 1, 1);
        gridPane.add(downloadPathBtn, 2, 1);
        gridPane.add(outputPath, 1, 2);
        gridPane.add(outputPathBtn, 2, 2);

        dialog.getDialogPane().setContent(gridPane);
        return dialog;
    }

    private void initMainPane() {
        // 第一行
        TextField collectionName = new TextField();
        collectionName.setId("collection-name");
        collectionName.setPromptText("在此处填写收藏夹的名字");
        mainPane.add(new Label("收藏夹名字："), 0, 0);
        mainPane.add(collectionName, 1, 0);

        // 第二行
        mainPane.add(new Label("开始时间："), 0, 1);
//        DatePicker datePicker = new DatePicker();
        TextField datePicker = new TextField();
        datePicker.setId("start-time");
        datePicker.setPromptText("在此处填写搜索开始时间");

        mainPane.add(datePicker, 1, 1);

        // 第三行
        mainPane.add(new Label("Mod："), 0, 2);
        ChoiceBox mod = new ChoiceBox(FXCollections.observableArrayList("None", "HardRock", "DoubleTime"));
//        mod.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println(mods[newValue.intValue()]);
//            System.out.println(mod.getSelectionModel().getSelectedIndex());
//        });

        mod.getSelectionModel().selectFirst();
//        mod.setId("mods");
        mainPane.add(mod, 1, 2);

        // 第四行
        FlowPane performancePane = new FlowPane();
        TextField maxpp = new TextField();
        maxpp.setId("max-pp");
        maxpp.setPromptText("填写过滤的最大PP");
        maxpp.setPrefWidth(80);
        FlowPane.setMargin(maxpp, new Insets(0, 5, 0, 0));
        TextField minpp = new TextField();
        minpp.setId("min-pp");
        minpp.setPromptText("填写过滤的最小PP");
        minpp.setPrefWidth(80);
        performancePane.getChildren().addAll(minpp, new Label("MaxPP："), maxpp);
        mainPane.add(new Label("MinPP："), 0, 3);
        mainPane.add(performancePane, 1, 3, 3, 1);

        // 第五行
        Button start = new Button("开始");
        Button resolve = new Button("解析");
        Button copy = new Button("复制");
        Button always = new Button("一次");

        HBox buttonPane = new HBox();
        buttonPane.setSpacing(5);
        buttonPane.getChildren().addAll(resolve, start, copy, always);

        Label rest = new Label("尚无");

        Label label = makeSelectable(new Label("2009-00-00-00-00-00"));


        HBox labelPane = new HBox();
        labelPane.setSpacing(5);
        labelPane.getChildren().addAll(new Label("剩余解析数："), rest, new Label("解析完毕时间："), label);


        mainPane.add(buttonPane, 0, 4, 2, 1);
        mainPane.add(labelPane, 0, 5, 4, 1);

        // 第六行
        HBox collectionInfoPane = new HBox();
        collectionInfoPane.setSpacing(10);

        TextArea readInfo = new TextArea();
        readInfo.setPromptText("此处将会输出collection文件夹的信息");
        readInfo.setWrapText(true);
        readInfo.setPrefSize(400, 320);

//        TextArea doneInfo = new TextArea();
//        doneInfo.setPromptText("此处将会输出collection文件夹的信息");
//        doneInfo.setWrapText(true);
//        doneInfo.setPrefSize(400, 320);

        collectionInfoPane.getChildren().addAll(readInfo);


        mainPane.add(collectionInfoPane, 0, 6, 4, 1);

        resolve.setOnAction(event -> {


            CollectionDecoder decoder = new CollectionDecoder();
            TextField collectionPath = (TextField) pathDialog.getDialogPane().getContent().lookup("#collection-path");
            if (!collectionPath.getText().isEmpty()) {
                decoder.setCollectionDbPath(collectionPath.getText());
                decoder.decode();
                readInfo.setText(decoder.getCollectionInfo().toString());
                collectionDb = decoder.getCollectionDb();
            } else {
                readInfo.setText("请输入正确的collection文件地址");

            }
        });
        always.setOnAction(event -> {
            alwaysStart = !alwaysStart;
            if (alwaysStart) {
                always.setText("持续");
            } else {
                always.setText("一次");
            }


        });

        copy.setOnAction(event -> datePicker.setText(label.getText()));

        start.setOnAction(event -> {
            Task task = new Task() {
                @Override
                protected Object call() {
                    TextField downloadPath = (TextField) pathDialog.getDialogPane().getContent().lookup("#download-path");
                    TextField outputPath = (TextField) pathDialog.getDialogPane().getContent().lookup("#output-path");


                    Http http = new Http();
                    ArrayList<Pair> pairs = spiltPP(maxpp, minpp);
                    http.setPairs(pairs);
                    http.initCollections();
                    http.setDownloadPath(downloadPath.getText());
                    http.setMods(mods[mod.getSelectionModel().getSelectedIndex()]);
                    http.setStartTime(datePicker.getText());
//                        http.setEndTime(findString(END_TIME));
                    http.setMaxPP(maxpp.getText());
                    http.setMinPP(minpp.getText());

                    http.setListener(new Http.OnGetScoreListener() {
                        @Override
                        public void getScore(String restNum) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {

                                    rest.setText(restNum);
                                }
                            });

                        }
                    });
                    http.getBeatmap();

                    // 获取pairs的size，生成同样多的collection
                    // 名字：collectionName + 相应index的
                    // 获取相应index的collections的arraylist的size
                    // 获取相应index的collections的arralylist的内容
                    // 初始化需要添加的收藏夹
//                    Collection add = new Collection();
//                    add.setName(collectionName.getText());
//                    add.setBeatmapTotal(http.getTargetMd5().size());
//                    add.setBeatmapMD5List(http.getTargetMd5());
//                    // 将收藏夹添加到文件中
//                    collectionDb.addCollection(add);

                    for (int i = 0; i < pairs.size(); i++) {
                        Collection added = new Collection();
                        added.setName(collectionName.getText() + ":" + pairs.get(i).getFirst() + "-" + pairs.get(i).getSecond());
                        added.setBeatmapTotal(http.getCollections().get(i).size());
                        added.setBeatmapMD5List(http.getCollections().get(i));

                        collectionDb.addCollection(added);

                    }


                    CollectionEncoder encoder = new CollectionEncoder();
                    encoder.setCollectionPath(outputPath.getText());
                    encoder.setCollectionDb(collectionDb);
                    encoder.encode();

                    label.setText(http.getStartTime());

                    CollectionDecoder decoder = new CollectionDecoder();
                    if (!outputPath.getText().isEmpty()) {
                        decoder.setCollectionDbPath(outputPath.getText());
                        decoder.decode();
                        readInfo.setText(decoder.getCollectionInfo().toString());
                    } else {
                        readInfo.setText("请输入正确的collection文件地址");

                    }
                    if (alwaysStart) {
                        datePicker.setText(label.getText());
                        start.fire();
                    }

                    return null;
                }

            };

            new Thread(task).start();
        });
    }

    private Label makeSelectable(Label label) {
        StackPane textStack = new StackPane();
        TextField textField = new TextField(label.getText());
        textField.setEditable(false);
        textField.setStyle(
                "-fx-background-color: transparent; -fx-background-insets: 0; -fx-background-radius: 0; -fx-padding: 0;"
        );
        // the invisible label is a hack to get the textField to size like a label.
        Label invisibleLabel = new Label();
        invisibleLabel.textProperty().bind(label.textProperty());
        invisibleLabel.setVisible(false);
        textStack.getChildren().addAll(invisibleLabel, textField);
        label.textProperty().bindBidirectional(textField.textProperty());
        label.setGraphic(textStack);
        label.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        return label;
    }

    public static class Pair {
        int first;
        int second;

        public Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }

        public int getFirst() {
            return first;
        }

        public int getSecond() {
            return second;
        }
    }

    /**
     * 根据给定最大值和最小值划分区间
     *
     * @param maxpp
     * @param minpp
     */
    private ArrayList<Pair> spiltPP(TextField maxpp, TextField minpp) {

        int max = Integer.valueOf(maxpp.getText());
        int min = Integer.valueOf(minpp.getText());
        ArrayList<Pair> pairs = new ArrayList<>();
        while (min + 10 < max) {
            pairs.add(new Pair(min, min + 10));
            min += 10;
        }
        pairs.add(new Pair(min, max));
        for (Pair pair : pairs) {
            System.out.println("min:" + pair.getFirst() + " and " + "max:" + pair.getSecond());
        }
        return pairs;
    }
}
