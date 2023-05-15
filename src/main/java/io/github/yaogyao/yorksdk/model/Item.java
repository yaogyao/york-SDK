package io.github.yaogyao.yorksdk.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Item {
    @SerializedName("_id")
    private String id;

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
