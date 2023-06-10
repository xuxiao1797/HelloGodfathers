package com.example.hellogodfather.datastructure;

/**
 * @author Ruizheng Shen
 * @Date 2021.10.14
 */


public class RBTreeNode<T extends Comparable<T>> {
    private T value; // node value
    private RBTreeNode<T> left; // left child
    private RBTreeNode<T> right; // right child
    private RBTreeNode<T> parent; // parent node
    private boolean color; // true - 'red', false - 'black'
    private static final boolean RED_NODE = true;
    private static final boolean BLACK_NODE = false;

    // COnstructors
    public RBTreeNode() {
        // empty constructor
    }
    public RBTreeNode(T value) {
        this.value = value;
    }
    public RBTreeNode(T value, boolean isRed) {
        this.value = value;
        this.color = isRed;
    }

    // Getters and Setters
    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public RBTreeNode<T> getLeft() {
        return left;
    }

    public void setLeft(RBTreeNode<T> left) {
        this.left = left;
    }

    public RBTreeNode<T> getRight() {
        return right;
    }

    public void setRight(RBTreeNode<T> right) {
        this.right = right;
    }

    public RBTreeNode<T> getParent() {
        return parent;
    }

    public void setParent(RBTreeNode<T> parent) {
        this.parent = parent;
    }

    public boolean isRed() {
        return color == RED_NODE;
    }

    public boolean isBlack() {
        return color == BLACK_NODE;
    }

    public boolean isLeaf() {
        return this.left == null && this.right == null;
    }

    public void makeRed() {
        this.color = RED_NODE;
    }

    public void makeBlack() {
        this.color = BLACK_NODE;
    }
}
