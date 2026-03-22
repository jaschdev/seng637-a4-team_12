package org.jfree.data;

import org.jfree.data.DataUtilities;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.jfree.data.Values2D;
import org.jfree.data.KeyedValues;
import org.jmock.Mockery;
import org.jmock.Expectations;
import java.security.InvalidParameterException;

public class DataUtilitiesTest{

	private static final double EPS = 0.000000001d;

   	// ASSIGNMENT 2 BLACK BOX TESTING START -------------------------------------------------------------------------------

    // CALCULATECOLUMNTOTAL() TESTS  ----------------------------------

    // Robustness Test: null dataset → expect IllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void testCalculateColumnTotalNullDataset() {
        DataUtilities.calculateColumnTotal(null, 0);
    }

    // Weak ECP: column contains only null cells → nulls ignored, total is 0
    @Test
    public void testCalculateColumnTotalAllNullCells() {
        Mockery context = new Mockery();
        final Values2D data = context.mock(Values2D.class);

        context.checking(new Expectations() {{
            allowing(data).getRowCount(); will(returnValue(2));
            allowing(data).getValue(0,0); will(returnValue(null));
            allowing(data).getValue(1,0); will(returnValue(null));
        }});

        double result = DataUtilities.calculateColumnTotal(data, 0);

        // Assert that null cells are ignored and total is 0
        assertEquals(0.0, result, EPS);

        context.assertIsSatisfied();
    }

    // BVA: empty dataset → total should be zero
    @Test
    public void testCalculateColumnTotalEmptyTable() {

        Mockery context = new Mockery();
        final Values2D data = context.mock(Values2D.class);

        context.checking(new Expectations() {{
            allowing(data).getRowCount(); will(returnValue(0));
        }});

        double result = DataUtilities.calculateColumnTotal(data, 0);

        assertEquals(0.0, result, EPS);
        context.assertIsSatisfied();
    }

    // BVA: single row positive value → total equals value
    @Test
    public void testCalculateColumnTotalSingleRow() {

        Mockery context = new Mockery();
        final Values2D data = context.mock(Values2D.class);

        context.checking(new Expectations() {{
            allowing(data).getRowCount(); will(returnValue(1));
            allowing(data).getValue(0,0); will(returnValue(5));
        }});

        double result = DataUtilities.calculateColumnTotal(data, 0);

        assertEquals(5.0, result, EPS);
        context.assertIsSatisfied();
    }

    // Weak ECP: multiple valid values → total equals sum
    @Test
    public void testCalculateColumnTotalMultipleRows() {

        Mockery context = new Mockery();
        final Values2D data = context.mock(Values2D.class);

        context.checking(new Expectations() {{
            allowing(data).getRowCount(); will(returnValue(5));
            allowing(data).getValue(0,0); will(returnValue(1));
            allowing(data).getValue(1,0); will(returnValue(-1));
            allowing(data).getValue(2,0); will(returnValue(0));
            allowing(data).getValue(3,0); will(returnValue(2));
            allowing(data).getValue(4,0); will(returnValue(10));
        }});

        double result = DataUtilities.calculateColumnTotal(data, 0);

        assertEquals(12.0, result, EPS);
        context.assertIsSatisfied();
    }

    // Robustness: negative column index → expect IndexOutOfBoundsException
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCalculateColumnTotalNegativeColumn() {

        Mockery context = new Mockery();
        final Values2D data = context.mock(Values2D.class);

        context.checking(new Expectations() {{
            allowing(data).getRowCount(); will(returnValue(1));
            allowing(data).getValue(0,-1);
            will(throwException(new IndexOutOfBoundsException()));
        }});

        DataUtilities.calculateColumnTotal(data, -1);
    }

    // BVA + Robustness: column index out of bounds → expect exception
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCalculateColumnTotalTooLargeMocked() {

        Mockery context = new Mockery();
        final Values2D data = context.mock(Values2D.class);

        context.checking(new Expectations() {{
            allowing(data).getRowCount(); will(returnValue(1));
            allowing(data).getValue(0,2);
            will(throwException(new IndexOutOfBoundsException()));
        }});

        DataUtilities.calculateColumnTotal(data, 2);
    }

    // CALCULATEROWTOTAL() TESTS  ----------------------------------

    // Robustness: dataset is null → expect IllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void testCalculateRowTotalNullDataset() {
        DataUtilities.calculateRowTotal(null, 0);
    }

    // Weak ECP: row contains only null cells → nulls ignored, total is 0
    @Test
    public void testCalculateRowTotalNullCells() {
        Mockery context = new Mockery();
        final Values2D data = context.mock(Values2D.class);

        context.checking(new Expectations() {{
            allowing(data).getColumnCount(); will(returnValue(2));
            allowing(data).getValue(0,0); will(returnValue(null));
            allowing(data).getValue(0,1); will(returnValue(null));
        }});

        double result = DataUtilities.calculateRowTotal(data, 0);

        // Assert that null cells are ignored and total is 0
        assertEquals(0.0, result, EPS);

        context.assertIsSatisfied();
    }

    // BVA: empty dataset → total should be zero
    @Test
    public void testCalculateRowTotalEmptyTable() {

        Mockery context = new Mockery();
        final Values2D data = context.mock(Values2D.class);

        context.checking(new Expectations() {{
            allowing(data).getColumnCount(); will(returnValue(0));
        }});

        double result = DataUtilities.calculateRowTotal(data, 0);

        assertEquals(0.0, result, EPS);
        context.assertIsSatisfied();
    }

    // BVA: single column positive value
    @Test
    public void testCalculateRowTotalSingleColumn() {

        Mockery context = new Mockery();
        final Values2D data = context.mock(Values2D.class);

        context.checking(new Expectations() {{
            allowing(data).getColumnCount(); will(returnValue(1));
            allowing(data).getValue(0,0); will(returnValue(1));
        }});

        double result = DataUtilities.calculateRowTotal(data, 0);

        assertEquals(1.0, result, EPS);
        context.assertIsSatisfied();
    }

    // Weak ECP: multiple positive values → total equals sum
    @Test
    public void testCalculateRowTotalMultipleColumns() {

        Mockery context = new Mockery();
        final Values2D data = context.mock(Values2D.class);

        context.checking(new Expectations() {{
            allowing(data).getColumnCount(); will(returnValue(3));
            allowing(data).getValue(0,0); will(returnValue(3));
            allowing(data).getValue(0,1); will(returnValue(4));
            allowing(data).getValue(0,2); will(returnValue(5));
        }});

        double result = DataUtilities.calculateRowTotal(data, 0);

        assertEquals(12.0, result, EPS);
        context.assertIsSatisfied();
    }

    // Robustness: negative row index → expect exception
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCalculateRowTotalNegativeRow() {

        Mockery context = new Mockery();
        final Values2D data = context.mock(Values2D.class);

        context.checking(new Expectations() {{
            allowing(data).getColumnCount(); will(returnValue(1));
            allowing(data).getValue(-1,0);
            will(throwException(new IndexOutOfBoundsException()));
        }});

        DataUtilities.calculateRowTotal(data, -1);
    }

    // BVA + Robustness: row index out of bounds → expect exception
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCalculateRowTotalRowTooLarge() {

        Mockery context = new Mockery();
        final Values2D data = context.mock(Values2D.class);

        context.checking(new Expectations() {{
            allowing(data).getColumnCount(); will(returnValue(1));
            allowing(data).getValue(1,0);
            will(throwException(new IndexOutOfBoundsException()));
        }});

        DataUtilities.calculateRowTotal(data, 1);
    }

    // CREATENUMBERARRAY() TESTS  ----------------------------------

    // Robustness: null input (invalid equivalence class)
	@Test (expected = IllegalArgumentException.class)
	public void testCreateNumberArrayNull() {
		//null not permitted
		Number[] data = DataUtilities.createNumberArray(null);
	}

    // Boundary Value: empty array input (size = 0)
	@Test
	public void testCreateNumberArrayEmpty() {
		double[] emptyArray = {};

		Number[] data = DataUtilities.createNumberArray(emptyArray);
		int len = data.length;
		//make sure number array has length of 0
		assertEquals(0,len, 0.0001d);
	}

    // Boundary Value: single element input (size = 1)
	@Test
	public void testCreateNumberArraySingleValue() {
		double[] singleValueArray = {1};

		Number[] data = DataUtilities.createNumberArray(singleValueArray);
		int len = data.length;
		//make sure number array has length of 1
		assertEquals(1, len, 0.0001d);
	}

    // Weak ECP: valid multi-element input array
	@Test
	public void testCreateNumberArrayMultipleValues() {
		double[] singleValueArray = {1, 2 , 3};

		Number[] data = DataUtilities.createNumberArray(singleValueArray);
		int len = data.length;
		//make sure that number array has length of 3
		assertEquals(3, len, 0.0001d);
	}

    // CREATENUMBERARRAY2D() TESTS  ----------------------------------

    // Robustness: Check for null input (not allowed)
    @Test(expected = IllegalArgumentException.class)
    public void createNumberArray2D_nullInput() {
       DataUtilities.createNumberArray2D(null);
    }

    // BVA: 2D array with 1 empty row
    @Test
    public void createNumberArray2D_emptyInnerArray() {

    	double[][] input = { {} };

        Number[][] output = DataUtilities.createNumberArray2D(input);

        assertEquals(1, output.length);
        assertNotNull(output[0]);
        assertEquals(0, output[0].length);
    }

    // BVA: Empty outer array
    @Test
    public void createNumberArray2D_emptyOuterArray() {

    	double[][] input = new double[0][];

        Number[][] output = DataUtilities.createNumberArray2D(input);

        assertNotNull(output);
        assertEquals(0, output.length);
    }

    // BVA: creating a filled 1x1 array
    @Test
    public void createNumberArray2D_singleElement() {

    	double[][] input = { { 5.5 } };

        Number[][] output = DataUtilities.createNumberArray2D(input);

        assertNotNull("Output should not be null", output);
        assertNotNull("Output should not be null", output[0][0]);

        assertEquals(1, output.length);
        assertEquals(1, output[0].length);
        assertEquals(5.5, output[0][0].doubleValue(), EPS);
    }

    // Weak ECP: Creating a 2x2 array with negative/zero/positive values
    @Test
    public void createNumberArray2D_negativeAndZeroValues() {

    	double[][] input = {
            { 1.0, -2.5 },
            { 0.0,  3.25 }
        };

        Number[][] output = DataUtilities.createNumberArray2D(input);

        assertNotNull("Output should not be null", output);
        assertNotNull("Output should not be null", output[0][0]);
        assertNotNull("Output should not be null", output[0][1]);
        assertNotNull("Output should not be null", output[1][0]);
        assertNotNull("Output should not be null", output[1][1]);

        assertTrue(output[0][0] instanceof Double);

        assertEquals(2, output.length);
        assertEquals(2, output[0].length);
        assertEquals(2, output[1].length);

        assertEquals(1.0,  output[0][0].doubleValue(), EPS);
        assertEquals(-2.5, output[0][1].doubleValue(), EPS);
        assertEquals(0.0,  output[1][0].doubleValue(), EPS);
        assertEquals(3.25, output[1][1].doubleValue(), EPS);

    }

    // GETCUMULATIVEPERCENTAGES() TESTS  ----------------------------------

    // Robustness: null input (not allowed)
    @Test(expected = IllegalArgumentException.class)
    public void getCumulativePercentages_nullInput() {
        DataUtilities.getCumulativePercentages(null);
    }

    // BVA: input empty dataset
    @Test
    public void getCumulativePercentages_emptyDataset() {

    	Mockery context = new Mockery();
        final KeyedValues data = context.mock(KeyedValues.class);

        context.checking(new Expectations() {{
            allowing(data).getItemCount();
            will(returnValue(0));
        }});


        KeyedValues result = DataUtilities.getCumulativePercentages(data);


        assertNotNull(result);
        assertEquals(0, result.getItemCount());

        context.assertIsSatisfied();
    }


    // BVA: input dataset with single key/value
    @Test
    public void getCumulativePercentages_singleValue() {
        Mockery context = new Mockery();
        final KeyedValues data = context.mock(KeyedValues.class);

        context.checking(new Expectations() {{
            allowing(data).getItemCount(); will(returnValue(1));
            allowing(data).getKey(0); will(returnValue("A"));
            allowing(data).getValue(0); will(returnValue(7.0));
        }});

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

        assertNotNull(result);
        assertEquals(1, result.getItemCount());
        assertEquals("A", result.getKey(0));
        assertEquals(1.0, result.getValue(0).doubleValue(), EPS);
    }

    // Weak ECP: input dataset with multiple positive values
    @Test
    public void getCumulativePercentages_multiplePositiveValues() {
        Mockery context = new Mockery();
        final KeyedValues data = context.mock(KeyedValues.class);

        context.checking(new Expectations() {{
            allowing(data).getItemCount(); will(returnValue(2));
            allowing(data).getKey(0); will(returnValue("A"));
            allowing(data).getValue(0); will(returnValue(7.0));
  		    allowing(data).getKey(1); will(returnValue("B"));
  		    allowing(data).getValue(1); will(returnValue(2.0));
        }});

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

        assertNotNull(result);
        assertEquals(2, result.getItemCount());
        assertEquals(7.0/9.0, result.getValue(0).doubleValue(), EPS);
        assertEquals(1.0, result.getValue(1).doubleValue(), EPS);
    }


    // Weak ECP: input dataset with multiple zero values
    @Test
    public void getCumulativePercentages_multipleZeroValues() {
        Mockery context = new Mockery();
        final KeyedValues data = context.mock(KeyedValues.class);

        context.checking(new Expectations() {{
            allowing(data).getItemCount(); will(returnValue(2));
            allowing(data).getKey(0); will(returnValue("A"));
            allowing(data).getValue(0); will(returnValue(0.0));
  		    allowing(data).getKey(1); will(returnValue("B"));
  		    allowing(data).getValue(1); will(returnValue(0.0));
        }});

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

        assertNotNull(result);
        assertEquals(2, result.getItemCount());
        assertTrue(Double.isNaN(result.getValue(0).doubleValue()));
        assertTrue(Double.isNaN(result.getValue(1).doubleValue()));
    }


    // Strong ECP: input dataset with mix of positive and negative values
    @Test
    public void getCumulativePercentages_mixedValues() {
        Mockery context = new Mockery();
        final KeyedValues data = context.mock(KeyedValues.class);

        context.checking(new Expectations() {{
            allowing(data).getItemCount(); will(returnValue(2));
            allowing(data).getKey(0); will(returnValue("A"));
            allowing(data).getValue(0); will(returnValue(7.0));
  		    allowing(data).getKey(1); will(returnValue("B"));
  		    allowing(data).getValue(1); will(returnValue(-2.0));
        }});

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

        assertNotNull(result);
        assertEquals(2, result.getItemCount());
        assertEquals(7.0/5.0, result.getValue(0).doubleValue(), EPS);
        assertEquals(1.0, result.getValue(1).doubleValue(), EPS);
    }


    // Special Case: input dataset with multiple zero values
    @Test
    public void getCumulativePercentages_zeroTotalSum() {
        Mockery context = new Mockery();
        final KeyedValues data = context.mock(KeyedValues.class);

        context.checking(new Expectations() {{
            allowing(data).getItemCount(); will(returnValue(2));
            allowing(data).getKey(0); will(returnValue("A"));
            allowing(data).getValue(0); will(returnValue(5.0));
  		    allowing(data).getKey(1); will(returnValue("B"));
  		    allowing(data).getValue(1); will(returnValue(-5.0));
        }});

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

        assertNotNull(result);
        assertEquals(2, result.getItemCount());
        assertTrue(Double.isInfinite(result.getValue(0).doubleValue()));
        assertTrue(Double.isNaN(result.getValue(1).doubleValue()));
    }

   	// BLACK BOX TESTING END -------------------------------------------------------------------------------
   	// WHITE BOX TESTING START -------------------------------------------------------------------------------

    // EQUAL(double[][], double[][]) TESTS ----------------------------

    // Both arrays null
    @Test
    public void testEqualBothNull() {
    	assertTrue(DataUtilities.equal(null, null));
    }

    // First array null, second not null
    @Test
    public void testEqualFirstNull() {
    	double[][] b = {{1.0}};
    	assertFalse(DataUtilities.equal(null, b));
    }

    // Second array null
    @Test
    public void testEqualSecondNull() {
    	double[][] a = {{1.0}};
    	assertFalse(DataUtilities.equal(a, null));
    }

    // Different lengths
    @Test
    public void testEqualDifferentLengths() {
    	double[][] a = {{1.0}, {2.0}};
    	double[][] b = {{1.0}};
    	assertFalse(DataUtilities.equal(a, b));
    }

    // Same arrays
    @Test
    public void testEqualSameArrays() {
    	double[][] a = {{1.0, 2.0}, {3.0, 4.0}};
    	double[][] b = {{1.0, 2.0}, {3.0, 4.0}};
    	assertTrue(DataUtilities.equal(a, b));
    }

    // Different same size arrays
    @Test
    public void testEqualDifferentArrays() {
    	double[][] a = {{1.0, 2.0}};
    	double[][] b = {{1.0, 3.0}};
    	assertFalse(DataUtilities.equal(a, b));
    }

    // CLONE(double[][]) TESTS ---------------------------------------

    // Null source
    @Test(expected = IllegalArgumentException.class)
    public void testCloneNullSource() {
    	DataUtilities.clone(null);
    }

    // Clone valid array
    @Test
    public void testCloneValidArray() {
    	double[][] source = {{1.0, 2.0}, {3.0, 4.0}};
    	double[][] cloned = DataUtilities.clone(source);

    	assertNotSame(source, cloned);
    	assertEquals(source.length, cloned.length);
    	assertArrayEquals(source[0], cloned[0], EPS);
    	assertArrayEquals(source[1], cloned[1], EPS);
    }

    // Clone with null row
    @Test
    public void testCloneWithNullRow() {
    	double[][] source = new double[2][];
    	source[0] = null;
    	source[1] = new double[]{5.0};

    	double[][] cloned = DataUtilities.clone(source);

    	assertNull(cloned[0]);
    	assertArrayEquals(new double[]{5.0}, cloned[1], EPS);
    }

    // CALCULATECOLUMNTOTAL(data,column,validRows) TESTS ---------------

    @Test
    public void testCalculateColumnTotalValidRows() {

    	Mockery context = new Mockery();
    	final Values2D data = context.mock(Values2D.class);

    	context.checking(new Expectations() {{
    		allowing(data).getRowCount(); will(returnValue(3));
    		allowing(data).getValue(0,0); will(returnValue(1));
    		allowing(data).getValue(1,0); will(returnValue(2));
    		allowing(data).getValue(2,0); will(returnValue(3));
    	}});

    	int[] rows = {0,2};
    	double result = DataUtilities.calculateColumnTotal(data,0,rows);
    	assertEquals(4.0,result,EPS);

    	context.assertIsSatisfied();
    }

    // CALCULATEROWTOTAL(data,row,validCols) TESTS ----------------------

    @Test
    public void testCalculateRowTotalValidColumns() {

    	Mockery context = new Mockery();
    	final Values2D data = context.mock(Values2D.class);

    	context.checking(new Expectations() {{
    		allowing(data).getColumnCount(); will(returnValue(3));
    		allowing(data).getValue(0,0); will(returnValue(1));
    		allowing(data).getValue(0,1); will(returnValue(2));
    		allowing(data).getValue(0,2); will(returnValue(3));
    	}});

    	int[] cols = {0,2};
    	double result = DataUtilities.calculateRowTotal(data,0,cols);
    	assertEquals(4.0,result,EPS);

    	context.assertIsSatisfied();
    }

}