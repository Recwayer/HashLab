import java.util.*;

public class HashTable<K, V> implements Iterable<KeyValue<K, V>> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.80d;

    private LinkedList<KeyValue<K, V>>[] slots;
    private int capacity;
    private int size;


    public HashTable() {
        slots = new LinkedList[INITIAL_CAPACITY];
        this.capacity = slots.length;
    }

    public HashTable(int capacity) {
        slots = new LinkedList[capacity];
        this.capacity = slots.length;
    }

    public void add(K key, V value) {
        int slotNumber = findSlotNumber(key);
        growIfNeeded();
        if (slots[slotNumber] == null) {
            slots[slotNumber] = new LinkedList<>();
        }
        for (KeyValue<K, V> entry : slots[slotNumber]) {
            if (entry.getKey().equals(key)) {
                entry.setValue(value);
                return;
            }
        }
            slots[slotNumber].add(new KeyValue<>(key, value));
            this.size++;
    }

    private int findSlotNumber(K key) {
        return Math.abs(key.hashCode()) % this.slots.length;
    }

    private void growIfNeeded() {
        if ((double) (this.size() + 1) / this.capacity() > LOAD_FACTOR) {
            this.grow();
        }
    }

    private void grow() {
        int newCapacity = 2 * capacity();
        LinkedList<KeyValue<K, V>>[] newSlots = new LinkedList[newCapacity];
        for (LinkedList<KeyValue<K, V>> slot : slots) {
            if (slot != null) {
                for (KeyValue<K, V> entry : slot) {
                    int newSlotNumber = Math.abs(entry.getKey().hashCode()) % newCapacity;

                    if (newSlots[newSlotNumber] == null) {
                        newSlots[newSlotNumber] = new LinkedList<>();
                    }

                    newSlots[newSlotNumber].add(entry);
                }
            }
        }
        this.slots = newSlots;
        this.capacity = newCapacity;
    }

    public int size() {
        return this.size;
    }

    public int capacity() {
        return this.capacity;
    }

    public boolean addOrReplace(K key, V value) {
        int slotNumber = findSlotNumber(key);
        growIfNeeded();

        if (slots[slotNumber] == null) {
            slots[slotNumber] = new LinkedList<>();
        }

        for (KeyValue<K, V> entry : slots[slotNumber]) {
            if (entry.getKey().equals(key)) {
                entry.setValue(value);
                return true;
            }
        }

        slots[slotNumber].add(new KeyValue<>(key, value));
        this.size++;
        return false;
    }

    public V get(K key) {
        int slotNumber = findSlotNumber(key);
        if (slots[slotNumber] != null) {
            for (KeyValue<K, V> entry : slots[slotNumber]) {
                if (entry.getKey().equals(key)) {
                    return entry.getValue();
                }
            }
        }

        throw new RuntimeException("Key not found: " + key);
    }

    public KeyValue<K, V> find(K key) {
        int slotNumber = findSlotNumber(key);
        if (slots[slotNumber] != null) {
            for (KeyValue<K, V> entry : slots[slotNumber]) {
                if (entry.getKey().equals(key)) {
                    return entry;
                }
            }
        }

        throw new RuntimeException("Key not found: " + key);
    }

    public boolean containsKey(K key) {
        int slotNumber = findSlotNumber(key);
        if (slots[slotNumber] != null) {
            for (KeyValue<K, V> entry : slots[slotNumber]) {
                if (entry.getKey().equals(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean remove(K key) {
        int slotNumber = findSlotNumber(key);

        if (slots[slotNumber] != null) {
            Iterator<KeyValue<K, V>> iterator = slots[slotNumber].iterator();

            while (iterator.hasNext()) {
                KeyValue<K, V> entry = iterator.next();
                if (Objects.equals(entry.getKey(), key)) {
                    iterator.remove();
                    this.size--;
                    return true;
                }
            }
        }
        throw new RuntimeException("Key not found: " + key);
    }

    public void clear() {
        for (int i = 0; i < capacity; i++) {
            if (slots[i] != null) {
                slots[i].clear();
            }
        }
        this.size = 0;
    }

    public Iterable<K> keys() {
        LinkedList<K> keys = new LinkedList<>();

        for (LinkedList<KeyValue<K, V>> slot : slots) {
            if (slot != null) {
                for (KeyValue<K, V> entry : slot) {
                    keys.add(entry.getKey());
                }
            }
        }

        return keys;
    }

    public Iterable<V> values() {
        LinkedList<V> values = new LinkedList<>();

        for (LinkedList<KeyValue<K, V>> slot : slots) {
            if (slot != null) {
                for (KeyValue<K, V> entry : slot) {
                    values.add(entry.getValue());
                }
            }
        }

        return values;
    }

    @Override
    public Iterator<KeyValue<K, V>> iterator() {
        return new KeyValueIterator();
    }

    @Override
    public String toString() {
        return "HashTable{" +
                "slots=" + Arrays.toString(slots) +
                ", capacity=" + capacity +
                ", size=" + size +
                '}';
    }

    private class KeyValueIterator implements Iterator<KeyValue<K, V>> {
        private int slotIndex = -1;
        private Iterator<KeyValue<K, V>> entryIterator;

        KeyValueIterator() {
            findNextNonEmptySlot();
        }

        @Override
        public boolean hasNext() {
            return entryIterator != null && entryIterator.hasNext();
        }

        @Override
        public KeyValue<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in the hash table");
            }
            KeyValue<K, V> next = entryIterator.next();
            if (!entryIterator.hasNext()) {
                findNextNonEmptySlot();
            }
            return next;
        }

        @Override
        public void remove() {
            if (entryIterator == null) {
                throw new IllegalStateException("Cannot remove an element before calling next()");
            }
            entryIterator.remove();
            size--;
        }

        private void findNextNonEmptySlot() {
            entryIterator = null;

            while (++slotIndex < capacity) {
                if (slots[slotIndex] != null && !slots[slotIndex].isEmpty()) {
                    entryIterator = slots[slotIndex].iterator();
                    return;
                }
            }
        }
    }
}
