import java.util.Random;
import java.util.Scanner;

public class Main {
    public static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        test2();
    }

    private static void test() {
        HashTable<String, Integer> hashTable = new HashTable<>();
        for (int i = 0; i < 16; i++) {
            hashTable.add("A" + i, i);
        }
        System.out.println(hashTable);
        for (int i = 0; i < 16; i++) {
            hashTable.addOrReplace("A" + i, 2 * i);
        }
        System.out.println(hashTable);
        System.out.println(hashTable.get(scanner.next()));
        System.out.println(hashTable.find("A12"));
        System.out.println(hashTable.containsKey("A17"));
        for (int i = 0; i < 8; i++) {
            hashTable.remove("A" + i);
        }
        hashTable.remove("A14");
        System.out.println(hashTable);
        for (String s : hashTable.keys()) {
            System.out.println(s);
        }
        for (int i : hashTable.values()) {
            System.out.println(i);
        }
        for (KeyValue<String, Integer> keyValue : hashTable) {
            System.out.println(keyValue);
        }
        hashTable.clear();
        System.out.println(hashTable);
    }

    private static void test2() {
        HashTable<String, Integer> hashTable = new HashTable<>();
        for (int i = 0; i < 10000; i++) {
            hashTable.add("A" + new Random().nextInt(1, 1000), i);
        }
        System.out.println(hashTable);
    }
}