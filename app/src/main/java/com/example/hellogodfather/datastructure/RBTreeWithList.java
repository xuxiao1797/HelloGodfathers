package com.example.hellogodfather.datastructure;

import java.util.ArrayList;

public class RBTreeWithList<Key extends Comparable<Key>> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private Node root;
    private class Node {
        private Key key; // Key of the node, could be set as any object that is comparable
        private ArrayList<Object> value; // the value mapped from the key
        private Node left; // the left child
        private Node right; // the right child
        private boolean color; // the color of the node(RED or BLACK)
        private int size; // subtree count
        public Node(Key key, ArrayList<Object> value, boolean color, int size) {
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

    public RBTreeWithList() {}

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
    private ArrayList<Object> get(Node node, Key key) {
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

    public ArrayList<Object> get(Key key) {
        if (key == null) throw new IllegalArgumentException("null key is invalid.");
        if (root == null) throw new NullPointerException("The tree need to be initialized.");
        return get(root, key);
    }

    public void put(Key key, ArrayList<Object> value) {
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
    private Node put(Node node, Key key, ArrayList<Object> value) {
        if (node == null) return new Node(key, value, RED, 1); // put the key here.
        if (key.compareTo(node.key) < 0) {
            node.left = put(node.left, key, value);
        } else if (key.compareTo(node.key) > 0) {
            node.right = put(node.right, key, value);
        } else {
            node.value.addAll(value);
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

    private Node balance(Node node) {
        if (isRed(node.right) && !isRed(node.left)) node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right)) flipColor(node);
        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }

    private boolean contain(Key key) {
        return get(key) != null;
    }
    public void deleteMin() {
        if (isEmpty()) throw new NullPointerException("The tree is empty!");
        if (!isRed(root.left) && !isRed(root.right)) root.paintRed();
        root = deleteMin(root);
        if (isEmpty()) root.paintBlack();
    }
    private Node deleteMin(Node node) {
        if (node.left == null) return null;
        if (!isRed(node.left) && !isRed(node.left.left)) {
            node = moveRedLeft(node);
        }
        node.left = deleteMin(node.left);
        return balance(node);
    }

    public void deleteMax() {
        if (isEmpty()) throw new NullPointerException("The tree is empty.");
        if (!isRed(root.left) && !isRed(root.right)) root.paintRed();
        root = deleteMax(root);
        if (isEmpty()) root.paintBlack();
    }
    private Node deleteMax(Node node) {
        if (isRed(node.left)) node = rotateRight(node);
        if (node.right == null) return null;
        if (!isRed(node.right) && !isRed(node.right.left)) {
            node = moveRedRight(node);
        }
        node.right = deleteMax(node.right);
        return balance(node);
    }

    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("Null key is invalid");
        if (!contain(key)) return;
        // If both children of root is black, set root to red.
        if (!isRed(root.left) && !isRed(root.right)) root.paintRed();
        root = delete(root, key);
        if (!isEmpty()) root.paintBlack(); // if the returned tree is not empty, paint the root to black.
    }
    private Node delete(Node node, Key key) {
        if (key.compareTo(node.key) < 0) {
            if (!isRed(node.left) && !isRed(node.left.left)) {
                node = moveRedLeft(node);
            }
            node.left = delete(node.left, key);
        } else {
            if (isRed(node.left)) {
                node = rotateRight(node);
            }
            if (key.compareTo(node.key) == 0 && node.right == null) {
                return null;
            }
            if (!isRed(node.right) && !isRed(node.right.left)) {
                node = moveRedRight(node);
            }
            if (key.compareTo(node.key) == 0) {
                Node minAtRight = min(node.right);
                node.key = minAtRight.key;
                node.value = minAtRight.value;
                node.right = deleteMin(node.right);

            } else {
                // key.compareTo(node.key) > 0
                node.right = delete(node.right, key);
            }
        }
        return balance(node);
    }

    /**
     * Find the smallest node(smallest key)
     * @param node node
     * @return the smallest node's instance
     */
    private Node min(Node node) {
        if (node.left == null) return node;
        else return min(node.left);
    }
    private Node moveRedLeft(Node node) {
        flipColor(node);
        if (isRed(node.right.left)) {
            node.right = rotateRight(node.right);
            node = rotateLeft(node);
            flipColor(node);
        }
        return node;
    }
    private Node moveRedRight(Node node) {
        flipColor(node);
        if (isRed(node.left.left)) {
            node = rotateRight(node);
            flipColor(node);
        }
        return node;
    }
}
