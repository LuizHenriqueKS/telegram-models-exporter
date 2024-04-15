package br.zul.telegrammodelsexporter.util;

import java.util.Iterator;

public class IteratorUtils {
    
    public static <T> Iterable<T> toIterable(Iterator<T> iterator) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return iterator;
            }
        };
    }

}
