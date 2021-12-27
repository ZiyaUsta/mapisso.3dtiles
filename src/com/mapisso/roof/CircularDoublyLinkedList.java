package com.mapisso.roof;

public class CircularDoublyLinkedList {
	
	public Node head = null;
	public Node tail = null;
	int size = 0;

	public void addNodeToEnd(int vertex) {
		Node n = new Node (vertex);
		if(size == 0){
            head = n;
            tail = n;
            n.next = head;
            n.prev = tail;
            
        } else {
 
            tail.next =n;
            n.prev = tail;
            tail=n;
            tail.next = head;
            
        }
        size++;
    }

	
	public void print(){
        System.out.print("Circular Linked List:");
        Node temp = head;
        if(size<=0){
            System.out.print("List is empty");
        }else{
            do {
                System.out.print(" " + temp.vertex);
                temp = temp.next;
            }
            while(temp!=head);
        }
        System.out.println();
	}
	
	//fonk tipi vertex olacak şimdilik test için int yaptım !!!
	//index 1 den başlıyor sıfırdan değil ilk eleman elementAt(1)
	
	public int elementAt(int index){
        if(index>size){
            return -1;
        }
        Node n = head;
        while(index-1!=0){
            n=n.next;
            index--;
        }
        return n.vertex;
    }

	   public int getSize(){
	        return size;
	    }
	
	
	public class Node{
	
		public Node next;
		Node prev;
		// test için vertex tipini int yaptım Vertex olacak!!!
		public int vertex;
	
	public Node(int vertex) {
		this.vertex = vertex;
	}
	
	}
	
	
}
