package com.suri.spring_test_practice;


import com.suri.spring_test_practice.dto.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends BaseIntegrationTest{
    private String USER_NAME = "TEST";

    @Autowired
    private UserSetUp userSetUp;

    @Test
    public void 유저생성() throws Exception {
        //given
        //given 절에서, 유저 생성을 위한 dto를 생성합니다.
        CreateUserRequest createUserRequest = new CreateUserRequest(USER_NAME);

        //when
        //when 절에서 테스트할 api를 지정합니다.
        //objectMapper를 이용해 String으로 변환하여 content()의 매개변수로 넘겨주어
        //요청 본문에 담길 데이터를 지정합니다.
        ResultActions resultActions = mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());


        //then
        //기대하는 응답코드를 지정한다.
        //마지막으로 응답 본문에 담긴 userId값이 null이 아님을 기대하는 코드를 작성합니다.
        resultActions
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("userId", is (notNullValue())));
    }

    @Test
    public void 유저조회() throws Exception {
        //given
        String USER_NAME = "TEST";
        long USER_ID = userSetUp.saveUser(USER_NAME);

        //when
        ResultActions resultActions = mvc.perform(get("/users/{userId}", USER_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());



        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("userName").value(USER_NAME));

       /* //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userList", hasSize(2)))
                .andExpect(jsonPath("$.userList[0].userName").value(USER_1_NAME))
                .andExpect(jsonPath("$.userLIst[1].userName").value(USER_2_NAME));
*/

    }

    @Test
    public void 유저전체조회() throws Exception {
        //given
        String USER_1_NAME = "TEST1";
        String USER_2_NAME = "TEST2";
        userSetUp.saveUser(USER_1_NAME);
        userSetUp.saveUser(USER_2_NAME);

        //when
        ResultActions resultActions = mvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userList", hasSize(2)))
                .andExpect(jsonPath("$.userList[0].userName").value(USER_1_NAME))
                .andExpect(jsonPath("$.userList[1].userName").value(USER_2_NAME));
    }
}

//상단의 userSetUp은 데이터베이스에 의존하는 유저조회 테스트를 위한 보조 클래스입니다.
@Component
class UserSetUp {
    @Autowired
    private UserRepository userRepository;

    public Long saveUser(String userName) {
        UserEntity user = UserEntity.builder()
                .userName(userName)
                .build();

        return userRepository.save(user).getUserId();
    }
}
