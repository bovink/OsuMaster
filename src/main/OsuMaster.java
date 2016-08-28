package main;

import collection.Collection;
import collection.CollectionDb;
import collection.CollectionDecoder;
import collection.CollectionEncoder;
import http.Http;

/**
 * Created by Bovink on 2016/5/7 0007.
 */
public class OsuMaster {
    public static void main(String[] args) {
        CollectionDecoder decoder = new CollectionDecoder();
        decoder.decode();
        CollectionDb collectionDb = decoder.getCollectionDb();

        Http http = new Http();
        http.getBeatmap();

        // 初始化需要添加的收藏夹
        Collection add = new Collection();
        add.setName("None:220-230");
        add.setBeatmapTotal(http.getTargetMd5().size());
        add.setBeatmapMD5List(http.getTargetMd5());

        // 将收藏夹添加到文件中
        collectionDb.addCollection(add);


        CollectionEncoder encoder = new CollectionEncoder();
        encoder.setCollectionDb(collectionDb);
        encoder.encode();
    }
}
