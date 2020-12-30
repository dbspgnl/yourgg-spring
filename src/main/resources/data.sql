insert into post values(20001, '글 내용1 입니다.', sysdate(), '김길동', 0, 3, '제목1', 0);
insert into post values(20002, '글 내용2 입니다.', sysdate(), '고길동', 0, 0, '제목2', 0);
insert into post values(20003, '글 내용3 입니다.', sysdate(), '나길동', 0, 0, '제목3', 0);

insert into reply values(30001, '댓글 내용1', sysdate(), 1, 1, 20001, '유저1');
insert into reply values(30002, '댓글 내용2', sysdate(), 2, 1, 20001, '김길동');
insert into reply values(30003, '댓글 내용3', sysdate(), 3, 1, 20001, '운영자');