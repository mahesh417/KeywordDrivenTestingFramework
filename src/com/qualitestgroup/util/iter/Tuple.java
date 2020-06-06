package com.qualitestgroup.util.iter;

import java.util.ArrayList;
import java.util.Collection;

public class Tuple<T> extends ArrayList<T> {
	private static final long serialVersionUID = 4557289428783467502L;
	private Integer hash;
	private int index = -1;
	
	public Tuple(Collection <? extends T> c) {
		super(c);
		hash = null;
	}
	
	public Tuple(Collection <? extends T> c, int index) {
		super(c);
		this.index = index;
		hash = null;
	}
	
	public int index() {
		return index;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Tuple)) {
			return false;
		}
		if(other == this) {
			return true;
		}
		
		Tuple<?> t = (Tuple<?>)other;
		if(this.size() == t.size()) {
			boolean eq = true;
			for(int i = 0; i < this.size(); i++) {
				eq = eq && this.get(i).equals(t.get(i));
			}
			return eq;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		sb.append("(");
		for(T item : this) {
			if(!first) {
				sb.append(", ");
			}
			first = false;
			sb.append(item);
		}
		sb.append(")");
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		if(hash == null) {
			int temp = 0;
			for(T item : this) {
				temp += item.hashCode();
			}
			hash = new Integer(temp);
		}
		return hash;
	}
	
	/*UNSUPPORTED OPERATIONS*/
	/**
	 * Unsupported operation.
	 */
	@Override
	@Deprecated
	public final boolean add(T element) {
		throw new UnsupportedOperationException("The immutable type Tuple does not support add operations.");
	}
	/**
	 * Unsupported operation.
	 */
	@Override
	@Deprecated
	public final void add(int index, T element) {
		throw new UnsupportedOperationException("The immutable type Tuple does not support add operations.");
	}
	/**
	 * Unsupported operation.
	 */
	@Override
	@Deprecated
	public final boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException("The immutable type Tuple does not support add operations.");
	}
	/**
	 * Unsupported operation.
	 */
	@Override
	@Deprecated
	public final boolean addAll(int index, Collection<? extends T> c) {
		throw new UnsupportedOperationException("The immutable type Tuple does not support add operations.");
	}
	/**
	 * Unsupported operation.
	 */
	@Override
	@Deprecated
	public final void clear() {
		throw new UnsupportedOperationException("The immutable type Tuple does not support clear");
	}
	/**
	 * Unsupported operation.
	 */
	@Override
	@Deprecated
	public final void ensureCapacity(int minCapacity) {
		throw new UnsupportedOperationException("The immutable type Tuple does not support redimensioning");
	}
	/**
	 * Unsupported operation.
	 */
	@Override
	@Deprecated
	public final T remove(int index) {
		throw new UnsupportedOperationException("The immutable type Tuple does not support remove operations");
	}
	/**
	 * Unsupported operation.
	 */
	@Override
	@Deprecated
	public final boolean remove(Object o) {
		throw new UnsupportedOperationException("The immutable type Tuple does not support remove operations");
	}
	/**
	 * Unsupported operation.
	 */
	@Override
	@Deprecated
	public final boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("The immutable type Tuple does not support remove operations");
	}
	/**
	 * Unsupported operation.
	 */
	@Override
	@Deprecated
	protected final void removeRange(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException("The immutable type Tuple does not support remove operations");
	}
	/**
	 * Unsupported operation.
	 */
	@Override
	@Deprecated
	public final boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("The immutable type Tuple does not support remove operations");
	}
	/**
	 * Unsupported operation.
	 */
	@Override
	@Deprecated
	public final T set(int index, T element) {
		throw new UnsupportedOperationException("The immutable type Tuple does not support modify operations");
	}
	/**
	 * Unsupported operation.
	 */
	@Override
	@Deprecated
	public final void trimToSize() {
		throw new UnsupportedOperationException("The immutable type Tuple does not support redimensioning");
	}
} 
