package org.java.training.streams.impl.collectors;

import lombok.experimental.UtilityClass;
import org.java.training.structures.impl.BinaryTree;
import org.java.training.structures.impl.HashTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collector;

@UtilityClass
public class CustomCollectors {

    public <T, K, V> Collector<T, ?, HashTable<K, V>> toHashTable(Function<? super T, ? extends K> keyMapper,
                                                                  Function<? super T, ? extends V> valueMapper) {
        Objects.requireNonNull(keyMapper);
        Objects.requireNonNull(valueMapper);
        return Collector.of(
                HashTable::new,
                (tab, el) -> tab.add(keyMapper.apply(el), valueMapper.apply(el)),
                (tab1, tab2) -> {
                    tab2.nodes().forEach(node -> tab1.add(node.getKey(), node.getValue()));
                    return tab1;
                }
        );
    }

    @SuppressWarnings("unchecked")
    public <T> Collector<T, ?, T[]> toArray() {
        return Collector.of(
                ArrayList<T>::new,
                List::add,
                (l1, l2) -> {
                    l1.addAll(l2);
                    return l1;
                },
                list -> (T[]) Arrays.copyOf(list.toArray(), list.size())
        );
    }

    public <T extends Comparable<? super T>> Collector<T, ?, BinaryTree<T>> toBinaryTree() {
        return Collector.of(
                BinaryTree::new,
                BinaryTree::insert,
                (tree1, tree2) -> {
                    tree2.traverseAscending(tree1::insert);
                    return tree1;
                }
        );
    }

    public <T, V extends Comparable<? super V>> Collector<T, ?, BinaryTree<V>> toBinaryTree(
            Function<? super T, ? extends V> extractor) {

        Objects.requireNonNull(extractor);
        return Collector.of(
                BinaryTree::new,
                (tree, el) -> tree.insert(extractor.apply(el)),
                (tree1, tree2) -> {
                    tree2.traverseAscending(tree1::insert);
                    return tree1;
                }
        );
    }

    public <T> Collector<T, ?, Integer> toTotalHashCode() {
        return Collector.of(
                () -> 0,
                (currHash, el) -> currHash += el.hashCode(),
                Integer::sum,
                Function.identity()
        );
    }
}