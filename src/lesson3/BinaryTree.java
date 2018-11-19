package lesson3;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// Attention: comparable supported but comparator is not
@SuppressWarnings("WeakerAccess")
public class BinaryTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    private static class Node<T> {
        final T value;

        Node<T> left = null;

        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> root = null;

    private int size = 0;

    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        }
        else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        }
        else {
            assert closest.right == null;
            closest.right = newNode;
        }
        size++;
        return true;
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     */
    @Override
    public boolean remove(Object o) {
        T val = (T) o;
        if (isEmpty() || find(val) == null) {
            return false;
        }
        root = remove(root, val);
        size--;
        return true;
    }

    private Node<T> remove(Node<T> cur, T val)
    {
        Node<T> p, p2, n;
        if (cur.value.compareTo(val) == 0)
        {
            Node<T> lt, rt;
            lt = cur.left;
            rt = cur.right;
            if (lt == null && rt == null)
                return null;
            else if (lt == null)
            {
                p = rt;
                return p;
            }
            else if (rt == null)
            {
                p = lt;
                return p;
            }
            else
            {
                p2 = rt;
                p = rt;
                while (p.left != null)
                    p = p.left;
                p.left  = lt;
                return p2;
            }
        }
        if (val.compareTo(cur.value) < 0)
        {
            n = remove(cur.left, val);
            cur.left = n;
        }
        else
        {
            n = remove(cur.right, val);
            cur.right = n;
        }
        return cur;
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        }
        else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        }
        else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    public class BinaryTreeIterator implements Iterator<T> {

        Stack<Node<T>> stack;
        Node<T> current;

        private BinaryTreeIterator() {
            Node<T> cur = BinaryTree.this.root;
            stack = new Stack<>();
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
        }

        /**
         * Поиск следующего элемента
         * Средняя
         */
        private Node<T> findNext() {
            current = stack.pop();
            Node<T> node = current;
            if (node.right != null) {
                node = node.right;
                while (node != null) {
                    stack.push(node);
                    node = node.left;
                }
            }
            return current;
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public T next() {
            Node<T> current = findNext();
            if (current == null)
                throw new NoSuchElementException();
            return current.value;
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        @Override
        public void remove() {
            BinaryTree.this.remove(current.value);
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinaryTreeIterator();
    }

    @Override
    public int size() {
        return size;
    }


    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    /**
     * Для этой задачи нет тестов (есть только заготовка subSetTest), но её тоже можно решить и их написать
     * Очень сложная
     */
    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        BinaryTree<T> result = new BinaryTree<>();
        headSetBuilder(root, result, toElement);
        return result;
    }

    private void headSetBuilder(Node<T> cur, BinaryTree<T> tree, T element) {
        int compResult = cur.value.compareTo(element);
        if (compResult <= 0) {
            if (cur.left != null) {
                copyTree(cur.left, tree);
            }
            if (compResult != 0) {
                tree.add(cur.value);
                if (cur.right != null) {
                    headSetBuilder(cur.right, tree, element);
                }
            }
        } else {
            if (cur.left != null) {
                headSetBuilder(cur.left, tree, element);
            }
        }
    }

    private void copyTree(Node<T> node, BinaryTree<T> tree) {
        tree.add(node.value);
        if (node.left != null) {
            copyTree(node.left, tree);
        }
        if (node.right != null) {
            copyTree(node.right, tree);
        }
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        BinaryTree<T> result = new BinaryTree<>();
        tailSetBuilder(root, result, fromElement);
        return result;
    }
    private void tailSetBuilder(Node<T> cur, BinaryTree<T> tree, T element) {
        int compResult = cur.value.compareTo(element);
        if (compResult >= 0) {
            tree.add(cur.value);
            if (cur.right != null) {
                copyTree(cur.right, tree);
            }
            if (compResult != 0) {
                if (cur.left != null) {
                    tailSetBuilder(cur.left, tree, element);
                }
            }
        } else {
            if (cur.right != null) {
                tailSetBuilder(cur.right, tree, element);
            }
        }
    }
    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }
}