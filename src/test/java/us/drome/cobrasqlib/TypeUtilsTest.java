/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.java.us.drome.cobrasqlib;

import java.sql.Types;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import us.drome.cobrasqlib.TypeUtils;

/**
 *
 * @author Ashleigh
 */
public class TypeUtilsTest {
    
    public TypeUtilsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getDefaultPrecision method, of class TypeUtils.
     */
    @org.junit.Test
    public void testGetDefaultPrecision() {
        System.out.println("getDefaultPrecision");
        Types type = null;
        int expResult = 0;
        int result = TypeUtils.getDefaultPrecision(type);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of canAutoIncrement method, of class TypeUtils.
     */
    @org.junit.Test
    public void testCanAutoIncrement() {
        System.out.println("canAutoIncrement");
        Types type = Types.ARRAY;
        boolean expResult = false;
        boolean result = TypeUtils.canAutoIncrement(type);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
