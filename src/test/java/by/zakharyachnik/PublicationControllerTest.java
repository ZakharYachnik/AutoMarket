package by.zakharyachnik;

import by.zakharyachnik.controller.PublicationController;
import by.zakharyachnik.entity.User;
import by.zakharyachnik.service.PublicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("zakhar")
public class PublicationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PublicationController publicationController;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PublicationService publicationService;

    @Test
    public void addNewPublicationTest() throws Exception {
        this.mockMvc.perform(get("/new_publication"))
                .andDo(print())
                .andExpect(view().name("publication"));
    }
}
