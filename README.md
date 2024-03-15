# SNS API
---
* **sns**
* **#유저 #게시글 #팔로우 #좋아요 #알림**
* **AOP를 활용한 메서드 실행 시간 비교**
* **ExceptionHandler를 활용한 예외 처리**
* **Custom Annotation을 활용한 검증**
* **실시간 알림 서비스 (SSE)**
* **QueryDSL을 활용한 검색 기능**

---
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-blueviolet?style=for-the-badge)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.5-brightgreen?style=for-the-badge)](https://gradle.org/)
[![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.3-blue?style=for-the-badge&logo=mysql)](https://www.mysql.com/)
[![JUnit](https://img.shields.io/badge/JUnit-5-green?style=for-the-badge&logo=junit)](https://junit.org/junit5/)
[![Mockito](https://img.shields.io/badge/Mockito-blue?style=for-the-badge&logo=mockito)](https://site.mockito.org/)

---

## **주요기능**
- [x] 사용자 인증
- [x] 게시물 관리
- [x] 고급 검색 기능
- [x] 좋아요 
- [x] 팔로우 
- [x] 실시간 알림

---

## API
![스크린샷 2024-03-15 105234](https://github.com/wooseok50/sns-api/assets/155416976/1b7b352a-678b-4778-bbf3-c87096fec6b9)
![스크린샷 2024-03-15 105251](https://github.com/wooseok50/sns-api/assets/155416976/2d9b8a84-5c46-406e-b87b-0ba5d9f60a57)
![스크린샷 2024-03-15 105352](https://github.com/wooseok50/sns-api/assets/155416976/f8fe8d5a-de8c-4652-877e-afb0677b3127)
![스크린샷 2024-03-15 105512](https://github.com/wooseok50/sns-api/assets/155416976/f59ff4f9-2902-496e-bcf8-55568bd24aa0)

---

## DB
![스크린샷 2024-03-15 111105](https://github.com/wooseok50/sns-api/assets/155416976/5c2fb698-e8f1-4745-85d8-b4f0f449636a)

---

## 구현부
1. 메소드의 실행 시간 측정
	*  Aop 적용으로 각 메소드의 실행 시간을 측정합니다.
	*  로직, 쿼리를 수정하고, 메서드의 수행 시간을 비교하며 성능을 개선하였습니다.
   *  ```
      @Aspect  
      @Component  
      @Slf4j  
      public class TimeTraceAspect {  
        
      @Around("execution(* com.sns.domain..*(..))")  
      public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {  
          long start = System.currentTimeMillis();  
          try {  
              return joinPoint.proceed();  
          } finally {  
              long finish = System.currentTimeMillis();  
              long timeMs = finish - start;  
              log.info("Method: {} Execution Time: {}ms", joinPoint.getSignature().toShortString(),  
                  timeMs);  
              }  
          }  
      }
      
---

2.  ExceptionHandler
	*  Exception 상황에 적합한 CustomExcept 생성 및 적용
	*  코드 의도 명확하게 하고,  해당 예외에 대한 메세지를 전달합니다.
    * ```
      @Slf4j
      @RestControllerAdvice
      public class ControllerAdvice {
  
        @ExceptionHandler(PostNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleInvalidInputException(PostNotFoundException e) {
            log.error(e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
          }
      }
      
      // 검증 메서드
      Post post = postRepository.searchPost(postId).orElseThrow(()  
      -> new PostNotFoundException("해당 게시글이 존재하지 않습니다."));
      ```
      
---

3. custom annotation
	* custom annotation을 생성하여, 검증을 위한 코드(메서드)의 중복을 해결합니다.
	* 검증이 필요한 메서드에  @ValidatePost annotation을 선업합니다.
	* 게시글의 검증 범위에서 aop를 사용합니다. 공통 범위에 사용하는  aop의 목적과 적합하지 않지만 , 코드가 반복되며, 검증을 위해 다른 서비스를 주입 받는 것을 방지하기 위하여 적용하였습니다.
    * ```
      // PostValidationAspect.class
      @Aspect  
      @Component  
      public class PostValidationAspect {  
      	  
          private final PostRepository postRepository;  
      	  
          @Autowired  
          public PostValidationAspect(PostRepository postRepository) {  
              this.postRepository = postRepository;  
          }  
      	  
          @Before("@annotation(com.sns.domain.post.aop.ValidatePost) && args(postId,..)")  
          public void validatePost(Long postId) {  
              postRepository.findById(postId).orElseThrow(  
                  () -> new PostNotFoundException("해당 게시글이 존재하지 않습니다."));  
          }  
      }
      
      // ValidatePost
      @Target(ElementType.METHOD)  
      @Retention(RetentionPolicy.RUNTIME)  
      public @interface ValidatePost {  
      }
      ```
---

4. 실시간 알림 서비스 (SSE)
   * 유저가 서버로 특정 이벤트를 구독하면 웹 브라우저로 팔로우, 댓글, 좋아요 생성 시 해당 사용자에게 실시간으로 알림을 전송합니다. ( user 님이 회원님을 팔로우 하였습니다. + time)
   * 경로로 GET 요청이 들어오면 현재 사용자를 인증하고, 해당 사용자의 ID를 사용하여 SSE를 구독할 수 있는 SseEmitter 객체를 반환하여, 실시간 알림 서비스를 연결합니다.
    * ```
      @Operation(summary = "알림 생성", description = "실시간 알림 서비스 연결")  
      @GetMapping("/notification/subscribe")  
      public SseEmitter subscribe(  
          @AuthenticationPrincipal UserDetailsImpl userDetails  
      ) {  
          SseEmitter sseEmitter = notificationService.subscribe(userDetails.getUser().getId());  
          return sseEmitter;  
      }
      ```
   * 해당 메서드를 좋아요 생성 요청 메서드에 적용하여, 좋아요 생성시에 실시간 알림을 보냅니다.
    * ```
      @Override
      @Transactional
      public void notifyLike(Long postId) {
          Like receiveLike = likeService.findLatestLike(postId);
          long userId = postService.findPost(postId).getUserId();

        if (NotificationController.sseEmitters.containsKey(userId)) {
            SseEmitter sseEmitter = NotificationController.sseEmitters.get(userId);
            try {
                Map<String, String> eventData = new HashMap<>();
                eventData.put("contents",
                    userService.findUser(receiveLike.getUserId()).getUsername()
                        + "님이 회원님의 게시물을 좋아합니다.❤️");

                sseEmitter.send(SseEmitter.event().name("addLike").data(eventData));

                // DB 저장
                Notification notification = new Notification();
                notification.setPostId(postId);
                notification.setContents("좋아요");
                notification.setSender(userService.findUser(receiveLike.getUserId()).getUsername());
                notificationRepository.save(notification);

                // 알림 개수 증가
                notificationCounts.put(userId, notificationCounts.getOrDefault(userId, 0) + 1);

                // 현재 알림 개수 전송
                sseEmitter.send(SseEmitter.event().name("notificationCount")
                    .data(notificationCounts.get(userId)));

            } catch (IOException e) {
                NotificationController.sseEmitters.remove(userId);
              }
          }
      }
      ```
---

5. QueryDSL 사용 검색 기능
   * title.contains 메서드로 제목과 부분일치하는 검색어을 입력할 시 관련 게시글을 페이지 처리하여 반환합니다.
   * 또한, username.eq 메서드로 이름을 검색할 경우에 검색어가 일치할 경우 반환합니다.
   * sofedelete된 게시글은 사용자가(관리자가 아닌) 조회할 수 없도록 쿼리를 작성했습니다.
    * ```
      @Override
      public Page<Post> queryPosts(String title, String username, Pageable pageable) {

        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(post.deleted_YN.eq("N"));

        if (title != null) {
            predicate.and(post.title.contains(title));
        }
        if (username != null) {
            predicate.and(post.username.eq(username));
        }

        List<Post> posts = jpaQueryFactory
            .selectFrom(post)
            .where(predicate)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long totalCount = jpaQueryFactory
            .selectFrom(post)
            .where(predicate)
            .fetchCount();

        return new PageImpl<>(posts, pageable, totalCount);
      }
    
      ``` 




      

