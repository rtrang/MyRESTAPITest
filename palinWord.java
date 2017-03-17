import java.util.*;
public class palinWord
{
	public static void main(String[] args)
	{
		ArrayList<Character> list = new ArrayList<Character>();

		Scanner sc = new Scanner(System.in);
		System.out.println("Enter a word");
		String word = sc.nextLine();
	
		char[] a=word.toCharArray();  // convert string to char array
	
		for (int i=a.length-1; i>=0; i--)
		{
		list.add(a[i]);
		}

		for (int i=a.length-1; i>=0; i--)
		{			
			if(a[i]!=list.get(i)) 
			{			
			System.out.println("The word is not a palindrome");
			break;					
			}
			if(i==0 && a[i] == list.get(i))
			{
			System.out.println("The word is palindrome");
			}					
		}
		
		
		
	}//end main		
}