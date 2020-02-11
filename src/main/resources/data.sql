-- INSERT INTO teacher (tea_id,password) VALUES ('0000000000','0000000000');
-- INSERT IGNORE INTO teacher VALUE (tea_id,password) VALUES ('0000000000','0000000000');
-- IF not exists(select * from teacher where tea_id = '0000000000') INSERT INTO teacher (tea_id,password) VALUES ('0000000000','0000000000');
INSERT IGNORE INTO teacher(tea_id,password,authority,email)  VALUE("0000000000","0000000000",99,"983604371@qq.com");