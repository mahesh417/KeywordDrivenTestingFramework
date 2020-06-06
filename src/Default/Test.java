package Default;

public class Test {

	public static void main(String[] args) {
		String s="$25.256445";
		String orderamount2=s.replace("$", "");
		System.out.println("orderamount"+" "+ orderamount2);

    }

 

/*    public static void check(String s) throws InterruptedException {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {

 

            char ch = s.charAt(i);
            count++;
            System.out.println(ch);

 

        }
        System.out.println(count);

 

    }
*/
}
