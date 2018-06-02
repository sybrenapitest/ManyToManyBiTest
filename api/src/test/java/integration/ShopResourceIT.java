package integration;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.ArrayList;
import org.shboland.persistence.db.repo.PersonRepository;
import org.shboland.persistence.db.hibernate.bean.Person;
import org.shboland.domain.entities.JsonShop;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.http.HttpStatus;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.shboland.api.Application;
import org.shboland.persistence.db.hibernate.bean.Shop;
import org.shboland.persistence.db.repo.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Set;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class ShopResourceIT {

    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ShopRepository shopRepository;

    
    @Autowired
    private PersonRepository personRepository;

     // @InjectInput

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void tearDown() {
        // @TearDownInputTop
        shopRepository.deleteAll();
        
        personRepository.deleteAll();
     // @TearDownInputBottom
    }

    // @Input

    @Test
    public void testDeletePerson_withShopWithPersons() throws Exception {
    
        Shop shop = IntegrationTestFactory.givenAShopWithPerson(shopRepository, personRepository);
        Person person = new ArrayList<>(shop.getPersonSet()).get(0);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.delete("/shops/" + shop.getId() + "/persons/" + person.getId()))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
        assertFalse("Wrong entity link returned.",
                mockMvc.perform(MockMvcRequestBuilders.get("/shops/" + shop.getId() + "/persons"))
                        .andReturn().getResponse().getContentAsString()
                        .contains("/persons/" + person.getId()));
    }

    @Test
    public void testDeletePerson_withShopNoPersons() throws Exception {
    
        Shop shop = IntegrationTestFactory.givenAShop(shopRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.delete("/shops/" + shop.getId() + "/persons/-1"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testDeletePerson_withoutShop() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.delete("shops/-1/persons/-1"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
        

        mockMvc.perform(MockMvcRequestBuilders.delete("/shops/-1/persons/-1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    
    @Test
    public void testPutPerson_withShop() throws Exception {

        Shop shop = IntegrationTestFactory.givenAShop(shopRepository);
        Person person = IntegrationTestFactory.givenAPerson(personRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.put("/shops/" + shop.getId() + "/persons/" + person.getId()))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
        assertTrue("Wrong entity link returned.",
                mockMvc.perform(MockMvcRequestBuilders.get("/shops/" + shop.getId() + "/persons"))
                        .andReturn().getResponse().getContentAsString()
                        .contains("/persons/" + person.getId()));
    }
    
    @Test
    public void testPutPerson_withShopWithPerson() throws Exception {
    
        Shop shop = IntegrationTestFactory.givenAShopWithPerson(shopRepository, personRepository);
        Person person = new ArrayList<>(shop.getPersonSet()).get(0);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.put("/shops/" + shop.getId() + "/persons/" + person.getId()))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
        assertTrue("Wrong entity link returned.",
                mockMvc.perform(MockMvcRequestBuilders.get("/shops/" + shop.getId() + "/persons"))
                        .andReturn().getResponse().getContentAsString()
                        .contains("/persons/" + person.getId()));
    }

    @Test
    public void testPutPerson_withShopNoPerson() throws Exception {
    
        Shop shop = IntegrationTestFactory.givenAShop(shopRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.put("/shops/" + shop.getId() + "/persons/-1"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testPutPerson_withoutShop() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.put("/shops/-1/persons/-1"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testGetPersons_withShopWithPersons() throws Exception {
    
        Shop shop = IntegrationTestFactory.givenAShopWithPerson(shopRepository, personRepository);
            IntegrationTestFactory.givenAPerson(personRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/shops/" + shop.getId() + "/persons"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong grand total returned.", response.getContentAsString().contains("\"grandTotal\":1"));
        assertTrue("Wrong number of results returned.", response.getContentAsString().contains("\"numberOfResults\":1"));
    }

    @Test
    public void testGetPersons_withShopNoPersons() throws Exception {
    
        Shop shop = IntegrationTestFactory.givenAShop(shopRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/shops/" + shop.getId() + "/persons"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong grand total returned.", response.getContentAsString().contains("\"grandTotal\":0"));
        assertTrue("Wrong number of results returned.", response.getContentAsString().contains("\"numberOfResults\":0"));
        assertTrue("Wrong entities returned.", response.getContentAsString().contains("\"results\":[]"));
    }

    @Test
    public void testGetPersons_withoutShop() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/shops/-1/persons"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong grand total returned.", response.getContentAsString().contains("\"grandTotal\":0"));
        assertTrue("Wrong number of results returned.", response.getContentAsString().contains("\"numberOfResults\":0"));
        assertTrue("Wrong entities returned.", response.getContentAsString().contains("\"results\":[]"));
    }

    @Test
    public void testGetShop_withShop() throws Exception {

        Shop shop = IntegrationTestFactory.givenAShop(shopRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/shops/" + shop.getId()))
                        .andReturn().getResponse();
                        
        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity link returned.", response.getContentAsString().contains("/shops/" + shop.getId()));
        assertTrue("Wrong field returned.", response.getContentAsString().contains("\"number\":" + shop.getNumber()));
        // @FieldInputAssert
    }

    @Test
    public void testGetShop_withoutShop() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/shops/-1"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testList_withoutShops() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/shops"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong grand total returned.", response.getContentAsString().contains("\"grandTotal\":0"));
        assertTrue("Wrong number of results returned.", response.getContentAsString().contains("\"numberOfResults\":0"));
        assertTrue("Wrong entities returned.", response.getContentAsString().contains("\"results\":[]"));
    }

    @Test
    public void testList_withShops() throws Exception {
    
        Shop savedShop = IntegrationTestFactory.givenAShop(shopRepository);
        IntegrationTestFactory.givenAShop(shopRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/shops"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong grand total returned.", response.getContentAsString().contains("\"grandTotal\":2"));
        assertTrue("Wrong number of results returned.", response.getContentAsString().contains("\"numberOfResults\":2"));
        assertTrue("Wrong entity link returned.", response.getContentAsString().contains("shops/" + savedShop.getId()));
    }

    @Test
    public void testPostShop_invalidObject() throws Exception {
    
         MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.post("/shops"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testPostShop_newObject() throws Exception {
    
        JsonShop shop = IntegrationTestFactory.givenAJsonShop();

        MockHttpServletResponse response =
                mockMvc.perform(IntegrationTestUtils.doPost("/shops", shop))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.CREATED.value(), response.getStatus());
        assertTrue("Wrong entity link returned.", response.getContentAsString().contains("/shops/"));
        assertTrue("Wrong field returned.", response.getContentAsString().contains("\"number\":" + shop.getNumber()));
        // @FieldInputAssert
    }

    @Test
    public void testPutShop_invalidObject() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.put("/shops/-1", new Object()))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testPutShop_newObject() throws Exception {
    
        JsonShop shop = IntegrationTestFactory.givenAJsonShop();

        MockHttpServletResponse response =
                mockMvc.perform(IntegrationTestUtils.doPut("/shops/-1", shop))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity link returned.", response.getContentAsString().contains("/shops/"));
        assertTrue("Wrong field returned.", response.getContentAsString().contains("\"number\":" + shop.getNumber()));
        // @FieldInputAssert
    }

    @Test
    public void testPutShop_updateObject() throws Exception {
    
        Shop savedShop = IntegrationTestFactory.givenAShop(shopRepository);

        JsonShop shop = IntegrationTestFactory.givenAJsonShop();

        MockHttpServletResponse response =
                mockMvc.perform(IntegrationTestUtils.doPut("/shops/" + savedShop.getId(), shop))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity link returned.", response.getContentAsString().contains("/shops/"));
        assertTrue("Wrong field returned.", response.getContentAsString().contains("\"number\":" + shop.getNumber()));
        // @FieldInputAssert
    }

    @Test
    public void testDeleteShop_unknownObject() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.delete("/shops/-1"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testDeleteShop_deleteObject() throws Exception {
    
        Shop savedShop = IntegrationTestFactory.givenAShop(shopRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.delete("/shops/" + savedShop.getId()))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
        assertFalse("Entity not deleted", shopRepository.findById(savedShop.getId()).isPresent());
    }

}
