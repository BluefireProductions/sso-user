package com.bluefirereader.ballard.test;


import com.bluefirereader.ballard.endpoints.EndpointUtils;
import com.bluefirereader.ballard.endpoints.MethodAndPath;
import com.bluefirereader.ballard.endpoints.EndpointPermissionsException;
import org.junit.Test;


import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;


public class EndpointUtilsTest {

    @Test
    public void staticTest() throws EndpointPermissionsException {
        EndpointUtils.initializeEndpoints("com.bluefirereader.ballard.test.endpoints");

        MethodAndPath goodMethodAndPath = EndpointUtils.getFunctionForPath("/good");

        assertNotNull(goodMethodAndPath);

        MethodAndPath badMethodAndPath = EndpointUtils.getFunctionForPath("/bad");
        assertNull(badMethodAndPath);

        //SsoUser ssoUser = mock(SsoUser.class);
        //when(ssoUser.getEmail()).thenReturn("palantar@gmail.com");





    }
}