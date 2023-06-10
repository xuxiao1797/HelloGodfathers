package com.example.hellogodfather.datastructure;

public class RBTree <Key extends Comparable<Key>, Value> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private Node root;
    private class Node {
        private Key key; // Key of the node, could be set as any object that is comparable
        private Value value; // the value mapped from the key
        private Node left; // the left child
        private Node right; // the right child
        private boolean color; // the color of the node(RED or BLACK)
        private int size; // subtree count
        public Node(Key key, Value value, boolean color, int size) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.size = size;
            this.left = null;
            this.right = null;
        }

        /**
         * Setter of the left child
         * @param node the node to be inserted
         * @return parent node instance
         */
        public Node setLeftChild(Node node) {
            this.left = node;
            return this;
        }

        /**
         * Setter of the right child
         * @param node the node to be inserted
         * @return parent node instance
         */
        private Node setRightChild(Node node) {
            this.right = node;
            return this;
        }

        /**
         * Change the color of this node(BLACK -> RED or RED -> BLACK)
         */
        private void changeColor() {
            this.color = !this.color;
        }
        private void paintRed() {
            this.color = RED;
        }
        private void paintBlack() {
            this.color = BLACK;
        }
        private boolean isRed() {
            return this.color == RED;
        }
        private boolean isBlack() {
            return this.color == BLACK;
        }

    }

    public RBTree() {}

    private boolean isRed(Node node) {
        if (node == null) return false;
        return node.isRed();
    }
    private int size(Node node) {
        if (node == null) return 0;
        return node.size;
    }

    public int size() {
        return size(root);
    }
    public boolean isEmpty() {
        return this.root == null;
    }

    /**
     * Get method of BST tree, if the key provided exists, then return the corresponding value.
     * Otherwise, return null.
     * @param node tree node
     * @param key the search key
     * @return Value if such key is found; null if no such key.
     */
    private Value get(Node node, Key key) {
        while (node != null) {
            if (key.compareTo(node.key) < 0) {
                node = node.left;
            } else if (key.compareTo(node.key) > 0) {
                node = node.right;
            } else {
                return node.value;
            }
        }
        return null;
    }

    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("null key is invalid.");
        if (root == null) throw new NullPointerException("The tree need to be initialized.");
        return get(root, key);
    }

    public void put(Key key, Value value) {
        if (key == null) throw new IllegalArgumentException("null key is invalid.");
        root = put(root, key, value);
        root.paintBlack();
    }

    /**
     * Insert key&value to the tree. If the key already exists in the tree, the old value will be replaced by new value.(Just like HashMap)
     * @param node node
     * @param key insert key
     * @param value insert value
     * @return node instance.
     */
    private Node put(Node node, Key key, Value value) {
        if (node == null) return new Node(key, value, RED, 1); // put the key here.
        if (key.compareTo(node.key) < 0) {
            node.left = put(node.left, key, value);
        } else if (key.compareTo(node.key) > 0) {
            node.right = put(node.right, key, value);
        } else {
            node.value = value;
        }
        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColor(node);
        }
        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    private Node rotateRight(Node node) {
        Node tmp = node.left;
        node.left = tmp.right;
        tmp.right = node;
        tmp.color = node.color;
        node.paintRed();
        tmp.size = node.size;
        node.size = size(node.left) + size(node.right) + 1;
        return tmp;
    }

    private Node rotateLeft(Node node) {
        Node tmp = node.right;
        node.right = tmp.left;
        tmp.left = node;
        tmp.color = node.color;
        node.paintRed();
        tmp.size = node.size;
        node.size = size(node.left) + size(node.right) + 1;
        return tmp;
    }

    /**
     * Flip the color of the node and its children.
     * @param node node
     */
    private void flipColor(Node node) {
        node.changeColor();
        node.left.changeColor();
        node.right.changeColor();
    }
}
