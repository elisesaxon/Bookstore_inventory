class Queue<E> {

   LinkedList<E> data;
   
   Queue() {
      data = new LinkedList<E>();
   }
   
   void add(E value) {
      data.add(value);
   }
   
   E remove() {
      return data.remove();
   }
   
   E peek() {
      return data.get(0);
   }
   
   int size() {
      return data.size();
   }
   
   boolean isEmpty() {
      return data.isEmpty();
   }
   
}