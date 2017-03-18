import java.util.*;

public class addNumberWithLongString
{
	public static void main(String[] args){
	if(args.length<2)// should use try & catch here
	{
		System.out.println("Usage: java addLargeNumber [number] [number]");
	}
	int min, max;
	String[] a1=args[0].split("");
	String[] a2=args[1].split("");
	
	// find the min and max if one of the arrays has more elements than the other
	if(a1.length < a2.length)
	{
		min = a1.length-1;
	}else{
		min = a2.length-1;
	}
	if(a1.length < a2.length)
	{
		max = a2.length-1;
	}else{
		max = a1.length-1;
	}
	
	// intialize new int arrays with all zeros
	int[] n1=new int[max];
	int[] n2=new int[max];

	// rebuild the list with integers including zeros for empty spaces	
	for (int i=1; i<a1.length-1; i++)				
	n1[i]=Integer.parseInt(a1[a1.length-1-i]);
		
	for (int i=1; i<a2.length-1; i++)	
	n2[i]=Integer.parseInt(a2[a2.length-1-i]);	
		
	
	// let's see how n1 and n2 look like
	for (int i=n1.length-1; i>=0; i--)
	{
	System.out.printf("%d",n1[i]);
	}
	System.out.println("");
	for (int i=n2.length-1; i>=0; i--)
	{
	System.out.printf("%d",n2[i]);
	}
	System.out.println("");	

	int c=0; //carry over when adding elements
	int[] sum=new int[max+1];	
	
	// calculate all the elements with carry over
	for(int i=0; i<max; i++){
		sum[i]=(n1[i]+n2[i]+c)%10;
		if((n1[i]+n2[i]+c)>=10)
		c=1;
		else
		c=0;
	}
	sum[max]=c;
	
	for(int i=0; i<max; i++)
	System.out.print("-");
	System.out.println("");
	for(int i=max; i>=0; i--)
	System.out.printf("%d",sum[i]);					
	}//end main

}//end class