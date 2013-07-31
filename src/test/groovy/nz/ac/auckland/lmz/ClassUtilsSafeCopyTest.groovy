package nz.ac.auckland.lmz

import org.junit.Before
import org.junit.Test;

/** @see ClassUtils#safeCopy */
public class ClassUtilsSafeCopyTest {

    TestClass testClass;
    Map data = [
            propertyA: 'value4',
            propertyB: 'value5',
            propertyD: 'value6'
    ];

    @Before
    public void setUp() throws Exception {
        testClass = new TestClass(
                propertyA: 'value1',
                propertyB: 'value2',
                propertyC: 'value3'
        );
    }

    @Test
    public void copiesOnlySelectedProperties() throws Exception {
        ClassUtils
                .safeCopy(testClass, data, 'propertyA', 'propertyC')
                .validate('value4', 'value2', 'value3');
    }

    @Test
    public void copiesAllValidPropertiesIfNotFiltering() throws Exception {
        ClassUtils
                .safeCopy(testClass, data)
                .validate('value4', 'value5', 'value3');
    }

    private class TestClass {
        String propertyA;
        String propertyB;
        String propertyC;

        public void validate(String propertyA, String propertyB, String propertyC) {
            assert this.propertyA == propertyA;
            assert this.propertyB == propertyB;
            assert this.propertyC == propertyC;
        }
    }
}
