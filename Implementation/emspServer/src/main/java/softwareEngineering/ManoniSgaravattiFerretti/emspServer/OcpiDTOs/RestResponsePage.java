package softwareEngineering.ManoniSgaravattiFerretti.emspServer.OcpiDTOs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

//Class for collecting Page responses when Rest calls are made
public class RestResponsePage<T> extends PageImpl<T> {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RestResponsePage(@JsonProperty ("content") List<T> content,
                            @JsonProperty ("number") int number,
                            @JsonProperty ("size") int size,
                            @JsonProperty ("totalElements") Long totalElements,
                            @JsonProperty ("pageable") JsonNode pageable,
                            @JsonProperty ("last") boolean last,
                            @JsonProperty ("totalPages") int totalPages,
                            @JsonProperty ("sort") JsonNode sort,
                            @JsonProperty ("first") boolean first,
                            @JsonProperty ("empty") boolean empty) {
        super (content, PageRequest.of (number, size), totalElements);
    }
}
