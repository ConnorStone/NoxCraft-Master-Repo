/*
 * Copyright (c) 2014. NoxPVP.com
 *
 * All rights are reserved.
 *
 * You are not permitted to
 * 	Modify
 * 	Redistribute nor distribute
 * 	Sublicense
 *
 * You are required to keep this license header intact
 *
 * You are allowed to use this for non commercial purpose only. This does not allow any ad.fly type links.
 *
 * When using this you are required to
 * 	Display a visible link to noxpvp.com
 * 	For crediting purpose.
 *
 * For more information please refer to the license.md file in the root directory of repo.
 *
 * To use this software with any different license terms you must get prior explicit written permission from the copyright holders.
 */

package com.noxpvp.core.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

public class Cycler<E> implements ListIterator<E> {
	private ArrayList<E> data;

	private int index;

	public Cycler(Collection<E> data) {
		this.data = new ArrayList<E>(data);
		index = 0;
	}

	public Cycler(int size) {
		data = new ArrayList<E>(size);
		index = 0;
	}

	public void add(E e) {
		data.add(currentIndex(), e);
	}

	/**
	 * Retrieves the current object on the current index / iteration.
	 *
	 * @return the current object.
	 */
	public E current() {
		return data.isEmpty() ? null : data.get(currentIndex());
	}

	/**
	 * Retrieves the current index.
	 *
	 * @return 0 - (Cycler Size)
	 */
	public int currentIndex() {
		return index;
	}

	public List<E> getList() {
		return data;
	}

	/*
	 * Warning this method will cause it to loop infinite on iteration.
	 * 
	 * YOU MUST BREAK THE LOOP YOURSELF!
	 * 
	 * (non-Javadoc)
	 * @see java.util.ListIterator#hasNext()
	 */
	public boolean hasNext() {
		return !data.isEmpty();
	}

	/*
	 * Warning this method will cause it to loop infinitely on iteration.
	 * 
	 * YOU MUST BREAK THE LOOP YOURSELF!
	 * 
	 * (non-Javadoc)
	 * @see java.util.ListIterator#hasPrevious()
	 */
	public boolean hasPrevious() {
		return !data.isEmpty();
	}

	public E next() {
		if (data.isEmpty()) return null;
		index = nextIndex();
		return data.get(currentIndex());
	}

	public int nextIndex() {
		return currentIndex() >= (data.size() - 1) ? 0 : (currentIndex() + 1);
	}

	public E peekNext() {
		return data.get(nextIndex());
	}

	public E peekPrevious() {
		return data.get(previousIndex());
	}

	public E previous() {
		if (data.isEmpty()) return null;
		index = previousIndex();
		return data.get(currentIndex());
	}

	public int previousIndex() {
		return currentIndex() <= 0 ? (data.size() - 1) : (currentIndex() - 1);
	}

	public void remove() {
		data.remove(currentIndex());
		if (data.size() <= currentIndex())
			index = data.size() - 1;
	}

	public void set(E e) {
		data.set(currentIndex(), e);
	}

	public void addAll(Collection<E> items) {
		data.addAll(items);
	}
}
