package com.mapisso.test;

import com.mapisso.roof.CircularDoublyLinkedList;

public class CircularDoublyLinkedListTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		CircularDoublyLinkedList CDLL = new CircularDoublyLinkedList();
		CDLL.addNodeToEnd(2);
		CDLL.addNodeToEnd(7);
		CDLL.addNodeToEnd(9);
		CDLL.addNodeToEnd(12);
	CDLL.print();
	System.out.println(CDLL.elementAt(3));
	}

}
