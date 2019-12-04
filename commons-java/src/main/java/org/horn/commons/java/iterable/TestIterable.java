package org.horn.commons.java.iterable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class TestIterable {
	public static void main(String[] args) {
		ScanAppleStore appleTree = new ScanAppleStore();

		System.out.println("Try normal iterator:");
		for (String str : appleTree) {
			System.out.println(str);

		}
		System.out.println("Try reverse iterator:");
		for (String str : appleTree.reverseIterator()) {
			System.out.println(str);
		}
	}
}

class ScanAppleStore implements Iterable<String> {
	ArrayList<String> appleStore = new ArrayList<String>();

	ScanAppleStore() {
		Collections.addAll(appleStore, "Sweet", "Sour", "Bitter", "litter Sweet", "litter Sour", "litter Bitter");
		System.out.println(appleStore);
	}

	public Iterator<String> iterator() {
		return new Iterator<String>() {
			private int i = 0;

			public boolean hasNext() {
				if (i < appleStore.size()) {
					return true;
				} else {
					return false;
				}
			}

			public String next() {

				return appleStore.get(i++);
			}

			public void remove() {
				System.out.println("not defined!");
			}
		};
	}

	public Iterable<String> reverseIterator() {
		return new Iterable<String>() {
			public Iterator<String> iterator() {
				return new Iterator<String>() {
					private int i = appleStore.size() - 1;

					public boolean hasNext() {
						if (i > -1) {
							return true;
						} else {
							return false;
						}
					}

					public String next() {

						return appleStore.get(i--);
					}

					public void remove() {
						System.out.println("not defined!");
					}
				};
			}
		};

	}
}