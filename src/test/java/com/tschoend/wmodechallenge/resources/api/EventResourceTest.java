package com.tschoend.wmodechallenge.resources.api;

import com.tschoend.wmodechallenge.client.AppDirectAuthorizedClient;
import com.tschoend.wmodechallenge.dao.AccountDao;
import com.tschoend.wmodechallenge.dao.UserDao;
import com.tschoend.wmodechallenge.model.appdirect.constants.AppDirectErrorCode;
import com.tschoend.wmodechallenge.model.appdirect.constants.Role;
import com.tschoend.wmodechallenge.model.appdirect.dto.AppDirectResultBean;
import com.tschoend.wmodechallenge.model.appdirect.entity.User;
import com.tschoend.wmodechallenge.resources.appdirect.EventResource;
import org.junit.Test;

import java.text.ParseException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.*;

/**
 * Created by tom on 2015-09-26.
 */
public class EventResourceTest {
    private final UserDao userDao = mock(UserDao.class);
    private final AccountDao accountDao = mock(AccountDao.class);
    private final AppDirectAuthorizedClient client = mock(AppDirectAuthorizedClient.class);

    @Test
    public void eventParsingFailedTest() {
        EventResource resource = new EventResource(client, accountDao, userDao);

        when(client.getEvent(anyString())).thenThrow(ParseException.class);

        AppDirectResultBean result = resource.createEvent("nothing");

        assertFalse(result.getSuccess());
        assertEquals(AppDirectErrorCode.UNKNOWN_ERROR, result.getErrorCode());
    }
}
