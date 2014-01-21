package io.msgs.v2.entity;

/**
 * ItemList enitiy.
 * 
 */
public class ItemList<T> {
    private int _total;
    private int _count;
    private T[] _items;

    /**
     * Get total.
     */
    public int getTotal() {
        return _total;
    }

    /**
     * Set total.
     */
    public void setTotal(int total) {
        _total = total;
    }

    /**
     * Get count.
     */
    public int getCount() {
        return _count;
    }

    /**
     * Set count.
     */
    public void setCount(int count) {
        _count = count;
    }

    /**
     * Get item for index.
     */
    public T get(int index) {
        return _items[index];
    }
    
    /**
     * Get items.
     */
    public T[] getItems() {
        return _items;
    }

    /**
     * Set items.
     */
    public void setItems(T[] items) {
        _items = items;
    }

}
