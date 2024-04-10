package fintechservice.communication.dto;

import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class SourceLinksRequestDto {
    private Map<String, String> indexToHTML;

}
