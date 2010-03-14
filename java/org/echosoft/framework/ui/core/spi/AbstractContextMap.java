package org.echosoft.framework.ui.core.spi;

import java.util.AbstractMap;
import java.util.Map;

/**
 * @author Anton Sharapov
 */
public abstract class AbstractContextMap<K,V> extends AbstractMap<K,V> {

    public static class Entry<K,V> implements Map.Entry<K,V> {
        private final K key;
        private final V value;

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(final V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int hashCode() {
            return (key != null ? key.hashCode() : 0) ^ (value != null ? value.hashCode() : 0);
        }

        public boolean equals(final Object obj) {
            if (obj == null || !getClass().equals(obj.getClass()))
                return false;
            final Map.Entry input = (Map.Entry)obj;
            final Object inputKey = input.getKey();
            final Object inputValue = input.getValue();
            return (inputKey == key || inputKey != null && inputKey.equals(key)) && (inputValue == value || inputValue != null && inputValue.equals(value));
        }

        public Entry(final K key, final V value) {
            this.key = key;
            this.value = value;
        }
    }


    public AbstractContextMap() {
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> t) {
        throw new UnsupportedOperationException();
    }

    @Override    
    public V remove(final Object key) {
        throw new UnsupportedOperationException();
    }
}
