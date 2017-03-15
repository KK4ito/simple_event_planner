package ch.fhnw.edu.eaf.eventmgmt;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
/*
@RunWith(SpringRunner.class)
@WebMvcTest(EventController.class)
@TestPropertySource(locations="classpath:test.properties")
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EventRepository userRepositoryMock;

	@Before
	public void setUp() {
		Mockito.reset(userRepositoryMock);
	}

	@Test
	public void findById_UserFound_ShouldReturnFound() throws Exception {
		Event user = new UserBuilder("Tester1", "Hugo").id(new Long(1))
				.email("hugo.tester1@xyz.ch").build();

		when(userRepositoryMock.findOne(1L)).thenReturn(user);

		mockMvc.perform(
				get("/users/{id}", 1L).header("Accept", "application/json"))
				// .andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.id", equalTo(1)))
				.andExpect(jsonPath("$.lastName", equalTo("Tester1")))
				.andExpect(jsonPath("$.firstName", equalTo("Hugo")))
				.andExpect(jsonPath("$.email", equalTo("hugo.tester1@xyz.ch")));
		verify(userRepositoryMock, times(1)).findOne(1L);
	}

	@Test
	public void findById_UserNotExisting_ShouldReturnNotFound()
			throws Exception {
		mockMvc.perform(
				get("/users/{id}", 2L).header("Accept", "application/json"))
				.andExpect(status().isNotFound());
		Mockito.verify(userRepositoryMock, times(1)).findOne(2L);
	}

	@Test
	public void findAll_QuestionnairesFound_ShouldReturnFoundUsers()
			throws Exception {
		Event user1 = new UserBuilder("Tester1", "Hugo").id(new Long(1))
				.email("hugo.tester1@xyz.ch").build();

		Event user2 = new UserBuilder("Tester2", "Hugo").id(new Long(2)).build();

		when(userRepositoryMock.findAll(new Sort("id")))
				.thenReturn(Arrays.asList(user1, user2));

		mockMvc.perform(get("/users").header("Accept", "application/json"))
				// .andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", equalTo(1)))
				.andExpect(jsonPath("$[0].lastName", equalTo("Tester1")))
				.andExpect(jsonPath("$[0].firstName", equalTo("Hugo")))
				.andExpect(jsonPath("$[1].id", equalTo(2)))
				.andExpect(jsonPath("$[1].lastName", equalTo("Tester2")))
				.andExpect(jsonPath("$[1].firstName", equalTo("Hugo")));
		Mockito.verify(userRepositoryMock, times(1)).findAll(new Sort("id"));
	}
}

class UserBuilder {
	private Event user;

	public UserBuilder(String lastName, String firstName) {
		user = new Event(lastName, firstName);
	}

	public UserBuilder id(Long id) {
		user.setId(id);
		return this;
	}

	public UserBuilder email(String email) {
		user.setEmail(email);
		return this;
	}

	public Event build() {
		return user;
	}
}
*/