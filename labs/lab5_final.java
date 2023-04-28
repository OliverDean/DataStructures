package labs;
import CITS2200.BinaryTree;
import CITS2200.Iterator;
import CITS2200.OutOfBounds;

public class BinTree<E> extends BinaryTree<E> {
    public BinTree() {
        super();
    }

    public BinTree(E item, BinaryTree<E> ltree, BinaryTree<E> rtree) {
        super(item, ltree, rtree);
    }

    /**
     * Determines if two BinTree instances are equal.
     *
     * @param obj The object to compare with the current BinTree instance.
     * @return true if the BinTree instances share identical structure and elements, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BinTree) {
            BinTree<?> other = (BinTree<?>) obj;

            if (this.isEmpty() && other.isEmpty()) {
                return true;
            }

            if (this.isEmpty() || other.isEmpty() || !this.getItem().equals(other.getItem())) {
                return false;
            }

            return this.getLeft().equals(other.getLeft()) && this.getRight().equals(other.getRight());
        }

        return false;
    }

    /**
     * Creates an iterator for the BinTree instance, using a breadth-first traversal technique.
     */
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node first;
            Node last;

            class Node {
                BinaryTree<E> treeItem;
                Node nextItem;

                public Node(BinaryTree<E> treeItem) {
                    this.treeItem = treeItem;
                    this.nextItem = null;
                }
            }

            {
                if (!BinTree.this.isEmpty()) {
                    first = last = new Node(BinTree.this);
                }
            }

            @Override
            public boolean hasNext() {
                return first != null;
            }

            @Override
            public E next() {
                if (hasNext()) {
                    BinaryTree<E> currNode = first.treeItem;

                    if (!currNode.getLeft().isEmpty()) {
                        last.nextItem = new Node(currNode.getLeft());
                        last = last.nextItem;
                    }
                    if (!currNode.getRight().isEmpty()) {
                        last.nextItem = new Node(currNode.getRight());
                        last = last.nextItem;
                    }

                    first = first.nextItem;
                    return currNode.getItem();
                }
                throw new IllegalStateException("No more elements in the iterator.");
            }
        };
    }
}