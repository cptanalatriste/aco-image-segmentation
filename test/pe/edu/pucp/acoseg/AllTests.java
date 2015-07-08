package pe.edu.pucp.acoseg;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import pe.edu.pucp.acoseg.ant.AntColonyTest;
import pe.edu.pucp.acoseg.ant.AntTest;

@RunWith(Suite.class)
@SuiteClasses({ ACOImageSegmentationTest.class, AntColonyTest.class,
		AntTest.class })
public class AllTests {

}
