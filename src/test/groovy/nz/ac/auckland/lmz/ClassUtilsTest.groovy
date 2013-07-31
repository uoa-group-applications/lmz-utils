package nz.ac.auckland.lmz

import org.junit.Test

/**
 * Unit tests for {@link ClassUtils}.
 * <p>Author: <a href="http://gplus.to/tzrlk">Peter Cummuskey</a></p>
 */
public class ClassUtilsTest {

    @Test
    public void listAncestryGeneratesExpectedContentAndOrdering() throws Exception {
        assert ClassUtils.listAncestry(this.class) == [ ClassUtilsTest, Object ];
    }

}
