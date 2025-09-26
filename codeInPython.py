import csv

class Node:
    def __init__(self, data):
        self.data = data
        self.next = None

class MemoryDatabase:
    def __init__(self):
        self.head = None
        self.header = []

    def load_csv(self, filename):
        with open(filename, 'r') as f:
            rows = list(csv.reader(f))
            self.header = rows[0]
            self.head = self._load_rows(rows[1:])

    def _load_rows(self, rows):
        if not rows:
            return None
        node = Node(rows[0])
        node.next = self._load_rows(rows[1:])
        return node

    def save_csv(self, filename):
        with open(filename, 'w', newline='') as f:
            writer = csv.writer(f)
            writer.writerow(self.header)
            self._write_rows(self.head, writer)

    def _write_rows(self, node, writer):
        if node:
            writer.writerow(node.data)
            self._write_rows(node.next, writer)

    # Bubble Sort for linked list by first column (Name)
    def bubble_sort(self):
        if not self.head:
            return
        swapped = True
        while swapped:
            swapped = False
            curr = self.head
            while curr and curr.next:
                if curr.data[0] > curr.next.data[0]:
                    curr.data, curr.next.data = curr.next.data, curr.data
                    swapped = True
                curr = curr.next

    # Insertion Sort for linked list by first column (Name)
    def insertion_sort(self):
        sorted_head = None
        curr = self.head
        while curr:
            next_node = curr.next
            sorted_head = self._sorted_insert(sorted_head, curr)
            curr = next_node
        self.head = sorted_head

    def _sorted_insert(self, sorted_head, new_node):
        if not sorted_head or sorted_head.data[0] > new_node.data[0]:
            new_node.next = sorted_head
            return new_node
        curr = sorted_head
        while curr.next and curr.next.data[0] <= new_node.data[0]:
            curr = curr.next
        new_node.next = curr.next
        curr.next = new_node
        return sorted_head

    # Add/Remove rows (unchanged)
    def add_row(self, row):
        current = self.head
        while current:
            if current.data == row:
                return False
            current = current.next
        new_node = Node(row)
        new_node.next = self.head
        self.head = new_node
        return True

    def remove_row(self, row):
        dummy = Node(None)
        dummy.next = self.head
        prev, current = dummy, self.head
        removed = False
        while current:
            if current.data == row:
                prev.next = current.next
                removed = True
            else:
                prev = current
            current = current.next
        self.head = dummy.next
        return removed

if __name__ == "__main__":
    db = MemoryDatabase()
    db.load_csv('student-data.csv')
    
    # Bubble Sort and save
    db.bubble_sort()
    db.save_csv('BubbleSortedStudentData.csv')
    
    # Reload original, insertion sort and save
    db.load_csv('student-data.csv')
    db.insertion_sort()
    db.save_csv('InsertionSortedStudentData.csv')
    
    print("Sorting complete and files saved.")

