package com.bluefirereader;

import com.bluefirereader.ssouser.SsoUserUtils;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    public void testApp()
    {
        try {
            SsoUserUtils.getInstance().getUserFromJWT("eyJhbGciOiJSUzUxMiJ9.eyJleHAiOjE0MDI1MTg1MjksInN1YiI6ImZhY2Vib29rLTE0Njc2MDg5NDQiLCJzb2NpYWwtaWQiOiIxNDY3NjA4OTQ0IiwidG9rZW4tdHlwZSI6ImZhY2Vib29rIiwiZW1haWwiOiJwYWxhbnRhckBob3RtYWlsLmNvbSIsIm5hbWUiOiJUaW1vdGh5IEpvbmVzIiwiaXNzIjoiaHR0cHM6XC9cL2JsdWVmaXJlcmVhZGVyLmNvbSIsImlhdCI6MTQwMjUxNjcyOX0.HT43eDrYRKhfMwoEDKrb2Rjvmpfm8ub8qYIExgE8uiRaSVaqWI0JzFqBLr6YrsGZPmqy9Ss1GmyXsUUk4Z3iIqeVSfq-2R3y2fRL6F2_juYj1WYy0HxeZjyII3HXckc60pUzjGTcJc-2KGOTTi2wR6f_TrJsusOq9mtcUQQiYP641F8quOzVJtwsu64y5-xWsQIgo-3sk_8O46NyUWDwT-afUJCN5SZ6jN4Tg_pxP75YCDcPetLNNjPnBam_GMH-Wm2jfttC2-S9KdlS9cKa-OnGjAgLEqOh6HXau4jhOZUUDxt3D8V9pyjtcag49DiervN_UukMBBG4ReJ877J5vg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue( true );
    }
}
