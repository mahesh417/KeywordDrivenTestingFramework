package com.qualitestgroup.util.iter.arrays;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayIterable<T> implements Iterable<T> {
	private T[] arr;

	ArrayIterable(T[] arr) {
		this.arr = arr;
	}
	
	@Override
	public Iterator<T> iterator() {
		return new ArrayIterator<T>(arr);
	}
	
	private class ArrayIterator<U> implements Iterator<U> {
		private U[] arr;
		private int position;
		
		ArrayIterator(U[] arr) {
			this.arr = arr;
		}

		@Override
		public boolean hasNext() {
			return position < arr.length;
		}

		@Override
		public U next() {
			try {
				return arr[position++];
			} catch(IndexOutOfBoundsException e) {
				NoSuchElementException nsee = new NoSuchElementException("The underlying array of an ArrayIterator has been exhausted");
				nsee.setStackTrace(e.getStackTrace());
				throw nsee;
			}
		}

		@Override
		@Deprecated
		public void remove() {
			throw new UnsupportedOperationException("The remove operation is not supported for ArrayIterator");
		}
	}
}
