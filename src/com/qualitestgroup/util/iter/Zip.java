package com.qualitestgroup.util.iter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.qualitestgroup.util.iter.arrays.ArrayIterableFactory;

/**
 * 
 * @author Brian Van Stone
 *
 * @param <T>
 *            The type of data contained in the collections, iterables, or
 *            iterators to zip
 */
public class Zip<T> implements Iterable<Tuple<T>> {
	private List<Iterator<T>> iterators;

	/**
	 * unit test
	 * 
	 * @param args
	 *            ignored
	 */
	public static void main(String[] args) {
		char[] x = { 'a', 'b', 'c' };
		char[] y = { '1', '2', '3' };
		char[] z = { '!', '@', '#' };
		Iterable<Character> xi = ArrayIterableFactory.create(x);
		Iterable<Character> yi = ArrayIterableFactory.create(y);
		Iterable<Character> zi = ArrayIterableFactory.create(z);

		for (Tuple<Character> t : new Zip<>(xi, yi, zi)) {
			System.out.print(t.get(0));
			System.out.print(t.get(1));
			System.out.println(t.get(2));
		}
	}

	private Zip(Object[] arr) {
		if (arr == null || arr.length < 1) {
			throw new IllegalArgumentException("Must supply at least one argument to the Zip constructor.");
		}

		this.iterators = new ArrayList<Iterator<T>>(arr.length);
	}

	@SafeVarargs
	public Zip(Iterator<T>... iterators) {
		this((Object[]) iterators);

		for (Iterator<T> i : iterators) {
			this.iterators.add(i);
		}
	}

	@SafeVarargs
	public Zip(Iterable<T>... iterables) {
		this((Object[]) iterables);

		for (Iterable<T> i : iterables) {
			this.iterators.add(i.iterator());
		}
	}

	@SafeVarargs
	public Zip(Collection<T>... collections) {
		this((Object[]) collections);

		for (Collection<T> i : collections) {
			this.iterators.add(i.iterator());
		}
	}

	@SafeVarargs
	public Zip(List<T>... lists) {
		this((Object[]) lists);

		for (List<T> i : lists) {
			this.iterators.add(i.iterator());
		}
	}

	@Override
	public Iterator<Tuple<T>> iterator() {
		return new Ziperator<T>(iterators);
	}

	private class Ziperator<ZT> implements Iterator<Tuple<ZT>> {

		private List<Iterator<ZT>> iterators;

		public Ziperator(List<Iterator<ZT>> iterators) {
			this.iterators = iterators;
		}

		@Override
		public boolean hasNext() {
			boolean hasNext = true;
			for (Iterator<ZT> i : iterators) {
				hasNext = hasNext && i.hasNext();
			}
			return hasNext;
		}

		@Override
		public Tuple<ZT> next() {
			List<ZT> temp = new ArrayList<ZT>();
			for (Iterator<ZT> i : iterators) {
				temp.add(i.next());
			}
			return new Tuple<ZT>(temp);
		}

		@Override
		@Deprecated
		public void remove() {
			throw new UnsupportedOperationException("This zip implementation does not support remove operations.");
		}
	}
}
