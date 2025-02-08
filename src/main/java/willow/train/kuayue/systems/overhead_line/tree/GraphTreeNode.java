package willow.train.kuayue.systems.overhead_line.tree;

import willow.train.kuayue.systems.overhead_line.power_network.PowerNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphTreeNode {
    public PowerNode element;
    public GraphTreeNode prev;
    public final List<GraphTreeNode> next;

    public GraphTreeNode(PowerNode element, GraphTreeNode prev, GraphTreeNode[] next) {
        this.element = element;
        this.prev = prev;
        this.next = new ArrayList<>(next.length);
        this.next.addAll(Arrays.asList(next));
    }

    public GraphTreeNode(PowerNode element, GraphTreeNode prev, List<GraphTreeNode> next) {
        this.element = element;
        this.prev = prev;
        this.next = new ArrayList<>(next.size());
        this.next.addAll(next);
    }

    public GraphTreeNode(PowerNode element, GraphTreeNode[] next) {
        this(element, null, next);
    }

    public GraphTreeNode(PowerNode element, GraphTreeNode prev) {
        this.element = element;
        this.prev = prev;
        this.next = new ArrayList<>();
    }

    public GraphTreeNode(PowerNode element) {
        this.element = element;
        this.prev = null;
        this.next = new ArrayList<>();
    }

    public void setElement(PowerNode element) {
        this.element = element;
    }

    public void setPrev(GraphTreeNode prev) {
        this.prev = prev;
    }

    public PowerNode getElement() {
        return element;
    }

    public GraphTreeNode getPrev() {
        return prev;
    }

    public List<GraphTreeNode> getNext() {
        return next;
    }

    public void addNext(GraphTreeNode next) {
        this.next.add(next);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof GraphTreeNode node)) return false;
        return this.element.equals(node.element);
    }
}
