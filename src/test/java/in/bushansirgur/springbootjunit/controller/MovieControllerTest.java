package in.bushansirgur.springbootjunit.controller;


import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.bushansirgur.springbootjunit.model.Movie;
import in.bushansirgur.springbootjunit.service.MovieService;

@WebMvcTest
public class MovieControllerTest {

//	is used to inject a bean into a test class
	@Autowired
	private MockMvc mockMvc;
	//MockMvc is a Spring test module that provides a way to test Spring MVC controllers without starting a full HTTP server.

	@MockBean
	private MovieService movieService;
	// Instead of using the real implementations
	// of these services, you can use @MockBean to replace them with mock versions.


	//When you use @Autowired instead of @MockBean, you are injecting the actual Spring bean into your test context.
	// This means that the real implementation of the bean will be used during the test,
	// and any interactions with that bean will invoke the actual methods and logic.
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private Movie avatarMovie;
	private Movie titanicMovie;
	
	@BeforeEach
	void init() {
		avatarMovie = new Movie();
		avatarMovie.setId(1L);
		avatarMovie.setName("Avatar");
		avatarMovie.setGenera("Action");
		avatarMovie.setReleaseDate(LocalDate.of(1999, Month.APRIL, 22));
		
		titanicMovie = new Movie();
		avatarMovie.setId(2L);
		titanicMovie.setName("Titanic");
		titanicMovie.setGenera("Romance");
		titanicMovie.setReleaseDate(LocalDate.of(2004, Month.JANUARY, 10));
	}
	
	@Test
	void shouldCreateNewMovie() throws Exception {
		
		when(movieService.save(any(Movie.class))).thenReturn(avatarMovie);
		
		this.mockMvc.perform(post("/movies")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(avatarMovie)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.name", is(avatarMovie.getName())))
		.andExpect(jsonPath("$.genera", is(avatarMovie.getGenera())))
		.andExpect(jsonPath("$.releaseDate", is(avatarMovie.getReleaseDate().toString())));
		// checks the value of a JSON property
	}
	
	@Test
	void shouldFetchAllMovies() throws Exception {
		
		List<Movie> list = new ArrayList<>();
		list.add(avatarMovie);
		list.add(titanicMovie);
		
		when(movieService.getAllMovies()).thenReturn(list);
		
		this.mockMvc.perform(get("/movies"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size()", is(list.size())));
	}
	
	@Test
	void shouldFetchOneMovieById() throws Exception {
		
		when(movieService.getMovieById(anyLong())).thenReturn(avatarMovie);
		
		this.mockMvc.perform(get("/movies/{id}", 1L))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", is(avatarMovie.getName())))
			.andExpect(jsonPath("$.genera", is(avatarMovie.getGenera())));
	}
	
	@Test
	void shouldDeleteMovie() throws Exception {
		
		doNothing().when(movieService).deleteMovie(anyLong());

		// donothing : It's often used for methods that return void or for methods where you don't want to perform any action during testing.
		
		this.mockMvc.perform(delete("/movies/{id}", 1L))
			.andExpect(status().isNoContent());
			
	}
	
	@Test
	void shouldUpdateMovie() throws Exception {
		
		when(movieService.updateMovie(any(Movie.class), anyLong())).thenReturn(avatarMovie);		
		this.mockMvc.perform(put("/movies/{id}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(avatarMovie)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name", is(avatarMovie.getName())))
		.andExpect(jsonPath("$.genera", is(avatarMovie.getGenera())));
	}
}




























