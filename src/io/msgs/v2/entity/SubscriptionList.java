package io.msgs.v2.entity;

/**
 * SubscriptionList enitiy.
 *
 */
public class SubscriptionList {
    private int _total;
    private int _count;
    private Subscription[] _subscriptions;

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
     * Get subscriptions
     */
    public Subscription[] getSubscriptions() {
        return _subscriptions;
    }

    /**
     * Set subscriptions
     */
    public void setSubscriptions(Subscription[] subscriptions) {
        _subscriptions = subscriptions;
    }

}
