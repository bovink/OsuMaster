package collection;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static util.ByteUtil.byte2Int;

/**
 * Created by Bovink on 2016/5/7 0007.
 */
public class CollectionDecoder {
    private String collectionDbPath = "G:/Project/IdeaProjects/buffer";

    private StringBuffer collectionInfo = new StringBuffer();

    /**
     * 解析后的collection.db文件
     */
    private CollectionDb collectionDb = new CollectionDb();

    public void decode() {

        readCollection();
    }

    /**
     * 获取解析后的collection.db文件
     *
     * @return
     */
    public CollectionDb getCollectionDb() {
        return collectionDb;
    }

    public void setCollectionDbPath(String collectionDbPath) {
        this.collectionDbPath = collectionDbPath;
    }

    /**
     * 读取文件二进制
     */
    private void readCollection() {

        File collection = new File(collectionDbPath + "/collection.db");
        try {
            // 获取文件2进制数组
            byte[] read = FileUtils.readFileToByteArray(collection);

            // 分割
            splitBytes(read);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void splitBytes(byte[] target) {
        // 获取version
        int doneLength = 0;
        byte[] version = new byte[4];
        System.arraycopy(target, doneLength, version, 0, version.length);
        collectionDb.setVersion(byte2Int(version));

        // 获取数量
        doneLength += version.length;
        byte[] collectionTotal = new byte[4];
        System.arraycopy(target, doneLength, collectionTotal, 0, collectionTotal.length);
        collectionDb.setCollectionTotal(byte2Int(collectionTotal));


        doneLength += collectionTotal.length;
        byte[] collections = new byte[target.length - doneLength];
        System.arraycopy(target, doneLength, collections, 0, collections.length);

        // 分割收藏夹
        splitCollections(collections);

    }

    private void splitCollections(byte[] target) {
        ArrayList<Byte> collections = arrayToArrayList(target);

        obtainCollection(collections);
    }

    /**
     * 获取一次收藏夹
     *
     * @param collections
     */
    private void obtainCollection(ArrayList<Byte> collections) {
        // 如果收藏夹的数量不为0
        if (collections.size() != 0) {
            // 存在收藏夹
            if (collections.get(0) == 11) {
                collection.Collection collection = new collection.Collection();
                collections.remove(0);

                // 获取长度
                int nameLength = collections.get(0);
                collections.remove(0);

                // 获取名字
                byte[] nameBytes = new byte[nameLength];
                for (int i = 0; i < nameLength; i++) {
                    nameBytes[i] = collections.get(0);
                    collections.remove(0);
                }
                try {
                    collection.setName(new String(nameBytes, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                //获取beatmap数量
                byte[] beatmapTotalBytes = new byte[4];
                for (int i = 0; i < 4; i++) {
                    beatmapTotalBytes[i] = collections.get(0);
                    collections.remove(0);

                }
                collection.setBeatmapTotal(byte2Int(beatmapTotalBytes));

                // 获取Beatmap
                obtainBeatmap(collection, collections);

            }
        } else {
            // 解析完毕
            System.out.println();
            collectionInfo.append("日期：" + collectionDb.getVersion() + "\n");
            collectionInfo.append("收藏夹数量：" + collectionDb.getCollectionTotal() + "\n");

            System.out.println("日期：" + collectionDb.getVersion());
            System.out.println("收藏夹数量：" + collectionDb.getCollectionTotal());
            for (collection.Collection collection : collectionDb.getCollections()) {
                System.out.println("收藏夹的名字：" + collection.getName());
                System.out.println("收藏夹中的图的数量：" + collection.getBeatmapTotal());

                collectionInfo.append("收藏夹的名字：" + collection.getName() + "\n");
                collectionInfo.append("收藏夹中的图的数量：" + collection.getBeatmapTotal() + "\n");
//                for (String s : collection.getBeatmapMD5List()) {
//                    System.out.println(s);

//                }
            }

        }

    }

    public StringBuffer getCollectionInfo() {
        return collectionInfo;
    }

    private void obtainBeatmap(collection.Collection collection, ArrayList<Byte> collections) {
        int index = collection.getBeatmapTotal();
        // 如果还有收藏夹没有解析
        while (index > 0) {
            if (collections.get(0) == 11) {
                collections.remove(0);

                // 获取md5的长度
                int md5Length = collections.get(0);
                collections.remove(0);

                // 获取md5的byte数组
                byte[] md5Bytes = new byte[md5Length];
                for (int i = 0; i < md5Length; i++) {
                    md5Bytes[i] = collections.get(0);
                    collections.remove(0);
                }

                // 获取md5
                try {
                    collection.getBeatmapMD5List().add(new String(md5Bytes, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            index -= 1;
        }
        collectionDb.getCollections().add(collection);
        obtainCollection(collections);

    }

    /**
     * 将数组转换成数组列表，方便操作
     */
    private ArrayList<Byte> arrayToArrayList(byte[] target) {
        ArrayList<Byte> list = new ArrayList<>();
        for (byte b : target) {
            list.add(b);
        }
        return list;
    }

}
