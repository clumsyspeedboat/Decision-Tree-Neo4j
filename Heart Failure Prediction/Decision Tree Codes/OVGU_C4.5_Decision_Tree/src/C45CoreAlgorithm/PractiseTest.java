package C45CoreAlgorithm;

import static org.junit.Assert.*;

import org.junit.Test;

public class PractiseTest {

	@Test
	public void test() {
		Practise p = new Practise();
		int answer = p.multiply(2, 3);
		assertEquals(6, answer);
	}

}
