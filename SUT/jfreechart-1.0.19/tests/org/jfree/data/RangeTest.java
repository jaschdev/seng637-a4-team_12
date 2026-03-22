package org.jfree.data;

import static org.junit.Assert.*; import org.jfree.data.Range; import org.junit.*;

public class RangeTest {
	private Range exampleRange;
    @BeforeClass public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception { exampleRange = new Range(-1, 1);
    }

   	// ASSIGNMENT 2 BLACK BOX TESTING START -------------------------------------------------------------------------------

    // SHIFT() TESTS  ----------------------------------

    // Robustness Test: base = null → expect IllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void testShiftNullBase() {
        Range.shift(null, 1.0, true);
    }

    // Boundary Value Analysis: delta = 0 → range unchanged
    @Test
    public void testShiftZeroDelta() {
        Range shifted = Range.shift(exampleRange, 0.0, true);
        assertEquals(-1.0, shifted.getLowerBound(), 0.000000001d);
        assertEquals(1.0, shifted.getUpperBound(), 0.000000001d);
    }

    // Weak ECP: delta > 0, allow zero crossing → both bounds increase
    @Test
    public void testShiftPositiveDeltaAllow() {
        Range shifted = Range.shift(exampleRange, 2.0, true);
        assertEquals(1.0, shifted.getLowerBound(), 0.000000001d);
        assertEquals(3.0, shifted.getUpperBound(), 0.000000001d);
    }

    // Weak ECP: delta < 0, allow zero crossing → both bounds decrease
    @Test
    public void testShiftNegativeDeltaAllow() {
        Range shifted = Range.shift(exampleRange, -2.0, true);
        assertEquals(-3.0, shifted.getLowerBound(), 0.000000001d);
        assertEquals(-1.0, shifted.getUpperBound(), 0.000000001d);
    }

	// Boundary Valye Analysis: Test shifting with Double.MAX_VALUE, allow zero crossing
    @Test
    public void testShiftMaxValueAllowZeroCross() {
        Range shifted = Range.shift(exampleRange, Double.MAX_VALUE, true);
        assertEquals(Double.MAX_VALUE, shifted.getLowerBound(), 0.000000001d);
        assertEquals(Double.MAX_VALUE, shifted.getUpperBound(), 0.000000001d);
    }

    // Strong ECP: zero crossing prevented when allowZeroCrossing = false
    @Test
    public void testShiftNegativeAllowZeroCross() {
        Range base = new Range(-1.0, 1.0);
        Range shifted = Range.shift(base, -2.0, false);

        // Lower bound should stop at 0 instead of crossing
        assertEquals(-3.0, shifted.getLowerBound(), 0.000000001d);
        assertEquals(0.0, shifted.getUpperBound(), 0.000000001d);
    }

    // toString() TESTS ----------------------------------

    // Weak ECP: normal positive range formatting
    @Test
    public void testToStringNormalRange() {
        Range rangeString1 = new Range(1.0, 5.0);
        assertEquals("Range[1.0,5.0]", rangeString1.toString());
    }

    // Boundary Value Analysis: negative bounds
    @Test
    public void testToStringNegativeRange() {
        Range rangeString2 = new Range(-5.0, -1.0);
        assertEquals("Range[-5.0,-1.0]", rangeString2.toString());
    }

    // Boundary Value Analysis: zero-length range (lower = upper)
    @Test
    public void testToStringZeroLength() {
        Range rangeString3 = new Range(2.0, 2.0);
        assertEquals("Range[2.0,2.0]", rangeString3.toString());
    }

	// Combine() TESTS ----------------------------------

    // Robustness Test: both ranges are null → expect null result
   	@Test
   	public void testCombineBothNull() {
   		Range result = Range.combine(null, null);
   		assertNull(result);
   	}

    // Robustness: first parameter null → return second range
   	@Test
   	public void testCombineFirstNull() {
   		Range r2 = new Range(1.0, 5.0);

   		Range result = Range.combine(null, r2);

   		assertEquals(r2.getLowerBound(), result.getLowerBound(), 0.000000001d);
   		assertEquals(r2.getUpperBound(), result.getUpperBound(), 0.000000001d);
   	}

    // Robustness: second parameter null → return first range
   	@Test
   	public void testCombineSecondNull() {
   		Range r1 = new Range(-3.0, 2.0);

   		Range result = Range.combine(r1, null);

   		assertEquals(r1.getLowerBound(), result.getLowerBound(), 0.000000001d);
   		assertEquals(r1.getUpperBound(), result.getUpperBound(), 0.000000001d);
   	}

    // Strong ECP: partially overlapping ranges
   	@Test
   	public void testCombineOverlapping() {
   		Range r1 = new Range(1.0, 5.0);
   		Range r2 = new Range(3.0, 8.0);

   		try {
   			Range result = Range.combine(r1, r2);

   			assertNotNull("Result should not be null", result);
   			assertEquals(1.0, result.getLowerBound(), 0.000000001d);
   			assertEquals(8.0, result.getUpperBound(), 0.000000001d);

   			// SUT fails here, tries to create new Range(1.0, 3.0), results in an error
   			// because assertions don't execute.
   			// Added assertNotNull() to catch this error, and result in the test failure

   		} catch (IllegalArgumentException e) {
   			fail("combine() should not throw exception for overlapping ranges");
   		}
   	}

    // Strong ECP: disjoint non-overlapping ranges
   	@Test
   	public void testCombineDisjoint() {
   		Range r1 = new Range(1.0, 2.0);
   		Range r2 = new Range(5.0, 7.0);

   		try {

   			Range result = Range.combine(r1, r2);

   			assertNotNull("Result should not be null", result);
   			assertEquals(1.0, result.getLowerBound(), 0.000000001d);
   			assertEquals(7.0, result.getUpperBound(), 0.000000001d);

   			// Same issue here, SUT tries to do new Range(5.0, 1.0). throws error.
   			// Added assertNotNull() to catch this error, and result in the test failure

   		} catch (IllegalArgumentException e) {
   			fail("combine() should not throw exception for overlapping ranges");
   		}
   	}

    // Strong ECP: one range fully contained inside the other
   	@Test
   	public void testCombineContained() {
   		Range r1 = new Range(1.0, 10.0);
   		Range r2 = new Range(3.0, 4.0);

   		try {

   			Range result = Range.combine(r1, r2);

   			assertNotNull("Result should not be null", result);
   			assertEquals(1.0, result.getLowerBound(), 0.000000001d);
   			assertEquals(10.0, result.getUpperBound(), 0.000000001d);

   			// Added assertNotNull() to catch this error, and result in the test failure

   		} catch (IllegalArgumentException e) {
   			fail("combine() should not throw exception for overlapping ranges");
   		}
   	}

    // CONTAINS() TESTS  ----------------------------------

    // Weak ECP: value strictly inside range
   	@Test
   	public void testContainsValueInside() {
   		Range r = new Range(1.0, 5.0);
   		assertTrue(r.contains(3.0));
   	}

    // Boundary Value Analysis: value equals lower boundary
   	@Test
   	public void testContainsLowerBoundary() {
   		Range r = new Range(1.0, 5.0);
   		assertTrue(r.contains(1.0));
   	}

    // Boundary Value Analysis: value equals upper boundary
   	@Test
   	public void testContainsUpperBoundary() {
   		Range r = new Range(1.0, 5.0);
   		assertTrue(r.contains(5.0));
   	}

    // Weak ECP: value below lower bound
   	@Test
   	public void testContainsBelowLower() {
   		Range r = new Range(1.0, 5.0);
   		assertFalse(r.contains(0.5));
   	}

    // Weak ECP: value above upper bound
   	@Test
   	public void testContainsAboveUpper() {
   		Range r = new Range(1.0, 5.0);
   		assertFalse(r.contains(6.0));
   	}

    // GETLENGTH() TESTS  ----------------------------------

    // Weak ECP: positive range
   	@Test
   	public void testGetLengthPositiveRange() {
   		Range r = new Range(2.0, 10.0);
   		assertEquals(8.0, r.getLength(), 0.000000001d);
   	}

    // Boundary Value Analysis: zero-length range (lower == upper)
   	@Test
   	public void testGetLengthZeroLength() {
   		Range r = new Range(3.0, 3.0);
   		assertEquals(0.0, r.getLength(), 0.000000001d);
   	}

    // Weak ECP: both bounds negative
   	@Test
   	public void testGetLengthNegativeBounds() {
   		Range r = new Range(-5.0, -2.0);
   		assertEquals(3.0, r.getLength(), 0.000000001d);
   	}

   	// BLACK BOX TESTING END -------------------------------------------------------------------------------
   	// WHITE BOX TESTING START -------------------------------------------------------------------------------

   	// RANGE() TESTS --------------------------

   	@Test (expected = IllegalArgumentException.class)
   	public void testRangeLowerGreaterThanUpper() {
   		Range r = new Range(-2.0, -5.0);
   	}

   	// GETLOWERBOUND() TEST -----------------------------

   	@Test
   	public void testGetLowerBoundInvalidState() throws Exception {
   	    Range r = new Range(1.0, 5.0);

   	    java.lang.reflect.Field lowerField = Range.class.getDeclaredField("lower");
   	    java.lang.reflect.Field upperField = Range.class.getDeclaredField("upper");
   	    lowerField.setAccessible(true);
   	    upperField.setAccessible(true);
   	    lowerField.set(r, 10.0);
   	    upperField.set(r, 5.0);

   	    assertEquals(10.0, r.getLowerBound(), 0.000000001d);
   	}

   	// GETUPPERBOUND() TEST -----------------------------

   	@Test
   	public void testGetUpperBoundInvalidState() throws Exception {
   	    Range r = new Range(1.0, 5.0);

   	    java.lang.reflect.Field lowerField = Range.class.getDeclaredField("lower");
   	    java.lang.reflect.Field upperField = Range.class.getDeclaredField("upper");
   	    lowerField.setAccessible(true);
   	    upperField.setAccessible(true);
   	    lowerField.set(r, 10.0);
   	    upperField.set(r, 5.0);

   	    assertEquals(5.0, r.getUpperBound(), 0.000000001d);
   	}

   	// GETLENGTH() TEST -----------------------------

   	@Test
   	public void testGetLengthInvalidState() throws Exception {
   	    Range r = new Range(1.0, 5.0);

   	    java.lang.reflect.Field lowerField = Range.class.getDeclaredField("lower");
   	    java.lang.reflect.Field upperField = Range.class.getDeclaredField("upper");
   	    lowerField.setAccessible(true);
   	    upperField.setAccessible(true);
   	    lowerField.set(r, 10.0);
   	    upperField.set(r, 5.0);

   	    assertEquals(-5.0, r.getLength(), 0.000000001d);
   	}

   	// GETCENTRALVALUE() TEST ----------------------------

   	@Test
   	public void testgetCentralValue() {
   		Range r = new Range(-5.0, -2.0);
   		assertEquals(-3.5, r.getCentralValue(), 0.000000001d);
   	}

   	// INTERSECTS(double b0, double b1) TEST ------------------------

   	@Test
   	public void testIntersectsTrueFirstBranch() {
   	    Range r = new Range(2.0, 6.0);
   	    assertTrue(r.intersects(1.0, 3.0));
   	}

   	@Test
   	public void testIntersectsFalseFirstBranch() {
   	    Range r = new Range(2.0, 6.0);
   	    assertFalse(r.intersects(0.0, 2.0));
   	}

   	@Test
   	public void testIntersectsSecondBranchTrue() {
   	    Range r = new Range(2.0, 6.0);
   	    assertTrue(r.intersects(3.0, 5.0));
   	}

   	@Test
   	public void testIntersectsSecondBranchFalse() {
   	    Range r = new Range(2.0, 6.0);
   	    assertFalse(r.intersects(7.0, 8.0));
   	}

   	// INTERSECTS(Range range) TESTS --------------------------------

   	@Test
   	public void testIntersectsRangeObject() {
   	    Range r1 = new Range(1.0, 5.0);
   	    Range r2 = new Range(4.0, 8.0);
   	    assertTrue(r1.intersects(r2));
   	}

   	// CONSTRAIN() TESTS ----------------------------

   	@Test
   	public void testConstrainInsideRange() {
   	    Range r = new Range(1.0, 5.0);
   	    assertEquals(3.0, r.constrain(3.0), 0.000000001d);
   	}

   	@Test
   	public void testConstrainAboveRange() {
   	    Range r = new Range(1.0, 5.0);
   	    assertEquals(5.0, r.constrain(10.0), 0.000000001d);
   	}

   	@Test
   	public void testConstrainBelowRange() {
   	    Range r = new Range(1.0, 5.0);
   	    assertEquals(1.0, r.constrain(-2.0), 0.000000001d);
   	}

   	// COMBINEIGNORINGNAN(Range range1, Range range2) TESTS ----------------------

   	@Test
   	public void testCombineIgnoringNaNRange1IsNullRange2IsNaN() {
   	    Range r2 = new Range(Double.NaN, Double.NaN);
   	    Range result = Range.combineIgnoringNaN(null, r2);
   	    assertNull(result);
   	}

   	@Test
   	public void testCombineIgnoringNaNRange1IsNull() {
   	    Range r2 = new Range(1.0, 5.0);
   	    Range result = Range.combineIgnoringNaN(null, r2);
   	    assertEquals(r2, result);
   	}

   	@Test
   	public void testCombineIgnoringNaNRange2IsNullRange1IsNaN() {
   	    Range r1 = new Range(Double.NaN, Double.NaN);
   	    Range result = Range.combineIgnoringNaN(r1, null);
   	    assertNull(result);
   	}

   	@Test
   	public void testCombineIgnoringNaNRange2IsNull() {
   	    Range r1 = new Range(1.0, 5.0);
   	    Range result = Range.combineIgnoringNaN(r1, null);
   	    assertEquals(r1, result);
   	}

   	@Test
   	public void testCombineIgnoringNaNNormal() {
   	    Range r1 = new Range(1.0, 5.0);
   	    Range r2 = new Range(3.0, 10.0);
   	    Range result = Range.combineIgnoringNaN(r1, r2);
   	    assertEquals(1.0, result.getLowerBound(), 0.000000001d);
   	    assertEquals(10.0, result.getUpperBound(), 0.000000001d);
   	}

   	@Test
   	public void testCombineIgnoringNaNBothNaN() {
   	    Range r1 = new Range(Double.NaN, Double.NaN);
   	    Range r2 = new Range(Double.NaN, Double.NaN);
   	    Range result = Range.combineIgnoringNaN(r1, r2);
   	    assertNull(result);
   	}

   	@Test
   	public void testCombineIgnoringNaNRange1UpperAndRange2LowerIsNaN() {
   	    Range r1 = new Range(Double.NaN, 1.0);
   	    Range r2 = new Range(1.0, Double.NaN);
   	    Range result = Range.combineIgnoringNaN(r1, r2);
   	    assertNotNull(result);
   	    assertEquals(1.0, result.getLowerBound(), 0.000000001d);
   	    assertEquals(1.0, result.getUpperBound(), 0.000000001d);
   	}

   	@Test
   	public void testCombineIgnoringNaNRange2UpperAndRange1LowerIsNaN() {
   	    Range r1 = new Range(1.0, Double.NaN);
   	    Range r2 = new Range(Double.NaN, 1.0);
   	    Range result = Range.combineIgnoringNaN(r1, r2);
   	    assertNotNull(result);
   	    assertEquals(1.0, result.getLowerBound(), 0.000000001d);
   	    assertEquals(1.0, result.getUpperBound(), 0.000000001d);
   	}


   	// EXPANDTOINCLUDE() TESTS -----------------------------

   	@Test
   	public void testExpandToIncludeNullRange() {
   	    Range r = Range.expandToInclude(null, 5.0);
   	    assertEquals(5.0, r.getLowerBound(), 0.000000001d);
   	    assertEquals(5.0, r.getUpperBound(), 0.000000001d);
   	}

   	@Test
   	public void testExpandToIncludeBelow() {
   	    Range base = new Range(2.0, 6.0);
   	    Range result = Range.expandToInclude(base, 1.0);
   	    assertEquals(1.0, result.getLowerBound(), 0.000000001d);
   	    assertEquals(6.0, result.getUpperBound(), 0.000000001d);
   	}

   	@Test
   	public void testExpandToIncludeAbove() {
   	    Range base = new Range(2.0, 6.0);
   	    Range result = Range.expandToInclude(base, 10.0);
   	    assertEquals(2.0, result.getLowerBound(), 0.000000001d);
   	    assertEquals(10.0, result.getUpperBound(), 0.000000001d);
   	}

   	@Test
   	public void testExpandToIncludeInside() {
   	    Range base = new Range(2.0, 6.0);
   	    Range result = Range.expandToInclude(base, 4.0);
   	    assertEquals(base, result);
   	}


   	// EXPAND() TESTS ---------------------------

   	@Test
   	public void testExpandNormal() {
   	    Range r = new Range(2.0, 6.0);
   	    Range expanded = Range.expand(r, 0.5, 0.5);
   	    assertEquals(0.0, expanded.getLowerBound(), 0.000000001d);
   	    assertEquals(8.0, expanded.getUpperBound(), 0.000000001d);
   	}

   	@Test
   	public void testExpandLowerGreaterThanUpperBranch() {
   	    Range r = new Range(2.0, 6.0);
   	    Range expanded = Range.expand(r, -1.0, -1.0);
   	    assertEquals(4.0, expanded.getLowerBound(), 0.000000001d);
   	    assertEquals(4.0, expanded.getUpperBound(), 0.000000001d);
   	}


   	// SHIFT(base, delta) TESTS -------------------------

   	@Test
   	public void testShiftWithoutZeroCrossingDefault() {
   	    Range r = new Range(1.0, 3.0);
   	    Range shifted = Range.shift(r, -5.0);
   	    assertEquals(0.0, shifted.getLowerBound(), 0.000000001d);
   	    assertEquals(0.0, shifted.getUpperBound(), 0.000000001d);
   	}

   	@Test
   	public void testShiftWithoutZeroCrossingZeroBounds() {
   	    Range r = new Range(0.0, 0.0);
   	    Range shifted = Range.shift(r, 1.0);
   	    assertEquals(1.0, shifted.getLowerBound(), 0.000000001d);
   	    assertEquals(1.0, shifted.getUpperBound(), 0.000000001d);
   	}


   	// SCALE() TESTS ----------------------------

   	@Test
   	public void testScaleNormal() {
   	    Range r = new Range(2.0, 4.0);
   	    Range scaled = Range.scale(r, 2.0);
   	    assertEquals(4.0, scaled.getLowerBound(), 0.000000001d);
   	    assertEquals(8.0, scaled.getUpperBound(), 0.000000001d);
   	}

   	@Test(expected = IllegalArgumentException.class)
   	public void testScaleNegativeFactor() {
   	    Range r = new Range(2.0, 4.0);
   	    Range.scale(r, -1.0);
   	}


   	// EQUALS() TESTS -----------------------------------

   	@Test
   	public void testEqualsTrue() {
   	    Range r1 = new Range(1.0, 5.0);
   	    Range r2 = new Range(1.0, 5.0);
   	    assertTrue(r1.equals(r2));
   	}

   	@Test
   	public void testEqualsFalseDifferentRange() {
   	    Range r1 = new Range(1.0, 5.0);
   	    Range r2 = new Range(2.0, 6.0);
   	    assertFalse(r1.equals(r2));
   	}

   	@Test
   	public void testEqualsDifferentUpper() {
   	    Range r1 = new Range(1.0, 5.0);
   	    Range r2 = new Range(1.0, 6.0);
   	    assertFalse(r1.equals(r2));
   	}

   	@Test
   	public void testEqualsNonRangeObject() {
   	    Range r = new Range(1.0, 5.0);
   	    assertFalse(r.equals("NotARange"));
   	}


   	// ISNANRANGE() TESTS ---------------------------------

   	@Test
   	public void testIsNaNRangeTrue() {
   	    Range r = new Range(Double.NaN, Double.NaN);
   	    assertTrue(r.isNaNRange());
   	}

   	@Test
   	public void testIsNaNRangeLowerIsNaN() {
   	    Range r = new Range(Double.NaN, 1.0);
   	    assertFalse(r.isNaNRange());
   	}

   	@Test
   	public void testIsNaNRangeUpperIsNaN() {
   	    Range r = new Range(1.0, Double.NaN);
   	    assertFalse(r.isNaNRange());
   	}

   	// HASHCODE() TESTS ---------------------------------

	@Test
	public void testHashCode() {
		Range r = new Range(1.0, 5.0);
		// Consistency: same object returns same hash on repeated calls
		assertEquals(r.hashCode(), r.hashCode());

		// Contract: equal Range instances must have the same hashCode
		Range same = new Range(1.0, 5.0);
		assertEquals(r.hashCode(), same.hashCode());
	}

    @After
    public void tearDown() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }
}