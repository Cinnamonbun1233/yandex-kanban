package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    Map<Integer, Node<Task>> mapNode = new HashMap<>();
    Node<Task> first = null;
    Node<Task> last = null;

    private List<Task> getTasks() {
        ArrayList<Task> historyList = new ArrayList<>();
        Node<Task> lastNode = last;
        Node<Task> thisNode = first;
        if (lastNode == null && thisNode == null) {
            return historyList;
        }
        do {
            historyList.add(thisNode.getCurrent());
            thisNode = thisNode.getNext();
        } while (thisNode != null);
        return historyList;
    }

    private void linkFirst(Task task) {
        if (task == null) {
            return;
        }
        Node<Task> nodeFirst = new Node<>(null, task, null);
        mapNode.put(task.getId(), nodeFirst);
        first = nodeFirst;
        last = nodeFirst;
    }

    private void linkLast(Task task) {
        if (task == null) {
            return;
        }
        Node<Task> nodeLast = new Node<>(last, task, null);
        last.setNext(nodeLast);
        last = nodeLast;
        mapNode.put(task.getId(), nodeLast);
    }

    private void removeNode(Node<Task> node) {
        if (node == null) {
            return;
        }
        if (node.getPrevious() == null && node.getNext() == null) {
            first = null;
            last = null;
        } else if (node.getPrevious() == null) {
            Node<Task> newFirstNode = node.getNext();
            newFirstNode.setPrevious(null);
            first = newFirstNode;
        } else if (node.getNext() == null) {
            Node<Task> newLastNode = node.getPrevious();
            newLastNode.setNext(null);
            last = newLastNode;
        } else if (node.getPrevious() != null && node.getNext() != null) {
            Node<Task> prevNode = node.getPrevious();
            Node<Task> nextNode = node.getNext();
            prevNode.setNext(nextNode);
            nextNode.setPrevious(prevNode);
        }
        mapNode.remove(node.getCurrent().getId());
    }

    @Override
    public void remove(Integer id) {
        removeNode(mapNode.get(id));
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (mapNode.size() == 0) {
            linkFirst(task);
        } else {
            removeNode(mapNode.get(task.getId()));
            linkLast(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}