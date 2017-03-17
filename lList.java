import java.util.*;

// reserse words using linked list
public class lList{
	public static void main(String[] args){
	String[] a = {"this","is","test"};
	LinkedList<String> list = new LinkedList<String>();
	for(int i=0; i<=a.length-1; i++){
	list.add(a[i]);
	}
	for(int i=a.length-1; i>=0; i--){
	System.out.println(list.get(i));
	}	
	}
}