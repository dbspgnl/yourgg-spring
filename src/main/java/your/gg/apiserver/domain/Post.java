package your.gg.apiserver.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post {
    @Id
    @GeneratedValue
    private Integer seq;
    @Size(min= 1, max = 20, message = "Title은 최소1글자 최대20글자 입니다.")
    private String title;
    @Size(min= 1, max=2000, message = "Content는 최소1글자 최대2000글자 입니다.")
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private Date date;
    @Size(min= 1, max = 20, message = "Name은 최소1글자 최대20글자 입니다.")
    private String name;
    private Integer replyCtn;
    private Integer rcmCtn;
    private Integer viewsCtn;

}
