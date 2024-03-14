//package com.sns.controller;
//
//
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sns.data.TestMockData;
//import com.sns.domain.post.controller.PostController;
//import com.sns.domain.post.dto.PostResponseDto;
//import com.sns.domain.post.entity.Post;
//import com.sns.domain.post.service.PostService;
//import com.sns.domain.user.entity.User;
//import com.sns.global.security.UserDetailsImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.filter.CharacterEncodingFilter;
//
//@WebMvcTest(PostController.class)
//class PostControllerTest {
//
//    @MockBean
//    private PostService postService;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private WebApplicationContext context;
//
//    User user;
//
//    @BeforeEach
//    void setUp() {
//        // Character SET에 대한 깨짐 방지
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
//            .addFilter(new CharacterEncodingFilter("UTF-8", true))
//            .alwaysDo(print())
//            .build();
////
////        user = TestMockData.testUser();
////        // Mock 테스트 UserDetails 생성
////        UserDetailsImpl testUserDetails = new UserDetailsImpl(user);
////
////        // SecurityContext 에 인증된 사용자 설정
////        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
////            testUserDetails, testUserDetails.getPassword(), testUserDetails.getAuthorities()));
//    }
//
//    @Test
//    void getPost() throws Exception {
//        Long postId = 1L;
//
//        Post post = Post.builder()
//            .userId(2L)
//            .username("testUsername")
//            .title("testTitle")
//            .content("testContent")
//            .build();
//        ReflectionTestUtils.setField(post, "id", 1L);
//
//        PostResponseDto postResponseDto = new PostResponseDto(post);
//
//        given(postService.getPost(anyLong())).willReturn(postResponseDto);
//
//        ResultActions actions =
//            mockMvc.perform(
//                get("/posts/{postId}", postId)
//                    .accept(MediaType.APPLICATION_JSON)
//                    .contentType(MediaType.APPLICATION_JSON)
//            );
//
//        actions
//            .andExpect(status().isOk());
//    }
//}
