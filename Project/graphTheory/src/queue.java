package Project.graphTheory.src;

// Helper class for Queue
private class Queue {
    private int[] data;
    private int head;
    private int tail;

    public Queue(int size) {
        data = new int[size];
        head = 0;
        tail = 0;
    }

    public void enqueue(int value) {
        data[tail++] = value;
    }

    public int dequeue() {
        return data[head++];
    }

    public boolean isEmpty() {
        return head == tail;
    }
}