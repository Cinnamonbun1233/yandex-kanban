package manager;

import java.util.Objects;

public class Node<Task> {

    Node<Task> previous;
    Task current;
    Node<Task> next;

    public Node(Node<Task> previous, Task current, Node<Task> next) {
        this.previous = previous;
        this.current = current;
        this.next = next;
    }

    public Node<Task> getPrevious() {
        return previous;
    }

    public void setPrevious(Node<Task> previous) {
        this.previous = previous;
    }

    public Task getCurrent() {
        return current;
    }

    public void setCurrent(Task current) {
        this.current = current;
    }

    public Node<Task> getNext() {
        return next;
    }

    public void setNext(Node<Task> next) {
        this.next = next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return Objects.equals(previous, node.previous) && Objects.equals(current, node.current) &&
                Objects.equals(next, node.next);
    }
}
