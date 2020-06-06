package com.qualitestgroup.util.iter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Chain<T> implements Iterable<T> {
	private ArrayList<Iterator<T>> iterators;
	
	/**
	 * unit test
	 * @param args ignored
	 */
	public static void main(String[] args) {
		List<Integer> x = new ArrayList<Integer>();
		x.add(1);
		x.add(2);
		x.add(3);
		
		List<Integer> y = new ArrayList<Integer>();
		System.out.println(y.iterator().hasNext());
		
		List<Integer> z = new ArrayList<Integer>();
		z.add(7);
		z.add(8);
		z.add(9);
		z.add(10);
		
		for(Integer i : new Chain<>(x,y,z)) {
			System.out.println(i);
		}
	}
	
	private Chain(Object[] arr) {
		if(arr == null || arr.length < 1) {
			throw new IllegalArgumentException("Must supply at least one argument to the Chain constructor.");
		}
		
		this.iterators = new ArrayList<Iterator<T>>(0);
	}
	
	@SafeVarargs
	public Chain(Iterator<T>... iterators) {
		this((Object[])iterators);
		
		for(Iterator<T> i : iterators) {
			if(i.hasNext()) {
				this.iterators.add(i);
			}
		}
	}
	
	@SafeVarargs
	public Chain(Iterable<T>... iterables) {
		this((Object[])iterables);
		
		for(Iterable<T> i : iterables) {
			Iterator<T> iter = i.iterator();
			if(iter.hasNext()) {
				this.iterators.add(iter);
			}
		}
	}
	
	@SafeVarargs
	public Chain(Collection<T>... collections) {
		this((Object[])collections);
		
		for(Collection<T> i : collections) {
			Iterator<T> iter = i.iterator();
			if(iter.hasNext()) {
				this.iterators.add(iter);
			}
		}
	}
	
	@SafeVarargs
	public Chain(List<T>... lists) {
		this((Object[])lists);
		
		for(List<T> i : lists) {
			Iterator<T> iter = i.iterator();
			if(iter.hasNext()) {
				this.iterators.add(iter);
			}
		}
	}

	@Override
	public Iterator<T> iterator() {
		return new ChainIterator<T>(this.iterators);
	}
	
	private class ChainIterator<CT> implements Iterator<CT> {
		private int position;
		private ArrayList<Iterator<CT>> iterators;
		
		public ChainIterator(ArrayList<Iterator<CT>> iterators) {
			this.iterators = iterators;
			position = 0;
		}

		@Override
		public boolean hasNext() {
			return (iterators.size() > 0 && (position < iterators.size() - 1 || iterators.get(position).hasNext()));
		}

		@Override
		public CT next() {
			Iterator<CT> iter = iterators.get(position);
			if(iter.hasNext()) {
				return iter.next();
			} else {
				position++;
				iter = iterators.get(position);
				return iter.next();
			}
		}

		@Override
		public void remove() {
			Iterator<CT> iter = iterators.get(position);
			iter.remove();
		}
	}

}
