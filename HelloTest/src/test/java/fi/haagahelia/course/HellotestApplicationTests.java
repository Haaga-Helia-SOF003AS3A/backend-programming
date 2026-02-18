package fi.haagahelia.course;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fi.haagahelia.course.web.HelloController;

/**
 * Testing that the context is creating your controller
 * 
 * (inserted also context test, 26.9.24) 
 * 
 * @author h01270, updated by h01340 
 *
 */

@SpringBootTest
public class HellotestApplicationTests {

    private final HelloController controller;

    @Autowired
    public HellotestApplicationTests(HelloController controller) {
        this.controller = controller;
    }

    @Test
	void contextLoads() {
	}

    @Test
    public void controllerLoads() throws Exception {
        assertThat(controller).isNotNull();
    }	
}
