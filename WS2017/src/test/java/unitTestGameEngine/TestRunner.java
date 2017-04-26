package unitTestGameEngine;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Created by m on 26/04/17.
 */
public class TestRunner {
    public static void main(String[] args){
        //Result result = JUnitCore.runClasses(TestCharacter.class);

        //for (Failure failure : result.getFailures()){
        //    System.out.println(failure.toString());
        //}
        //System.out.println(result.wasSuccessful());

        Result result = JUnitCore.runClasses(TestTurnController.class);

        for (Failure failure : result.getFailures()){
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }
}
