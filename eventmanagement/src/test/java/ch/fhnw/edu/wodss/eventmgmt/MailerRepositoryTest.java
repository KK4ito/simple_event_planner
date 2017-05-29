package ch.fhnw.edu.wodss.eventmgmt;

import ch.fhnw.edu.wodss.eventmgmt.endpoints.MailerRepository;
import ch.fhnw.edu.wodss.eventmgmt.endpoints.UserRepository;
import ch.fhnw.edu.wodss.eventmgmt.entities.User;
import ch.fhnw.edu.wodss.eventmgmt.security.Pac4jSecurityConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.pac4j.core.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MailerRepository.class)
@TestPropertySource(locations="classpath:test.properties")
@ContextConfiguration(classes=Pac4jSecurityConfig.class,loader= AnnotationConfigContextLoader.class)
public class MailerRepositoryTest {

    private MockMvc mockMvc;

    @Autowired
    private Config config;

    @MockBean
    UserRepository userRepository;

    @InjectMocks
    MailerRepository mailerRepo;

	MailerRepository mailerRepository;

	//Tuesday
    //10.10.2000 10:10
	long testdateLong = 971165400000l;
	Date testdate;

	@Before
	public void setUp() {
	    mailerRepository = new MailerRepository();
	    testdate = new Date(testdateLong);
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mailerRepo).build();
	}

	@Test
    public void testGetDateMethods() {
	    String date = mailerRepository.getEventDate(testdate);
	    Assert.assertEquals("10.10.2000", date);

        date = mailerRepository.getEventDateTime(testdate);
        Assert.assertEquals("10:10 10.10.2000", date);

        //date = mailerRepository.getEventDay(testdate);
        //Assert.assertEquals("Dienstag", date); // This fails if OS is not german

        date = mailerRepository.getEventTime(testdate);
        Assert.assertEquals("10:10", date);
    }

    /*@Test
    public void testGetTemplate() throws Exception {
	    List<User> users = new ArrayList<>();

	    User u1 = new User();
	    u1.setEmail("test1@test.ch");
        users.add(u1);

        User u2 = new User();
        u2.setEmail("test2@test.ch");
	    users.add(u2);

	    when(userRepository.externalOptIn()).thenReturn(users);

        mockMvc.perform(get("/api/template").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.to", equalTo("test1@test.ch,test2@test.ch")))
            .andExpect(jsonPath("$.from", equalTo("wodss17@gmail.com")))
            .andExpect(jsonPath("$.cc", equalTo("")))
            .andExpect(jsonPath("$.subject", equalTo("Einladung CS-Seminar")))
            .andExpect(jsonPath("$.body", equalTo("Testtext")));

    }*/
}
