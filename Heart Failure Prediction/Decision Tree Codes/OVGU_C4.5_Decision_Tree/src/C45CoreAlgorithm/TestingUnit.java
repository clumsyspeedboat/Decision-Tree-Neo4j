package C45CoreAlgorithm;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestingUnit {

	@Test
	public void test() {
		JUnitTesting j = new JUnitTesting();
		int result = j.square(5);
		assertEquals(25, result);
	}

}
