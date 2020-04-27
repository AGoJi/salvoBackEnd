package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Date;

@SpringBootApplication
public class SalvoApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	/*@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
		return (args) -> {

			Player p1 = new Player("Jack Bauer", "j.bauer@ctu.gov", passwordEncoder().encode("24"));
			Player p2 = new Player("Chloe O'Brian", "c.obrian@ctu.gov", passwordEncoder().encode("42"));
			Player p3 = new Player("Kim Bauer", "kim_bauer@gmail.com", passwordEncoder().encode("kb"));
			Player p4 = new Player("Tony Almeida", "t.almeida@ctu.gov", passwordEncoder().encode("mole"));
			playerRepository.save(p1);
			playerRepository.save(p2);
			playerRepository.save(p3);
			playerRepository.save(p4);

			Date newDate = new Date();

			Game g1 = new Game(newDate);
			Game g2 = new Game(Date.from(newDate.toInstant().plusSeconds(3600)));
			Game g3 = new Game(Date.from(newDate.toInstant().plusSeconds(7200)));
			Game g4 = new Game(Date.from(newDate.toInstant().plusSeconds(10800)));
			Game g5 = new Game(Date.from(newDate.toInstant().plusSeconds(14400)));
			Game g6 = new Game(Date.from(newDate.toInstant().plusSeconds(18000)));
			Game g7 = new Game(Date.from(newDate.toInstant().plusSeconds(21600)));
			Game g8 = new Game(Date.from(newDate.toInstant().plusSeconds(25200)));
			gameRepository.save(g1);
			gameRepository.save(g2);
			gameRepository.save(g3);
			gameRepository.save(g4);
			gameRepository.save(g5);
			gameRepository.save(g6);
			gameRepository.save(g7);
			gameRepository.save(g8);

			GamePlayer gp1 = new GamePlayer(g1,p1);
			GamePlayer gp2 = new GamePlayer(g1,p2);
			GamePlayer gp3 = new GamePlayer(g2,p1);
			GamePlayer gp4 = new GamePlayer(g2,p2);
			GamePlayer gp5 = new GamePlayer(g3,p2);
			GamePlayer gp6 = new GamePlayer(g3,p4);
			GamePlayer gp7 = new GamePlayer(g4,p2);
			GamePlayer gp8 = new GamePlayer(g4,p1);
			GamePlayer gp9 = new GamePlayer(g5,p4);
			GamePlayer gp10 = new GamePlayer(g5,p1);
			GamePlayer gp11 = new GamePlayer(g6,p3);
			GamePlayer gp12 = new GamePlayer(g7,p4);
			GamePlayer gp13 = new GamePlayer(g8,p3);
			GamePlayer gp14 = new GamePlayer(g8,p4);
			gamePlayerRepository.save(gp1);
			gamePlayerRepository.save(gp2);
			gamePlayerRepository.save(gp3);
			gamePlayerRepository.save(gp4);
			gamePlayerRepository.save(gp5);
			gamePlayerRepository.save(gp6);
			gamePlayerRepository.save(gp7);
			gamePlayerRepository.save(gp8);
			gamePlayerRepository.save(gp9);
			gamePlayerRepository.save(gp10);
			gamePlayerRepository.save(gp11);
			gamePlayerRepository.save(gp12);
			gamePlayerRepository.save(gp13);
			gamePlayerRepository.save(gp14);

			Ship s1 = new Ship("Destroyer", Arrays.asList("H2","H3","H4"));
			Ship s2 = new Ship("Submarine", Arrays.asList("E1", "F1", "G1"));
			Ship s3 = new Ship("Patrol Boat", Arrays.asList("B4","B5"));
			Ship s4 = new Ship("Destroyer", Arrays.asList("B5","C5","D5"));
			Ship s5 = new Ship("Patrol Boat", Arrays.asList("F1","F2"));
			Ship s6 = new Ship("Destroyer",Arrays.asList("B5","C5","D5"));
			Ship s7 = new Ship("Patrol Boat", Arrays.asList("C6","C7"));
			Ship s8 = new Ship("Submarine",Arrays.asList("A2","A3","A4"));
			Ship s9 = new Ship("Patrol Boat", Arrays.asList("G6","H6"));
			Ship s10 = new Ship("Destroyer",Arrays.asList("B5","C5","D5"));
			Ship s11 = new Ship("Patrol Boat", Arrays.asList("C6","C7"));
			Ship s12 = new Ship("Submarine",Arrays.asList("A2","A3","A4"));
			Ship s13 = new Ship("Patrol Boat", Arrays.asList("G6","H6"));
			Ship s14 = new Ship("Destroyer",Arrays.asList("B5","C5","D5"));
			Ship s15 = new Ship("Patrol Boat", Arrays.asList("C6","C7"));
			Ship s16 = new Ship("Submarine",Arrays.asList("A2","A3","A4"));
			Ship s17 = new Ship("Patrol Boat", Arrays.asList("G6","H6"));
			Ship s18 = new Ship("Destroyer",Arrays.asList("B5","C5","D5"));
			Ship s19 = new Ship("Patrol Boat", Arrays.asList("C6","C7"));
			Ship s20 = new Ship("Submarine",Arrays.asList("A2","A3","A4"));
			Ship s21 = new Ship("Patrol Boat", Arrays.asList("G6","H6"));
			Ship s22 = new Ship("Destroyer",Arrays.asList("B5","C5","D5"));
			Ship s23 = new Ship("Patrol Boat", Arrays.asList("C6","C7"));
			Ship s24 = new Ship("Destroyer",Arrays.asList("B5","C5","D5"));
			Ship s25 = new Ship("Patrol Boat", Arrays.asList("C6","C7"));
			Ship s26 = new Ship("Submarine",Arrays.asList("A2","A3","A4"));
			Ship s27 = new Ship("Patrol Boat", Arrays.asList("G6","H6"));
			gp1.addShip(s1);
			gp1.addShip(s2);
			gp1.addShip(s3);
			gp2.addShip(s4);
			gp2.addShip(s5);
			gp3.addShip(s6);
			gp3.addShip(s7);
			gp4.addShip(s8);
			gp4.addShip(s9);
			gp5.addShip(s10);
			gp5.addShip(s11);
			gp6.addShip(s12);
			gp6.addShip(s13);
			gp7.addShip(s14);
			gp7.addShip(s15);
			gp8.addShip(s16);
			gp8.addShip(s17);
			gp9.addShip(s18);
			gp9.addShip(s19);
			gp10.addShip(s20);
			gp10.addShip(s21);
			gp11.addShip(s22);
			gp11.addShip(s23);
			gp13.addShip(s24);
			gp13.addShip(s25);
			gp14.addShip(s26);
			gp14.addShip(s27);
			shipRepository.save(s1);
			shipRepository.save(s2);
			shipRepository.save(s3);
			shipRepository.save(s4);
			shipRepository.save(s5);
			shipRepository.save(s6);
			shipRepository.save(s7);
			shipRepository.save(s8);
			shipRepository.save(s9);
			shipRepository.save(s10);
			shipRepository.save(s11);
			shipRepository.save(s12);
			shipRepository.save(s13);
			shipRepository.save(s14);
			shipRepository.save(s15);
			shipRepository.save(s16);
			shipRepository.save(s17);
			shipRepository.save(s18);
			shipRepository.save(s19);
			shipRepository.save(s20);
			shipRepository.save(s21);
			shipRepository.save(s22);
			shipRepository.save(s23);
			shipRepository.save(s24);
			shipRepository.save(s25);
			shipRepository.save(s26);
			shipRepository.save(s27);

			Salvo sv1 = new Salvo(1, Arrays.asList("B5","C5","F1"));
			Salvo sv2 = new Salvo(1, Arrays.asList("B4","B5","B6"));
			Salvo sv3 = new Salvo(2,Arrays.asList("F2","D5"));
			Salvo sv4 = new Salvo(2,Arrays.asList("E1","H3","A2"));
			Salvo sv5 = new Salvo(1,Arrays.asList("A2","A4","G6"));
			Salvo sv6 = new Salvo(1,Arrays.asList("B5","D5","C7"));
			Salvo sv7 = new Salvo(2,Arrays.asList("A3","H6"));
			Salvo sv8 = new Salvo(2,Arrays.asList("C5","C6"));
			Salvo sv9 = new Salvo(1,Arrays.asList("G6","H6","A4"));
			Salvo sv10 = new Salvo(1,Arrays.asList("H1","H2","H3"));
			Salvo sv11 = new Salvo(2,Arrays.asList("A2","A3","D8"));
			Salvo sv12 = new Salvo(2,Arrays.asList("E1","F2","G3"));
			Salvo sv13 = new Salvo(1,Arrays.asList("A3","A4","F7"));
			Salvo sv14 = new Salvo(1,Arrays.asList("B5","C6","H1"));
			Salvo sv15 = new Salvo(2,Arrays.asList("A2","G6","H6"));
			Salvo sv16 = new Salvo(2,Arrays.asList("C5","C7","D5"));
			Salvo sv17 = new Salvo(1,Arrays.asList("A1","A2","A3"));
			Salvo sv18 = new Salvo(1,Arrays.asList("B5","B6","C7"));
			Salvo sv19 = new Salvo(2,Arrays.asList("G6","G7","G8"));
			Salvo sv20 = new Salvo(2,Arrays.asList("C6","D6","E6"));
			Salvo sv21 = new Salvo(3,Arrays.asList());
			Salvo sv22 = new Salvo(3,Arrays.asList("H1","H8"));
			gp1.addSalvo(sv1);
			gp2.addSalvo(sv2);
			gp1.addSalvo(sv3);
			gp2.addSalvo(sv4);
			gp3.addSalvo(sv5);
			gp4.addSalvo(sv6);
			gp3.addSalvo(sv7);
			gp4.addSalvo(sv8);
			gp5.addSalvo(sv9);
			gp6.addSalvo(sv10);
			gp5.addSalvo(sv11);
			gp6.addSalvo(sv12);
			gp7.addSalvo(sv13);
			gp8.addSalvo(sv14);
			gp7.addSalvo(sv15);
			gp8.addSalvo(sv16);
			gp9.addSalvo(sv17);
			gp10.addSalvo(sv18);
			gp9.addSalvo(sv19);
			gp10.addSalvo(sv20);
			gp9.addSalvo(sv21);
			gp10.addSalvo(sv22);
			salvoRepository.save(sv1);
			salvoRepository.save(sv2);
			salvoRepository.save(sv3);
			salvoRepository.save(sv4);
			salvoRepository.save(sv5);
			salvoRepository.save(sv6);
			salvoRepository.save(sv7);
			salvoRepository.save(sv8);
			salvoRepository.save(sv9);
			salvoRepository.save(sv10);
			salvoRepository.save(sv11);
			salvoRepository.save(sv12);
			salvoRepository.save(sv13);
			salvoRepository.save(sv14);
			salvoRepository.save(sv15);
			salvoRepository.save(sv16);
			salvoRepository.save(sv17);
			salvoRepository.save(sv18);
			salvoRepository.save(sv19);
			salvoRepository.save(sv20);
			salvoRepository.save(sv21);
			salvoRepository.save(sv22);

			Score sc1 = new Score(g1, p1, 1.0);
			Score sc2 = new Score(g1, p2, 0.0);
			Score sc3 = new Score(g2, p1, 0.5);
			Score sc4 = new Score(g2, p2, 0.5);
			Score sc5 = new Score(g3, p2, 1.0);
			Score sc6 = new Score(g3, p4, 0.0);
			Score sc7 = new Score(g4, p1, 0.5);
			Score sc8 = new Score(g4, p2, 0.5);
			scoreRepository.save(sc1);
			scoreRepository.save(sc2);
			scoreRepository.save(sc3);
			scoreRepository.save(sc4);
			scoreRepository.save(sc5);
			scoreRepository.save(sc6);
			scoreRepository.save(sc7);
			scoreRepository.save(sc8);
		};
	}*/
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository playerRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName -> {
			Player player = playerRepository.findByUserName(inputName);
			if (player != null) {
				return new User(player.getUserName(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unkown user: " + inputName);
			}
		});
	}
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors();
		http.authorizeRequests()
				.antMatchers("/api/games", "/api/leader_board", "/api/players", "/h2-console/**").permitAll()
				.anyRequest().fullyAuthenticated();

		http.formLogin()
				.usernameParameter("userName")
				.passwordParameter("password")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");
		http.headers().frameOptions().disable();

		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}
	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		// The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("HEAD",
				"GET", "POST", "PUT", "DELETE", "PATCH"));
		// setAllowCredentials(true) is important, otherwise:
		// will fail with 403 Invalid CORS request
		configuration.setAllowCredentials(true);
		// setAllowedHeaders is important! Without it, OPTIONS preflight request
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}