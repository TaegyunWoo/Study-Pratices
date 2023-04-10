package com.my.springboot.domain.posts;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository repository;

    @After
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기() {
        //given
        String title = "테스트 게시글";
        String content = "테스트 본문";

        repository.save(Posts.builder().title(title).content(content).author("dnxprbs@gmail.com").build());

        //when
        List<Posts> postsList = repository.findAll();

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }

    @Test
    public void BaseTimeEntity_등록() {
        //given
        LocalDateTime now = LocalDateTime.now();
        repository.save(Posts.builder().title("title").content("content").author("author").build());

        //when
        List<Posts> postsList = repository.findAll();

        //then
        Posts posts = postsList.get(0);

        System.out.println(">>>>>>>>>posts.getCreatedDate() = " + posts.getCreatedDate());
        System.out.println(">>>>>>>>>posts.getModifiedDate() = " + posts.getModifiedDate());

        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);

    }
}