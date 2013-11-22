package foodev.jsondiff;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;

public class JsonPatchTestMethods extends GWTTestCase {



    @Before
    public void noSetup() {

        JsonPatch.setHint(null);

    }


    @After
    public void noTearDown() {

        JsonPatch.setHint(null);

    }


    @Test
    public void testPrimAdd() {

        String n = JsonPatch.apply("{}", "{a:1}");
        assertEquals("{\"a\":1}", n);

    }


    @Test
    public void testPrimMerge() {

        String n = JsonPatch.apply("{}", "{\"~a\":1}");
        assertEquals("{\"a\":1}", n);

    }


    @Test
    public void testPrimRemove() {

        String n = JsonPatch.apply("{a:1}", "{\"-a\":0}");
        assertEquals("{}", n);

    }


    @Test
    public void testPrimChange() {

        String n = JsonPatch.apply("{a:1}", "{a:2}");
        assertEquals("{\"a\":2}", n);

    }


    @Test
    public void testPrimChangeMerge() {

        String n = JsonPatch.apply("{a:1}", "{\"~a\":2}");
        assertEquals("{\"a\":2}", n);

    }


    @Test
    public void testNullAdd() {

        String n = JsonPatch.apply("{}", "{a:null}");
        assertEquals("{\"a\":null}", n);

    }


    @Test
    public void testNullMerge() {

        String n = JsonPatch.apply("{}", "{\"~a\":null}");
        assertEquals("{\"a\":null}", n);

    }


    @Test
    public void testNullRemove() {

        String n = JsonPatch.apply("{a:null}", "{\"-a\":0}");
        assertEquals("{}", n);

    }


    @Test
    public void testObjAdd() {

        String n = JsonPatch.apply("{a:1}", "{a:{}}");
        assertEquals("{\"a\":{}}", n);

    }


    @Test
    public void testObjRemove() {

        String n = JsonPatch.apply("{a:{}}", "{\"-a\":{}}");
        assertEquals("{}", n);

    }


    @Test
    public void testObjMerge() {

        String n = JsonPatch.apply("{a:{b:1}}", "{\"~a\":{c:2}}");
        assertEquals("{\"a\":{\"b\":1,\"c\":2}}", n);

    }


    @Test
    public void testObjMergeToPrim() {

        String n = JsonPatch.apply("{a:1}", "{\"~a\":{b:1}}");
        assertEquals("{\"a\":{\"b\":1}}", n);

    }


    @Test
    public void testObjMergetToNull() {

        String n = JsonPatch.apply("{a:null}", "{\"~a\":{b:1}}");
        assertEquals("{\"a\":{\"b\":1}}", n);

    }


    @Test
    public void testObjMergetToArr() {

        String n = JsonPatch.apply("{a:[1]}", "{\"~a\":{b:1}}");
        assertEquals("{\"a\":{\"b\":1}}", n);

    }


    @Test
    public void testArrayAddBad() {

        try {
        	JsonPatch.apply("{}", "{\"a[bad]\": 2}");
        	fail();
        } catch(IllegalArgumentException e) {}
    }


    @Test
    public void testArrayAddFull() {

        String n = JsonPatch.apply("{}", "{a:[0,1]}");
        assertEquals("{\"a\":[0,1]}", n);

    }


    @Test
    public void testArrayMergeFull() {

        String n = JsonPatch.apply("{}", "{\"~a\":[0,1]}");
        assertEquals("{\"a\":[0,1]}", n);

    }


    @Test
    public void testArrayAddToEmpty() {

        String n = JsonPatch.apply("{a:[]}", "{\"a[+0]\":1}");
        assertEquals("{\"a\":[1]}", n);

    }


    @Test
    public void testArrayAddLast() {

        String n = JsonPatch.apply("{a:[0]}", "{\"a[+1]\":1}");
        assertEquals("{\"a\":[0,1]}", n);

    }


    @Test
    public void testArrayAddFirst() {

        String n = JsonPatch.apply("{a:[0]}", "{\"a[+0]\":1}");
        assertEquals("{\"a\":[1,0]}", n);

    }


    @Test
    public void testArrayInsertMiddle() {

        String n = JsonPatch.apply("{a:[0,1]}", "{\"a[+1]\":2}");
        assertEquals("{\"a\":[0,2,1]}", n);

    }


    @Test
    public void testArrRemoveToEmpty() {
        String n = JsonPatch.apply("{a:[0]}", "{\"-a[0]\":null}");
        assertEquals("{\"a\":[]}", n);
    }


