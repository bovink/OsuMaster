package collection;

import java.util.ArrayList;

/**
 * Created by Bovink on 2016/5/8 0008.
 */
public class CollectionDb {
    private int version;
    private int collectionTotal;
    private ArrayList<Collection> collections = new ArrayList<>();

    public ArrayList<Collection> getCollections() {
        return collections;
    }

    public void setCollections(ArrayList<Collection> collections) {
        this.collections = collections;
    }

    public int getCollectionTotal() {
        return collectionTotal;
    }

    public void setCollectionTotal(int collectionTotal) {
        this.collectionTotal = collectionTotal;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void addCollection(Collection collection) {
        Collection existCollection = notExist(collection.getName());
        if (existCollection != null) {
            for (String s : collection.getBeatmapMD5List()) {
                if (!existCollection.getBeatmapMD5List().contains(s)) {
                    existCollection.getBeatmapMD5List().add(s);

                }

            }

        } else {
            collections.add(collection);
            collectionTotal = collections.size();

        }


    }

    private Collection notExist(String name) {
        for (Collection collection : collections) {
            if (collection.getName().equals(name)) {
                return collection;

            }
        }

        return null;
    }
}
