package com.tschoend.wmodechallenge.model.appdirect;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.tschoend.wmodechallenge.model.appdirect.AppDirectErrorCode;
import com.tschoend.wmodechallenge.model.appdirect.AppDirectResult;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by tom on 2015-09-20.
 */
public class AppDirectResultTest extends TestCase {
    XmlMapper mapper;

    @Before
    public void setUp() {
        mapper = new XmlMapper();
        mapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
    }

    @Test
    public void testSerializationNoError() throws Exception {
        String message = "It worked";
        UUID id = UUID.randomUUID();

        AppDirectResult result = new AppDirectResult();
        result.setSuccess(true);
        result.setAccountIdentifier(id);
        result.setMessage(message);

        String valueAsString = mapper.writeValueAsString(result);

        assertEquals(
                "<?xml version='1.0' encoding='UTF-8'?><result><success>true</success><message>"
                        + message
                        + "</message><accountIdentifier>"
                        + id.toString()
                        + "</accountIdentifier><errorCode/></result>",
                valueAsString);
    }

    @Test
    public void testSerializationWithError() throws Exception {
        String message = "It didn't work :(";

        AppDirectResult result = new AppDirectResult();
        result.setSuccess(false);
        result.setMessage(message);
        result.setErrorCode(AppDirectErrorCode.UNKNOWN_ERROR);

        String valueAsString = mapper.writeValueAsString(result);

        assertEquals(
                "<?xml version='1.0' encoding='UTF-8'?><result><success>false</success><message>"
                        + message
                        + "</message><accountIdentifier/><errorCode>"
                        + AppDirectErrorCode.UNKNOWN_ERROR.toString()
                        + "</errorCode></result>",
                valueAsString);
    }
}