package collection;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static util.ByteUtil.int2Byte;

/**
 * Created by Bovink on 2016/5/8 0008.
 */
public class CollectionEncoder {
    ArrayList<Byte> list = new ArrayList<>();
    ArrayList<String> nameList = new ArrayList<>();
    private String collectionPath = "G:/Project/IdeaProjects/buffer/output";
    private ArrayList<String> targetMd5 = null;

    private CollectionDb collectionDb = null;

    public void setTargetMd5(ArrayList<String> targetMd5) {
        this.targetMd5 = targetMd5;
    }

    public void setCollectionDb(CollectionDb collectionDb) {

        this.collectionDb = collectionDb;
    }

    public void setCollectionPath(String collectionPath) {
        this.collectionPath = collectionPath;
    }

    public String getCollectionPath() {
        return collectionPath;
    }

    public void encode() {
        if (targetMd5 == null) {
            System.out.println("请添加地图");
//            return;
        }
        if (collectionDb == null) {
            System.out.println("请添加collection文件");
            return;
        }

//        initCollectionName();
//        addVersion();
//        addCollections();
        parse();
        byte[] b = convertListToArray(list);
        try {
            FileUtils.writeByteArrayToFile(new File(collectionPath + "/collection.db"), b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parse() {
        // 添加version
        byte[] version = int2Byte(collectionDb.getVersion());
        addBytes(version);

        // 添加collection数量
        byte[] collectionTotal = int2Byte(collectionDb.getCollectionTotal());
        addBytes(collectionTotal);

        // 添加collections
        for (Collection collection : collectionDb.getCollections()) {
            // head
            list.add((byte) 11);

            // name length
            list.add((byte) collection.getName().length());

            // name done
            try {
                byte[] nameBytes = collection.getName().getBytes("UTF-8");
                addBytes(nameBytes);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            // beatmapTotal
            byte[] beatmapTotal = int2Byte(collection.getBeatmapMD5List().size());
            addBytes(beatmapTotal);

            for (int i = 0; i < collection.getBeatmapMD5List().size(); i++) {
                list.add((byte) 11);
                try {
                    byte[] md5Bytes = collection.getBeatmapMD5List().get(i).getBytes("UTF-8");


                    list.add((byte) md5Bytes.length);
                    addBytes(md5Bytes);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

        }

    }


    // 添加需要生成的收藏文件夹名字 done
    private void initCollectionName() {
        nameList.add("None:160-170");
    }

    // 添加Version
    private void addVersion() {

        list.add((byte) 166);
        list.add((byte) 159);
        list.add((byte) 51);
        list.add((byte) 1);
    }


    private void addCollections() {
        // 添加数量
        byte[] collectionsNum = int2Byte(nameList.size());
        addBytes(collectionsNum);

        // 添加收藏
        addCollection();
    }

    private void addCollection() {
        for (String name : nameList) {
            // head done
            list.add((byte) 11);

            // name length
            list.add((byte) name.length());

            // name done
            try {
                byte[] nameBytes = name.getBytes("UTF-8");
                addBytes(nameBytes);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // beatmapTotal
            byte[] beatmapTotal = int2Byte(targetMd5.size());
            addBytes(beatmapTotal);

            for (int i = 0; i < targetMd5.size(); i++) {
                list.add((byte) 11);
                try {
                    byte[] md5Bytes = targetMd5.get(i).getBytes("UTF-8");


                    list.add((byte) md5Bytes.length);
                    addBytes(md5Bytes);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    // 转换数组列表到数组
    public byte[] convertListToArray(ArrayList<Byte> list) {
        byte[] bytes = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            bytes[i] = list.get(i);
        }

        return bytes;
    }

    // 将byte添加到数组列表
    public void addBytes(byte[] bytes) {
        for (byte b : bytes) {
            list.add(b);
        }
    }
}
