package com.tschoend.wmodechallenge.model;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.jaxrs.xml.JacksonJaxbXMLProvider;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import java.io.ByteArrayOutputStream;
import java.lang.annotation.Annotation;
import java.util.UUID;

/**
 * Created by tom on 2015-09-20.
 */
public class AppDirectResultTest extends TestCase {
   XmlMapper mapper;

    @Before
    public void setUp() {
        mapper = new XmlMapper();
    }

    @Test
    public void testSerializationNoError() throws Exception {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        XmlMapper mapper = new XmlMapper();

        AppDirectResult result = new AppDirectResult();
        result.setSuccess(true);
        result.setAccountIdentifier(UUID.randomUUID());
        result.setMessage("It worked");

        String valueAsString = mapper.writeValueAsString(result);

        assertEquals(
                "<result>" + System.lineSeparator()
                        + "</result>",
                valueAsString);
    }
}