    @Test
    public void testArrRemoveFirst() {
        String n = JsonPatch.apply("{a:[0,1]}", "{\"-a[0]\":null}");
        assertEquals("{\"a\":[1]}", n);
    }


    @Test
    public void testArrRemoveLast() {
        String n = JsonPatch.apply("{a:[0,1]}", "{\"-a[1]\":null}");
        assertEquals("{\"a\":[0]}", n);
    }


    @Test
    public void testArrRemoveMiddle() {
        String n = JsonPatch.apply("{a:[0,1,2]}", "{\"-a[1]\":null}");
        assertEquals("{\"a\":[0,2]}", n);
    }


    @Test
    public void testArrRemoveInsertMiddle() {
    	try {
    		JsonPatch.apply("{a:[0,1,2]}", "{\"-a[+1]\":null}");
    		fail();
    	} catch(IllegalArgumentException e) {}
    }


    @Test
    public void testAddRemoveOrderMatters() {
        String n = JsonPatch.apply("{a:[0,1,2]}", "{\"-a[0]\":null,\"-a[1]\":null,\"a[+3]\":3}");
        assertEquals("{\"a\":[2,3]}", n);
    }


    @Test
    public void testAddRemoveOrderMatters2() {
        String n = JsonPatch.apply("{a:[{b:0},{b:1},{b:2},{b:3}]}",
                "{\"a[+3]\":{d:2},\"-a[0]\":0,\"-a[1]\":0,\"~a[2]\":{c:2}}");
        assertEquals("{\"a\":[{\"b\":2,\"c\":2},{\"d\":2},{\"b\":3}]}", n);
    }


    @Test
    public void testAddRemoveOrderMatters3() {
        String n = JsonPatch.apply("{a:[{b:0},{b:1},{b:2},{b:3}]}",
                "{\"a[+2]\":{d:2},\"-a[1]\":0,\"~a[2]\":{c:2}}");
        assertEquals("{\"a\":[{\"b\":0},{\"d\":2},{\"b\":2,\"c\":2},{\"b\":3}]}", n);
    }


    @Test
    public void testArrObjMerge() {
        String n = JsonPatch.apply("{a:[0,{b:1},3]}", "{\"~a[1]\":{c:2}}");
        assertEquals("{\"a\":[0,{\"b\":1,\"c\":2},3]}", n);
    }


    @Test
    public void testArrObjMergeInsert() {
    	try {
            JsonPatch.apply("{a:[0,{b:1},3]}", "{\"~a[+1]\":{c:2}}");
    		fail();
    	} catch(IllegalArgumentException e) {}

    }


    // test for Issue #7 but how did we end up here?
    // Thanks to DrLansing for finding the problem.
    @Test
    public void testCompareArrays() {

//        assertEquals(0, JsonPatch.compareArrays(true, null, null));
//        assertEquals(-1, JsonPatch.compareArrays(true, Arrays.asList(1), null));
//        assertEquals(1, JsonPatch.compareArrays(true, null, Arrays.asList(1)));
//
//        assertEquals(0, JsonPatch.compareArrays(true, Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3)));
//        assertEquals(-1, JsonPatch.compareArrays(true, Arrays.asList(1, 2), Arrays.asList(1, 2, 3)));
//        assertEquals(1, JsonPatch.compareArrays(true, Arrays.asList(1, 2, 3), Arrays.asList(1, 2)));
//
//        assertEquals(1, JsonPatch.compareArrays(true, Arrays.asList(1, 3), Arrays.asList(1, 2)));
//        assertEquals(-1, JsonPatch.compareArrays(true, Arrays.asList(1, 2), Arrays.asList(1, 3)));
//
//        assertEquals(-1, JsonPatch.compareArrays(false, Arrays.asList(1, 3), Arrays.asList(1, 2)));
//        assertEquals(1, JsonPatch.compareArrays(false, Arrays.asList(1, 2), Arrays.asList(1, 3)));

    }


    // test for Issue #7, this will hang if not fixed.
    // Thanks to DrLansing for finding the problem.
    @Test
    public void testCompareArraysIndirect() {

        // must not hang
        JsonPatch.apply("{a:[[[0]]]}", "{\"a[0][0]\":2,\"a[0][0][0]\":2}");

    }


    @Test
    public void testBadDeleteAfterReplace() {

        String from = "{a:[1,2]}";
        String patch = "{a:3, \"-a[0]\": 0}";

    	try {
            JsonPatch.apply(from, patch);
    		fail();
    	} catch(IllegalArgumentException e) {}

    }
    
    @Override
	public String getModuleName() {
		return "foodev.JsonDiff";
	}
}
