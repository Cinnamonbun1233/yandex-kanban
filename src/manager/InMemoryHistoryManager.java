package manager;

import task.NormalTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    Map<Integer, Node<NormalTask>> mapNode = new HashMap<>();
    Node<NormalTask> first = null;
    Node<NormalTask> last = null;

    private List<NormalTask> getTasks() {
        ArrayList<NormalTask> historyList = new ArrayList<>();
        Node<NormalTask> lastNode = last;
        Node<NormalTask> firstNode = first;
        if (lastNode == null && firstNode == null) {
            return historyList;
        }
        do {
            historyList.add(firstNode.getCurrent());
            firstNode = firstNode.getNext();
        } while (firstNode != null);
        return historyList;
    }

    private void linkFirst(NormalTask normalTask) {
        Node<NormalTask> nodeFirst = new Node<>(null, normalTask, null);
        mapNode.put(normalTask.getId(), nodeFirst);
        first = nodeFirst;
        last = nodeFirst;
    }

    private void linkLast(NormalTask normalTask) {
        Node<NormalTask> nodeLast = new Node<>(last, normalTask, null);
        last.setNext(nodeLast);
        last = nodeLast;
        mapNode.put(normalTask.getId(), nodeLast);
    }

    private void removeNode(Node<NormalTask> node) {
        if (node == null) {
            return;
        }
        if (node.getPrevious() == null && node.getNext() == null) {
            first = null;
            last = null;
        } else if (node.getPrevious() == null) {
            Node<NormalTask> newFirstNode = node.getNext();
            newFirstNode.setPrevious(null);
            first = newFirstNode;
        } else if (node.getNext() == null) {
            Node<NormalTask> newLastNode = node.getPrevious();
            newLastNode.setNext(null);
            last = newLastNode;
        } else if (node.getPrevious() != null && node.getNext() != null) {
            Node<NormalTask> prevNode = node.getPrevious();
            Node<NormalTask> nextNode = node.getNext();
            prevNode.setNext(nextNode);
            nextNode.setPrevious(prevNode);
        }
        mapNode.remove(node.getCurrent().getId());
    }

    @Override
    public void remove(Integer taskId) {
        removeNode(mapNode.get(taskId));
    }

    @Override
    public void add(NormalTask normalTask) {
        if (mapNode.size() == 0) {
            linkFirst(normalTask);
        } else {
            removeNode(mapNode.get(normalTask.getId()));
            linkLast(normalTask);
        }
    }

    @Override
    public List<NormalTask> getHistory() {
        return getTasks();
    }
}