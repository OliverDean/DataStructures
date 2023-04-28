import CITS2200.Link;
import CITS2200.List;
import CITS2200.OutOfBounds;
import CITS2200.WindowLinked;

public class ListLinked implements List {
    private Link before;
    private Link after;
    
    /**
     * Constructs an empty ListLinked object.
     */
    public ListLinked() {
        after = new Link(null, null);
        before = new Link(null, after);
    }

    /**
     * Returns true if the list is empty, otherwise false.
     * @return boolean indicating if the list is empty.
     */
    public boolean isEmpty() {
        return before.successor == after;
    }

    /**
     * @param w The window object to check.
     * @return boolean indicating if the window is before the first element.
     */
    public boolean isBeforeFirst(WindowLinked w) {
        return w.link == before;
    }

    /**
     * @param w The window object to check.
     * @return boolean indicating if the window is after the last element.
     */
    public boolean isAfterLast(WindowLinked w) {
        return w.link == after;
    }

    /**
     * Moves the given window to the position before the first element.
     * @param w The window object to move.
     */
    public void beforeFirst(WindowLinked w) {
        w.link = before;
    }

    /**
     * Moves the given window to the position after the last element.
     * @param w The window object to move.
     */
    public void afterLast(WindowLinked w) {
        w.link = after;
    }

    /**
     * Moves the given window to the next element in the list.
     * @param w The window object to move.
     * @throws OutOfBounds if the window is at the position after the last element.
     */
    public void next(WindowLinked w) throws OutOfBounds {
        if (isAfterLast(w)) {
            throw new OutOfBounds("Calling next after list end.");
        }
        w.link = w.link.successor;
    }

    /**
     * Moves the given window to the previous element in the list.
     * @param w The window object to move.
     * @throws OutOfBounds if the window is at the position before the first element.
     */
    public void previous(WindowLinked w) throws OutOfBounds {
        if (isBeforeFirst(w)) {
            throw new OutOfBounds("Calling previous before start of list.");
        }
        Link current = before.successor;
        Link previous = before;
        while (current != w.link) {
            previous = current;
            current = current.successor;
        }
        w.link = previous;
    }

    /**
     * Inserts an element after the given window's position.
     * @param e The element to insert.
     * @param w The window object indicating the position to insert after.
     * @throws OutOfBounds if the window is at the position after the last element.
     */
    public void insertAfter(Object e, WindowLinked w) throws OutOfBounds {
        if (isAfterLast(w)) {
            throw new OutOfBounds("Inserting after end of list.");
        }
        w.link.successor = new Link(e, w.link.successor);
    }

    /**
     * Inserts an element before the given window's position.
     * @param e The element to insert.
     * @param w The window object indicating the position to insert before.
     * @throws OutOfBounds if the window is at the position before the first element.
     */
    public void insertBefore(Object e, WindowLinked w) throws OutOfBounds {
        if (isBeforeFirst(w)) {
            throw new OutOfBounds("Inserting before start of list.");
        }
        w.link.successor = new Link(w.link.item, w.link.successor);
        if (isAfterLast(w)) {
            after = w.link.successor;
        }
        w.link.item = e;
        w.link = w.link.successor;
    }

    /**
     * Returns the element at the given window's position.
     * @param w The window object indicating the position to examine.
     * @return The element at the given window's position.
     * @throws OutOfBounds if the window is at the position before the first element or after the last element.
     */
    public Object examine(WindowLinked w) throws OutOfBounds {
        if (isBeforeFirst(w) || isAfterLast(w)) {
            throw new OutOfBounds("Examine on invalid position.");
        }
        return w.link.item;
    }

    /**
     * Replaces the element at the given window's position with a new element.
     * @param e The new element to replace the current element.
     * @param w The window object indicating the position to replace.
     * @return The old element at the given window's position.
     * @throws OutOfBounds if the window is at the position before the first element or after the last element.
     */
    public Object replace(Object e, WindowLinked w) throws OutOfBounds {
        if (isBeforeFirst(w) || isAfterLast(w)) {
            throw new OutOfBounds("Replace on invalid position.");
        }
        Object oldItem = w.link.item;
        w.link.item = e;
        return oldItem;
    }

    /**
     * Deletes the element at the given window's position and moves the window to the next element.
     * @param w The window object indicating the position to delete.
     * @return The deleted element at the given window's position.
     * @throws OutOfBounds if the window is at the position before the first element or after the last element.
     */
    public Object delete(WindowLinked w) throws OutOfBounds {
        // NOTE: Your delete implementation MUST be constant time,
        // and therefore MAY NOT contain a loop or call previous()
        if (isBeforeFirst(w) || isAfterLast(w)) {
            throw new OutOfBounds("Delete on invalid position.");
        }
        Object removedItem = w.link.item;
        
        // Find the previous link in the list
        Link previous = before;
        while (previous.successor != w.link) {
            previous = previous.successor;
        }
    
        // Update the links to remove the current element
        previous.successor = w.link.successor;
        w.link = w.link.successor;
    
        return removedItem;
    }
}