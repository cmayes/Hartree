package org.cmayes.hartree.loader.gaussian;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.FileReader;

import org.cmayes.hartree.model.CalculationSnapshot;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests load cases using different files.
 * 
 * @author cmayes
 * 
 */
public class TestSummaryLoader {
    /** The prefix for file locations. */
    private static final String FILE_DIR_PFX = "src/test/resources/files/g09/snapshot/";
    private static final SnapshotLoader LOADER = new SnapshotLoader();
    private static CalculationSnapshot calc1;

    /**
     * Load test files.
     * 
     * @throws Exception
     *             When there are problems.
     */
    @BeforeClass
    public static final void setUpClass() throws Exception {
        calc1 = LOADER.load(new FileReader(FILE_DIR_PFX
                + "glucNa3eO4areacttwater.out"));
    }

    /**
     * Test.
     * 
     * @throws Exception
     *             When there's a problem.
     */
    @Test
    public void testMult() throws Exception {
        assertThat(calc1.getMult(), equalTo(1));
    }

    /**
     * Test.
     * 
     * @throws Exception
     *             When there's a problem.
     */
    @Test
    public void testElecEn() throws Exception {
        assertThat(calc1.getElecEn(), closeTo(-849.236562278, .01));
    }

    /**
     * Test.
     * 
     * @throws Exception
     *             When there's a problem.
     */
    @Test
    public void testFreqVals() throws Exception {
        assertThat(calc1.getFrequencyValues().get(0), closeTo(60.7784, .01));
        assertThat(calc1.getFrequencyValues().get(1), closeTo(90.3398, .01));
    }

    /**
     * Test.
     * 
     * @throws Exception
     *             When there's a problem.
     */
    @Test
    public void testFunctional() throws Exception {
        assertThat(calc1.getFunctional(), equalTo("m062x"));
    }

    /**
     * Test.
     * 
     * @throws Exception
     *             When there's a problem.
     */
    @Test
    public void testBasisSet() throws Exception {
        assertThat(calc1.getBasisSet(), equalTo("6-31+g(2df,p)"));
    }

}
