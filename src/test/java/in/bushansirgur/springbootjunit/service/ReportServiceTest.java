package in.bushansirgur.springbootjunit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import in.bushansirgur.springbootjunit.model.Movie;
import in.bushansirgur.springbootjunit.repository.MovieRepository;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {


//	A mock object is a dummy implementation of a class or interface that is used for testing.
//	When testing the MovieService, the MovieRepository is often mocked to isolate
//	the unit of code being tested and to control the behavior of the dependencies.
	@Mock
	private MovieRepository movieRepository;

	@InjectMocks
	private MovieService movieService;
	//@InjectMocks: This annotation is used to inject mock objects into the fields of the target class.
	// In your example, MovieService is likely to have some dependencies (collaborators or other services) that need to be mocked
	// for testing purposes.
	// @InjectMocks helps Mockito automatically inject those mocked dependencies into the MovieService instance.

	//@InjectMocks injects the mocked MovieRepository into the MovieService.


	//ki yebdew barcha howa ya3ref win yemchi based lil contructor autowired
	
	private Movie avatarMovie;
	private Movie titanicMovie;
	
	@BeforeEach
	void init() {
		avatarMovie = new Movie();
		avatarMovie.setId(1L);
		avatarMovie.setName("Avatar");
		avatarMovie.setGenera("Action");
		avatarMovie.setReleaseDate(LocalDate.of(2000, Month.APRIL, 23));

		titanicMovie = new Movie();
		titanicMovie.setId(2L);
		titanicMovie.setName("Titanic");
		titanicMovie.setGenera("Romance");
		titanicMovie.setReleaseDate(LocalDate.of(2004, Month.JANUARY, 10));
	}

	@Test
	void saveReport() {

		when(movieRepository.save(any(Movie.class))).thenReturn(avatarMovie);
		//savi anny movie class
		Movie newMovie = movieService.save(avatarMovie);

		assertNotNull(newMovie);
		assertThat(newMovie.getName()).isEqualTo("Avatar");
	}
	
	@Test
	void getAllReports() {
		
		List<Movie> list = new ArrayList<>();
		list.add(avatarMovie);
		list.add(titanicMovie);
		
		when(movieRepository.findAll()).thenReturn(list);
		
		List<Movie> movies = movieService.getAllMovies();
		
		assertEquals(2, movies.size());
		assertNotNull(movies);
	}
	
	@Test
	void getReportById() {
		
		when(movieRepository.findById(anyLong())).thenReturn(Optional.of(avatarMovie));
		Movie existingMovie = movieService.getMovieById(avatarMovie.getId());
		assertNotNull(existingMovie);
		assertThat(existingMovie.getId()).isNotEqualTo(null);
	}
	
	@Test
	void getReportByIdForException() {
		
		when(movieRepository.findById(2L)).thenReturn(Optional.of(avatarMovie));
		assertThrows(RuntimeException.class, () -> {
			movieService.getMovieById(avatarMovie.getId());
		});
	}
	
	@Test
	void UpdateReport() {
		
		when(movieRepository.findById(anyLong())).thenReturn(Optional.of(avatarMovie));
		
		when(movieRepository.save(any(Movie.class))).thenReturn(avatarMovie);
		avatarMovie.setGenera("Fantacy");
		Movie exisitingMovie = movieService.updateMovie(avatarMovie, avatarMovie.getId());
		
		assertNotNull(exisitingMovie);
		assertEquals("Fantacy", avatarMovie.getGenera());
	}
	
	@Test
	void deleteReport() {
		
		Long movieId = 1L;
		when(movieRepository.findById(anyLong())).thenReturn(Optional.of(avatarMovie));
		doNothing().when(movieRepository).delete(any(Movie.class));
		
		movieService.deleteMovie(movieId);
		
		verify(movieRepository, times(1)).delete(avatarMovie);
		//verifi method delete called once
		
	}
}

//Given
//when
//Then


//private method : This rule does not raise an issue when the visibility is set to private, because private test methods and classes
//are systematically ignored by JUnit5, (just convention and readibility, JUnit4, which required everything to be public.
// Test classes and methods can have any visibility except private.
// It is however recommended to use the default package visibility to improve readability.

//only 1 verify or asset













