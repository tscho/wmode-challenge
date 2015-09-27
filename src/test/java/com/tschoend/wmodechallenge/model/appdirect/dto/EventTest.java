package com.tschoend.wmodechallenge.model.appdirect.dto;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.tschoend.wmodechallenge.model.appdirect.constants.*;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by tom on 2015-09-21.
 */
public class EventTest extends TestCase {
    public static final String testSubCreateXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<event xmlns:atom=\"http://www.w3.org/2005/Atom\"><type>SUBSCRIPTION_ORDER</type><marketplace><baseUrl>https://acme.appdirect.com</baseUrl><partner>ACME</partner></marketplace><flag>STATELESS</flag><creator><email>test-email+creator@appdirect.com</email><firstName>DummyCreatorFirst</firstName><language>fr</language><lastName>DummyCreatorLast</lastName><openId>https://www.appdirect.com/openid/id/ec5d8eda-5cec-444d-9e30-125b6e4b67e2</openId><uuid>ec5d8eda-5cec-444d-9e30-125b6e4b67e2</uuid></creator><payload><company><country>CA</country><email>company-email@example.com</email><name>Example CompanyBean Name</name><phoneNumber>415-555-1212</phoneNumber><uuid>d15bb36e-5fb5-11e0-8c3c-00262d2cda03</uuid><website>http://www.example.com</website></company><configuration><entry><key>domain</key><value>mydomain</value></entry></configuration><order><editionCode>BASIC</editionCode><pricingDuration>MONTHLY</pricingDuration><item><quantity>10</quantity><unit>USER</unit></item><item><quantity>15</quantity><unit>MEGABYTE</unit></item></order></payload><returnUrl>https://www.appdirect.com/finishprocure?token=dummyOrder</returnUrl></event>";


    XmlMapper mapper;

    @Before
    @Override
    public void setUp() {
        mapper = new XmlMapper();
        mapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
    }

    @Test
    public void testDeserializeExample() throws Exception {
        EventBean result = mapper.readValue(testSubCreateXml, EventBean.class);

        // EventBean details
        assertEquals(EventType.SUBSCRIPTION_ORDER, result.getType());
        assertEquals(EventFlag.STATELESS, result.getFlag());
        assertEquals("https://www.appdirect.com/finishprocure?token=dummyOrder", result.getReturnUrl().toString());

        // Marketplace details
        assertEquals("https://acme.appdirect.com", result.getMarketplace().getBaseUrl().toString());
        assertEquals("ACME", result.getMarketplace().getPartner());

        // Creator UserBean details
        UserBean creator = result.getCreator();
        assertNotNull(creator);

        assertEquals("test-email+creator@appdirect.com", creator.getEmail());
        assertEquals("DummyCreatorFirst", creator.getFirstName());
        assertEquals("fr", creator.getLanguage());
        assertEquals("DummyCreatorLast", creator.getLastName());
        assertEquals(
                "https://www.appdirect.com/openid/id/ec5d8eda-5cec-444d-9e30-125b6e4b67e2",
                creator.getOpenId().toString());
        assertEquals("ec5d8eda-5cec-444d-9e30-125b6e4b67e2", creator.getUuid().toString());

        // PayloadBean details
        PayloadBean payload = result.getPayload();
        assertNotNull(payload);

        // PayloadBean CompanyBean details
        CompanyBean company = payload.getCompany();
        assertNotNull(company);

        assertEquals("CA", company.getCountry());
        assertEquals("company-email@example.com", company.getEmail());
        assertEquals("Example CompanyBean Name", company.getName());
        assertEquals("415-555-1212", company.getPhoneNumber());
        assertEquals("d15bb36e-5fb5-11e0-8c3c-00262d2cda03", company.getUuid().toString());
        assertEquals("http://www.example.com", company.getWebsite().toString());

        // PayloadBean Configuration details
        assertNotNull(payload.getConfigurationItemList());
        assertEquals(1, payload.getConfigurationItemList().size());

        assertEquals("domain", payload.getConfigurationItemList().get(0).getKey());
        assertEquals("mydomain", payload.getConfigurationItemList().get(0).getValue());

        // PayloadBean account details (does not exist in SUBSCRIPTION_CREATE event
        assertNull(payload.getAccount());

        // PayloadBean OrderBean details
        OrderBean order = payload.getOrder();
        assertNotNull(order);

        assertEquals(EditionCode.BASIC, order.getEditionCode());
        assertEquals(PricingDuration.MONTHLY, order.getPricingDuration());

        assertNotNull(order.getItems());

        assertEquals(Unit.USER, order.getItems().get(0).getUnit());
        assertEquals(10, order.getItems().get(0).getQuantity());

        assertEquals(Unit.MEGABYTE, order.getItems().get(1).getUnit());
        assertEquals(15, order.getItems().get(1).getQuantity());
    }
}
