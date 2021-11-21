
import java.util.ArrayList;
import java.util.Collections;

public class TestReverseArrayList {

    public static void main(String[] args) {
        ArrayList aList = new ArrayList();
//Add elements to ArrayList object
        aList.add("1");
        aList.add("2");
        aList.add("3");
        aList.add("4");
        aList.add("5");
        Collections.reverse(aList);
        System.out.println("After Reverse Order, ArrayList Contains : " + aList);
    }
}
