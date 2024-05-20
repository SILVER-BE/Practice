package com.ohigraffers.practice.post.controller;

import com.ohigraffers.practice.post.dto.request.PostCreateRequest;
import com.ohigraffers.practice.post.dto.request.PostUpdateRequest;
import com.ohigraffers.practice.post.dto.response.ResponseMessage;
import com.ohigraffers.practice.post.model.Post;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


/* Swagger 문서화 시 Grouping 작성 */
@RestController
@RequestMapping("/posts")
public class PostController {

    private List<Post> posts;

    public PostController(){
        posts = new ArrayList<>();
        posts.add(new Post(1L, "강아지는", "멍멍", "홍길동"));
        posts.add(new Post(2L, "고양이는", "야옹", "유관순"));
        posts.add(new Post(3L, "병아리는", "삐약", "신사임당"));
        posts.add(new Post(4L, "참새는", "짹짹", "이순신"));
        posts.add(new Post(5L, "소는", "음메", "장보고"));
    }

    /* 1. 전체 포스트 조회 */
    /* Swagger 문서화 시 설명 어노테이션 작성 */
    /* RequestMapping 어노테이션 작성 */

    @Operation(summary = "전체 게시물 조회", description = "전체 게시물을 조회한다.")
    @GetMapping("/list")
    public ResponseEntity<ResponseMessage> findAllPosts() {

        /* 응답 데이터 설정 */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        Map<String, Object> respnseMap = new HashMap<>();
        respnseMap.put("posts", posts);
        ResponseMessage responseMessage = new ResponseMessage(200, "조회 성공", respnseMap);

        /* hateoas 적용 */
        List<EntityModel<Post>> postsWithRel = posts.stream().map(
                post ->
                        EntityModel.of(
                                post,
                                linkTo(methodOn(PostController.class).findPostByCode(post.getCode())).withSelfRel(),
                                linkTo(methodOn(PostController.class).findAllPosts()).withRel("posts")
                        )
                ).toList();

        /* ResponseEntity 반환 */
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    /* 2. 특정 코드로 포스트 조회 */
    /* Swagger 문서화 시 설명 어노테이션 작성 */
    /* RequestMapping 어노테이션 작성 */
    @Operation(summary = "코드로 글 조회", description = "코드를 통해 특정 글 조회")
    @GetMapping("/list/{postCode}")
    public ResponseEntity<ResponseMessage> findPostByCode(@PathVariable Long postCode) {

        /* 응답 데이터 설정 */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        Post foundPost = posts.stream().filter(post -> post.getCode() == postCode).toList().get(0);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("posts", posts);

        ResponseMessage responseMessage = new ResponseMessage(200, "조회 성공", responseMap);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(responseMessage);
    }

    /* 3. 신규 포스트 등록 */
    /* Swagger 문서화 시 설명 어노테이션 작성 */
    /* RequestMapping 어노테이션 작성 */
    @Operation(summary = "신규 글 등록")
    @PostMapping("/list")
       public ResponseEntity<Void> registPost(@Valid @RequestBody PostCreateRequest postCreateRequest) {

           /* 리스트에 추가 */
            Long newCode = posts.get(posts.size() - 1).getCode() + 1;
            posts.add(new Post(newCode, postCreateRequest.getTitle(), postCreateRequest.getContent(), postCreateRequest.getWriter()));

            return ResponseEntity
                   .created(URI.create("/posts/list/" + posts.get(posts.size() - 1).getCode()))
                   .build();
       }

   /* 4. 포스트 제목과 내용 수정 */
   /* Swagger 문서화 시 설명 어노테이션 작성 */
   /* RequestMapping 어노테이션 작성 */
    @Operation(summary = "글 수정")
    @PutMapping("/list/{postCode}")
    public ResponseEntity<Void> modifyPost(@Valid @PathVariable Long postCode,
                                           @RequestBody PostUpdateRequest modifyPost) {

        /* 리스트에서 찾아서 수정 */
        Post foundPost = posts.stream().filter(post -> post.getCode() == postCode).toList().get(0);

        /* 수정 메소드 활용 */
        foundPost.modifyTitleAndContent(modifyPost.getTitle(), modifyPost.getContent());

        /* ResponseEntity 반환 */
        return ResponseEntity
                .created(URI.create("/posts/list" + postCode))
                .build();
    }

    /* 5. 포스트 삭제 */
    @Operation(summary = "글 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "회원 정보 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못 입력된 파라미터")
    })
    @DeleteMapping("/posts/{postCode}")
    /* Swagger 문서화 시 설명 어노테이션 작성 */
    /* RequestMapping 어노테이션 작성 */
    public ResponseEntity<Void> removeUser(@PathVariable Long postCode) {

        /* 리스트에서 찾아서 삭제 */
        Post foundPost = posts.stream().filter(post -> post.getCode() == postCode).toList().get(0);

        posts.remove(foundPost);

        /* ResponseEntity 반환 */
        return ResponseEntity
                .noContent()
                .build();
    }

}
