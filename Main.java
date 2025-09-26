import java.io.*;
import java.util.*;

class Node {
    String[] data;
    Node next;

    Node(String[] data) {
        this.data = data;
        this.next = null;
    }
}

class MemoryDatabase {
    private Node head;
    private List<String> header;

    public MemoryDatabase() {
        head = null;
        header = new ArrayList<>();
    }

    // Load CSV file into linked list
    public void loadCSV(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line = reader.readLine();
        if (line != null) {
            header = Arrays.asList(line.split(","));
        }
        List<String[]> rows = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            rows.add(line.split(","));
        }
        reader.close();

        head = loadRowsRec(rows, 0);
    }

    // Recursively load rows into linked list
    private Node loadRowsRec(List<String[]> rows, int index) {
        if (index >= rows.size()) return null;
        Node node = new Node(rows.get(index));
        node.next = loadRowsRec(rows, index + 1);
        return node;
    }

    // Save linked list back to CSV using recursion
    public void saveCSV(String filename) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(filename));
        writer.println(String.join(",", header));
        writeRowsRec(head, writer);
        writer.close();
    }

    private void writeRowsRec(Node node, PrintWriter writer) {
        if (node != null) {
            writer.println(String.join(",", node.data));
            writeRowsRec(node.next, writer);
        }
    }

    // Bubble sort linked list nodes by age (column 2)
    public void bubbleSort() {
        if (head == null) return;

        boolean swapped;
        do {
            swapped = false;
            Node curr = head;

            while (curr != null && curr.next != null) {
                int age1 = Integer.parseInt(curr.data[2]);
                int age2 = Integer.parseInt(curr.next.data[2]);

                if (age1 > age2) {
                    String[] temp = curr.data;
                    curr.data = curr.next.data;
                    curr.next.data = temp;
                    swapped = true;
                }

                curr = curr.next;
            }
        } while (swapped);
    }

    // Insertion sort linked list nodes by age (column 2)
    public void insertionSort() {
        Node sorted = null;
        Node current = head;

        while (current != null) {
            Node next = current.next;
            sorted = sortedInsert(sorted, current);
            current = next;
        }
        head = sorted;
    }

    private Node sortedInsert(Node sortedHead, Node newNode) {
        int newNodeAge = Integer.parseInt(newNode.data[2]);

        if (sortedHead == null || Integer.parseInt(sortedHead.data[2]) > newNodeAge) {
            newNode.next = sortedHead;
            return newNode;
        }

        Node current = sortedHead;
        while (current.next != null && Integer.parseInt(current.next.data[2]) <= newNodeAge) {
            current = current.next;
        }
        newNode.next = current.next;
        current.next = newNode;

        return sortedHead;
    }
}

public class Main {
    public static void main(String[] args) {
        try {
            MemoryDatabase db = new MemoryDatabase();
            db.loadCSV("student-data.csv");

            db.bubbleSort();
            db.saveCSV("BubbleSortedByAge.csv");
            System.out.println("Bubble sort complete. Output saved in BubbleSortedByAge.csv");

            db.loadCSV("student-data.csv");
            db.insertionSort();
            db.saveCSV("InsertionSortedByAge.csv");
            System.out.println("Insertion sort complete. Output saved in InsertionSortedByAge.csv");

        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}

